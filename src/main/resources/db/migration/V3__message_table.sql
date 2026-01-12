create table if not exists messages (
                                        id bigserial primary key,

                                        user_id bigint not null,
                                        text text not null,
                                        admin_reply text,

                                        status varchar(32) not null,

    created_at timestamptz not null default now(),
    answered_at timestamptz,

    constraint fk_messages_user
    foreign key (user_id)
    references users(id)
    on delete cascade
    );

create index if not exists idx_messages_user_id
    on messages(user_id);

create index if not exists idx_messages_status
    on messages(status);
