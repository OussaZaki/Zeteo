-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 29, 2014 at 05:46 PM
-- Server version: 5.6.15
-- PHP Version: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `zeteo`
--
CREATE DATABASE IF NOT EXISTS `zeteo` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `zeteo`;

-- --------------------------------------------------------

--
-- Table structure for table `acomments`
--

CREATE TABLE IF NOT EXISTS `acomments` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `answer_id` int(10) unsigned zerofill NOT NULL,
  `body` varchar(140) NOT NULL,
  `likes` int(11) NOT NULL DEFAULT '0',
  `time_posted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Triggers `acomments`
--
DROP TRIGGER IF EXISTS `add_acomment`;
DELIMITER //
CREATE TRIGGER `add_acomment` AFTER INSERT ON `acomments`
 FOR EACH ROW update answers
set comments = comments + 1
where _id = new.answer_id
//
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_acomment`;
DELIMITER //
CREATE TRIGGER `delete_acomment` AFTER DELETE ON `acomments`
 FOR EACH ROW update questions
set comments = comments - 1
where _id = old.answer_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `answers`
--

CREATE TABLE IF NOT EXISTS `answers` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned zerofill NOT NULL,
  `question_id` int(10) unsigned zerofill NOT NULL,
  `body` text NOT NULL,
  `time_posted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_edited` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_votes` int(11) NOT NULL DEFAULT '0',
  `comments` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`_id`),
  KEY `user_id` (`user_id`,`time_posted`,`time_edited`,`total_votes`,`comments`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='question inserted into database' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `answers`
--

INSERT INTO `answers` (`_id`, `user_id`, `question_id`, `body`, `time_posted`, `time_edited`, `total_votes`, `comments`) VALUES
(0000000001, 0000000001, 0000000002, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.9', '2014-01-21 21:26:25', '2014-01-21 21:26:25', 0, 0),
(0000000002, 0000000001, 0000000002, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-21 21:26:33', '2014-01-21 21:26:33', 0, 0),
(0000000003, 0000000002, 0000000003, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-21 22:10:43', '2014-01-21 22:10:43', 0, 0),
(0000000004, 0000000001, 0000000004, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-22 14:55:41', '2014-01-22 14:55:41', 0, 0),
(0000000005, 0000000001, 0000000005, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-29 13:59:07', '2014-01-29 13:59:07', 0, 0);

--
-- Triggers `answers`
--
DROP TRIGGER IF EXISTS `add_answer`;
DELIMITER //
CREATE TRIGGER `add_answer` AFTER INSERT ON `answers`
 FOR EACH ROW BEGIN
  UPDATE questions
     SET answers = answers + 1
   WHERE _id = NEW.question_id;

  UPDATE users
     SET answers = answers + 1
   WHERE _id = NEW.user_id;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_answer`;
DELIMITER //
CREATE TRIGGER `delete_answer` AFTER DELETE ON `answers`
 FOR EACH ROW update questions
set answers = answers - 1
where _id = old.question_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `avotes`
--

