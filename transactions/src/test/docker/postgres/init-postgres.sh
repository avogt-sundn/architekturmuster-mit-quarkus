#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE referenz;
	GRANT ALL PRIVILEGES ON DATABASE referenz TO "$POSTGRES_USER";
EOSQL
