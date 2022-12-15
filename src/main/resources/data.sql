MERGE INTO genre (id, name)
    VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер')
         , (5, 'Документальный'), (6, 'Боевик');

MERGE INTO mpa (mpa_id, mpa_name)
    VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

update films f set rate = (select count(l.user_id) from film_likes l where l.film_id = f.id) where f.id = 1;
//