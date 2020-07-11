CREATE TABLE n3c_admin.registration (
       email TEXT NOT NULL
     , official_first_name TEXT
     , official_last_name TEXT
     , first_name TEXT
     , last_name TEXT
     , institution TEXT
     , created TIMESTAMP
     , PRIMARY KEY (email)
);

