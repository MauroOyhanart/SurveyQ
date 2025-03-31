#!/usr/bin/bash
set -e

docker build -t survey-logging-postgres:"$1" .
echo "done"

##