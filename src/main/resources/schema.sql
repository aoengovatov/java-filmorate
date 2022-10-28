create table IF NOT EXISTS rating
(
    rating_id integer primary key not null,
    rating_name varchar(50) not null
);

create table IF NOT EXISTS genre
(
    genre_id integer primary key not null,
    genre_name varchar(50) not null
);

create table IF NOT EXISTS films
(
    id      integer      auto_increment
                primary key,
    name    varchar(50)  not null,
    description  varchar(200) not null,
    release_date date         not null,
    duration     integer      not null,
    rating_id    integer      not null,
    rate         integer,
    constraint FILMS_RATING__FK
        foreign key (rating_id) references rating
);

create table IF NOT EXISTS films_category
(
    film_id integer primary key not null,
    genre_id integer not null,
    constraint FILMS_CATEGORY_GENRE__FK
        foreign key (genre_id) references genre,
    constraint FILMS_CATEGORY_FILMS__FK
        foreign key (film_id) references films(id)
);

create table IF NOT EXISTS users
(
    id integer auto_increment
        primary key not null,
    name varchar(100),
    login varchar(50),
    email varchar(50) not null,
    birthday date not null
);

create table IF NOT EXISTS film_likes
(
    film_id integer primary key not null,
    user_id integer not null,
    constraint FILMS_LIKES_FILMS__FK
        foreign key (film_id) references films(id),
    constraint FILMS_LIKES_USERS__FK
        foreign key (user_id) references users(id)
);

create table IF NOT EXISTS friends
(
    user_id integer not null,
    friend_id integer not null,
    status boolean not null,
    constraint FRIENDS_USERS__FK
        foreign key (user_id) references users(id),
    constraint FRIENDS_USERS_FRIENDS_FK
        foreign key (friend_id) references users (id)
);