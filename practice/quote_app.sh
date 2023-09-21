# /bin/bash

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
	--header 'X-RapidAPI-Key: '$API_KEY'' | jq -r '.[] | .[] ')

if [ "$?" -ne 0]; then
	echo "Error running command"
	exit 1
fi

AMZN_symbol=$(echo "$AMZN_response" | head -1 | xargs)
echo $AMZN_symbol
AMZN_open=$(echo "$AMZN_response" | head -2 | tail -1 | xargs)
echo $AMZN_open
AMZN_high=$(echo "$AMZN_response" | head -3 | tail -1| xargs)
echo $AMZN_high
AMZN_low=$(echo "$AMZN_response" | head -4 | tail -1 |  xargs)
echo $AMZN_low
AMZN_price=$(echo "$AMZN_response" | head -5 | tail -1 | xargs)
echo $AMZN_price
AMZN_volume=$(echo "$AMZN_response" | head -6 | tail -1 | xargs)
echo $AMZN_volume


MSFT_response=$(curl --request GET \
        --url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=MSFT&datatype=json' \
        --header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
        --header 'X-RapidAPI-Key: '$API_KEY'' | jq -r '.[] | .[] ')

if [ "$?" -ne 0]; then
        echo "Error running command"
        exit 1
fi

MSFT_symbol=$(echo "$MSFT_response" | head -1 | xargs)
echo $MSFT_symbol
MSFT_open=$(echo "$MSFT_response" | head -2 | tail -1 | xargs)
echo $MSFT_open
MSFT_high=$(echo "$MSFT_response" | head -3 | tail -1 | xargs)
echo $MSFT_high
MSFT_low=$(echo "$MSFT_response" | head -4 | tail -1 | xargs)
echo $MSFT_low
MSFT_price=$(echo "$MSFT_response" | head -5 | tail -1 | xargs)
echo $MSFT_price
MSFT_volume=$(echo "$MSFT_response" | head -6 | tail -1 | xargs)
echo $MSFT_volume


AAPL_response=$(curl --request GET \
        --url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=AAPL&datatype=json' \
        --header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
        --header 'X-RapidAPI-Key: '$API_KEY'' | jq -r  '.[] | .[] ')

if [ "$?" -ne 0]; then
        echo "Error running command"
        exit 1
fi

AAPL_symbol=$(echo "$AAPL_response" | head -1 | xargs)
echo $AAPL_symbol
AAPL_open=$(echo "$AAPL_response" | head -2 | tail -1 | xargs)
echo $AAPL_open
AAPL_high=$(echo "$AAPL_response" | head -3 | tail -1 | xargs)
echo $AAPL_high
AAPL_low=$(echo "$AAPL_response" | head -4 | tail -1 | xargs)
echo $AAPL_low
AAPL_price=$(echo "$AAPL_response" | head -5 | tail -1 | xargs)
echo $AAPL_price
AAPL_volume=$(echo "$AAPL_response" | head -6 | tail -1 | xargs)
echo $AAPL_volume

insert_stmt="INSERT INTO quotes(id,symbol,open,high,low,price,volume) VALUES (1, '$AMZN_symbol', $AAPL_open, $AAPL_high, $AAPL_low, $AAPL_price, $AAPL_volume);"

PGPASSWORD=$PSQL_PASSWORD psql -h $PSQL_HOST -p $PSQL_PORT -d $PSQL_DATABASE -U $PSQL_USERNAME -c "$insert_stmt"

