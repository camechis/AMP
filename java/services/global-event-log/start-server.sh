mvn clean package
java -jar target/amp-gel-3.2.0-SNAPSHOT.jar server conf/server-basicAuth.yaml

# Run in production mode (Accumulo) 
# java -Dspring.profiles.active=production -jar target/amp-gel-3.2.0-SNAPSHOT.jar server conf/server-basicAuth.yaml