create table booking (
                         client_name varchar(255),
                         client_phone_number varchar(20),
                         client_email varchar(255),
                         room_number int,
                         room_description text,
                         booking_date date default current_date
);