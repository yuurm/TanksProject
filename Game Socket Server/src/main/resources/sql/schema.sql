drop table if exists shot;
drop table if exists game;
drop table if exists player;

create table player
(
    id              bigserial primary key,
    ip              varchar(100),
    name            varchar(100),
    points          integer default 0,
    max_wins_count  integer default 0,
    max_loses_count integer default 0
);

create table game
(
    id                        bigserial primary key,
    dateTime                  varchar(100),
    player_first              bigint,
    player_second             bigint,
    player_first_shots_count  integer,
    player_second_shots_count integer,
    second_game_time_amount   bigint,
    foreign key (player_first) references player (id),
    foreign key (player_second) references player (id)
);

create table shot
(
    id       bigserial primary key,
    dateTime varchar(100),
    shooter  bigint,
    target   bigint,
    foreign key (shooter) references player (id),
    foreign key (target) references player (id)
);

alter table shot add game_id bigint;
alter table shot add foreign key (game_id) references game(id);
