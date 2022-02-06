package com.example.rateit.controller;

import com.example.rateit.MyUtilities;
import com.example.rateit.model.*;
import com.example.rateit.model.entity.Post;
import com.example.rateit.model.entity.User;
import com.example.rateit.model.entity.WatchList;
import com.example.rateit.model.entity.WishList;
import com.example.rateit.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by chethan on 19-12-2021
 **/
@Controller
@RequestMapping("/")
@Slf4j
public class MainController {

    @Autowired
    private APIService apiService;
    @Autowired
    private UserService userService;
    @Autowired
    private ListService listService;
    @Autowired
    private PostService postService;
    @Autowired
    private FriendService friendService;

    @ModelAttribute("user")
    public User getUser(){
        return new User();
    }

    @GetMapping
    public ModelAndView home(@AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {

        ModelAndView mav = new ModelAndView("index");
        List<Media> trendingMovies = apiService.getTrendingMovies();
        List<Media> trendingTv = apiService.getTrendingTV();

        try {
            User user = myUserDetails.getUser();
            mav.addObject("hasPendingFriends", friendService.hasPendingRequests(user.getId()));
            mav.addObject("username", user.getUsername());
        } catch (NullPointerException ex){
            // do nothing if the user is not logged in
        }

        mav.addObject("trendingMovies",trendingMovies);
        mav.addObject("trendingTv",trendingTv);
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView processRegister(@Valid @ModelAttribute User user) {

        User existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser != null)
            return new ModelAndView("redirect:/?failure-register");
        user.setUsername(Jsoup.clean(user.getUsername(),Safelist.basic()));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.save(user);
        log.debug("user {} has registered", user.getUsername());
        return new ModelAndView("redirect:/?sucess-register");
    }


    @GetMapping("/search")
    public ModelAndView search(@RequestParam String query) throws JsonProcessingException {
        log.debug("searched for {}", query);
        query = Jsoup.clean(query, Safelist.basic());
        List<Media> mediaList = apiService.search(query);

        ModelAndView mav = new ModelAndView("search");
        mav.addObject("search", query);
        mav.addObject("mediaList", mediaList);
        return mav;
    }

    @GetMapping("/movie/{id}")
    public ModelAndView movie(@PathVariable int id,@AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {

        ModelAndView mav = new ModelAndView("movie");
        Media movie = apiService.getMovie(id);
        if (movie == null)
            return new ModelAndView("redirect:/?error");

        List<Cast> casts = apiService.getMediaCast(id,"movie");
        List<Media> similarMovies = apiService.getSimilarMedia(id,"movie");
        List<Review> movieReviews = apiService.getMediaReviews(id,"movie");


        try{
            User user = myUserDetails.getUser();
            boolean hasWatched = listService.hasWatched(user.getId(),id);
            boolean hasWished = listService.hasWished(user.getId(),id);
            boolean hasPosted = postService.hasPosted(user.getId(), id);
            mav.addObject("hasWatched",hasWatched);
            mav.addObject("hasWished",hasWished);
            mav.addObject("hasPosted",hasPosted);
        } catch (NullPointerException ex){
            System.out.println("user not logged in from /movie/id");
        }

        mav.addObject("movie", movie);
        mav.addObject("runtime", MyUtilities.minToHours(((Movie) movie).getRuntime()));
        mav.addObject("releaseYear", movie.getReleaseDate().getYear());
        mav.addObject("customPost", new PostRequestBody());
        mav.addObject("casts", casts);
        mav.addObject("similarMovies", similarMovies);
        mav.addObject("movieReviews", movieReviews);
        mav.addObject("isReviewsEmpty", movieReviews.isEmpty());
        mav.addObject("numberOfReviews", movieReviews.size());
        return mav;
    }

    @GetMapping("/tv/{id}")
    public ModelAndView tv(
            @PathVariable int id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) throws JsonProcessingException {

        ModelAndView mav = new ModelAndView("tv");
        Media tv = apiService.getTV(id);

        if (tv == null)
            return new ModelAndView("redirect:/?error");

        List<Cast> casts = apiService.getMediaCast(id,"tv");
        List<Media> similarTv = apiService.getSimilarMedia(id,"tv");
        List<Review> tvReviews = apiService.getMediaReviews(id,"tv");

        try{
            User user = myUserDetails.getUser();
            boolean hasWatched = listService.hasWatched(user.getId(),id);
            boolean hasWished = listService.hasWished(user.getId(),id);
            boolean hasPosted = postService.hasPosted(user.getId(), id);
            mav.addObject("hasWatched", hasWatched);
            mav.addObject("hasWished", hasWished);
            mav.addObject("hasPosted", hasPosted);
        } catch (NullPointerException ex){
            System.out.println("user not logged in from /tv/id");
        }

        int runTime = 0;

        List<Integer> runTimes = ((TV) tv).getEpisodeRuntime();

        if (!runTimes.isEmpty())
            runTime = runTimes.get(0);


        mav.addObject("tv", tv);
        mav.addObject("runtime", MyUtilities.minToHours(runTime));
        mav.addObject("releaseYear", tv.getReleaseDate().getYear());
        mav.addObject("customPost", new PostRequestBody());
        mav.addObject("casts", casts);
        mav.addObject("similarTv", similarTv);
        mav.addObject("tvReviews", tvReviews);
        mav.addObject("isReviewsEmpty", tvReviews.isEmpty());
        mav.addObject("numberOfReviews", tvReviews.size());
        return mav;
    }

    @GetMapping("/list/{mediaType}/{topic}")
    public ModelAndView getTopicMedia(
            @PathVariable String mediaType,
            @PathVariable String topic
    ) throws JsonProcessingException {

        ModelAndView mav = new ModelAndView("list");
        if (mediaType.equals("movie")) {
            List<Media> movieList = null;
            switch (topic) {
                case "top"-> {
                    movieList = apiService.getTopRatedMovies();
                    mav.addObject("topic", "Top Rated Movies");
                }
                case "upcoming" -> {
                    movieList = apiService.getUpcomingMovies();
                    mav.addObject("topic", "Upcoming Movies");
                }
                case "popular" -> {
                    movieList = apiService.getPopularMovies();
                    mav.addObject("topic", "Popular Movies");
                }
            }
            mav.addObject("movieList", movieList);

        } else if (mediaType.equals("tv")) {
            List<Media> tvList = null;
            switch (topic) {
                case "top" -> {
                    tvList = apiService.getTopRatedTV();
                    mav.addObject("topic","Top Rated TV Shows");
                }
                case "air" -> {
                    tvList = apiService.getOnAirTV();
                    mav.addObject("topic","On Air TV Shows");
                }
            }
            mav.addObject("tvList", tvList);
        }
        return mav;
    }

    @GetMapping("/watchlist/{media}/{id}")
    public ModelAndView addMediaToWatchList(
            @PathVariable String media,
            @PathVariable int id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/" + media + "/" + id);
        }

        WatchList newMedia = new WatchList(
                user,
                media,
                id,
                LocalDateTime.now()
        );
        listService.saveWatchList(newMedia);
        return new ModelAndView("redirect:/" + media + "/" + id);
    }

    @GetMapping("/wishlist/{media}/{id}")
    public ModelAndView addMediaToWishList(
            @PathVariable String media,
            @PathVariable int id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ){
        User user;

        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/" + media + "/" + id);
        }

        WishList newMedia = new WishList(
                user,
                media,
                id,
                LocalDateTime.now()
        );

        listService.saveWishList(newMedia);
        return new ModelAndView("redirect:/" + media + "/" + id);
    }

