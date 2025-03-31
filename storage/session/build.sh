#!/usr/bin/bash
set -e

docker build -t survey-session-postgres:"$1" .
echo "done"

##