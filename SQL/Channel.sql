CREATE TABLE `channel` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;


CREATE TABLE `exmessage` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `channel_id` int(10) NOT NULL,
  `sequenceNo` int(10) NOT NULL,
  `httpmessagepath` varchar(1255) DEFAULT NULL,
  `localmessagepath` varchar(1255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;