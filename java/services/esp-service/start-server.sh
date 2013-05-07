mvn clean
mvn package
java -jar target/esp-service-1.0-SNAPSHOT.jar server conf/server-basicAuth.yaml
