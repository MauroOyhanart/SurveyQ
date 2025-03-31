FROM postgres:16.3-alpine

COPY create-db.sql /create-db.sql
COPY create-user-publication.sql /create-user-publication.sql
COPY create-user-table.sql /create-user-table.sql
RUN chmod 644 /create-db.sql
RUN chmod 644 /create-user-publication.sql
RUN chmod 644 /create-user-table.sql

# Entrypoint script to init the DB and create publication
COPY init-db.sh /docker-entrypoint-initdb.d/init-db.sh
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh

CMD ["postgres",\
    "-c", "wal_level=logical",\
    "-c", "max_wal_senders=10",\
    "-c", "max_replication_slots=10"\
]
