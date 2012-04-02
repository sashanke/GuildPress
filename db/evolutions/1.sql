# ItemLayout schema
 
# --- !Ups
INSERT INTO `ItemLayout` (`id`, `itemOrder`, `itemPlace`, `itemSide`, `itemSlotName`, `itemTextSide`, `isBottom`, `isLeft`, `isRight`) VALUES
	(1, 100, 0, 'left', 'head', 'left', NULL, b'1', NULL),
	(2, 110, 58, 'left', 'neck', 'left', NULL, b'1', NULL),
	(3, 120, 116, 'left', 'shoulder', 'left', NULL, b'1', NULL),
	(4, 130, 174, 'left', 'back', 'left', NULL, b'1', NULL),
	(5, 140, 232, 'left', 'chest', 'left', NULL, b'1', NULL),
	(6, 150, 290, 'left', 'shirt', 'left', NULL, b'1', NULL),
	(7, 160, 348, 'left', 'tabard', 'left', NULL, b'1', NULL),
	(8, 170, 406, 'left', 'wrist', 'left', NULL, b'1', NULL),
	(9, 180, 0, 'right', 'hands', 'right', NULL, NULL, b'1'),
	(10, 190, 58, 'right', 'waist', 'right', NULL, NULL, b'1'),
	(11, 200, 116, 'right', 'legs', 'right', NULL, NULL, b'1'),
	(12, 210, 174, 'right', 'feet', 'right', NULL, NULL, b'1'),
	(13, 220, 232, 'right', 'finger1', 'right', NULL, NULL, b'1'),
	(14, 230, 290, 'right', 'finger2', 'right', NULL, NULL, b'1'),
	(15, 240, 348, 'right', 'trinket1', 'right', NULL, NULL, b'1'),
	(16, 250, 406, 'right', 'trinket2', 'right', NULL, NULL, b'1'),
	(17, 260, 0, 'left', 'mainHand', 'right', b'1', NULL, NULL),
	(18, 270, 271, 'left', 'offHand', 'left', b'1', NULL, NULL),
	(19, 280, 448, 'left', 'ranged', 'left', b'1', NULL, NULL);