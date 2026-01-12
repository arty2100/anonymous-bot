create table if not exists message_to_admin (
                                                id bigserial primary key,

                                                message_id bigint not null,
                                                admin_id bigint not null,

                                                status varchar(32) not null,

    created_at timestamptz not null default now(),
    sent_at timestamptz,

    constraint fk_mta_message
    foreign key (message_id)
    references messages(id)
    on delete cascade,

    constraint fk_mta_admin
    foreign key (admin_id)
    references users(id)
    on delete cascade,

    constraint uq_message_admin
    unique (message_id, admin_id)
    );

create index if not exists idx_mta_status
    on message_to_admin(status);

create index if not exists idx_mta_admin
    on message_to_admin(admin_id);
