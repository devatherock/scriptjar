FROM groovy:2.5-alpine

MAINTAINER 'Devaprasadh.Xavier <devatherock@gmail.com>'

ADD scriptjar.groovy /scripts/scriptjar.groovy
ADD docker/drone-entry-point.sh /scripts/entry-point.sh

# Set the user as root to make writes work with drone
# Only root user can write to docker volumes
USER root

ENTRYPOINT sh /scripts/entry-point.sh
