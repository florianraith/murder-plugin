FROM amazoncorretto:21

WORKDIR /var/build-tools

RUN yum install -y wget git && \
    wget -O paper.jar https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/496/downloads/paper-1.20.4-496.jar

VOLUME /var/paper
WORKDIR /var/paper

EXPOSE 25565/tcp
EXPOSE 25565/udp

CMD ["java", "-jar", "/var/build-tools/paper.jar", "--nogui"]