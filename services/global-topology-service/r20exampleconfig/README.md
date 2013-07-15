R20 Example Config
==================

In order to get GTS tested and verified for use within the 
R20 Delivery of Galileo, I  generated a series of 
certificates and ran things in a certain 
configuration to verify that the system worked.

This document explains the process used, to verify proper usage
of GTS within an SSL environment.

As I was learning as I went, and wasn't planning on delivering
this documentation/certificates/etc. 
I didn't name things the best. It was only after lots more
anguish than originally planned I figured I'd document
this process.


*So, please take the names with a grain of salt. :)*

Overview
----------------------------

My test consisted of the following setup, with each box/app being 
configured with its own certificate.

* An application server  (e.g. tomcat/jboss for the jmclientbox.berico.com certificate) running the applications:
  * galileo.war
  * r20-solr-connector.war

* A rabbitmq server  (jmrabbitbox.berico.com certificate)

* The Global Topology Service  (jmserverbox.berico.com certificate)
  * Was configured by hand to support the galileo application



Certificates
----------------------------

The certificates were generated using Rich's 
[CMF-AMQP-Configuration](https://github.com/Berico-Technologies/CMF-AMQP-Configuration) project,
in particular using the steps and process from the ssl portion of 
that project.  The steps used to generate certs are documented
on [Rich's blog](http://www.gettingcirrius.com/)
in particular the [series on  rabbitMQ](http://www.gettingcirrius.com/2013/01/rabbitmq-configuration-and-management.html)
and the article on *Configuring SSL for RabbitMQ*.



My certificates were generated in an SSL Utilities directory (e.g.
_/Users/jmccune/bprojx/\_UTILITIES/ssl/_ as the base directory).

You will need to put the certificate files somewhere and _adjust paths accordingly_.





jmrabbitbox configuration
----------------------------

jmrabbitbox -- the certificates for the rabbit server

Rabbit (importantly) needs to configured to use SSL as well, and
to use the proper security certificates (as exemplified below):

    [
      {rabbit, [
        {auth_mechanisms, ['PLAIN', 'AMQPLAIN', 'EXTERNAL']},
        {ssl_cert_login_from, common_name},
        {ssl_listeners, [5671]},
        {ssl_options, [{cacertfile, "/Users/jmccune/bprojx/_UTILITIES/ssl/ca/cacert.pem"},
                       {certfile, "/Users/jmccune/bprojx/_UTILITIES/ssl/jmrabbitbox/jmrabbitbox.berico.com.cert.pem"},
                       {keyfile, "/Users/jmccune/bprojx/_UTILITIES/ssl/jmrabbitbox/jmrabbitbox.berico.com.key.pem"},
                       {verify, verify_peer},
                       {fail_if_no_peer_cert, true}
                      ]}
      ]}
    ].

jmclientbox configuration
----------------------------

The certificates were set as follows
in the /opt/pegasus/config/eventbus.properties file 
(and corresppondingly in the /opt/orion/config/eventbus.properties) file:

    event.bus.ssl=true
    event.bus.pathToClientCertificate=/Users/jmccune/bprojx/_UTILITIES/ssl/jmclientbox/jmclientbox.berico.com.keycert.p12
    event.bus.pathToRemoteCertStore=/Users/jmccune/bprojx/_UTILITIES/ssl/jmclientbox/jmclientbox.jks
    #event.bus.password=ENCRYPTED:4B96B58181EEBA87B69729922F98071EEC9E7D1FBC0D89D8EB2D
    event.bus.password=password
    event.bus.gtskeystore=/Users/jmccune/bprojx/_UTILITIES/ssl/jmclientbox/jmclientbox.jks
    event.bus.gtstruststore=/Users/jmccune/bprojx/_UTILITIES/ssl/jmtruststore.jks

jmserverbox configuration
----------------------------

The jmserverbox is actually the GTS certificate and the 
example configuration files are checked into the same folder
structure as this README.md file is in.


Other Notes
---------------------------
All my boxes were tested on localhost.  This can be done 
by making sure that jmrabbitbox.berico.com, jmserverbox.beri... etc.
point to 127.0.0.1 in /etc/hosts

You may also need to import the client certificate into your browser
and the "root"/ca certificate into your system's root trust store in 
order for things to work properly.


