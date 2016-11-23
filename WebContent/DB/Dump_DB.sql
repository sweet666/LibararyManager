-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.1.53-community - MySQL Community Server (GPL)
-- ОС Сервера:                   Win64
-- HeidiSQL Версия:              9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Дамп структуры базы данных library
CREATE DATABASE IF NOT EXISTS `library` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `library`;


-- Дамп структуры для таблица library.authors
CREATE TABLE IF NOT EXISTS `authors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(200) NOT NULL,
  `lastName` varchar(200) NOT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы library.authors: ~7 rows (приблизительно)
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` (`id`, `firstName`, `lastName`, `middleName`) VALUES
	(5, 'Александр', 'Пушкин', ''),
	(6, 'Стивен', 'Кинг', NULL),
	(27, 'Джером', 'Сэлинджер', 'Дэвид'),
	(28, 'Рэй', 'Брэдбери', ''),
	(29, 'Аркадий И Борис', 'Стругацкие', ''),
	(30, 'Гарри', 'Гаррисон', ''),
	(31, 'Роджер', 'Желязны', '');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;


-- Дамп структуры для таблица library.books
CREATE TABLE IF NOT EXISTS `books` (
  `isbn` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  `edition` int(10) NOT NULL,
  `year` int(10) NOT NULL,
  `description` text,
  PRIMARY KEY (`isbn`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы library.books: ~4 rows (приблизительно)
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` (`isbn`, `title`, `author_id`, `edition`, `year`, `description`) VALUES
	('123-123-1233', 'Специалист по этике', 30, 1, 2016, 'Фантастика'),
	('333-333-444', 'Мир смерти', 30, 3, 1978, 'фантастика, science fiction'),
	('456-1234-2', 'Одиссея 2000', 28, 1, 1970, 'Фанстастика'),
	('665-3435-343', 'Стихи и поэмы', 5, 1, 2016, '');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;


-- Дамп структуры для таблица library.bookscopies
CREATE TABLE IF NOT EXISTS `bookscopies` (
  `serialNumber` varchar(255) NOT NULL,
  `isbn` varchar(255) NOT NULL,
  `bookCopyState` varchar(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `lastStateChange` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`serialNumber`),
  KEY `isbn` (`isbn`),
  KEY `lastStateChange` (`lastStateChange`),
  CONSTRAINT `books_copy_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `books` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Дамп данных таблицы library.bookscopies: ~9 rows (приблизительно)
/*!40000 ALTER TABLE `bookscopies` DISABLE KEYS */;
INSERT INTO `bookscopies` (`serialNumber`, `isbn`, `bookCopyState`, `username`, `lastStateChange`) VALUES
	('02f2dc2b-86c0-4c36-afe3-e97a90b44935', '456-1234-2', 'AVAILABLE', NULL, 1457931656),
	('1539519f-ea4b-4ce3-8072-e7614589b7bd', '123-123-1233', 'AVAILABLE', NULL, 1457931661),
	('22dedbbc-9f60-4dda-93b4-fadc45742d0b', '456-1234-2', 'AVAILABLE', NULL, 1457931657),
	('31104223-4bc7-4cce-8499-edc771c8cdc4', '123-123-1233', 'AVAILABLE', NULL, 1457931660),
	('750298d3-e9e2-4cbd-9b25-bb27f17ca630', '123-123-1233', 'AVAILABLE', NULL, 1457931651),
	('8199c3e9-dd12-4a3f-b30b-a9ba8fdb137e', '665-3435-343', 'AVAILABLE', NULL, 1457931672),
	('c2fe34bc-3dd2-4ba6-b904-2beb0390c924', '456-1234-2', 'AVAILABLE', NULL, 1457931649),
	('cf5c075a-fa17-48c1-b861-672588e7360a', '333-333-444', 'AVAILABLE', NULL, 1457931640),
	('e87caa37-0aac-4e8d-b152-0484f1e9a887', '123-123-1233', 'AVAILABLE', NULL, 1457931654);
/*!40000 ALTER TABLE `bookscopies` ENABLE KEYS */;


-- Дамп структуры для таблица library.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `lastLogin` bigint(20) NOT NULL DEFAULT '0',
  `passwordSalt` varchar(128) NOT NULL,
  `passwordHash` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы library.users: ~2 rows (приблизительно)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `email`, `role`, `lastLogin`, `passwordSalt`, `passwordHash`) VALUES
	(1, 'reader1', 'reader1@gmail.com', 'READER', 1457926858, '7cfeb082-6d7d-49e5-a1da-a085fcbd5389', 'lN4zz5TgO909yRCIhJAxeNiRKYtOeVwt55Tl8s8LNj8'),
	(2, 'librarian1', 'librarian1@gmail.com', 'LIBRARIAN', 1457926874, '85508b0c-7ae5-4d75-a51c-4a6c7b1d8282', 'NwZFmHLYYI6zs0CUhKgFIlvvgARyFMgoYd/+1TS/JgU');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
