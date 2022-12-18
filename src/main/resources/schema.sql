create table IF NOT EXISTS mpa
(
    mpa_id integer primary key not null,
    mpa_name varchar(50) not null
);

create table IF NOT EXISTS genre
(
    id integer primary key not null,
    name varchar(50) not null
);

create table IF NOT EXISTS films
(
    id integer auto_increment primary key,
    name    varchar(50)  not null,
    description  varchar(200) not null,
    release_date date         not null,
    duration     integer      not null,
    mpa    integer      not null,
    rate         integer,
    constraint FILMS_RATING__FK
        foreign key (mpa) references mpa(mpa_id)
);

create table IF NOT EXISTS film_genres
(
    film_id integer not null references FILMS,
    genre_id integer not null references GENRE,
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

create table IF NOT EXISTS users
(
    id integer auto_increment primary key,
    name varchar(100),
    login varchar(50),
    email varchar(50) not null,
    birthday date not null
);

create table IF NOT EXISTS film_likes
(
    film_id integer not null references FILMS,
    user_id integer not null references USERS,
    PRIMARY KEY (USER_ID,FILM_ID)
);

create table if not exists FRIENDS (
    user_id   int not null references USERS,
    friend_id int not null references USERS,
    PRIMARY KEY (USER_ID,FRIEND_ID)
);