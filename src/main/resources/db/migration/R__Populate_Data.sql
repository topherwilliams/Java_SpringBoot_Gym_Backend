-- ${flyway:timestamp}

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Class_Booking;
DROP TABLE IF EXISTS Fitness_Class;
DROP TABLE IF EXISTS Instructor;
DROP TABLE IF EXISTS Workout_Exercise;
DROP TABLE IF EXISTS Workout;
DROP TABLE IF EXISTS Member;

CREATE TABLE Member (
    id integer primary key auto_increment,
    email_address varchar(100) not null,
    username varchar(100) not null,
    member_name varchar(100) not null,
    password varchar(100),
    token varchar(200)
);

CREATE TABLE Workout (
    id integer primary key auto_increment,
    uuid varchar(100) not null,
    member_id integer not null,
    foreign key (member_id) references Member(id)
);

CREATE TABLE Workout_Exercise (
     id integer primary key auto_increment,
     workout_id int not null,
     exercise_name varchar(50) not null,
     weight_kg int not null,
     reps int not null,
     exercise_sets int not null,
     foreign key (workout_id) references Workout(id)
);

CREATE TABLE Instructor (
    id integer primary key auto_increment,
    instructor_name VARCHAR(100) NOT NULL,
    email varchar(100) not null,
    password varchar(100),
    token varchar(200)
);

CREATE TABLE Fitness_Class (
    id integer primary key auto_increment,
    uuid varchar(100) not null,
    instructor_id integer not null,
    class_name varchar(50) not null,
    duration integer not null,
    spaces integer not null,
    booked_spaces integer not null,
    class_date date not null,
    foreign key (instructor_id) references Instructor(id)
);

CREATE TABLE Class_Booking (
    id integer primary key auto_increment,
    class_id integer not null,
    member_id integer not null,
    foreign key (class_id) references Fitness_Class(id),
    foreign key (member_id) references Member(id)
);

INSERT INTO Instructor(instructor_name, email, password)
VALUES ('Instructor 1', 'email@one.com', 'password1'),
       ('Instructor 2', 'email@one.com',  'password2'),
       ('Instructor 3', 'email@one.com',  'password3');

insert into Fitness_Class(instructor_id, uuid, class_name, duration, spaces, booked_spaces, class_date)
values (1, '4b102ba9-9ef6-4e17-b196-3f6b9ab82dcd' , 'Pilates', 45, 20, 2, DATE '2023-12-01'),
       (1,'6ca59bde-d2b4-4fd9-8c7e-1fe7bc268182', 'Yoga', 60, 20, 0, DATE '2023-12-02'),
       (1, 'e601b1e2-70de-47ba-8811-f06df6b27ce2', 'Pilates', 45, 20, 20, DATE '2023-12-05'),
       (2, 'a411e732-908c-4ed9-8e16-5e7387d24bc7', 'Spin', 60, 10, 0, DATE '2023-12-01'),
       (2, 'fe4d3903-fe05-454b-9bee-32da0969d965', 'WeightLifting', 60, 30, 1, DATE '2023-12-08');

insert into Member(email_address, username, member_name, password)
values ('email_one@email.com', 'username1', 'Member One', 'password1'),
       ('email_two@email.com', 'username2', 'Member Two', 'password2'),
       ('email_three@email.com', 'username3', 'Member Three', 'password3'),
       ('email_four@email.com', 'username4', 'Member Four', 'password4'),
       ('email_five@email.com', 'username5', 'Member Five', 'password5');

insert into Class_Booking(class_id, member_id)
values (1, 1),
       (1, 2),
       (5, 3);

insert into Workout(uuid, member_id)
values ('f44ef031-e14c-49d6-a2c9-d8b1e807e6bd', 1),
        ('9b6ab36a-88e5-4ac4-ab1f-bab5263cb0d6', 2),
        ('3bdd88ad-8a92-4101-bfb4-e61387b952c9', 1);
