CREATE VIEW palantir.n3c_user AS
SELECT
    email,
    official_first_name,
    official_last_name,
    first_name,
    last_name,
    ror_id,
    ror_name,
    orcid_id,
    expertise,
    therapeutic_area,
    false as citizen_scientist,
    false as international,
    created,
    updated,
    una_path
FROM n3c_admin.gsuite_view
WHERE enclave = 'TRUE'
  AND ror_id IN (SELECT institutionid from n3c_admin.dua_master)
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
	institutionid as id,
	institutionname as name,
	duaexecuted as dua_signed,
	duacontactfirstname as contact_first_name,
	duacontactsurname as contact_last_name,
	duacontactrole as contact_role,
	duacontactemail as contact_email,
    signatoryfirst as signatory_first_name,
    signatorylast as signatory_last_name,
    signatoryrole as signatory_role,
    signatoryemail as signatory_email
FROM n3c_admin.dua_master
;

CREATE VIEW palantir.n3c_organization AS
SELECT
 	organization.id,
    organization.name,
    duaexecuted as dua_signed,
    organization.wikipedia_url
   FROM ror.organization, n3c_admin.dua_master
  WHERE (
  	organization.id = dua_master.institutionid
  AND
  	organization.name IN ( 
  		SELECT registration.institution FROM n3c_admin.registration WHERE enclave and official_institution != ' NIH'
  		UNION
  		SELECT nih_ic.title FROM nih_foa.nih_ic where nih_ic.ic IN (select substring(official_full_name from '/([^)]+)') FROM n3c_admin.registration WHERE enclave and official_institution = 'NIH')
  		UNION
  		SELECT ror from n3c_admin.registration_remap
  		UNION
  		SELECT institutionname from n3c_admin.dua_master,n3c_admin.registration_domain_remap where registration_domain_remap.ror=dua_master.institutionid
  		));

CREATE VIEW palantir.citizen_scientist AS
SELECT * FROM n3c_admin.citizen_master;

CREATE VIEW palantir.dua_master AS
SELECT * from n3c_admin.dua_master;

GRANT SELECT ON ALL TABLES IN SCHEMA palantir TO palantir;
