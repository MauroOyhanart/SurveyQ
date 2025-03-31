#!/bin/sh
set -e

# ---------- 1

until pg_isready -U "$POSTGRES_USER"; do
  echo "Waiting for PostgreSQL to be ready..."
  sleep 2
done

psql -U "$POSTGRES_USER" -f /create-db.sql
psql -U "$POSTGRES_USER" -d survey_session -f /create-user-table.sql
psql -U "$POSTGRES_USER" -c "CREATE USER $POSTGRES_SUBSCRIPTION_USER PASSWORD '$POSTGRES_SUBSCRIPTION_PASSWORD' REPLICATION;"
psql -U "$POSTGRES_USER" -d survey_session -c "
  GRANT SELECT ON public.app_user TO $POSTGRES_SUBSCRIPTION_USER;
"
psql -U "$POSTGRES_USER" -d survey_session -f /create-user-publication.sql


# --- 3

echo "Init-db.sh finished successfully"
