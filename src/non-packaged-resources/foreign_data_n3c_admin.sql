CREATE EXTENSION IF NOT EXISTS postgres_fdw;

--
-- neuromancer.n3c_admin
--
CREATE SERVER neuromancer_server
  FOREIGN DATA WRAPPER postgres_fdw
  OPTIONS (host 'neuromancer.slis.uiowa.edu', dbname 'cd2h', use_remote_estimate 'true', fetch_size '50000', batch_size '10000');

CREATE USER MAPPING FOR CURRENT_USER
  SERVER neuromancer_server
  OPTIONS (user 'eichmann', password 'translational');

CREATE SCHEMA neuromancer_n3c_admin;

IMPORT FOREIGN SCHEMA n3c_admin
  FROM SERVER neuromancer_server
  INTO neuromancer_n3c_admin;

--
-- deep-thought.pubmed_central
--
CREATE SERVER deep_thought_server
  FOREIGN DATA WRAPPER postgres_fdw
  OPTIONS (host 'deep-thought.slis.uiowa.edu', dbname 'loki', use_remote_estimate 'true', fetch_size '50000', batch_size '10000');

CREATE USER MAPPING FOR CURRENT_USER
  SERVER deep_thought_server
  OPTIONS (user 'eichmann', password 'translational');

CREATE SCHEMA deep_thought_pubmed_central;

IMPORT FOREIGN SCHEMA pubmed_central
  FROM SERVER deep_thought_server
  INTO deep_thought_pubmed_central;

CREATE SCHEMA deep_thought_covid_pmc;

IMPORT FOREIGN SCHEMA covid_pmc
  FROM SERVER deep_thought_server
  INTO deep_thought_covid_pmc;

--
-- hal.local
--
CREATE SERVER hal_server
  FOREIGN DATA WRAPPER postgres_fdw
  OPTIONS (host 'hal.local', dbname 'cd2h', use_remote_estimate 'true', fetch_size '50000', batch_size '10000');

CREATE USER MAPPING FOR CURRENT_USER
  SERVER hal_server
  OPTIONS (user 'eichmann', password 'translational');

CREATE SCHEMA hal_covid_pmc;

IMPORT FOREIGN SCHEMA covid_pmc
  FROM SERVER hal_server
  INTO hal_covid_pmc;
