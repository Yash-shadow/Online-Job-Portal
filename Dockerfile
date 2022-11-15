FROM openjdk:8
USER root
COPY ./naukri /home/app
Workdir /home/app/build/libs
ENTRYPOINT ["java","-jar","naukri-0.0.1-SNAPSHOT.jar"]
#CMD ["sh","-c","while true; do echo Success-pod2!!! >> /home/dilip.log; sleep 5;done"]
