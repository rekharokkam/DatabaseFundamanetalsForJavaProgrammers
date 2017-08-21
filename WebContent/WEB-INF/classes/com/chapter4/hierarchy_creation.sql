CREATE TABLE orgchart (
    name varchar (25) primary key,
    reportsTo varchar (25) references orgchart (name)
);

INSERT INTO orgchart VALUES('Carol',  NULL);

INSERT INTO orgchart VALUES('Alice', 'Carol');
INSERT INTO orgchart VALUES('Bob',   'Carol');
INSERT INTO orgchart VALUES('Ted',   'Carol');

INSERT INTO orgchart VALUES('Edna',  'Alice');
INSERT INTO orgchart VALUES('Joyce', 'Alice');
INSERT INTO orgchart VALUES('Edgar', 'Alice');

INSERT INTO orgchart VALUES('Al',    'Bob');
INSERT INTO orgchart VALUES('Maria', 'Bob');
INSERT INTO orgchart VALUES('Nick',  'Bob');

INSERT INTO orgchart VALUES('Ruth', 'Ted');
INSERT INTO orgchart VALUES('Ky',   'Ted');
INSERT INTO orgchart VALUES('Nina', 'Ted');

INSERT INTO orgchart VALUES('Joe', 'Edna');

INSERT INTO orgchart VALUES('Terry', 'Edgar');

INSERT INTO orgchart VALUES('Petra', 'Ky');


--Breadth-First Search (BFS)
WITH RECURSIVE
  under_carol(name, level) AS (
    VALUES('Carol',0)
    UNION ALL
    SELECT orgchart.name, under_carol.level + 1
      FROM orgchart JOIN under_carol ON orgchart.reportsTo = under_carol.name
     ORDER BY 2	   
  )
SELECT substr('..........', 1, level * 3) || name FROM under_carol;  /* || is string concatenation */


Depth-First Search (DFS)
/* depth-first search */

WITH RECURSIVE
  under_carol(name, level) AS (
    VALUES('Carol', 0)
    UNION ALL
    SELECT orgchart.name, under_carol.level+1
      FROM orgchart JOIN under_carol ON orgchart.reportsTo=under_carol.name
     ORDER BY 2 DESC                                                  
  )
SELECT SUBSTR('..........', 1, level * 3) || name FROM under_carol;   /* || is string concatenation */
