create table post(
	id serial primary key,
	name text,
	post_desc text,
	link text unique,
	created date
);