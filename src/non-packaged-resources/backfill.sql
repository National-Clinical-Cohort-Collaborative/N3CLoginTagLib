create view staging_simple as
select email_address,email,onboard_master.first_name,onboard_master.last_name from onboard_master,registration where lower(email_address) = lower(email)
union
select distinct email_address,email,onboard_master.first_name,onboard_master.last_name from onboard_master,registration where lower(aliases__) ~ lower(email)
;

create view simple as
select distinct * from staging_simple order by last_name,first_name
;

create view staging_simple2 as
select email_address,email,onboard_master.first_name,onboard_master.last_name
from onboard_master,registration
where email_address not in (select email_address from simple)
  and onboard_master.last_name=registration.last_name
  and onboard_master.first_name = registration.first_name
union
select * from simple
;

create view simple2 as
select distinct * from staging_simple2 order by last_name,first_name
;

create view staging_possible as
select email_address,email,onboard_master.first_name,onboard_master.last_name,registration.first_name
from onboard_master,registration
where email_address not in (select email_address from simple2)
  and onboard_master.last_name=registration.last_name
  and onboard_master.first_name ~ substring(registration.first_name from '^.') order by 4
;

create view staging_match as
select
	email_address,
	email
from simple2;

create view staging_no_match as
select
	email_address
from onboard_master
where email_address not in (select email_address from simple2)
;
