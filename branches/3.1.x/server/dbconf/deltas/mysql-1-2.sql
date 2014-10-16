ALTER TABLE CHANNEL ADD COLUMN LAST_MODIFIED TIMESTAMP DEFAULT NOW()

UPDATE CHANNEL SET LAST_MODIFIED = NOW()

ALTER TABLE PERSON ADD COLUMN LAST_LOGIN TIMESTAMP DEFAULT NOW()

UPDATE PERSON SET LAST_LOGIN = NOW()

ALTER TABLE MESSAGE ADD COLUMN ATTACHMENT SMALLINT DEFAULT 0

UPDATE MESSAGE SET ATTACHMENT = 0

CREATE TABLE ATTACHMENT
    (ID VARCHAR(255) NOT NULL PRIMARY KEY,
     MESSAGE_ID VARCHAR(255) NOT NULL,
     ATTACHMENT_DATA LONGBLOB,
     ATTACHMENT_SIZE INTEGER,
     ATTACHMENT_TYPE VARCHAR(40))
