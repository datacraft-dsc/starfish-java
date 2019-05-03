#!/bin/bash

# package docs and deploy

DOC_PATH='./target/site/'
# FIXME get version from mvn
VERSION="0.0.1-SNAPSHOT"
# FIXME get project name from mvn
PROJECT_NAME="starfish-java"
PACKAGE_NAME="docs_${PROJECT_NAME}_${VERSION}"
DEPLOY_FILENAME="./target/${PACKAGE_NAME}.tar.gz"

DEPLOY_USER=docs_deploy

if [ ! -z "$1" ]; then
    DEPLOY_SERVER="$1"
fi

if [ ! -z "$2" ]; then
    DEPLOY_USER="$2"
fi

if [ "$3" = "dev" ]; then
    # create safe branch name
    DEV_BRANCH=$(echo $TRAVIS_BRANCH | sed -e 's^/^%^g' -e 's/[()<>&*!]/_/g' -e 's/\s/_/g')
else
    DEV_BRANCH=""
fi

echo "building docs package $PACKAGE_NAME"

# make the docs from source
rm -rf target/site
mvn site || true # do NOT fail due to javadoc failure

# package into a tar.gz file for deployment
(cd "$DOC_PATH"; tar -czf "../../$DEPLOY_FILENAME" ./)

if [ ! -z "$DEPLOY_SERVER" ]; then
    DEPLOY_BUILD_URL="http://${DEPLOY_SERVER}/docs_build"

    echo "Deploying doc file to $DEPLOY_SERVER"
    openssl aes-256-cbc -K $encrypted_f390bb15dfe8_key -iv $encrypted_f390bb15dfe8_iv \
    -in dex-docs-deploy.enc \
    -out dex-docs-deploy -d

    chmod 0600 /tmp/dex-docs-deploy

    if [ -z "$DEV_BRANCH" ]; then
        scp -i /tmp/dex-docs-deploy "$DEPLOY_FILENAME" ${DEPLOY_USER}@${DEPLOY_SERVER}:
    else
        # for debugging send the environment from travis
        mkdir -p "target/shrimp/$PROJECT_NAME/branches/$DEV_BRANCH"
        mv "$DEPLOY_FILENAME" "target/shrimp/$PROJECT_NAME/branches/$DEV_BRANCH/"
        env > "target/shrimp/$PROJECT_NAME/branches/$DEV_BRANCH/env.txt"
        rsync -auvW --rsh 'ssh -i /tmp/dex-docs-deploy' target/shrimp/$PROJECT_NAME/ ${DEPLOY_USER}@${DEPLOY_SERVER}:$PROJECT_NAME/
    fi

    rm /tmp/dex-docs-deploy

    # echo "requesting docs rebuild at $DEPLOY_BUILD_URL"
    # curl -H "Content-Type: application/json" -X POST -d '{"file":"$DEPLOY_FILENAME"}' "$DEPLOY_BUILD_URL"
fi
