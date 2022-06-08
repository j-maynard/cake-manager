FROM openjdk:17
ARG github-client-id
ARG github-client-secret
COPY ./target/cakemgr-0.0.1-SNAPSHOT.jar .
ENV GITHUB_CLIENT_ID $github-client-id
ENV GITHUB_CLIENT_SECRET $github-client-secret
CMD ["java", "-jar", "./cakemgr-0.0.1-SNAPSHOT.jar"]
EXPOSE 8282