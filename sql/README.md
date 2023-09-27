# Introduction
In this project, several SQL tables, as well as queries on those tables, have been created. The tables are created in a PostgreSQL instance, managed from a Docker container. The queries may be executed outside the database, using ```queries.sql```. The intent is to illustrate the design of SQL tables, as well as how to design queries in PostgreSQL (and SQL in general), and what those queries do. There are several dozen queries. They demonstrate Data Query Language (DQL) features such as SELECT, and Data Manipulation Language (DML) features such as INSERT and DELETE. There are also several uses of Joins, with their different types being demonstrated.  

# SQL Queries

###### Table Setup (DDL)

    CREATE TABLE cd.members
        (
            memid integer NOT NULL,
            surname character varying(200) NOT NULL,
            firstname character varying(200) NOT NULL,
            address character varying(300) NOT NULL,
            zipcode integer NOT NULL,
            telephone character varying(20) NOT NULL,
            recommendedby integer,
            joindate timestamp NOT NULL,
            CONSTRAINT members_pk PRIMARY KEY (memid),
            CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) ON DELETE SET NULL
        );

    CREATE TABLE cd.facilities
        (
            facid integer NOT NULL, 
            name character varying(100) NOT NULL, 
            membercost numeric NOT NULL, 
            guestcost numeric NOT NULL, 
            initialoutlay numeric NOT NULL, 
            monthlymaintenance numeric NOT NULL, 
            CONSTRAINT facilities_pk PRIMARY KEY (facid)
        );

    CREATE TABLE cd.bookings
        (
            bookid integer NOT NULL, 
            facid integer NOT NULL, 
            memid integer NOT NULL, 
            starttime timestamp NOT NULL,
            slots integer NOT NULL,
            CONSTRAINT bookings_pk PRIMARY KEY (bookid),
            CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
            CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
        );


## Modifying Data

###### Question 1: Insert some data into a table

```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert calculated data into a table

```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;
```

###### Question 3: Update some existing data

```sql
UPDATE cd.facilities
    SET initialoutlay = 10000
    where facid = 1;
```

###### Question 4: Update a row based on the contents of another row

```sql
UPDATE cd.facilities facilities
    SET
        membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
        guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
    WHERE facilities.facid = 1;
```

###### Question 5: Delete all bookings

```sql
TRUNCATE cd.bookings;
```

###### Question 6: Delete a member from the cd.members table

```sql
DELETE FROM cd.members
    WHERE memid = 37;
```

## Basics

###### Question 1: Control which rows are retrieved - part 2

```sql
SELECT facid,name,membercost,monthlymaintenance
    FROM cd.facilities
    WHERE
        membercost > 0
        AND membercost < monthlymaintenance/50;
```

###### Question 2: Basic string searches

```sql
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%'
```

###### Question 3: Matching against multiple possible values

```sql
SELECT *
    FROM cd.facilities
    WHERE facid in (1,5);
```

###### Question 4: Working with dates

```sql
SELECT memid, surname, firstname, joindate
    FROM cd.members
    WHERE joindate >= '2012-09-01'::date;
```

###### Question 5: Combining results from multiple queries

```sql
SELECT surname
    FROM cd.members
UNION
SELECT name
    FROM cd.facilities;
```

## Join

###### Question 1: Retrieve the start times of members' bookings

```sql
SELECT bookings.starttime
    FROM
        cd.bookings bookings
            INNER JOIN cd.members members
                   on members.memid = bookings.memid
    WHERE
        members.firstname='David'
        AND members.surname='Farrell';
```

###### Question 2: Work out the start times of bookings for tennis courts

```sql
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
```

###### Question 3: Produce a list of all members, along with their recommender

```sql
SELECT members.firstname AS member_firstname, members.surname AS member_surname, recommenders.firstname AS recommender_firstname, recommenders.surname AS recommender_surname
FROM
    cd.members members
    LEFT OUTER JOIN cd.members recommenders
        ON recommenders.memid = members.recommendedby
ORDER BY member_surname, member_firstname;
```

###### Question 4: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT recommenders.firstname AS firstname, recommenders.surname AS surname
FROM
    cd.members members
        INNER JOIN cd.members recommenders
                   ON recommenders.memid = members.recommendedby
ORDER BY surname, firstname;
```

###### Question 5: Produce a list of all members, along with their recommender, using no joins.

```sql
SELECT DISTINCT members.firstname || ' ' || members.surname AS member,
    (SELECT recommenders.firstname || ' ' || recommenders.surname AS recommender
        FROM cd.members recommenders
        WHERE recommenders.memid = members.recommendedby)
    FROM
        cd.members members
ORDER BY member;
```

## Aggregation

###### Question 1: Count the number of recommendations each member makes

```sql
SELECT recommendedby, count(*)
    FROM cd.members
    WHERE recommendedby IS NOT NULL
    GROUP BY recommendedby
ORDER BY recommendedby;
```

###### Question 2: List the total slots booked per facility

```sql
SELECT facid, sum(slots) AS "Total Slots"
    FROM cd.bookings
    GROUP BY facid
ORDER BY facid;
```

###### Question 3: List the total slots booked per facility in a given month

```sql
SELECT facid, sum(slots) AS "Total Slots"
    FROM cd.bookings
    WHERE
        starttime >= '2012-09-01'
        AND starttime < '2012-10-01'
    GROUP BY facid
ORDER BY sum(slots);
```

###### Question 4: List the total slots booked per facility per month

```sql
SELECT facid, extract (month FROM starttime) AS month, sum(slots) AS "Total Slots"
    FROM cd.bookings
    WHERE extract(year FROM starttime) = 2012
    GROUP BY facid, month
ORDER BY facid, month
```

###### Question 5: Find the count of members who have made at least one booking

```sql
SELECT count (DISTINCT memid) FROM cd.bookings
```

###### Question 6: List each member's first booking after September 1st 2012

```sql
SELECT members.surname, members.firstname, members.memid, min(bookings.starttime) AS starttime
    FROM cd.bookings bookings
    INNER JOIN cd.members members ON
        members.memid = bookings.memid
    WHERE starttime >= '2012-09-01'
    GROUP BY members.surname, members.firstname, members.memid
ORDER BY members.memid;
```

###### Question 7: Produce a list of member names, with each row containing the total member count

```sql
SELECT count(*) OVER(), firstname, surname
    FROM cd.members
ORDER BY joindate
```

###### Question 8: Produce a numbered list of members

```sql
SELECT row_number() OVER (ORDER BY joindate), firstname, surname
    FROM cd.members
ORDER BY joindate
```

###### Question 9: Output the facility id that has the highest number of slots booked, again

```sql
SELECT facid, total FROM
    (
        SELECT facid, sum(slots) total, rank() OVER (ORDER BY sum(slots) DESC) rank
        FROM cd.bookings
        GROUP BY facid
    )
    AS ranked
WHERE rank = 1
```

## String

###### Question 1: Format the names of members

```sql
SELECT surname || ', ' || firstname AS name FROM cd.members;
```

###### Question 2: Find telephone numbers with parentheses

```sql
SELECT memid, telephone FROM cd.members WHERE telephone ~ '[()]';
```

###### Question 3: Count the number of members whose surname starts with each letter of the alphabet

```sql
SELECT substr (members.surname,1,1) AS letter, count(*) AS count
    FROM cd.members members
    GROUP BY letter
    ORDER by letter;
```
