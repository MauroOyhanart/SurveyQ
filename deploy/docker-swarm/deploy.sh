#!/usr/bin/bash

mkdir -p ./traefik/letsencrypt-certs 2>/dev/null
chmod 600 ./traefik/letsencrypt-certs 2>/dev/null

docker stack deploy -c service.yml survey-app --with-registry-auth
#