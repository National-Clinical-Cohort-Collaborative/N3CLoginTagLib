CREATE VIEW n3c_admin.staging_user_incommon AS
SELECT -- InCommon-federated
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    registration.institution,
    registration.orcid_id,
    registration.expertise,
    registration.therapeutic_area,
    false as citizen_scientist,
    false as international,
    registration.created,
    registration.updated
FROM n3c_admin.registration
WHERE registration.enclave
  AND institution != 'NIH'
  AND official_institution != ''
  AND official_institution is not null
  AND official_institution != 'login.gov'
  AND official_institution NOT IN (SELECT incommon from n3c_admin.registration_remap)
;

CREATE VIEW n3c_admin.staging_user_incommon_mismatch AS
SELECT -- InCommon-federated, but name mismatch
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    registration_remap.ror as institution,
    registration.orcid_id,
    registration.expertise,
    registration.therapeutic_area,
    false as citizen_scientist,
    false as international,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, n3c_admin.registration_remap
  WHERE
  	registration.enclave
  AND
  	registration.official_institution = registration_remap.incommon
;

CREATE VIEW n3c_admin.staging_user_non_incommon AS
SELECT -- not InCommon-federated, but a ROR organization
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    institutionname as institution,
    registration.orcid_id,
    registration.expertise,
    registration.therapeutic_area,
    false as citizen_scientist,
    false as international,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, n3c_admin.registration_domain_remap, n3c_admin.dua_master
  WHERE
  	registration.enclave
  AND
  	substring(email from '@(.*)$')=email_domain
  AND
  	registration_domain_remap.ror=dua_master.institutionid
;

CREATE VIEW n3c_admin.staging_user_citizen AS
SELECT -- citizen-scientist
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    null as institution,
    registration.orcid_id,
    registration.expertise,
    registration.therapeutic_area,
    true as citizen_scientist,
    false as international,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, n3c_admin.citizen_master
  WHERE
  	registration.enclave
  AND
  	date_of_dua_signed is not null
  AND
  	registration.email=citizen_master.email_address
;

CREATE VIEW n3c_admin.staging_user_nih AS
SELECT -- NIH personnel
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    nih_ic.title as institution,
    registration.orcid_id,
    registration.expertise,
    registration.therapeutic_area,
    false as citizen_scientist,
    false as international,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, nih_foa.nih_ic
where
	registration.enclave
and	email~'nih.gov$'
and substring(official_full_name from '/([^/)]+)') = nih_ic.ic;

CREATE VIEW n3c_admin.user_stats AS
SELECT
    'InCommon' as category,
    count(*)
FROM staging_user_incommon
UNION
SELECT
    'InCommon (name mismatch)' as category,
    count(*)
FROM staging_user_incommon_mismatch
UNION
SELECT
    'Non-InCommon' as category,
    count(*)
FROM staging_user_non_incommon
UNION
SELECT
    'Citizen Scientist' as category,
    count(*)
FROM staging_user_citizen
UNION
SELECT
    'NIH' as category,
    count(*)
FROM staging_user_nih
;

CREATE VIEW n3c_admin.dua_status AS
SELECT DISTINCT
	institutionid,
	official_institution,
	name,
	duaexecuted
FROM ror.organization,n3c_admin.registration,n3c_admin.dua_master
WHERE name=official_institution
  AND institutionid=id
UNION
SELECT DISTINCT
	institutionid,
	official_institution,
	name,
	duaexecuted
FROM ror.organization,n3c_admin.registration_remap,n3c_admin.registration,n3c_admin.dua_master
WHERE incommon=official_institution
  AND ror=name and institutionid=id
;

create view n3c_admin.user_org_map_step1 as
select  -- standard InCommon connection listed in DUA master
    email,
    id as ror_id,
    official_institution as ror_name
from n3c_admin.registration,ror.organization
where official_institution = name
  and official_institution  not in (select incommon from n3c_admin.registration_remap)
  and id in (select institutionid from n3c_admin.dua_master)
union
select  -- remapped InCommon connection
    email,
    id as ror_id,
    name as ror_name
from n3c_admin.registration, n3c_admin.registration_remap, ror.organization
where registration.official_institution = incommon
  and ror = name
;

create view n3c_admin.user_org_map as
select
    *,
    'InCommon' AS una_path
from n3c_admin.user_org_map_step1
union
select   -- standard InCommon connection not in DUA master
    email,
    id as ror_id,
    official_institution as ror_name,
    'InCommon' AS una_path
from n3c_admin.registration,ror.organization
where official_institution = name
  and email  not in (select email from n3c_admin.user_org_map_step1)
union
select
	email,
	institutionid as ror_id,
	institutionname as ror_name,
	'login.gov' AS una_path
from n3c_admin.registration,n3c_admin.registration_domain_remap,n3c_admin.dua_master
 where email ~ email_domain
   and ror = institutionid
union
select
	email,
	id as ror_id,
	name as ror_name,
	'NIH' AS una_path
from n3c_admin.registration,n3c_admin.registration_remap_nih,ror.organization
where email~'nih.gov$'
  and substring(official_full_name from '/([^/)]+)') = registration_remap_nih.ic
  and registration_remap_nih.ror_id = organization.id
;

create view n3c_admin.staging_membership as
select email, analytics, governance, harmonization, implementation, phenotype, synthetic
from registration
natural left join
(select email,joined as analytics from membership where label='analytics') as t1
natural left join
(select email,joined as governance from membership where label='governance') as t2
natural left join
(select email,joined as harmonization from membership where label='harmonization') as t3
natural left join
(select email,joined as implementation from membership where label='implementation') as t4
natural left join
(select email,joined as phenotype from membership where label='phenotype') as t5
natural left join
(select email,joined as synthetic from membership where label='synthetic') as t6
;

create view n3c_admin.gsuite_view as
select
	registration.email,
	official_first_name,
	official_last_name,
	first_name,
	last_name,
	ror_id,
	ror_name,
	duaexecuted as dua_executed,
	una_path,
	orcid_id,
	gsuite_email,
	slack_id,
	github_id,
	twitter_id,
	expertise,
	therapeutic_area,
	assistant_email,
	case when enclave then 'TRUE' else 'FALSE' end as enclave,
	case when workstreams then 'TRUE' else 'FALSE' end as workstreams,
	created,
	updated,
	official_full_name,
	official_institution,
	emailed,
	governance,
	phenotype,
	harmonization,
	analytics,
	synthetic,
	implementation
from
(	n3c_admin.registration natural join n3c_admin.staging_membership
left outer join
	n3c_admin.user_org_map
on registration.email=user_org_map.email
)
left outer join
	n3c_admin.dua_master
on ror_id = institutionid
order by last_name,first_name
;
