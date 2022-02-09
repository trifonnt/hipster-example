#!/bin/bash
#set -x #echo on

# Get new authorization token!
#/opt/<app>/script-01-get_auth_token-DEV-user.sh

#export SERVER_AUTH_HEADER="Bearer ..."
export SERVER_URL=http://localhost:8080

curl -v -X POST -H "Content-Type: multipart/form-data" -H "Authorization: $SERVER_AUTH_HEADER" -F "file=@./../../src/main/resources/config/liquibase/fake-data/user_data.csv" $SERVER_URL/api/user-data/import/by-code.csv
