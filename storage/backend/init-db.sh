#!/bin/bash
set -e

# Wait until PostgreSQL is ready
until pg_isready -U "$POSTGRES_USER"; do
  echo "Waiting for PostgreSQL to be ready..."
  sleep 2
done

# Ensure the database survey exists
psql -U "$POSTGRES_USER" -tc "SELECT 1 FROM pg_database WHERE datname = 'survey';" | grep -q 1 || \
psql -U "$POSTGRES_USER" -c "CREATE DATABASE survey;"

# Ensure the app_user table exists
psql -U "$POSTGRES_USER" -d survey -c "
CREATE TABLE IF NOT EXISTS public.app_user
(
    id bigint NOT NULL,
    email character varying(255) COLLATE pg_catalog.\"default\" NOT NULL,
    CONSTRAINT app_user_pkey PRIMARY KEY (id),
    CONSTRAINT uk1j9d9a06i600gd43uu3km82jw UNIQUE (email)
) TABLESPACE pg_default;
ALTER TABLE public.app_user OWNER TO survey_backend_admin;
"

psql -U "$POSTGRES_USER" -d survey -c "
  GRANT INSERT, UPDATE, DELETE ON public.app_user TO survey_backend_admin;
"

max_retries=30
attempt=0

while (( attempt++ < max_retries )); do
  echo "Attempt $attempt to create subscription..."

  # Check if the subscription already exists
  sub_exists=$(psql -U "$POSTGRES_USER" -d survey -Atc "SELECT 1 FROM pg_subscription WHERE subname = 'app_user_pub';")

  if [ "$sub_exists" = "1" ]; then
    echo "Subscription already exists. Exiting."
    break
  fi

  # Try to create the subscription
  if psql -U "$POSTGRES_USER" -d survey -c "CREATE SUBSCRIPTION app_user_pub
    CONNECTION 'host=$POSTGRES_SESSION_HOST port=$POSTGRES_SESSION_PORT dbname=survey_session
    user=$POSTGRES_SESSION_SUBSCRIPTION_USER password=$POSTGRES_SESSION_SUBSCRIPTION_PASSWORD'
    PUBLICATION app_user_pub WITH (copy_data = true);"; then
    echo "Subscription created successfully."
    break
  fi

  echo "Retrying in 5 seconds..."
  sleep 5
done

if (( attempt > max_retries )); then
  echo "Failed to create subscription after $max_retries attempts."
  exit 1
fi

echo "Subscription and schema setup completed successfully."
echo "$(date) -> Backend Database is setup"