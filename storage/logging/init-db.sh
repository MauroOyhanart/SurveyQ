#!/bin/bash
set -e

# Wait until PostgreSQL is ready
until pg_isready -U "$POSTGRES_USER"; do
  echo "Waiting for PostgreSQL to be ready..."
  sleep 2
done

# Ensure the database survey_logging exists
psql -U "$POSTGRES_USER" -tc "SELECT 1 FROM pg_database WHERE datname = 'survey_logging';" | grep -q 1 || \
psql -U "$POSTGRES_USER" -c "CREATE DATABASE survey_logging;"

