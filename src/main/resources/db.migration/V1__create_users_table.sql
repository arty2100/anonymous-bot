create table users (
                       id bigserial primary key,

                       telegram_id bigint not null unique,
                       username varchar(64),
                       first_name varchar(128),
                       last_name varchar(128),

                       type varchar(32) not null,
                       status varchar(32) not null,

                       created_at timestamptz not null default now(),
                       updated_at timestamptz not null default now()
);

create index idx_users_telegram_id on users (telegram_id);