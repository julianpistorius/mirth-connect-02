DROP TABLE SCHEMA_INFO;

CREATE TABLE SCHEMA_INFO
	(VERSION VARCHAR(40));

DROP TABLE EVENT;

CREATE TABLE EVENT
	(ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	EVENT CLOB NOT NULL,
	EVENT_LEVEL VARCHAR(40) NOT NULL,
	DESCRIPTION CLOB,
	ATTRIBUTES CLOB);

ALTER TABLE MESSAGE DROP FOREIGN KEY CHANNEL_ID_FK;

ALTER TABLE CHANNEL_STATISTICS DROP FOREIGN KEY CHANNEL_STATS_ID_FK;

DROP TABLE CHANNEL;

CREATE TABLE CHANNEL
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	DESCRIPTION CLOB,
	IS_ENABLED SMALLINT,
	VERSION VARCHAR(40),
	REVISION INTEGER,
	LAST_MODIFIED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	SOURCE_CONNECTOR CLOB,
	DESTINATION_CONNECTORS CLOB,
	PROPERTIES CLOB,
	PREPROCESSING_SCRIPT CLOB,
	POSTPROCESSING_SCRIPT CLOB,
	DEPLOY_SCRIPT CLOB,
	SHUTDOWN_SCRIPT CLOB);

DROP TABLE CHANNEL_STATISTICS;

CREATE TABLE CHANNEL_STATISTICS
	(SERVER_ID VARCHAR(255) NOT NULL,
	CHANNEL_ID VARCHAR(255) NOT NULL,
	RECEIVED INTEGER,
	FILTERED INTEGER,
	SENT INTEGER,
	ERROR INTEGER,
	QUEUED INTEGER,
	ALERTED INTEGER,
	PRIMARY KEY(SERVER_ID, CHANNEL_ID),
	CONSTRAINT CHANNEL_STATS_ID_FK FOREIGN KEY(CHANNEL_ID) REFERENCES CHANNEL(ID) ON DELETE CASCADE);

DROP TABLE ATTACHMENT;

CREATE TABLE ATTACHMENT
    (ID VARCHAR(255) NOT NULL PRIMARY KEY,
     MESSAGE_ID VARCHAR(255) NOT NULL,
     ATTACHMENT_DATA BLOB,
     ATTACHMENT_SIZE INTEGER,
     ATTACHMENT_TYPE VARCHAR(40));

DROP TABLE MESSAGE;

CREATE TABLE MESSAGE
	(SEQUENCE_ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
	ID VARCHAR(255) NOT NULL,
	SERVER_ID VARCHAR(255) NOT NULL,
	CHANNEL_ID VARCHAR(255) NOT NULL,
	SOURCE VARCHAR(255),
	TYPE VARCHAR(255),
	DATE_CREATED TIMESTAMP NOT NULL,
	VERSION VARCHAR(40),
	IS_ENCRYPTED SMALLINT NOT NULL,
	STATUS VARCHAR(40),
	RAW_DATA CLOB,
	RAW_DATA_PROTOCOL VARCHAR(40),
	TRANSFORMED_DATA CLOB,
	TRANSFORMED_DATA_PROTOCOL VARCHAR(40),
	ENCODED_DATA CLOB,
	ENCODED_DATA_PROTOCOL VARCHAR(40),
	CONNECTOR_MAP CLOB,
	CHANNEL_MAP CLOB,
	RESPONSE_MAP CLOB,
	CONNECTOR_NAME VARCHAR(255),
	ERRORS CLOB,
	CORRELATION_ID VARCHAR(255),
	ATTACHMENT SMALLINT,
	UNIQUE (ID),
	CONSTRAINT CHANNEL_ID_FK FOREIGN KEY(CHANNEL_ID) REFERENCES CHANNEL(ID) ON DELETE CASCADE);

CREATE INDEX MESSAGE_INDEX1 ON MESSAGE(CHANNEL_ID, DATE_CREATED);

