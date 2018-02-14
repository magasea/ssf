FROM fabric8/java-jboss-openjdk8-jdk:1.2.3
ENV JAVA_APP_JAR   aaas-api-user-login-0.0.1-SNAPSHOT.jar
ENV AB_ENABLED off
ENV AB_JOLOKIA_AUTH_OPENSHIFT true
ENV JAVA_OPTIONS -Xmx256m -Dspring.profiles.active=k8s  -Djava.security.egd=file:///dev/./urandom
EXPOSE 10060
EXPOSE 10061
ADD target/aaas-api-user-login-0.0.1-SNAPSHOT.jar  /deployments/