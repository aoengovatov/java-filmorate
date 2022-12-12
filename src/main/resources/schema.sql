create table IF NOT EXISTS mpa
(
    id integer primary key not null,
    name varchar(50) not null
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
        foreign key (mpa) references mpa(id)
);

create table IF NOT EXISTS film_genres
(
    id integer auto_increment primary key,
    film_id integer not null,
    genre_id integer not null,
    constraint FILM_GENRES_GENRE__FK
        foreign key (genre_id) references genre(id),
    constraint FILM_GENRES_FILMS__FK
        foreign key (film_id) references films(id)
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
    id integer auto_increment primary key,
    film_id integer not null,
    user_id integer not null,
    constraint FILM_LIKES_FILMS__FK
        foreign key (film_id) references films(id) ON DELETE CASCADE,
    constraint FILM_LIKES_USERS__FK
        foreign key (user_id) references users(id)
);

create table IF NOT EXISTS friends
(
    id integer auto_increment primary key,
    user_id integer not null,
    friend_id integer not null,
    status boolean not null,
    constraint FRIENDS_USERS__FK
        foreign key (user_id) references users(id),
    constraint FRIENDS_USERS_FRIENDS_FK
        foreign key (friend_id) references users(id)
);