CREATE TABLE IF NOT EXISTS `avotes` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `answer_id` int(10) unsigned zerofill NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`_id`),
  KEY `question_id` (`answer_id`),
  KEY `type` (`type`),
  KEY `question_id_2` (`answer_id`),
  KEY `question_id_3` (`answer_id`),
  KEY `type_2` (`type`),
  KEY `question_id_4` (`answer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Triggers `avotes`
--
DROP TRIGGER IF EXISTS `add_avote`;
DELIMITER //
CREATE TRIGGER `add_avote` AFTER INSERT ON `avotes`
 FOR EACH ROW update answers
set total_votes = total_votes + new.type
where _id = new.answer_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `qcomments`
--

CREATE TABLE IF NOT EXISTS `qcomments` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `question_id` int(10) unsigned zerofill NOT NULL,
  `body` varchar(140) NOT NULL,
  `likes` int(11) NOT NULL DEFAULT '0',
  `time_posted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Triggers `qcomments`
--
DROP TRIGGER IF EXISTS `add_qcomment`;
DELIMITER //
CREATE TRIGGER `add_qcomment` AFTER INSERT ON `qcomments`
 FOR EACH ROW update questions
set comments = comments + 1
where _id = new.question_id
//
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_qcomment`;
DELIMITER //
CREATE TRIGGER `delete_qcomment` AFTER DELETE ON `qcomments`
 FOR EACH ROW update questions
set comments = comments - 1
where _id = old.question_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE IF NOT EXISTS `questions` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned zerofill NOT NULL,
  `body` text NOT NULL,
  `time_posted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_edited` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_votes` int(11) NOT NULL DEFAULT '0',
  `comments` int(11) NOT NULL DEFAULT '0',
  `views` int(11) NOT NULL DEFAULT '0',
  `answers` int(11) NOT NULL DEFAULT '0',
  `tags` varchar(200) NOT NULL,
  PRIMARY KEY (`_id`),
  KEY `user_id` (`user_id`,`time_posted`,`time_edited`,`total_votes`,`comments`,`answers`),
  KEY `views` (`views`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='question inserted into database' AUTO_INCREMENT=8 ;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`_id`, `user_id`, `body`, `time_posted`, `time_edited`, `total_votes`, `comments`, `views`, `answers`, `tags`) VALUES
(0000000001, 0000000001, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-21 20:42:30', '2014-01-21 20:42:30', 0, 0, 0, 0, 'firstpostfirstpost'),
(0000000002, 0000000001, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-21 21:22:33', '2014-01-21 21:22:33', 0, 0, 0, 2, 'wtp'),
(0000000003, 0000000002, '''\\/"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-21 21:38:45', '2014-01-21 21:38:45', 0, 0, 0, 1, 'ghhhj'),
(0000000004, 0000000001, 'uwalLorem ipsum dolor sit tusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-22 14:55:30', '2014-01-22 14:55:30', 0, 0, 0, 1, 'vfydfd'),
(0000000005, 0000000001, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-29 13:57:58', '2014-01-29 13:57:58', 0, 0, 0, 1, 'tkgxjfjjh'),
(0000000006, 0000000001, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-29 14:01:17', '2014-01-29 14:01:17', 0, 0, 0, 0, 'gjjfhj'),
(0000000007, 0000000001, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2014-01-29 14:01:22', '2014-01-29 14:01:22', 0, 0, 0, 0, 'fgycj');

--
-- Triggers `questions`
--
DROP TRIGGER IF EXISTS `add_question`;
DELIMITER //
CREATE TRIGGER `add_question` AFTER INSERT ON `questions`
 FOR EACH ROW update users
set questions = questions + 1
where _id = new.user_id
//
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_question`;
DELIMITER //
CREATE TRIGGER `delete_question` AFTER DELETE ON `questions`
 FOR EACH ROW update users
set questions = questions - 1
where _id = old.user_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `qvotes`
--

CREATE TABLE IF NOT EXISTS `qvotes` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `question_id` int(10) unsigned zerofill NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`_id`),
  KEY `question_id` (`question_id`),
  KEY `type` (`type`),
  KEY `question_id_2` (`question_id`),
  KEY `question_id_3` (`question_id`),
  KEY `type_2` (`type`),
  KEY `question_id_4` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Triggers `qvotes`
--
DROP TRIGGER IF EXISTS `add_qvote`;
DELIMITER //
CREATE TRIGGER `add_qvote` AFTER INSERT ON `qvotes`
 FOR EACH ROW update questions
set total_votes = total_votes + new.type
where _id = new.question_id
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `user_name` varchar(15) NOT NULL,
  `password` varchar(100) NOT NULL,
  `rep_points` int(11) NOT NULL DEFAULT '0',
  `questions` int(11) NOT NULL DEFAULT '0',
  `answers` int(11) NOT NULL DEFAULT '0',
  `votes` int(11) NOT NULL DEFAULT '0',
  `comments` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`_id`,`user_name`),
  UNIQUE KEY `_id` (`_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`_id`, `user_name`, `password`, `rep_points`, `questions`, `answers`, `votes`, `comments`) VALUES
(0000000001, 'Olayinka', 'c42e7e25a51df62627ebdb1f5e31c3a9491c2612b613867448f7dc00c2a07b38', 0, 6, 4, 0, 0),
(0000000002, 'emulator5554', '123aab4b138f1656b2ce053dba987566b40bf707f72530c9f29ff72f046de72b', 0, 1, 1, 0, 0),
(0000000003, 'random4ker', 'f11ebda3ae7cb77d3314afd534b8c54165d7e817e9fa3cbeeb72e4a36638f1b3', 0, 0, 0, 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
