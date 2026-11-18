# syntax=docker/dockerfile:1
# Builds gameServer.jar + worldServer.jar from source and packages them with the game data.
# Build context: repository root.

########## build ##########
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /src
COPY pom.xml ./
COPY .mvn .mvn
COPY third-party third-party
COPY server-common server-common
COPY game-server game-server
COPY world-server world-server
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests package \
 && cp game-server/target/gameServer.jar world-server/target/worldServer.jar /

########## runtime ##########
FROM eclipse-temurin:8-jre
WORKDIR /app

# Static game data: config tables, battle maps, logback configs, GM test pages
COPY Server/conf/data       conf/data
COPY Server/conf/map        conf/map
COPY Server/conf/httpserver conf/httpserver
COPY Server/conf/logback_game.xml Server/conf/logback_world.xml conf/

COPY --from=build /gameServer.jar /worldServer.jar ./
COPY docker/server/templates /app/templates
COPY docker/server/entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh && mkdir -p /app/logs

ENV TZ=Asia/Ho_Chi_Minh \
    JAVA_OPTS="-Xms512m -Xmx1024m -Xss228k"

ENTRYPOINT ["entrypoint.sh"]
# "game" or "world"
CMD ["game"]
