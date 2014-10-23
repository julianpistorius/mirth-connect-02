CREATE TABLE SCHEMA_INFO (VERSION VARCHAR(40)) ENGINE=InnoDB;

CREATE TABLE EVENT (
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	DATE_CREATED TIMESTAMP NULL DEFAULT NULL,
	NAME LONGTEXT NOT NULL,
	EVENT_LEVEL VARCHAR(40) NOT NULL,
	OUTCOME VARCHAR(40) NOT NULL,
	ATTRIBUTES LONGTEXT,
	USER_ID INTEGER NOT NULL,
	IP_ADDRESS VARCHAR(40)
) ENGINE=InnoDB;

CREATE TABLE CHANNEL (
	ID CHAR(36) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	REVISION INTEGER,
	CHANNEL LONGTEXT
) ENGINE=InnoDB;

CREATE TABLE SCRIPT (
	GROUP_ID VARCHAR(40) NOT NULL,
	ID VARCHAR(40) NOT NULL,
	SCRIPT LONGTEXT,
	PRIMARY KEY(GROUP_ID, ID)
) ENGINE=InnoDB;

CREATE TABLE PERSON (
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	USERNAME VARCHAR(40) NOT NULL,
	FIRSTNAME VARCHAR(40),
	LASTNAME VARCHAR(40),
	ORGANIZATION VARCHAR(255),
	INDUSTRY VARCHAR(255),
	EMAIL VARCHAR(255),
	PHONENUMBER VARCHAR(40),
	DESCRIPTION VARCHAR(255),
	LAST_LOGIN TIMESTAMP NULL DEFAULT NULL,
	GRACE_PERIOD_START TIMESTAMP NULL DEFAULT NULL,
	LOGGED_IN BIT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE PERSON_PREFERENCE
	(PERSON_ID INTEGER NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	VALUE LONGTEXT,
	CONSTRAINT PERSON_ID_PERSON_PREF_FK FOREIGN KEY(PERSON_ID) REFERENCES PERSON(ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX PERSON_PREFERENCE_INDEX1 ON PERSON_PREFERENCE(PERSON_ID);

CREATE TABLE PERSON_PASSWORD (
	PERSON_ID INTEGER NOT NULL,
	PASSWORD VARCHAR(255) NOT NULL,
	PASSWORD_DATE TIMESTAMP NULL DEFAULT NULL,
	CONSTRAINT PERSON_ID_PP_FK FOREIGN KEY(PERSON_ID) REFERENCES PERSON(ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ALERT (
	ID VARCHAR(36) NOT NULL PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL UNIQUE,
	ALERT LONGTEXT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE CODE_TEMPLATE (
	ID VARCHAR(255) NOT NULL PRIMARY KEY,
	CODE_TEMPLATE LONGTEXT
) ENGINE=InnoDB;		

CREATE TABLE CONFIGURATION (
	CATEGORY VARCHAR(255) NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	VALUE LONGTEXT
) ENGINE=InnoDB;

INSERT INTO PERSON (USERNAME, LOGGED_IN) VALUES('admin', FALSE);

INSERT INTO PERSON_PASSWORD (PERSON_ID, PASSWORD) VALUES(1, 'YzKZIAnbQ5m+3llggrZvNtf5fg69yX7pAplfYg0Dngn/fESH93OktQ==');

INSERT INTO SCHEMA_INFO (VERSION) VALUES ('3.2.0');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'stats.enabled', '1');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'server.resetglobalvariables', '1');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'smtp.timeout', '5000');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'smtp.auth', '0');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'smtp.secure', '0');

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'server.queuebuffersize', '1000');