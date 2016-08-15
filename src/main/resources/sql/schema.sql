create table Todo (
	id identity,
	description varchar(2000),
	complete boolean
);

insert into Todo (description, complete) values ('something', false);
insert into Todo (description, complete) values ('another thing', false);
