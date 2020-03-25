FROM adoptopenjdk/openjdk11:alpine

COPY build/libs/authenticator-api.jar /opt/app/

ENTRYPOINT ["java"]
CMD ["-jar", "/opt/app/authenticator-api.jar"]
