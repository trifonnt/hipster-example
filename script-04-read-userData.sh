#!/bin/bash

token=`cat .token.txt`
export SERVER_AUTH_HEADER="Bearer $token"

export SERVER_URL=http://localhost:8080

curl -v -X GET -H "Content-Type: application/json" -H "Authorization: $SERVER_AUTH_HEADER" $SERVER_URL/api/user-data
