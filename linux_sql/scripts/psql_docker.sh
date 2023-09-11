#!/bin/bash

cmd=$1
db_username=$2
db_password=$3

sudo systemctl status docker || sudo systemctl start docker

docker container inspect jrvs-psql
container_status=$?

case $cmd in 
  create)
  
  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1	
	fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create required username and password'
    exit 1
  fi
  
  # Create container
	docker volume create pgdata
  # Start the container
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
  # Make sure you understand what's `$?`
	exit $?
	;;

  start|stop) 
  # Check instance status; exit 1 if container has not been created
  if [ $container_status == '[]' ]
  then
        exit 1
  fi

  # Start or stop the container
	docker container $cmd jrvs-psql
	exit $?
	;;	
  
  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac 
