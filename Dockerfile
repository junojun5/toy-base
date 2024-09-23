#FROM openjdk:17
# 작업 디렉토리 생성
#WORKDIR /app
# JAR 파일 복사
#COPY *.jar /app/myapp.jar
# 애플리케이션 실행 명령어
#CMD ["java", "-jar", "myapp.jar"]
FROM harbor.xiilab.com/library/openjdk:17.0.2-jdk
ENV LC_ALL=C.UTF-8
RUN mkdir /java
COPY ["build/libs/data_learnway-0.0.1-SNAPSHOT.jar", "/java/app.jar"]
EXPOSE 8080
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul" , "-jar", "/java/app.jar"]
