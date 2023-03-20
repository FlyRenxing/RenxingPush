FROM openjdk:17
ADD target/renxing-push-*.jar /root/app.jar
ADD /www/wwwroot/push.imzdx.top/service/application.yml /root/application.yml
#ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar /root/app.jar resetAdmin
ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar /root/app.jar
EXPOSE 5005
EXPOSE 8000
ARG NETWORK=host