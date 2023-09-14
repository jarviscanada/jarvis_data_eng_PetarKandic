#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

lscpu_var=`lscpu`

hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_var"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_var"  | grep "Architecture" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_var"  | grep "Model name" |awk '{$1=$2=""; print $0}'| xargs)
cpu_mhz=$(echo "$lscpu_var"  | grep "CPU MHz" |awk '{print $3}'| xargs)
l2_cache=$(echo "$lscpu_var"  | grep "L2 cache" |awk '{print $3}'| sed 's/K//' | xargs)
timestamp=$(date +"%Y-%m-%d %T")
total_mem=$(vmstat -t | tail -1 | awk '{print $4}')

insert_stmt="INSERT INTO host_info(id,hostname,cpu_number,cpu_architecture,cpu_model,cpu_mhz,L2_cache,\"timestamp\",total_mem) VALUES (1, '$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"
PGPASSWORD=$psql_password psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
