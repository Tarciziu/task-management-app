#!/usr/bin/env bash

##
# Stop stack
##
stop_stack() {
  docker_compose_filename=docker-compose.yaml

  docker-compose -f ${docker_compose_filename} -p ubb-platform-service down
}

stop_stack
