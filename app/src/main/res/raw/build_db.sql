CREATE TABLE Favorite (ID integer NOT NULL PRIMARY KEY, TYPE  integer NOT NULL, JSON  text NOT NULL);
CREATE TABLE Customers (ID integer NOT NULL PRIMARY KEY, cf_user_id integer NOT NULL UNIQUE, name text NOT NULL, logo text NOT NULL);