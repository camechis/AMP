#!/bin/sh

java -cp .:lib/*:target/anubis-3.2.0-SNAPSHOT.jar amp.anubis.AnubisService server config/anubis-spnegoAuth.yaml