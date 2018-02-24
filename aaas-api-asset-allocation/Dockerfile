FROM fabric8/java-jboss-openjdk8-jdk:1.2.3
ENV JAVA_APP_JAR  aaas-api-asset-allocation.jar  
ENV AB_ENABLED off
ENV AB_JOLOKIA_AUTH_OPENSHIFT true
ENV JAVA_OPTIONS -Xmx256m  -Dspring.profiles.active=k8s  -Djava.security.egd=file:///dev/./urandom
EXPOSE 10020  10021
ADD target/aaas-api-asset-allocation.jar  /deployments/
