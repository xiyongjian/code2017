
create table users (
    id int(11) not null auto_increment,
    username varchar(45) not null,
    password varchar(45) not null,
    roles varchar(256),
    primary key (id),
    unique index (username)
    );

insert into users (username, password, roles) values 
    ('admin', 'passwrod', 'ADMIN,USER'),
    ('user', 'user', 'USER');