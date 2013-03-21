# Generating a Development PKI Infrastructure for GTS

If you want to lock down the GTS, you're going to need to use SSL.  This will show you how to generate the necessary Public Key Infrastructure required to authenticate clients with the server and vice versa.

In general, you will not roll your own infrastructure, instead relying on your organization's infrastructure.  If you do need to implement your own, their are much better solutions: Windows Active Directory, EJBCA, etc. (i.e. don't rely on this!!!!).

> IBM has a decent, and succinct article on how to use keytool: http://www.ibm.com/developerworks/java/library/j-customssl/sidebar.html.

###Create Client Certs and Key Store

```bash
keytool -genkey -alias app01 -keystore app01.jks
keytool -genkey -alias app02 -keystore app02.jks
keytool -genkey -alias admin -keystore admin.jks
keytool -genkey -alias gts   -keystore gts.jks
```

### Export Certificates for inclusion in other key stores.

```bash
keytool -export -alias app01 -keystore app01.jks -file app01.cer
keytool -export -alias app02 -keystore app02.jks -file app02.cer
keytool -export -alias admin -keystore admin.jks -file admin.cer
keytool -export -alias gts   -keystore gts.jks   -file gts.cer
```

### Import Certificates into each key store.

#### Import the server certificate.

```bash
keytool -import -alias gts -keystore app01.jks -file gts.cer
keytool -import -alias gts -keystore app02.jks -file gts.cer
keytool -import -alias gts -keystore admin.jks -file gts.cer
```

#### Import the client certificates.

```bash
keytool -import -alias app01 -keystore gts.jks -file app01.cer
keytool -import -alias app02 -keystore gts.jks -file app02.cer
keytool -import -alias admin -keystore gts.jks -file admin.cer
```

### Create PEM versions of the store.

```bash
openssl x509 -in app01.cer -inform DER -out app01.pem -outform PEM
openssl x509 -in app02.cer -inform DER -out app02.pem -outform PEM
openssl x509 -in admin.cer -inform DER -out admin.pem -outform PEM
```

### Create PKCS#12 versions of the store.

This is useful for importing certs into the browser, however, keytool will "bark" at you about not being able to import the GTS certificate because `TrustedCertEntry` not supported.  This is not a big deal, but it will mean you will have to manually confirm the server certificate in the browser.

```bash
keytool -importkeystore -srckeystore app01.jks -srcstoretype JKS \
  -destkeystore app01.pfx -deststoretype PKCS12
keytool -importkeystore -srckeystore app02.jks -srcstoretype JKS \
  -destkeystore app02.pfx -deststoretype PKCS12
keytool -importkeystore -srckeystore admin.jks -srcstoretype JKS \
  -destkeystore admin.pfx -deststoretype PKCS12
```