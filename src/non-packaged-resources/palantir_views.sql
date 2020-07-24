CREATE VIEW palantir.n3c_user AS
SELECT
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    registration.institution,
    registration.orcid_id,
    registration.gsuite_email,
    registration.slack_id,
    registration.github_id,
    registration.twitter_id,
    registration.expertise,
    registration.therapeutic_area,
    registration.assistant_email,
    registration.enclave,
    registration.workstreams,
    registration.created,
    registration.updated
   FROM n3c_admin.registration
  WHERE
  	registration.enclave
  AND
  	institution != 'NIH'
  AND
  	institution NOT IN (SELECT incommon from n3c_admin.registration_remap)
UNION
SELECT
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    registration_remap.ror as instutition,
    registration.orcid_id,
    registration.gsuite_email,
    registration.slack_id,
    registration.github_id,
    registration.twitter_id,
    registration.expertise,
    registration.therapeutic_area,
    registration.assistant_email,
    registration.enclave,
    registration.workstreams,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, n3c_admin.registration_remap
  WHERE
  	registration.enclave
  AND
  	registration.institution = registration_remap.incommon
UNION
SELECT
 	registration.email,
    registration.official_first_name,
    registration.official_last_name,
    registration.first_name,
    registration.last_name,
    nih_ic.title,
    registration.orcid_id,
    registration.gsuite_email,
    registration.slack_id,
    registration.github_id,
    registration.twitter_id,
    registration.expertise,
    registration.therapeutic_area,
    registration.assistant_email,
    registration.enclave,
    registration.workstreams,
    registration.created,
    registration.updated
   FROM n3c_admin.registration, nih_foa.nih_ic
where
	registration.enclave
and	institution='NIH'
and substring(official_full_name from '/([^)]+)') = nih_ic.ic;

CREATE VIEW palantir.n3c_organization AS
SELECT
 	organization.id,
    organization.name,
    '2020-07-20'::date as dua_signed,
    organization.wikipedia_url
   FROM ror.organization
  WHERE (organization.name IN ( 
  		SELECT registration.institution FROM n3c_admin.registration WHERE enclave and institution != ' NIH'
  		UNION
  		SELECT nih_ic.title FROM nih_foa.nih_ic where nih_ic.ic IN (select substring(official_full_name from '/([^)]+)') FROM n3c_admin.registration WHERE enclave and institution = 'NIH')
  		UNION
  		SELECT ror from n3c_admin.registration_remap
  		));
