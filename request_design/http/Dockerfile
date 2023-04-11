FROM alpine/httpie

RUN mkdir -p /root/.config/httpie
COPY config.json /root/.config/httpie

RUN mv /usr/local/bin/http /usr/local/bin/http_real
COPY http /usr/local/bin
COPY httpsh /usr/local/bin

RUN mkdir /logs
VOLUME /logs

ENTRYPOINT /usr/local/bin/httpsh
