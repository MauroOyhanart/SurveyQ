#!/usr/bin/bash
set -e

docker build -t survey-backend-postgres:"$1" .
echo "done"

###