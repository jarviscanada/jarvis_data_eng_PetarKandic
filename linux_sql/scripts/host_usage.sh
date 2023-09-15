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

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $(NF-2)}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $(NF-3)}' | xargs)
disk_io=$(vmstat -d | awk '{print $10}' | tail -1 | xargs)
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | sed 's/M//g' | xargs)

timestamp=$(vmstat -t | tail -1 | awk '{ print $(NF-1), "\t", $NF}' | xargs)

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage(timestamp,host_id,memory_free,cpu_idle,cpu_kernel,disk_io,disk_available) VALUES ('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

PGPASSWORD=$psql_password psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?

