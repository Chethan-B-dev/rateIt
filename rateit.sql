-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 01, 2022 at 07:14 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rateit`
--

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `username`) VALUES
(1, 'chethanborigin@gmail.com', '$2a$10$cexMKy/ZFCcNHjWB1Vv0T.52Iv/j0FY8dgkqa0vbReI1oRGSuddrq', 'chethan'),
(2, 'rohan@test.com', '$2a$10$WmkzXlQ/tQUyXqsEo.4IXuY7Tgk.dOPBLps0RXVEnG1KIqgsTpEEO', 'rohan'),
(3, 'test@test.com', '$2a$10$WVUnGgScVkeDE9GAag7PdeEQUR0kR9xLUKBYejtJKND8YuwVNQVmu', 'test'),
(4, 'shivu@test.com', '$2a$10$.vHei1.AYjMRefFAl6qDXuqw4hxvYBdCUBNAmLL/MQeugMBSX/bCO', 'shivu');

-- --------------------------------------------------------

--
-- Table structure for table `user_friends`
--

CREATE TABLE `user_friends` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `from_id` bigint(20) DEFAULT NULL,
  `to_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_friends`
--

INSERT INTO `user_friends` (`id`, `created_at`, `status`, `from_id`, `to_id`) VALUES
(2, '2021-12-29 21:02:02', 1, 1, 3),
(3, '2021-12-30 16:30:48', 1, 2, 3),
(4, '2021-12-31 11:48:16', 1, 1, 4),
(6, '2021-12-31 14:43:35', 1, 3, 4),
(7, '2021-12-31 15:03:28', 1, 4, 2),
(9, '2022-01-01 11:30:41', 1, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_posts`
--

CREATE TABLE `user_posts` (
  `id` bigint(20) NOT NULL,
  `content` text DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `media_id` int(11) NOT NULL,
  `media_type` varchar(255) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_posts`
--

INSERT INTO `user_posts` (`id`, `content`, `created_at`, `media_id`, `media_type`, `rating`, `user_id`) VALUES
(2, 'fdsgfdgfdgdfgg', '2021-12-31 10:52:25', 460458, 'movie', 5, 3),
(3, 'i am chethan and this is encanto', '2021-12-31 13:50:10', 568124, 'movie', 9, 1),
(5, 'this is a review from rohan', '2021-12-31 15:07:55', 512195, 'movie', 8, 2),
(6, 'i love this movie', '2021-12-31 19:55:15', 95057, 'tv', 8, 1),
(9, 'dsfsdf', '2021-12-31 20:32:54', 438695, 'movie', 1, 1),
(10, 'fgdgfdfgfg', '2021-12-31 22:45:47', 580489, 'movie', 5, 1),
(11, 'i liked this movie', '2022-01-01 10:52:47', 166076, 'movie', 7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_watch_list`
--

CREATE TABLE `user_watch_list` (
  `id` bigint(20) NOT NULL,
  `added_at` datetime DEFAULT NULL,
  `media_id` int(11) NOT NULL,
  `media_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_watch_list`
--

INSERT INTO `user_watch_list` (`id`, `added_at`, `media_id`, `media_type`, `user_id`) VALUES
(4, '2021-12-29 21:38:21', 568124, 'movie', 2),
(5, '2021-12-29 21:38:29', 424, 'movie', 2),
(10, '2021-12-31 09:56:18', 585245, 'movie', 4),
(13, '2022-01-01 10:52:24', 166076, 'movie', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_wish_list`
--

CREATE TABLE `user_wish_list` (
  `id` bigint(20) NOT NULL,
  `added_at` datetime DEFAULT NULL,
  `media_id` int(11) NOT NULL,
  `media_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_wish_list`
--

INSERT INTO `user_wish_list` (`id`, `added_at`, `media_id`, `media_type`, `user_id`) VALUES
(6, '2021-12-30 13:52:09', 566525, 'movie', 1),
(7, '2021-12-31 10:44:10', 94605, 'tv', 1),
(8, '2021-12-31 21:41:17', 438695, 'movie', 1),
(9, '2021-12-31 21:58:06', 588921, 'movie', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`);

--
-- Indexes for table `user_friends`
--
ALTER TABLE `user_friends`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKot6h4y1o18k2o5q5bcq2159op` (`from_id`),
  ADD KEY `FKgfom4fyon4nevbbtrn26hl1tn` (`to_id`);

--
-- Indexes for table `user_posts`
--
ALTER TABLE `user_posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9spao74qxqjkns4i8h2r0kpnj` (`user_id`);

--
-- Indexes for table `user_watch_list`
--
ALTER TABLE `user_watch_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmluml45smwbrh67l6t4qrtbvo` (`user_id`);

--
-- Indexes for table `user_wish_list`
--
ALTER TABLE `user_wish_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5rmgondir5xx5s0jxew2dtpkx` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user_friends`
--
ALTER TABLE `user_friends`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `user_posts`
--
ALTER TABLE `user_posts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `user_watch_list`
--
ALTER TABLE `user_watch_list`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user_wish_list`
--
ALTER TABLE `user_wish_list`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `user_friends`
--
ALTER TABLE `user_friends`
  ADD CONSTRAINT `FKgfom4fyon4nevbbtrn26hl1tn` FOREIGN KEY (`to_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKot6h4y1o18k2o5q5bcq2159op` FOREIGN KEY (`from_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `user_posts`
--
ALTER TABLE `user_posts`
  ADD CONSTRAINT `FK9spao74qxqjkns4i8h2r0kpnj` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `user_watch_list`
--
ALTER TABLE `user_watch_list`
  ADD CONSTRAINT `FKmluml45smwbrh67l6t4qrtbvo` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `user_wish_list`
--
ALTER TABLE `user_wish_list`
  ADD CONSTRAINT `FK5rmgondir5xx5s0jxew2dtpkx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
