use rateIt;
show tables;
describe users;
select * from users;
describe user_watch_list;
select * from user_watch_list;

show tables;
select * from user_friends;
describe user_friends;
select * from users;
select * from user_posts;
select * from user_friends;

-- get all my friends
-- SELECT to_id FROM user_friends u where (u.from_id = 1 or u.to_id = 1) and status = 1;

-- get all posts

-- select *
-- from user_posts
-- where 
-- (user_id in (SELECT to_id FROM user_friends u where (u.from_id = 2 or u.to_id = 2) and status = 1)
-- or user_id in ((SELECT to_id FROM user_friends u where (u.from_id = 2 or u.to_id = 2) and status = 1)))
-- and user_id != 1;





