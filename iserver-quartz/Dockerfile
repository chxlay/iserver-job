ARG JDK

FROM ${JDK}

ARG TIME_ZONE

ARG SERVER_NAME
ENV NAME=${SERVER_NAME}

ARG SERVER_PORT

ENV JAVA_OPTS="-Xms128m -Xmx128m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

RUN mkdir -p /${SERVER_NAME}

WORKDIR /${SERVER_NAME}

EXPOSE ${SERVER_PORT}

ADD ./target/${SERVER_NAME}.jar ./

CMD sleep 5;java $JAVA_OPTS -jar ${NAME}.jar