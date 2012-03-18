-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               5.5.21 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2012-03-18 14:38:50
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping structure for table dev_guildpress.itemlayout
DROP TABLE IF EXISTS `itemlayout`;
CREATE TABLE IF NOT EXISTS `itemlayout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemOrder` bigint(20) DEFAULT NULL,
  `itemPlace` bigint(20) DEFAULT NULL,
  `itemSide` varchar(255) DEFAULT NULL,
  `itemSlotName` varchar(255) DEFAULT NULL,
  `itemTextSide` varchar(255) DEFAULT NULL,
  `isBottom` bit(1) DEFAULT NULL,
  `isLeft` bit(1) DEFAULT NULL,
  `isRight` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- Dumping data for table dev_guildpress.itemlayout: ~19 rows (approximately)
/*!40000 ALTER TABLE `itemlayout` DISABLE KEYS */;
INSERT INTO `itemlayout` (`id`, `itemOrder`, `itemPlace`, `itemSide`, `itemSlotName`, `itemTextSide`, `isBottom`, `isLeft`, `isRight`) VALUES
	(1, 100, 0, 'left', 'head', 'left', NULL, b'10000000', NULL),
	(2, 110, 58, 'left', 'neck', 'left', NULL, b'10000000', NULL),
	(3, 120, 116, 'left', 'shoulder', 'left', NULL, b'10000000', NULL),
	(4, 130, 174, 'left', 'back', 'left', NULL, b'10000000', NULL),
	(5, 140, 232, 'left', 'chest', 'left', NULL, b'10000000', NULL),
	(6, 150, 290, 'left', 'shirt', 'left', NULL, b'10000000', NULL),
	(7, 160, 348, 'left', 'tabard', 'left', NULL, b'10000000', NULL),
	(8, 170, 406, 'left', 'wrist', 'left', NULL, b'10000000', NULL),
	(9, 180, 0, 'right', 'hands', 'right', NULL, NULL, b'10000000'),
	(10, 190, 58, 'right', 'waist', 'right', NULL, NULL, b'10000000'),
	(11, 200, 116, 'right', 'legs', 'right', NULL, NULL, b'10000000'),
	(12, 210, 174, 'right', 'feet', 'right', NULL, NULL, b'10000000'),
	(13, 220, 232, 'right', 'finger1', 'right', NULL, NULL, b'10000000'),
	(14, 230, 290, 'right', 'finger2', 'right', NULL, NULL, b'10000000'),
	(15, 240, 348, 'right', 'trinket1', 'right', NULL, NULL, b'10000000'),
	(16, 250, 406, 'right', 'trinket2', 'right', NULL, NULL, b'10000000'),
	(17, 260, 0, 'left', 'mainHand', 'right', b'10000000', NULL, NULL),
	(18, 270, 271, 'left', 'offHand', 'left', b'10000000', NULL, NULL),
	(19, 280, 448, 'left', 'ranged', 'left', b'10000000', NULL, NULL);
/*!40000 ALTER TABLE `itemlayout` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
