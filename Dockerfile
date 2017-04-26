FROM alpine:edge

ENV LANG C.UTF-8

RUN { \
      echo '#!/bin/sh'; \
      echo 'set -e'; \
      echo; \
      echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
   } > /usr/local/bin/docker-java-home \
   && chmod +x /usr/local/bin/docker-java-home

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:$JAVA_HOME/bin
ENV JAVA_VERSION 8u92
ENV MAVEN_OPTS "-Xmx4096m -XX:MaxPermSize=1024m"

RUN apk update && apk upgrade
RUN apk add --no-cache bash
RUN apk add --no-cache openjdk8
RUN [ "$JAVA_HOME" = "$(docker-java-home)" ]

RUN apk add --update nodejs=6.10.1-r0 nodejs-npm make maven curl

# Add environment variable
ENV APP_DIR /tobot

# Creating project folder
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
VOLUME $APP_DIR

# Install node dependencies
WORKDIR $APP_DIR/web
COPY web/package.json package.json
RUN node \
  --max_semi_space_size=1 \
  --max_old_space_size=198 \
  --max_executable_size=148
RUN npm install --depth=0 --quiet

# Install maven dependencies
WORKDIR $APP_DIR/java
COPY java/pom.xml pom.xml
RUN mvn -Dmaven.test.skip=true -B install

# Go back to project root
WORKDIR $APP_DIR
