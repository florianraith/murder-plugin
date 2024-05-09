FROM amazoncorretto:21

WORKDIR /var/build-tools

#RUN yum install -y wget git && \
#    wget -O BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastStableBuild/artifact/target/BuildTools.jar && \
#    java -jar BuildTools.jar --rev 1.20.4 && \
#    mv spigot-1.20.4.jar spigot.jar
RUN yum install -y wget git && \
    wget -O paper.jar https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/496/downloads/paper-1.20.4-496.jar

VOLUME /var/paper
WORKDIR /var/paper

EXPOSE 25565/tcp
EXPOSE 25565/udp

CMD ["java", "-jar", "/var/build-tools/paper.jar", "--nogui"]