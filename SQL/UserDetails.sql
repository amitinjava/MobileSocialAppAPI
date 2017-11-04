CREATE TABLE USERDETAILS (
  ID int(10) NOT NULL AUTO_INCREMENT,
  userid int(10) NOT NULL,
  name varchar(1255) DEFAULT NULL,
  mobile varchar(1255) DEFAULT NULL,
  profilepix varchar(1255) DEFAULT NULL,
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(1255) DEFAULT NULL,
  updated_by varchar(1255) DEFAULT NULL,
  active tinyint(1) DEFAULT NULL,
  PRIMARY KEY (ID)
);

ALTER TABLE USERDETAILS ADD CONSTRAINT uq_userid UNIQUE(userid);