FROM amazoncorretto:17-al2023-jdk
VOLUME /tmp
COPY  build/libs/user-service-1.0.jar UserService.jar

ENTRYPOINT ["java","-jar","UserService.jar"]