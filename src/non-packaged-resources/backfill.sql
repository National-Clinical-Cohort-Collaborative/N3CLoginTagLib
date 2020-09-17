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
select distinct
	registration.email,
    official_first_name,
    official_last_name,
    registration.first_name,
    registration.last_name,
    institution,
    case when registration.orcid_id != '' then registration.orcid_id else onboard_master.orcid_id end as orcid_id,
    case when registration.gsuite_email != '' then registration.gsuite_email else onboard_master.google__g_suite_enabled_email_ end as gsuite_email,
    case when registration.slack_id != '' then registration.slack_id else onboard_master.slack_email_harmonized__ end as slack_id,
    case when registration.github_id != '' then registration.github_id else onboard_master.github_handle end as github_id,
    case when registration.twitter_id != '' then registration.twitter_id else onboard_master.twitter_handle end as twitter_id,
    expertise,
    therapeutic_area,
    registration.assistant_email,
    enclave,
    workstreams,
    created,
    updated,
    official_full_name,
    official_institution,
    emailed
from registration,simple2,onboard_master
where registration.email = simple2.email
  and simple2.email_address = onboard_master.email_address
;

create view staging_no_match as
select
	email_address
from onboard_master
where email_address not in (select email_address from simple2)
;
