#!/bin/sh
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "users_db" <<-EOSQL
        CREATE_TABLE user_profile (
          username text NOT NULL,
          telegram_id text NOT NULL,
          user_id bigint NOT NULL,
          role text NOT NULL,
          description text NOT NULL
        );
