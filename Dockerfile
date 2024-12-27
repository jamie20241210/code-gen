FROM openjdk:8-jre-alpine3.9

MAINTAINER tanghc

EXPOSE 6969

VOLUME /tmp
RUN mkdir /gen

ADD dist/gen /gen/

# set jvm
ENV JAVA_OPTS="-server -Xmx64m -Xms64m"
ENV LOCAL_DB_PATH="/opt/gen/gen.db"
ENV CONFIG_FILE="/gen/conf/app.yml"
ENV GEN_EXT_PATH="/gen/ext"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -DLOCAL_DB=$LOCAL_DB_PATH -Djava.ext.dirs=$JAVA_HOME/lib/ext:$GEN_EXT_PATH -Dsolon.config.add=$CONFIG_FILE -Duser.timezone=Asia/Shanghai -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom -jar /gen/gen.jar" ]
