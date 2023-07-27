FROM openjdk:18-jdk-alpine AS builder

WORKDIR /app/msvcUsers

COPY ./pom.xml /app
COPY ./msvcUsers/.mvn ./.mvn
COPY ./msvcUsers/mvnw .
COPY ./msvcUsers/pom.xml .


#copia el jar a la carpeta raiz
#COPY ./target/msvcUsers-0.0.1-SNAPSHOT.jar .
#RUN chmod a+x ./mvnw
RUN sed -i -e 's/\r$//' ./mvnw
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline
COPY ./msvcUsers/src ./src

RUN ./mvnw clean package -DskipTests


FROM openjdk:18-jdk-alpine

WORKDIR /app

RUN echo "Antes"
COPY --from=builder /app/msvcUsers/target/msvcUsers-0.0.1-SNAPSHOT.jar .
RUN echo "Lo pase!!"

#referencia del jar
ENTRYPOINT ["java","-jar", "msvcUsers-0.0.1-SNAPSHOT.jar"]