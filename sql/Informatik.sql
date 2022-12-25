create table Informatik
(
    Title     varchar(150) not null,
    Author    varchar(600) null,
    Publisher varchar(200) null,
    Rating    int(1)       null,
    Subareas  varchar(200) null,
    constraint Informatik_pk
        primary key (Title)
);

