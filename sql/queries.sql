-- Question 1: Insert some data into a table

INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Question 2: Insert calculated data into a table

INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

-- Question 3: Update some existing data

UPDATE cd.facilities
    SET initialoutlay = 10000
    where facid = 1;

-- Question 4: Update a row based on the contents of another row

UPDATE cd.facilities facilities
    SET
        membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
        guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
    WHERE facilities.facid = 1;

-- Question 5: Delete all bookings

TRUNCATE cd.bookings;

-- Question 6: Delete a member from the cd.members table

DELETE FROM cd.members
    WHERE memid = 37;

-- Basics

-- Question 1: Control which rows are retrieved - part 2

SELECT facid,name,membercost,monthlymaintenance
    FROM cd.facilities
    WHERE
        membercost > 0
        AND membercost < monthlymaintenance/50;

-- Question 2: Basic string searches

SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

-- Question 3: Matching against multiple possible values

SELECT *
    FROM cd.facilities
    WHERE facid in (1,5);

-- Question 4: Working with dates

SELECT memid, surname, firstname, joindate
    FROM cd.members
    WHERE joindate >= '2012-09-01'::date;

-- Question 5: Combining results from multiple queries

SELECT surname
    FROM cd.members
UNION
SELECT name
    FROM cd.facilities;

-- Join

-- Question 1: Retrieve the start times of members' bookings

SELECT bookings.starttime
    FROM
        cd.bookings bookings
            INNER JOIN cd.members members
                   on members.memid = bookings.memid
    WHERE
        members.firstname='David'
        AND members.surname='Farrell';

-- Question 2: Work out the start times of bookings for tennis courts

SELECT bookings.starttime AS start, faculties.name AS name
	FROM
		cd.facilities faculties
		INNER JOIN cd.bookings bookings
			ON faculties.facid = bookings.facid
	WHERE
		faculties.name IN ('Tennis Court 2', 'Tennis Court 1') AND
		bookings.starttime >= '2012-09-21' AND
		bookings.starttime < '2012-09-22'
ORDER BY bookings.starttime;

-- Question 3: Produce a list of all members, along with their recommender

SELECT members.firstname AS member_firstname, members.surname AS member_surname, recommenders.firstname AS recommender_firstname, recommenders.surname AS recommender_surname
FROM
    cd.members members
    LEFT OUTER JOIN cd.members recommenders
        ON recommenders.memid = members.recommendedby
ORDER BY member_surname, member_firstname;

-- Question 4: Produce a list of all members who have recommended another member

SELECT DISTINCT recommenders.firstname AS firstname, recommenders.surname AS surname
FROM
    cd.members members
        INNER JOIN cd.members recommenders
                   ON recommenders.memid = members.recommendedby
ORDER BY surname, firstname;

-- Question 5: Produce a list of all members, along with their recommender, using no joins.

SELECT DISTINCT members.firstname || ' ' || members.surname AS member,
    (SELECT recommenders.firstname || ' ' || recommenders.surname AS recommender
        FROM cd.members recommenders
        WHERE recommenders.memid = members.recommendedby)
    FROM
        cd.members members
ORDER BY member;

-- Aggregation

-- Question 1: Count the number of recommendations each member makes

SELECT recommendedby, count(*)
    FROM cd.members
    WHERE recommendedby IS NOT NULL
    GROUP BY recommendedby
ORDER BY recommendedby;

-- Question 2: List the total slots booked per facility

SELECT facid, sum(slots) AS "Total Slots"
    FROM cd.bookings
    GROUP BY facid
ORDER BY facid;

-- Question 3: List the total slots booked per facility in a given month

SELECT facid, sum(slots) AS "Total Slots"
    FROM cd.bookings
    WHERE
        starttime >= '2012-09-01'
        AND starttime < '2012-10-01'
    GROUP BY facid
ORDER BY sum(slots);

-- Question 4: List the total slots booked per facility per month

SELECT facid, extract (month FROM starttime) AS month, sum(slots) AS "Total Slots"
    FROM cd.bookings
    WHERE extract(year FROM starttime) = 2012
    GROUP BY facid, month
ORDER BY facid, month;

-- Question 5: Find the count of members who have made at least one booking

SELECT count (DISTINCT memid) FROM cd.bookings

-- Question 6: List each member's first booking after September 1st 2012

SELECT members.surname, members.firstname, members.memid, min(bookings.starttime) AS starttime
    FROM cd.bookings bookings
    INNER JOIN cd.members members ON
        members.memid = bookings.memid
    WHERE starttime >= '2012-09-01'
    GROUP BY members.surname, members.firstname, members.memid
ORDER BY members.memid;

-- Question 7: Produce a list of member names, with each row containing the total member count

SELECT count(*) OVER(), firstname, surname
    FROM cd.members
ORDER BY joindate;


-- Question 8: Produce a numbered list of members

SELECT row_number() OVER (ORDER BY joindate), firstname, surname
    FROM cd.members
ORDER BY joindate;

-- Question 9: Output the facility id that has the highest number of slots booked, again

SELECT facid, total FROM
    (
        SELECT facid, sum(slots) total, rank() OVER (ORDER BY sum(slots) DESC) rank
        FROM cd.bookings
        GROUP BY facid
    )
    AS ranked
WHERE rank = 1;

-- String

-- Question 1: Format the names of members

SELECT surname || ', ' || firstname AS name FROM cd.members;

-- Question 2: Find telephone numbers with parentheses

SELECT memid, telephone FROM cd.members WHERE telephone ~ '[()]';

-- Question 3: Count the number of members whose surname starts with each letter of the alphabet

SELECT substr (members.surname,1,1) AS letter, count(*) AS count
    FROM cd.members members
    GROUP BY letter
    ORDER by letter;
