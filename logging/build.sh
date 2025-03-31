#!/usr/bin/bash

docker build --build-arg MODULE_NAME=logging --build-arg VERSION="$1" -t survey-logging:"$1" -f ../Dockerfile ..