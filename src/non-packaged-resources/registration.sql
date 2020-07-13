CREATE TABLE n3c_admin.access_level (
       level INT NOT NULL
     , label TEXT
     , description TEXT
     , PRIMARY KEY (level)
);

CREATE TABLE n3c_admin.workstream (
       label TEXT NOT NULL
     , full_name TEXT
     , description TEXT
     , PRIMARY KEY (label)
);

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
     , created TIMESTAMP
     , updated TIMESTAMP
     , PRIMARY KEY (email)
);

CREATE TABLE n3c_admin.enclave (
       email TEXT NOT NULL
     , sftp BOOLEAN
     , level INT
     , requested TIMESTAMP
     , approved TIMESTAMP
     , PRIMARY KEY (email)
     , CONSTRAINT FK_enclave_1 FOREIGN KEY (email)
                  REFERENCES n3c_admin.registration (email)
     , CONSTRAINT FK_enclave_2 FOREIGN KEY (level)
                  REFERENCES n3c_admin.access_level (level)
);

CREATE TABLE n3c_admin.membership (
       email TEXT NOT NULL
     , label TEXT NOT NULL
     , joined TIMESTAMP
     , PRIMARY KEY (email, label)
     , CONSTRAINT FK_membership_1 FOREIGN KEY (email)
                  REFERENCES n3c_admin.registration (email)
     , CONSTRAINT FK_membership_2 FOREIGN KEY (label)
                  REFERENCES n3c_admin.workstream (label)
);

