/*
 * Any time cost changes its tracked in cost changes table. 
 * Trigger is a good candidate to be used for auditing purposes and sometime notify external systems - update a file's time stamp and all systems watching this can be aware that something changed. 
 * 
 * A trigger is event driven.
 */

 
CREATE TABLE costchanges 
(
    costchange_id INTEGER PRIMARY KEY AUTOINCREMENT,
    oldcost DECIMAL NOT NULL,
    newcost DECIMAL NOT NULL,
    whenT DATE NOT NULL
);


CREATE TRIGGER costChangeTrackerTrigger UPDATE OF cost ON activities
BEGIN -- begin of a new transaction
    INSERT INTO costchanges (oldcost, newcost, whenT) values (old.cost, new.cost, CURRENT_TIMESTAMP);
END

