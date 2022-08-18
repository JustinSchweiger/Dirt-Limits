CREATE TABLE IF NOT EXISTS LIMITS
(
    limits_block       VARCHAR(50) NOT NULL,
    limits_displayName VARCHAR(50) NOT NULL,
    limits_amount      tinyint     NOT NULL,
    primary key (limits_block)
);

CREATE TABLE IF NOT EXISTS TRACKER
(
    tracker_id         INTEGER PRIMARY KEY auto_increment,
    tracker_playerUuid VARCHAR(36) NOT NULL,
    tracker_playerName VARCHAR(16) NOT NULL,
    limits_block       VARCHAR(50) NOT NULL,
    tracker_placed     tinyint     NOT NULL,
    FOREIGN KEY (limits_block) REFERENCES LIMITS (limits_block) ON DELETE CASCADE,
    CONSTRAINT tracker_constraint UNIQUE (tracker_playerUuid, limits_block)
);