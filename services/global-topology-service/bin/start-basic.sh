mvn clean
mvn package
java -jar target/amp.topology.service-3.2.0-SNAPSHOT.jar server configuration/gts-basicAuth.yaml
