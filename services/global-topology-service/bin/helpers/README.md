# GTS Management - Python

This is a simple set of scripts for managing the GTS via Python.  We use it to help automate scripting commands for GTS.

## Usage

The following describes how to use the GTS Management Client.

### Getting a client instance

Create a GTS instance using the `conf.yaml` file as the source of configuration:

```python
from gtsclient import GTS

gts = GTS()
```

Example `conf.yaml`; this needs to be colocated with the `gtsclient.py` script:

```yaml
host: localhost
port: 15677
adminPort: 8081
creds:
  username: admin
  password: admin
```

Alternatively, you can specify configuration:

```python
from gtsclient import GTS

conf = { "host": "localhost", "port": 15677,
         "creds": { "username": "admin", "password": "admin" }}

gts = GTS(conf)
```

### Working with Cluster Registrations

```python
cluster = gts.clusters()

cluster = gts.cluster("localhost")
```

### Working with Exchanges on a specific Cluster

```python
# "/" is the vhost
exchanges = gts.exchanges("localhost", "/")

# Filtering
from filters import *

testExchanges = gts.exchanges("localhost", "/", Filter=Where("name").Contains("test"))

# Creating new Exchanges
from gtsmodel import *

ex = Exchange("a.new.test.exchange")

gts.addExchange("localhost", ex)

addedExchange = gts.exchange("localhost", "/", "a.new.test.exchange")

# Adding an exchange to another vhost:
ex2 = Exchange("exchange.name", vhost="test")

gts.addExchange("localhost", ex2)

# Delete an exchange:
gts.deleteExchange("localhost", "/", "a.new.test.exchange")
```

