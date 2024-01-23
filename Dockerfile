#FROM nexus-ci.corp.dev.vtb/ubi9/openjdk-17:1.15-1
FROM registry.access.redhat.com/ubi9/openjdk-17:1.15-1
COPY /target/T5_MCSRV-0.0.1-SNAPSHOT.jar /app/productSrv.jar
ENTRYPOINT ["java","-jar","/app/productSrv.jar"]