    @GetMapping("/mywatchlist")
    public ModelAndView myWatchList(@AuthenticationPrincipal MyUserDetails myUserDetails){

        User user = myUserDetails.getUser();
        List<Media> mediaList = listService.getWatchListMedia(user.getId());
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        mediaList.forEach(media -> {
            MediaDTO mediaDTO = new MediaDTO(media,true);
            mediaDTOList.add(mediaDTO);
        });

        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaDTOList", mediaDTOList);
        mav.addObject("topic","My WatchList");
        mav.addObject("list","watchlist");
        return mav;
    }

    @GetMapping("/mywishlist")
    public ModelAndView myWishList(@AuthenticationPrincipal MyUserDetails myUserDetails){
        User user = myUserDetails.getUser();
        List<Media> mediaList = listService.getWishListMedia(user.getId());
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        mediaList.forEach(media -> {
            MediaDTO mediaDTO = new MediaDTO(media,true);
            mediaDTOList.add(mediaDTO);
        });

        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaDTOList",mediaDTOList);
        mav.addObject("topic","My WishList");
        mav.addObject("list","wishlist");
        return mav;
    }

    @PostMapping("/addPost")
    public ModelAndView addPost(@ModelAttribute PostRequestBody postRequestBody,
                                @AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }

        Post post = new Post(
                user,
                Jsoup.clean(postRequestBody.getContent(),Safelist.basic()),
                LocalDateTime.now(),
                postRequestBody.getRating(),
                Jsoup.clean(postRequestBody.getMediaType(),Safelist.basic()),
                Integer.parseInt(postRequestBody.getMediaId())
        );

