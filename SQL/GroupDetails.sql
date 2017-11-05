CREATE TABLE GROUPS (
  ID int(10) NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  owner int(10) NOT NULL,
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp ,
  created_by varchar(1255) DEFAULT NULL,
  updated_by varchar(1255) DEFAULT NULL,
  active tinyint(1) DEFAULT NULL,
  PRIMARY KEY (ID)
);

drop table groups;

ALTER TABLE GROUPS ADD CONSTRAINT uq_name_owner UNIQUE(name, owner);

CREATE TABLE GROUPSMEMBERS (
  ID int(10) NOT NULL AUTO_INCREMENT,
  groupid int(10) NOT NULL,
  userid int(10) NOT NULL,
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(1255) DEFAULT NULL,
  updated_by varchar(1255) DEFAULT NULL,
  active tinyint(1) DEFAULT NULL,
  PRIMARY KEY (ID)
);

ALTER TABLE GROUPSMEMBERS ADD CONSTRAINT uq_gpid_userid UNIQUE(ID, userid);