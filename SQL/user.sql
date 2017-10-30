CREATE TABLE `apiuser` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registrationCode` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;



CREATE TABLE `UserFriends` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `UserId` int(10) NOT NULL,
  `FriendId` int(10) NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;

ALTER TABLE UserFriends
  ADD CONSTRAINT uq_userfriends UNIQUE(UserId, FriendId);
