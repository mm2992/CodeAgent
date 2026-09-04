FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
#暴露应用运行的端口
EXPOSE 8848
# 运行应用程序
CMD ["java", "-jar", "/app/target/ai-agent-0.0.1-SNAPSHOT.jar","--spring.profiles.active=local"]
