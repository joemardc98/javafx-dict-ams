-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.21-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for ams
CREATE DATABASE IF NOT EXISTS `ams` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ams`;

-- Dumping structure for table ams.attendee
CREATE TABLE IF NOT EXISTS `attendee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT '',
  `middle_initial` varchar(100) DEFAULT '',
  `last_name` varchar(100) DEFAULT '',
  `gender` varchar(10) DEFAULT '',
  `email` varchar(100) DEFAULT '',
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table ams.attendee: ~0 rows (approximately)
/*!40000 ALTER TABLE `attendee` DISABLE KEYS */;
INSERT INTO `attendee` (`id`, `first_name`, `middle_initial`, `last_name`, `gender`, `email`, `deleted_at`) VALUES
	(1, 'fdsf', NULL, 'sdfdsf', NULL, NULL, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `attendee` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
