#!/usr/bin/bash

docker build --build-arg MODULE_NAME=session --build-arg VERSION="$1" -t survey-session:"$1" -f ../Dockerfile ..