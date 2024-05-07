FROM amazoncorretto:21

WORKDIR /var/build-tools

RUN yum install -y wget git && \
    wget -O BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastStableBuild/artifact/target/BuildTools.jar && \
    java -jar BuildTools.jar --rev 1.20.4 && \
    mv spigot-1.20.4.jar spigot.jar

VOLUME /var/spigot
WORKDIR /var/spigot

EXPOSE 25565/tcp
EXPOSE 25565/udp

CMD ["java", "-jar", "/var/build-tools/spigot.jar"]