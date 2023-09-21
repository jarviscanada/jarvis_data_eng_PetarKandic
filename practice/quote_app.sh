#!/bin/bash

API_KEY=$1
PSQL_HOST=$2 
PSQL_PORT=$3
PSQL_DATABASE=$4 
PSQL_USERNAME=$5
PSQL_PASSWORD=$6
SYMBOLS=$7

if [ "$#" -ne 7 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

AMZN_response=$(curl --request GET \
	--url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=AMZN&datatype=json' \
	--header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
	--header 'X-RapidAPI-Key: '$API_KEY'' | jq -c  '.[]')

if [ "$?" -ne 0]; then
	echo "Error running command"
	exit 1
fi

MSFT_response=$(curl --request GET \
        --url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=MSFT&datatype=json' \
        --header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
        --header 'X-RapidAPI-Key: '$API_KEY'' | jq -c  '.[]')

echo "$MSFT_response"

if [ "$?" -ne 0]; then
        echo "Error running command"
        exit 1
fi

AAPL_response=$(curl --request GET \
        --url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=AAPL&datatype=json' \
        --header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
        --header 'X-RapidAPI-Key: '$API_KEY'' | jq -c  '.[]')

if [ "$?" -ne 0]; then
        echo "Error running command"
        exit 1
fi

echo $AAPL_response

##insert_stmt="INSERT INTO host_usage1(id,symbol,open,high,low,price,volume) VALUES (1, '$
