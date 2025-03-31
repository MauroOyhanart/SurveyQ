#!/usr/bin/bash
set -e

version="$1"

cd ../../storage/backend
pwd
echo 'building backend'
bash build.sh "$version"

cd ../../storage/session
pwd
echo 'building session'
bash build.sh "$version"

cd ../../storage/logging
pwd
echo 'building logging'
bash build.sh "$version"

echo 'built images'
