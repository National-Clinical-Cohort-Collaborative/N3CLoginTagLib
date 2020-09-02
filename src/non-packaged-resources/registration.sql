CREATE TABLE n3c_admin.registration (
       email TEXT NOT NULL
     , official_first_name TEXT
     , official_last_name TEXT
     , first_name TEXT
     , last_name TEXT
     , institution TEXT
     , orcid_id TEXT
     , gsuite_email TEXT
     , slack_id TEXT
     , github_id TEXT
     , twitter_id TEXT
     , expertise TEXT
     , therapeutic_area TEXT
     , assistant_email TEXT
     , enclave BOOLEAN
     , workstreams BOOLEAN
     , created TIMESTAMP
     , updated TIMESTAMP
     , official_full_name TEXT
     , official_institution text
     , emailed TIMESTAMP
     , PRIMARY KEY (email)
);

CREATE TABLE n3c_admin.workstream (
       label TEXT NOT NULL
     , full_name TEXT
     , description TEXT
     , PRIMARY KEY (label)
);

CREATE TABLE n3c_admin.membership (
       email TEXT NOT NULL
     , label TEXT NOT NULL
     , joined TIMESTAMP
     , PRIMARY KEY (email, label)
     , CONSTRAINT FK_membership_2 FOREIGN KEY (label)
                  REFERENCES n3c_admin.workstream (label) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_membership_1 FOREIGN KEY (email)
                  REFERENCES n3c_admin.registration (email) ON DELETE CASCADE ON UPDATE CASCADE
);

