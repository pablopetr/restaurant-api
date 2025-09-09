create table if not exists items (
    id uuid primary key,
    name varchar(255) not null,
    slug varchar(255),
    description text,
    price double precision,
    stock integer not null default 1,
    category_id uuid not null,
    created_at timestamp without time zone default now(),
    constraint fk_items_category
        foreign key (category_id)
        references categories (id)
);
