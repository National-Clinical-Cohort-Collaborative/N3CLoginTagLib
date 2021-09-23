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

CREATE VIEW palantir.una_path AS
select substring(lower(email) from '.*@(.*)'),una_path,count(*) from palantir.n3c_user group by 1,2 
;

create view n3c_admin.domain_team_lead as
select distinct domain_team_lead.nid,lead_nid,title,email,last_name,first_name,registration.orcid_id,unite_user_id
from n3c_web.bio,n3c_web.domain_team_lead,n3c_admin.registration,n3c_admin.user_binding
where bio.nid = domain_team_lead.lead_nid
  and registration.orcid_id = user_binding.orcid_id
  and title ~(first_name||'.*'||replace(last_name,'-','\-'))
  and last_name != ''  
union
select domain_team_lead.nid,lead_nid,title,email,last_name,first_name,registration.orcid_id,unite_user_id
from n3c_web.domain_team_lead natural join domain_team_lead_map natural join registration natural join n3c_admin.user_binding,n3c_web.bio
where lead_nid=bio.nid  
order by 1,2;

create view palantir.domain_team_lead as select * from n3c_admin.domain_team_lead;


select email,official_institution,una_path.* from n3c_admin.registration,palantir.una_path where enclave and email not in (select email from palantir.n3c_user) and substring(lower(email) from '.*@(.*)') = substring and official_institution='login.gov' and substring not in ('gmail.com','hotmail.com','nih.gov','yahoo.com') order by 3,1;

GRANT SELECT ON ALL TABLES IN SCHEMA palantir TO palantir;
