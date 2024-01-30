#!/usr/bin/env bash

##
# Deploy new stack
##
deploy_stack() {
  docker_compose_filename=docker-compose.yaml
  project_name=ubb-platform-service

  docker-compose -f ${docker_compose_filename} -p ${project_name} down --remove-orphans

  docker-compose -f ${docker_compose_filename} -p ${project_name} build --no-cache
  docker-compose -f ${docker_compose_filename} -p ${project_name} up -d
}

deploy_stack
