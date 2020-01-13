#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

BUILDER="java-container-builder"

docker build -f maven-3-jdk-8.dockerfile -t $BUILDER . \
  && docker run --network="host" -v $DIR:/project -v ~/.m2:/.m2 -u $(id -u):$(id -g) --rm -it $BUILDER clean install \
  && docker build -t axon-serialization-issue .
