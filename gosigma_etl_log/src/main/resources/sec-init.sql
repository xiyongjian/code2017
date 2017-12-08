
create table users (
    id int(11) not null auto_increment,
    username varchar(45) not null,
    password varchar(45) not null,
    primary key (id),
    unique index (username)
    );

insert into users (username, password) values 
    ('admin', 'passwrod'),
    ('user', 'password');