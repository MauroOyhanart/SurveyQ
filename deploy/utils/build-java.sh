#!/usr/bin/bash
set -e

version="$1"

cd ../../backend
pwd
echo 'building backend'
bash build.sh "$version"

cd ../session
pwd
echo 'building session'
bash build.sh "$version"

cd ../logging
pwd
echo 'building logging'
bash build.sh "$version"

echo 'built images'