        postService.save(post);
        String redirectTo = "/" + postRequestBody.getMediaType() + "/" + postRequestBody.getMediaId();
        return new ModelAndView(String.format("redirect:%s", redirectTo));
    }

    @GetMapping("/myposts")
    public ModelAndView myPosts(@AuthenticationPrincipal MyUserDetails myUserDetails,@RequestParam(required = false) Integer pageNo){
        User user = myUserDetails.getUser();
        ModelAndView mav = new ModelAndView("post");
        if (pageNo == null) pageNo = 1;
        Page<Post> posts = apiService.getPostsOfUser(user.getId(),pageNo);
        List<DisplayPost> myPosts = mapPostsToDisplayPosts(posts.getContent(), true);
        mav.addObject("displayPosts", myPosts);
        mav.addObject("noPosts", myPosts.isEmpty());
        mav.addObject("username", user.getUsername());
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", posts.getTotalPages());
        mav.addObject("totalItems", posts.getTotalElements());
        mav.addObject("page","myposts");
        return mav;
    }

    @GetMapping("/posts/{userId}")
    public ModelAndView friendPosts(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(required = false) Integer pageNo,
            @PathVariable Long userId
    ){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        if (Objects.equals(userId, user.getId()))
            return new ModelAndView("redirect:/myposts");
        ModelAndView mav = new ModelAndView("post");
        if (pageNo == null) pageNo = 1;
        boolean isMyFriend = friendService.isMyFriend(user.getId(),userId);
        if (!isMyFriend) return new ModelAndView("redirect:/");
        Page<Post> posts = apiService.getPostsOfUser(userId,pageNo);
        String username = userService.getUser(userId).getUsername();
        List<DisplayPost> displayPosts = mapPostsToDisplayPosts(posts.getContent(), false);
        mav.addObject("displayPosts", displayPosts);
        mav.addObject("noPosts",displayPosts.isEmpty());
        mav.addObject("username", username);
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", posts.getTotalPages());
        mav.addObject("totalItems", posts.getTotalElements());
        mav.addObject("page","posts/" + String.valueOf(userId));
        return mav;
    }


    @GetMapping("/myfeed")
    public ModelAndView feedPosts(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestParam(required = false) Integer pageNo
    ){
        User user = myUserDetails.getUser();;
        ModelAndView mav = new ModelAndView("post");

        if (pageNo == null)
            pageNo = 1;

        List<User> friends = friendService.getFriends(user);
        List<Long> userIds = new ArrayList<>();

        friends.forEach(friend -> userIds.add(friend.getId()));

        Page<Post> posts = postService.getFeed(userIds, user.getId(), pageNo);

        List<DisplayPost> displayPosts = mapPostsToDisplayPosts(posts.getContent(), true);

        mav.addObject("displayPosts", displayPosts);
        mav.addObject("noPosts", displayPosts.isEmpty());
        mav.addObject("username", user.getUsername());
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", posts.getTotalPages());
        mav.addObject("totalItems", posts.getTotalElements());
        mav.addObject("page","myfeed");
        return mav;
    }

    private List<DisplayPost> mapPostsToDisplayPosts(List<Post> posts, boolean isMyPost){

        List<DisplayPost> displayPosts = new ArrayList<>();

        posts.forEach(post -> {
            Media media = apiService.getMediaByType(post.getMediaType().toLowerCase(), post.getMediaId());
            DisplayPost displayPost = DisplayPost.builder()
                    .post(post)
                    .media(media)
                    .isMyPost(isMyPost)
                    .build();
            displayPosts.add(displayPost);
        });

        return displayPosts;
    }

    @GetMapping("/login")
    public ModelAndView redirectToHome(){
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/post/delete/{id}")
    public ModelAndView deletePost(@PathVariable Long id,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }
        boolean isMyPost = postService.isMyPost(user.getId(), id);
        if (isMyPost)
            postService.deletePost(id);
        return new ModelAndView("redirect:/myposts");
    }

    @GetMapping("/list/{list}/{mediaId}")
    public ModelAndView deleteList(
            @PathVariable int mediaId,
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable String list
    ){
        User user;
        try {
            user = myUserDetails.getUser();
        } catch (NullPointerException ex){
            return new ModelAndView("redirect:/");
        }

        if (list.equals("watchlist")){
            boolean isMyWatchList = listService.isMyWatchList(user.getId(), mediaId);
            if (isMyWatchList)
                listService.deleteWatchListByMediaAndUser(user.getId(),mediaId);
        } else{
            boolean isMyWishList = listService.isMyWishList(user.getId(), mediaId);
            if (isMyWishList)
                listService.deleteWishListByMediaAndUser(user.getId(),mediaId);
        }

        String redirectTo = "/my" + list;
        return new ModelAndView("redirect:" + redirectTo);
    }


    @GetMapping("/myfriends")
    public ModelAndView getFriends(@AuthenticationPrincipal MyUserDetails myUserDetails){

        User user = myUserDetails.getUser();
        List<User> friends = friendService.getFriends(user);

        ModelAndView mav = new ModelAndView("search_friend");
        mav.addObject("friends", friends);
        mav.addObject("noFriends", friends.isEmpty());
        mav.addObject("username", user.getUsername());
        return mav;
    }

    @GetMapping("/myfriendrequests")
    public ModelAndView getPendingFriends(@AuthenticationPrincipal MyUserDetails myUserDetails){

        User user = myUserDetails.getUser();
        List<User> friends = friendService.getPendingFriends(user);

        ModelAndView mav = new ModelAndView("friend_requests");
        mav.addObject("friends", friends);
        mav.addObject("noFriends", friends.isEmpty());
        mav.addObject("username", user.getUsername());
        return mav;
    }

    @GetMapping("/searchfriend")
    public ModelAndView searchFriend(
            @RequestParam String query,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        if (query.isBlank())
            return new ModelAndView("redirect:/myfriends");

        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        log.debug("user {} searched for {}", user.getUsername(), query);

        // sanitizing input because this will end up on the page
        query = Jsoup.clean(query.toLowerCase(),Safelist.basic());

        ModelAndView mav = new ModelAndView("search_friends_results");
        List<User> users = friendService.searchFriends(query, user.getId());
        List<SearchFriendDTO> searchFriendDTOS = new ArrayList<>();

        users.forEach(friend -> {
            boolean isAccepted = friendService.isMyFriend(user,friend);
            boolean isPending = friendService.hasRequested(user,friend);
            boolean isReceived = friendService.haveRecived(user,friend);
            SearchFriendDTO searchFriendDTO = SearchFriendDTO.builder()
                    .user(friend)
                    .isAccepted(isAccepted)
                    .isPending(isPending)
                    .isReceived(isReceived)
                    .build();
            searchFriendDTOS.add(searchFriendDTO);
        });

        mav.addObject("search",query);
        mav.addObject("searchFriendDTOS", searchFriendDTOS);
        mav.addObject("noUsers", users.isEmpty());
        return mav;
    }

    @GetMapping("/addfriend/{id}")
    public ModelAndView addFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        friendService.saveFriend(user,id);
        return new ModelAndView("redirect:/myfriends");
    }

    @GetMapping("/acceptfriend/{id}")
    public ModelAndView acceptFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        friendService.acceptFriend(id,user.getId());
        return new ModelAndView("redirect:/myfriends");
    }

    @GetMapping("/unfriend/{id}")
    public ModelAndView unFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        friendService.unFriend(user.getId(), id);
        return new ModelAndView("redirect:/myfriends");
    }

    @GetMapping("/rejectfriend/{id}")
    public ModelAndView rejectFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        friendService.deleteFriend(id, user.getId());
        return new ModelAndView("redirect:/myfriends");
    }

    @GetMapping("/watchlist/{friendID}")
    public ModelAndView friendWatchList(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long friendID
    ){
        User user;
        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        if (Objects.equals(friendID, user.getId()))
            return new ModelAndView("redirect:/mywatchlist");

        User friend = userService.getUser(friendID);

        // if friend does not exist or if that user is not our friend
        if (friend == null || !friendService.isMyFriend(user,friend))
            return new ModelAndView("redirect:/myfriends");


        List<Media> mediaList = listService.getWatchListMedia(friend.getId());
        List<MediaDTO> mediaDTOList = new ArrayList<>();


        // map medialist to mediaDTO list
        mediaList.forEach(media -> {
            MediaDTO mediaDTO = new MediaDTO(media,false);
            mediaDTOList.add(mediaDTO);
        });


        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaDTOList",mediaDTOList);
        mav.addObject("topic",String.format("%s's Watch list",friend.getUsername()));
        return mav;
    }

    @GetMapping("/wishlist/{friendID}")
    public ModelAndView friendWishList(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long friendID
    ){
        User user;

        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        if (Objects.equals(friendID, user.getId()))
            return new ModelAndView("redirect:/mywishlist");

        User friend = userService.getUser(friendID);

        // check if user not exists or user is not our friend
        if (friend == null || !friendService.isMyFriend(user,friend))
            return new ModelAndView("redirect:/myfriends");

        List<Media> mediaList = listService.getWishListMedia(friend.getId());
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        // map each media to mediaDTO
        mediaList.forEach(media -> {
            MediaDTO mediaDTO = new MediaDTO(media,false);
            mediaDTOList.add(mediaDTO);
        });

        // send data object to the model
        ModelAndView mav = new ModelAndView("media_list");
        mav.addObject("mediaDTOList",mediaDTOList);
        mav.addObject("topic",String.format("%s's Wish list", friend.getUsername()));
        return mav;
    }

    @GetMapping("/deleteprofile")
    @Transactional
    public ModelAndView deleteProfile(@AuthenticationPrincipal MyUserDetails myUserDetails){

        User user;

        try{
            user = myUserDetails.getUser();
        } catch (NullPointerException err){
            return new ModelAndView("redirect:/");
        }

        log.debug("user {} is deleting account", user.getUsername());

        friendService.deleteMyFriendship(user);
        postService.deleteAllMyPosts(user);
        listService.deleteMyLists(user);
        userService.deleteUser(user);

        return new ModelAndView("redirect:/logout");

    }

}

