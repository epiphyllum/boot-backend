# 构建镜像，执行命令：【docker build -t boot_io:2.0 .】
FROM eclipse-temurin:17-jre
MAINTAINER hary
# 时区问题
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

# 字体
COPY fonts/simsun.ttc /usr/share/fonts/simsun.ttc

ADD apache-skywalking-apm-bin/agent/ /agent

ENTRYPOINT ["java", "-server", "-Xms512M", "-Xmx512M", "-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError", "-javaagent:/agent/skywalking-agent.jar", "-jar", "/app/app.jar" ]


