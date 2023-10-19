SELECT gender_table.gender, COUNT(*) AS count
FROM superhero AS s
JOIN alignment AS a ON s.alignment_id = a.id
JOIN colour AS ec ON s.eye_colour_id = ec.id
JOIN colour AS hc ON s.hair_colour_id = hc.id
JOIN gender AS gender_table ON s.gender_id = gender_table.id
WHERE a.alignment = 'Good'
AND 
(
  (ec.colour = 'Green' OR hc.colour LIKE '%Red%' OR ec.colour = 'White')
  AND hc.colour NOT LIKE '%Black%'
)
GROUP BY gender_table.gender
ORDER BY gender_table.gender;

WITH publisher_superhero_counts AS
(
    SELECT p.publisher_name AS publisher, COUNT(s.id) AS superhero_count
    FROM superhero s
    INNER JOIN publisher p ON s.publisher_id = p.id
    WHERE s.publisher_id IS NOT NULL
    GROUP BY p.publisher_name
),
total_superheroes AS
( 
  SELECT COUNT(id) AS total_count
  FROM superhero
  WHERE publisher_id IS NOT NULL
)
SELECT
  psc.publisher,
  psc.superhero_count,
  ROUND((psc.superhero_count * 100.0) / tsh.total_count, 2) AS percentage
FROM publisher_superhero_counts psc
INNER JOIN total_superheroes tsh ON 1 = 1
WHERE (psc.superhero_count * 100.0) / tsh.total_count >= 10
ORDER BY percentage DESC;

WITH superheroes_with_race AS 
(
  SELECT s.superhero_name, s.race_id, s.height_cm, r.race,
    AVG(s.height_cm) OVER (PARTITION BY s.race_id) AS avg_height_race
  FROM superhero s
  LEFT JOIN race r ON s.race_id = r.id
  WHERE s.weight_kg < 80 AND s.race_id IS NOT NULL AND s.height_cm IS NOT NULL
),
superheroes_with_differential AS 
(
  SELECT superhero_name, race, height_cm, avg_height_race, height_cm - avg_height_race AS height_differential
  FROM superheroes_with_race
)
SELECT superhero_name, race, height_differential
FROM superheroes_with_differential
ORDER BY height_differential DESC
LIMIT 10;
