
-- Create tables section -------------------------------------------------

-- Table observer

CREATE TABLE observer(
  observer_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  name  Varchar2(100 ) NOT NULL,
  changed  Timestamp NOT NULL,
  entity_capacity_left  Integer NOT NULL,
  monitoring_type_id  Integer NOT NULL,
  location_id  Integer NOT NULL
)
;

-- Add keys for table observer

ALTER TABLE observer ADD CONSTRAINT  Unique_Identifier1_project  PRIMARY KEY ( observer_id )
;

-- Table entity

CREATE TABLE entity(
  entity_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  name  Varchar2(100 ) NOT NULL,
  uri  Varchar2(2048 ) NOT NULL,
  params  Varchar2(2048 ),
  users_id  Integer NOT NULL,
  monitoring_type_id  Integer NOT NULL
)
;

-- Add keys for table entity

ALTER TABLE entity ADD CONSTRAINT  Unique_Identifier2_project  PRIMARY KEY ( entity_id )
;

-- Table snapshot

CREATE TABLE snapshot(
  snapshot_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  timestamp  Timestamp NOT NULL,
  response_time  Integer,
  orig_return_value  Varchar2(250 ) NOT NULL,
  orig_return_desc  Varchar2(1000 ) NOT NULL,
  orig_status_id  Integer NOT NULL,
  orig_request  Varchar2(4000 ),
  orig_response  Varchar2(4000 ),
  observer_id  Integer NOT NULL,
  entity_id  Integer NOT NULL
)
;

-- Add keys for table snapshot

ALTER TABLE snapshot ADD CONSTRAINT  Unique_Identifier3_project  PRIMARY KEY ( snapshot_id )
;

-- Table monitoring_type

CREATE TABLE monitoring_type(
  monitoring_type_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  name  Varchar2(100 ) NOT NULL,
  day_price  Integer NOT NULL
)
;

-- Add keys for table monitoring_type

ALTER TABLE monitoring_type ADD CONSTRAINT  Unique_Identifier6_project  PRIMARY KEY ( monitoring_type_id )
;

-- Table location

CREATE TABLE location(
  location_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  country  Varchar2(100 ) NOT NULL,
  city  Varchar2(100 ) NOT NULL
)
;

-- Add keys for table location

ALTER TABLE location ADD CONSTRAINT  Unique_Identifier7_project  PRIMARY KEY ( location_id )
;

-- Table sniff_config

CREATE TABLE sniff_config(
  sniff_config_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  frequency  Integer NOT NULL,
  enabled  Char(1 ) NOT NULL,
  subscription_to  Timestamp,
  auto_subscribe  Char(1 ) NOT NULL,
  observer_id  Integer NOT NULL,
  entity_id  Integer NOT NULL,
  status_rules_id  Integer NOT NULL
)
;

-- Add keys for table sniff_config

ALTER TABLE sniff_config ADD CONSTRAINT  sniff_config_pk  PRIMARY KEY ( sniff_config_id )
;

-- Table status_rules

CREATE TABLE status_rules(
  status_rules_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  threshold_warning  Integer NOT NULL,
  threshold_error  Integer NOT NULL,
  quantity  Integer NOT NULL
)
;

-- Add keys for table status_rules

ALTER TABLE status_rules ADD CONSTRAINT  status_rules_pk  PRIMARY KEY ( status_rules_id )
;

-- Table users

CREATE TABLE users(
  users_id  Integer GENERATED BY DEFAULT ON NULL AS IDENTITY,
  name  Varchar2(50 ) NOT NULL,
  bundle  Integer NOT NULL
)
;

-- Add keys for table users

ALTER TABLE users ADD CONSTRAINT  Unique_Identifier146567  PRIMARY KEY ( users_id )
;

-- Create relationships section -------------------------------------------------

ALTER TABLE observer ADD CONSTRAINT  of_type1  FOREIGN KEY ( monitoring_type_id ) REFERENCES monitoring_type ( monitoring_type_id )
;

ALTER TABLE snapshot ADD CONSTRAINT  snap  FOREIGN KEY ( observer_id ) REFERENCES observer ( observer_id )
;

ALTER TABLE snapshot ADD CONSTRAINT  entity_snapshot  FOREIGN KEY ( entity_id ) REFERENCES entity ( entity_id )
;

ALTER TABLE observer ADD CONSTRAINT  observer_location  FOREIGN KEY ( location_id ) REFERENCES location ( location_id )
;

ALTER TABLE sniff_config ADD CONSTRAINT  Relationship1  FOREIGN KEY ( observer_id ) REFERENCES observer ( observer_id )
;

ALTER TABLE sniff_config ADD CONSTRAINT  Relationship2  FOREIGN KEY ( entity_id ) REFERENCES entity ( entity_id )
;

ALTER TABLE sniff_config ADD CONSTRAINT  Relationship3  FOREIGN KEY ( status_rules_id ) REFERENCES status_rules ( status_rules_id )
;

ALTER TABLE entity ADD CONSTRAINT  Relationship5  FOREIGN KEY ( users_id ) REFERENCES users ( users_id )
;

ALTER TABLE entity ADD CONSTRAINT  Relationship6  FOREIGN KEY ( monitoring_type_id ) REFERENCES monitoring_type ( monitoring_type_id )
;


