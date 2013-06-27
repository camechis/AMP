#!/bin/sh

java -cp lib/spring-security-kerberos-core-1.0.1.BERICO.jar:target/anubis-3.2.0-SNAPSHOT.jar amp.anubis.AnubisService server config/anubis-spnegoAuth.yaml