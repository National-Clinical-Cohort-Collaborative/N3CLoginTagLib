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
    citizen_scientist,
    international,
    created,
    updated,
    una_path
FROM n3c_admin.gsuite_view
WHERE enclave = 'TRUE'
  AND (ror_id IN (SELECT institutionid from n3c_admin.dua_master)
  		OR
  		email in (SELECT email_address from n3c_admin.citizen_master)
  		)
;

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
    signatoryemail as signatory_email,
    durcontact as dur_contact
FROM n3c_admin.dua_master
;

CREATE VIEW palantir.citizen_scientist AS
SELECT * FROM n3c_admin.citizen_master;

CREATE VIEW palantir.dua_master AS
SELECT * from n3c_admin.dua_master;

select email,official_institution,una_path.* from n3c_admin.registration,palantir.una_path where enclave and email not in (select email from palantir.n3c_user) and substring(lower(email) from '.*@(.*)') = substring and official_institution='login.gov' and substring not in ('gmail.com','hotmail.com','nih.gov','yahoo.com') order by 3,1;

GRANT SELECT ON ALL TABLES IN SCHEMA palantir TO palantir;
