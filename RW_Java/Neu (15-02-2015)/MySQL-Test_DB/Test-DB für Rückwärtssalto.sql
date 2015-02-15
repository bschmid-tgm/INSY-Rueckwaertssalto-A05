CREATE DATABASE RW_Test_DB;
USE RW_Test_DB;

CREATE TABLE countries (
  code CHAR(2) PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE airports (
  airportcode CHAR(3) PRIMARY KEY,
  name VARCHAR(100),
  country CHAR(2) NOT NULL default '' REFERENCES countries(code),
  city VARCHAR(100)
);

CREATE TABLE airlines (
  id CHAR(2) NOT NULL,
  name VARCHAR(100) NOT NULL default '',
  country CHAR(2) NOT NULL default '' REFERENCES countries(code),
  PRIMARY KEY (id)
);

CREATE TABLE planes (
  id INT auto_increment,
  manufacturer VARCHAR(50) NOT NULL,
  type VARCHAR(50) NOT NULL,
  lengthoverall FLOAT,
  span FLOAT,
  maxspeed INT,
  PRIMARY KEY (id)
);

CREATE TABLE planefleet (
  airline CHAR(2), 
  plane INT,
  nr INT,
  bought DATE,
  price DECIMAL(11,2),
  PRIMARY KEY (airline, plane, nr),
  FOREIGN KEY (airline) REFERENCES airlines(id),
  FOREIGN KEY (plane) REFERENCES planes(id)
);

CREATE TABLE passengerplanes (
  id INT PRIMARY KEY,
  maxseats INT,
  seatsperrow SMALLINT,
  FOREIGN KEY (id) REFERENCES planes(id)
);

CREATE TABLE freightplanes (
  id INT PRIMARY KEY,
  maxcargo INT,
  FOREIGN KEY (id) REFERENCES planes(id)
);

CREATE TABLE flights (
  airline CHAR(2) NOT NULL default '',
  flightnr CHAR(3) NOT NULL default '',
  departure_time timestamp default '0000-00-00',
  departure_airport CHAR(3) default NULL,
  destination_time timestamp default '0000-00-00',
  destination_airport CHAR(3) default NULL,
  planetype INT,
  PRIMARY KEY (airline,flightnr),
  FOREIGN KEY (airline,planetype) REFERENCES planefleet(airline,plane),
  FOREIGN KEY (departure_airport) REFERENCES airports(airportcode),
  FOREIGN KEY (destination_airport) REFERENCES airports(airportcode)
);

CREATE TABLE passengers (
  id INT auto_increment,
  firstname VARCHAR(50),
  lastname VARCHAR(50),
  airline CHAR(2),
  flightnr CHAR(3),
  PRIMARY KEY (id),
  FOREIGN KEY (airline, flightnr) REFERENCES flights (airline, flightnr) ON UPDATE CASCADE
);

CREATE TABLE tickets (
  id INT auto_increment,
  passenger INT UNIQUE NOT NULL,
  issued DATE NOT NULL,
  rownr INT,
  seatposition CHAR(1),
  specialmenu VARCHAR(200),
  PRIMARY KEY (id),
  FOREIGN KEY (passenger) REFERENCES passengers(id)
);

