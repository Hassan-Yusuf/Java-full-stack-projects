DROP DATABASE IF EXISTS userDB;
CREATE DATABASE userDB;

USE userDB;

CREATE TABLE User(
userName CHAR(20) PRIMARY KEY NOT NULL,
Password VARCHAR(100) NOT NULL
);

CREATE TABLE Recipe(
recipeId CHAR(20) PRIMARY KEY NOT NULL
);

CREATE TABLE UserRecipe(
userName CHAR(20) NOT NULL,
recipeId CHAR(20) NOT NULL,
PRIMARY KEY PK_UserRecipe(userName,recipeId),
FOREIGN KEY FK_UserRecipe_User(userName)
REFERENCES User(userName),
FOREIGN KEY FK_UserRecipe_Recipe(recipeId)
REFERENCES Recipe(recipeId)
);

-- Test data

INSERT INTO user(userName, Password) VALUES('jojojaja', "pass1");
INSERT INTO user(userName, Password) VALUES('kappa', "pass2");
INSERT INTO Recipe values(10); -- Change this to actual recipeID later
INSERT INTO UserRecipe VALUES ('jojojaja',10);

