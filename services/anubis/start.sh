#!/bin/sh

# if you want SPNEGO/Kerberos, uncomment the following
#java -cp .:lib/*:target/anubis-3.2.0-SNAPSHOT.jar amp.anubis.AnubisService server config/anubis-spnegoAuth.yaml

java -jar target/anubis-3.2.0-SNAPSHOT.jar server config/anubis-x509Auth.yaml 
#java  -Djavax.net.debug=ssl:handshake -jar target/anubis-3.2.0-SNAPSHOT.jar server config/anubis-x509Auth.yaml 
