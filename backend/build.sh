#!/usr/bin/bash

docker build --build-arg MODULE_NAME=backend --build-arg VERSION="$1" -t survey-backend:"$1" -f ../Dockerfile ..

#