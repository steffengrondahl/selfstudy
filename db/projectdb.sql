USE projectdb;
DROP TABLE IF EXISTS dependency;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS priority;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS estimate;

CREATE TABLE estimate (id INT(11) NOT NULL, name VARCHAR(16), PRIMARY KEY(id)) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO estimate (id, name) values (1, 'Hours');  
INSERT INTO estimate (id, name) values (2, 'Days');  
INSERT INTO estimate (id, name) values (3, 'Weeks');  
INSERT INTO estimate (id, name) values (4, 'Months');   

CREATE TABLE status (id INT(11) NOT NULL, name VARCHAR(16), PRIMARY KEY(id)) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO status (id, name) values (1, 'Open');  
INSERT INTO status (id, name) values (2, 'Started');  
INSERT INTO status (id, name) values (3, 'Completed');  

CREATE TABLE priority (id INT(11) NOT NULL, name VARCHAR(16), PRIMARY KEY(id)) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO priority (id, name) values (1, 'Very low');  
INSERT INTO priority (id, name) values (2, 'Low');  
INSERT INTO priority (id, name) values (3, 'Medium');  
INSERT INTO priority (id, name) values (4, 'High');  
INSERT INTO priority (id, name) values (5, 'Very high');  

CREATE TABLE project (id INT(11) NOT NULL AUTO_INCREMENT, 
description VARCHAR(32) NOT NULL, goals LONGTEXT, actions LONGTEXT,
start_date DATE DEFAULT NULL, deadline DATE DEFAULT NULL, 
estimate_fk INT(11) NOT NULL, 
priority_fk INT(11) NOT NULL, status_fk INT(11) NOT NULL, 
PRIMARY KEY (id), 
FOREIGN KEY (priority_fk) REFERENCES priority (id) ON DELETE CASCADE ON UPDATE CASCADE, 
FOREIGN KEY (status_fk) REFERENCES status (id) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (estimate_fk) REFERENCES estimate (id) ON DELETE CASCADE ON UPDATE CASCADE) 
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE dependency (presupposed INT(11) NOT NULL, subsequent INT(11) NOT NULL, 
PRIMARY KEY (presupposed, subsequent),
FOREIGN KEY (presupposed) REFERENCES project (id) ON DELETE CASCADE ON UPDATE CASCADE, 
FOREIGN KEY (subsequent) REFERENCES project (id) ON DELETE CASCADE ON UPDATE CASCADE) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE hyperlink (id INT(11) NOT NULL AUTO_INCREMENT,
project_fk INT(11) NOT NULL, url VARCHAR(255),
PRIMARY KEY (id),
FOREIGN KEY (project_fk) REFERENCES project (id) ON DELETE CASCADE ON UPDATE CASCADE)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
