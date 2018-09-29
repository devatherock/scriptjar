FROM groovy:2.4-alpine

MAINTAINER 'Devaprasadh.Xavier <devatherock@gmail.com>'

ADD scriptjar.groovy /scripts/scriptjar.groovy
ADD entry-point.sh /scripts/entry-point.sh

LABEL com.circleci.preserve-entrypoint=true

ENTRYPOINT sh /scripts/entry-point.sh