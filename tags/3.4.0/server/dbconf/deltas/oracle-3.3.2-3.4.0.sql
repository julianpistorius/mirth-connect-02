CREATE TABLE CHANNEL_GROUP
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL UNIQUE,
	REVISION NUMBER(10),
	CHANNEL_GROUP CLOB)