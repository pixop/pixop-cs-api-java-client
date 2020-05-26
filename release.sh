#!/bin/bash -e
export VERSION=$1

if [[ -z "$VERSION" ]]; then
  echo "ERROR: Version is undefined"
  exit 1
fi

mvn versions:set -DnewVersion=${VERSION} -DgenerateBackupPoms=false
mvn clean deploy -B -P release --settings ${DIR}/settings.xml
git checkout .
git tag $VERSION
git push tags
