#!/bin/bash

if [ "$#" -lt 7 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

arr=("$@") 

while true; do
        API_KEY=${arr[0]}
        PSQL_HOST=${arr[1]}
        PSQL_PORT=${arr[2]}
        PSQL_DATABASE=${arr[3]}
        PSQL_USERNAME=${arr[4]}
        PSQL_PASSWORD=${arr[5]}

        SYMBOLS=($(echo ${arr[@]:6:$#} | tr " " "\n"))

	echo $SYMBOLS
	for i in "${SYMBOLS[@]}"
        do
        echo $i
        	curl_response=$(curl --request GET \
		--url 'https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol='$i'&datatype=json' \
		--header 'X-RapidAPI-Host: alpha-vantage.p.rapidapi.com' \
		--header 'X-RapidAPI-Key: '$API_KEY'' | jq -r '.[] | .[] ')

        	if [ "$?" -ne 0]; then
	        	 echo "Error running command"
	         	exit 1
        	fi
        	symbol=$(echo "$curl_response" | head -1 | xargs)
                open=$(echo "$curl_response" | head -2 | tail -1 | xargs)
                high=$(echo "$curl_response" | head -3 | tail -1 | xargs)
                low=$(echo "$curl_response" | head -4 | tail -1 | xargs)
                price=$(echo "$curl_response" | head -5 | tail -1 | xargs)
                volume=$(echo "$curl_response" | head -6 | tail -1 | xargs)

                insert_stmt="INSERT INTO quotes(id,symbol,open,high,low,price,volume) VALUES (1, '$symbol', $open, $high, $low, $price, $volume);"
                  PGPASSWORD=$PSQL_PASSWORD psql -h $PSQL_HOST -p $PSQL_PORT -d $PSQL_DATABASE -U $PSQL_USERNAME -c "$insert_stmt"
	done
        exit 0
done
