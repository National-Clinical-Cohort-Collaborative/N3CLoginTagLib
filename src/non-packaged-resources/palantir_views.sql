CREATE VIEW palantir.n3c_user AS
SELECT *,'InCommon' AS una_path from n3c_admin.staging_user_incommon
UNION
SELECT *,'InCommon' AS una_path from n3c_admin.staging_user_incommon_mismatch
UNION
SELECT *,'login.gov' AS una_path from n3c_admin.staging_user_non_incommon
UNION
SELECT *,'login.gov' AS una_path from n3c_admin.staging_user_citizen
UNION
SELECT *,'NIH' AS una_path from n3c_admin.staging_user_nih
;

CREATE VIEW palantir.n3c_organization_lax AS
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
  		UNION
  		SELECT institutionname from n3c_admin.site_master,n3c_admin.registration_domain_remap where registration_domain_remap.ror=site_master.institutionid
  		));

CREATE VIEW palantir.n3c_organization AS
SELECT
 	organization.id,
    organization.name,
    duaexecuted as dua_signed,
    organization.wikipedia_url
   FROM ror.organization, n3c_admin.site_master
  WHERE (
  	organization.id = site_master.institutionid
  AND
  	organization.name IN ( 
  		SELECT registration.institution FROM n3c_admin.registration WHERE enclave and official_institution != ' NIH'
  		UNION
  		SELECT nih_ic.title FROM nih_foa.nih_ic where nih_ic.ic IN (select substring(official_full_name from '/([^)]+)') FROM n3c_admin.registration WHERE enclave and official_institution = 'NIH')
  		UNION
  		SELECT ror from n3c_admin.registration_remap
  		UNION
  		SELECT institutionname from n3c_admin.site_master,n3c_admin.registration_domain_remap where registration_domain_remap.ror=site_master.institutionid
  		));

CREATE VIEW palantir.citizen_scientist AS
SELECT * FROM n3c_admin.citizen_master;

CREATE VIEW palantir.dua_master AS
SELECT * from n3c_admin.dua_master;

GRANT SELECT ON ALL TABLES IN SCHEMA palantir TO palantir;