CREATE INDEX MESSAGE_INDEX2 ON MESSAGE(CHANNEL_ID, DATE_CREATED, CONNECTOR_NAME);

CREATE INDEX MESSAGE_INDEX3 ON MESSAGE(CHANNEL_ID, DATE_CREATED, RAW_DATA_PROTOCOL);

CREATE INDEX MESSAGE_INDEX4 ON MESSAGE(CHANNEL_ID, DATE_CREATED, SOURCE);

CREATE INDEX MESSAGE_INDEX5 ON MESSAGE(CHANNEL_ID, DATE_CREATED, STATUS);

CREATE INDEX MESSAGE_INDEX6 ON MESSAGE(CHANNEL_ID, DATE_CREATED, TYPE);
	
DROP TABLE SCRIPT;

CREATE TABLE SCRIPT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	SCRIPT CLOB);

DROP TABLE TEMPLATE;

CREATE TABLE TEMPLATE
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	TEMPLATE CLOB);
	
DROP TABLE PERSON;

CREATE TABLE PERSON
	(ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
	USERNAME VARCHAR(40) NOT NULL,
	PASSWORD VARCHAR(40) NOT NULL,
	SALT VARCHAR(40) NOT NULL,
	FIRSTNAME VARCHAR(40),
	LASTNAME VARCHAR(40),
	ORGANIZATION VARCHAR(255),
	EMAIL VARCHAR(255),
	PHONENUMBER VARCHAR(40),
	DESCRIPTION VARCHAR(255),
	LAST_LOGIN TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	LOGGED_IN SMALLINT NOT NULL);

DROP TABLE ALERT;

CREATE TABLE ALERT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	IS_ENABLED SMALLINT NOT NULL,
	EXPRESSION CLOB,
	TEMPLATE CLOB);

DROP TABLE CODE_TEMPLATE;

CREATE TABLE CODE_TEMPLATE
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	CODE_SCOPE VARCHAR(40) NOT NULL,
	CODE_TYPE VARCHAR(40) NOT NULL,
	TOOLTIP VARCHAR(255) NOT NULL,
	CODE CLOB);	
	
DROP TABLE CHANNEL_ALERT;

CREATE TABLE CHANNEL_ALERT
	(CHANNEL_ID VARCHAR(255) NOT NULL,
	ALERT_ID VARCHAR(255) NOT NULL,
	CONSTRAINT ALERT_ID_CA_FK FOREIGN KEY(ALERT_ID) REFERENCES ALERT(ID) ON DELETE CASCADE);

DROP TABLE ALERT_EMAIL;

CREATE TABLE ALERT_EMAIL
	(ALERT_ID VARCHAR(255) NOT NULL,
	EMAIL VARCHAR(255) NOT NULL,
	CONSTRAINT ALERT_ID_AE_FK FOREIGN KEY(ALERT_ID) REFERENCES ALERT(ID) ON DELETE CASCADE);

DROP TABLE CONFIGURATION;

CREATE TABLE CONFIGURATION
	(ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	DATA CLOB NOT NULL);
	
DROP TABLE ENCRYPTION_KEY;

CREATE TABLE ENCRYPTION_KEY
	(DATA CLOB NOT NULL);

DELETE TABLE PREFERENCES;

CREATE TABLE PREFERENCES
	(PERSON_ID INTEGER NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	VALUE VARCHAR(255),
	CONSTRAINT PERSON_ID_PR_FK FOREIGN KEY(PERSON_ID) REFERENCES PERSON(ID));

INSERT INTO PERSON (USERNAME, PASSWORD, SALT, LOGGED_IN) VALUES('admin', 'NdgB6ojoGb/uFa5amMEyBNG16mE=', 'Np+FZYzu4M0=', 0);
INSERT INTO SCHEMA_INFO (VERSION) VALUES ('4');