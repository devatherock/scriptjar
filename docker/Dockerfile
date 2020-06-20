ARG GROOVY_VERSION
FROM groovy:${GROOVY_VERSION}

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="0.6.0"

# Set the user as root to make writes work with drone
# Only root user can write to docker volumes
USER root

COPY docker/entry-point.sh /scripts/entry-point.sh
COPY scriptjar.groovy /scripts/scriptjar.groovy

ENTRYPOINT sh /scripts/entry-point.sh