#!/usr/bin/env bash
podman run -d -p 3306:3306 -e MYSQL_USER=taskmesh -e MYSQL_PASSWORD=abcd1234 -e MYSQL_RANDOM_ROOT_PASSWORD=1 -e MYSQL_DATABASE=taskmesh --name taskmesh-mysql mysql
