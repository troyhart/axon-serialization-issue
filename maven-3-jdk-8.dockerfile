FROM maven:3-jdk-8

RUN mkdir -p 700 /project && \
    mkdir -p 700 /.m2/repository

WORKDIR /project

VOLUME /project
VOLUME /.m2

ENTRYPOINT ["mvn","-Dmaven.repo.local=/.m2/repository"]

