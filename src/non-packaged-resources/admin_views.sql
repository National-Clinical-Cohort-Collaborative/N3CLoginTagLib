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
  WHERE
  	registration.enclave
  AND
  	institution != 'NIH'
  AND
  	(official_institution is null OR official_institution != 'login.gov')
  AND
  	institution NOT IN (SELECT incommon from n3c_admin.registration_remap)
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
  	registration.institution = registration_remap.incommon
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
   FROM n3c_admin.registration, n3c_admin.registration_domain_remap, n3c_admin.site_master
  WHERE
  	registration.enclave
  AND
  	substring(email from '@(.*)$')=email_domain
  AND
  	registration_domain_remap.ror=site_master.institutionid
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
and	institution='NIH'
and substring(official_full_name from '/([^)]+)') = nih_ic.ic;

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
FROM ror.organization,n3c_admin.registration,n3c_admin.site_master
WHERE name=official_institution
  AND institutionid=id
UNION
SELECT DISTINCT
	institutionid,
	official_institution,
	name,
	duaexecuted
FROM ror.organization,n3c_admin.registration_remap,n3c_admin.registration,n3c_admin.site_master
WHERE incommon=official_institution
  AND ror=name and institutionid=id
;
