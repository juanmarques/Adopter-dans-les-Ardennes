create table adopters
(
    id             BIGSERIAL primary key,
    process_number text not null,
    name           text not null,
    image_url      text,
    address        text,
    phone          text,
    email          text
);
