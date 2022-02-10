#!/bin/bash

token=`cat .token.txt`
export SERVER_AUTH_HEADER="Bearer $token"

export SERVER_URL=http://localhost:8080

curl -v -X POST -H "Content-Type: application/json" -H "Authorization: $SERVER_AUTH_HEADER" -d '{"userId":3}' $SERVER_URL/api/user-data
