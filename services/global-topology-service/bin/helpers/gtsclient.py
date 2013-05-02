#!/user/bin/env python

import requests, json, yaml
from urllib import quote_plus
from requests.auth import HTTPBasicAuth
from gtscommon import *

urlEncode = quote_plus

def getId(Obj):
  if hasattr(Obj, "id"):
    return Obj.id
  return Obj["id"]

def applyFilters(collection, Filter, Sort, ViewDesc):
  items = collection
  if Filter != None:
    items = filter(Filter, collection)
  if Sort != None:
    if hasattr(Sort, "__call__"):
      items = sorted(collection, key=Sort, reverse=ViewDesc)
    else:
      items = sorted(collection, key=lambda item: item[Sort], reverse=ViewDesc)
  return items

class GTS:
  
  def __init__(self, conf=None):
    
    if not conf:
      self.conf = yaml.load(open("conf.yaml", "r"))
    else:
      self.conf = conf
    
    protocol = "http://"
    
    if 'cert' in self.conf['creds']:
      protocol = "https://"
      self.auth = None
    else:
      username = self.conf["creds"]["username"]
      password = self.conf["creds"]["password"]
      self.auth = HTTPBasicAuth(username, password)
    
    self.baseurl = protocol + self.conf["host"] + ":" + str( self.conf["port"] )
    
    services = "/service"
    
    if "servicePath" in self.conf:
      services = self.conf["servicePath"]
    
    self.servicesUrl = self.baseurl + services
    
    self.session = requests.Session()
    self.session.auth = self.auth
    self.session.headers.update({"Content-Type": "application/json"})
  
  def get(self, partialUrl):
    
    response = self.session.get(self.servicesUrl + partialUrl)
    
    if response.status_code == 200:
      return response.json()
    
    return None  
  
  def put(self, partialUrl, data):
    
    response = self.session.put(self.servicesUrl + partialUrl,
                                data=json.dumps(data, default=objectToJson))
    
    return self.handleResponse(response)
  
  def post(self, partialUrl, data, form=False):

    payload = data if form else json.dumps(data, default=objectToJson)

    if form:
      headers = { "Content-Type": "application/x-www-form-urlencoded" }
      response = self.session.post(self.servicesUrl + partialUrl, data=data, headers=headers )
    else:
      response = self.session.post(self.servicesUrl + partialUrl,
                                  data=json.dumps(data, default=objectToJson))

    return self.handleResponse(response)

  def delete(self, partialUrl, data=None):

    if data == None:
      response = self.session.delete(self.servicesUrl + partialUrl)
    else:
      response = self.session.delete(self.servicesUrl + partialUrl,
                                   data=json.dumps(data, default=objectToJson))

    return self.handleResponse(response)

  def handleResponse(self, response):

    if response.status_code >= 200 and response.status_code < 300:
      body = response.text
      if not body:
        return True
      return body

    return False


  '''
    Methods for Model CRUD
  '''
  
  def clusters(self):
    return self.get("/clusters")
  
  def cluster(self, id):
    return self.get("/clusters/" + id)
  
  def addCluster(self, cluster):
    return self.put("/clusters/" + cluster['id'], cluster)
  
  def updateCluster(self, cluster):
    return self.post("/clusters/" + cluster['id'], cluster)
  
  def deleteCluster(self, id):
    return self.delete("/clusters/" + id)
  
  '''
    GTS Management Methods
  '''
  
  def whoami(self):
    return self.get("/whoami")
  
  '''
    Methods for Topology Definitions
  '''
  
  def routingInfoDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/routing-info")
    return applyFilters(defs, Filter, SortBy, Desc)
    
  def routingInfoDefinition(self, id):
    return self.get("/definitions/routing-info/" + id)
  
  def addRoutingInfoDefinition(self, definition):
    return self.put("/definitions/routing-info/" + getId(definition), definition)
  
  def updateRoutingInfoDefinition(self, definition):
    return self.post("/definitions/routing-info/" + getId(definition), definition)
    
  def deleteRoutingInfoDefinition(self, id):
    return self.delete("/definitions/routing-info/" + id)
  
  def producingRouteDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/producing-route")
    return applyFilters(defs, Filter, SortBy, Desc)
  
  def producingRouteDefinition(self, id):
    return self.get("/definitions/producing-route/" + id)
  
  def addProducingRouteDefinition(self, definition):
    return self.put("/definitions/producing-route/" + getId(definition), definition)
  
  def updateProducingRouteDefinition(self, definition):
    return self.post("/definitions/producing-route/" + getId(definition), definition)
  
  def deleteProducingRouteDefinition(self, id):
    return self.delete("/definitions/producing-route/" + id)
  
  def consumingRouteDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/consuming-route")
    return applyFilters(defs, Filter, SortBy, Desc)
  
  def consumingRouteDefinition(self, id):
    return self.get("/definitions/consuming-route/" + id)
  
  def addConsumingRouteDefinition(self, definition):
    return self.put("/definitions/consuming-route/" + getId(definition), definition)
  
  def updateConsumingRouteDefinition(self, definition):
    return self.post("/definitions/consuming-route/" + getId(definition), definition)
  
  def deleteConsumingRouteDefinition(self, id):
    return self.delete("/definitions/consuming-route/" + id)
  
  def exchangeDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/exchange")
    return applyFilters(defs, Filter, SortBy, Desc)
  
  def exchangeDefinition(self, id):
    return self.get("/definitions/exchange/" + id)
  
  def addExchangeDefinition(self, definition):
    return self.put("/definitions/exchange/" + getId(definition), definition)
  
  def updateExchangeDefinition(self, definition):
    return self.post("/definitions/exchange/" + getId(definition), definition)
  
  def deleteExchangeDefinition(self, id):
    return self.delete("/definitions/exchange/" + id)  
  
  def queueDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/queue")
    return applyFilters(defs, Filter, SortBy, Desc)

  def queueDefinition(self, id):
    return self.get("/definitions/queue/" + id)

  def addQueueDefinition(self, definition):
    return self.put("/definitions/queue/" + getId(definition), definition)

  def updateQueueDefinition(self, definition):
    return self.post("/definitions/queue/" + getId(definition), definition)

  def deleteQueueDefinition(self, id):
    return self.delete("/definitions/queue/" + id)
  
  def clusterDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/cluster")
    return applyFilters(defs, Filter, SortBy, Desc)

  def clusterDefinition(self, id):
    return self.get("/definitions/cluster/" + id)

  def addClusterDefinition(self, definition):
    return self.put("/definitions/cluster/" + getId(definition), definition)

  def updateClusterDefinition(self, definition):
    return self.post("/definitions/cluster/" + getId(definition), definition)

  def deleteClusterDefinition(self, id):
    return self.delete("/definitions/cluster/" + id)
  
  def routingContextDefinitions(self, Filter=None, SortBy=None, Desc=False):
    defs = self.get("/definitions/routing-context")
    return applyFilters(defs, Filter, SortBy, Desc)

  def routingContextDefinition(self, id):
    return self.get("/definitions/routing-context/" + id)

  def addRoutingContextDefinition(self, definition):
    return self.put("/definitions/routing-context/" + getId(definition), definition)

  def updateRoutingContextDefinition(self, definition):
    return self.post("/definitions/routing-context/" + getId(definition), definition)

  def deleteRoutingContextDefinition(self, id):
    return self.delete("/definitions/routing-context/" + id)
  
  '''
    RMQ Management Methods
  '''

  def exchangeTypes(self, cluster):
    return self.get("/rmq/cluster-info/" + cluster + "/exchange-types")

  def clusterInfo(self, cluster):
    return self.get("/rmq/cluster-info/" + cluster)

  def clusterBindings(self, cluster):
    return self.get("/rmq/cluster-info/" + cluster + "/listeners")

  def clusterNodes(self, cluster):
    return self.get("/rmq/cluster-info/" + cluster + "/nodes")

  def clusterNode(self, cluster, name):
    return self.get("/rmq/cluster-info/" + cluster + "/nodes/" + name)

  def vhosts(self, cluster):
    return self.get("/rmq/clusters/" + cluster + "/vhosts")

  def vhostStatus(self, cluster, vhost):
    return self.get("/rmq/clusters/" + cluster + "/vhosts/" + urlEncode(vhost) + "/status")

  def vhostPermissions(self, cluster, vhost):
    return self.get("/rmq/clusters/" + cluster + "/vhosts/" + urlEncode(vhost) + "/permissions")

  def addVhost(self, cluster, vhost):
    return self.put("/rmq/clusters/" + cluster + "/vhosts/" + urlEncode(vhost.name), vhost)

  def addVhostPermission(self, cluster, vhost, permission):
    return self.put("/rmq/clusters/" + cluster + "/vhosts/"
                + urlEncode(vhost) + "/permissions", permission)

  def deleteVhost(self, cluster, vhost):
    return self.delete("/rmq/clusters/" + cluster + "/vhosts/" + urlEncode(vhost))

  def permissions(self, cluster, user, Filter=None, SortBy=None, Desc=False):
    permissions = self.get("/rmq/clusters/" + cluster + "/permissions/user/" + user)
    return applyFilters(permissions, Filter, SortBy, Desc)

  def addPermission(self, cluster, permission):
    return self.put("/rmq/clusters/" + cluster + "/permissions/vhost/"
                + urlEncode(permission.vhost) + "/user/" + permission.user)

  def deletePermission(self, cluster, vhost, user):
    return self.delete("/rmq/clusters/" + cluster + "/permissions/vhost/"
                + urlEncode(vhost) + "/user/" + user)

  def users(self, cluster, Filter=None, SortBy=None, Desc=False):
    users = self.get("/rmq/clusters/" + cluster + "/users")
    return applyFilters(users, Filter, SortBy, Desc)

  def user(self, cluster, user):
    return self.get("/rmq/clusters/" + cluster + "/users/" + user)

  def addUser(self, cluster, user):
    return self.put("/rmq/clusters/" + cluster + "/users/" + user.name, user)

  def deleteUser(self, cluster, user):
    return self.delete("/rmq/clusters/" + cluster + "/users/" + user)

  def exchanges(self, cluster, vhost, Filter=None, SortBy=None, Desc=False):
    exchanges = self.get("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/exchanges")
    return applyFilters(exchanges, Filter, SortBy, Desc)
  
  def exchange(self, cluster, vhost, name):
    return self.get("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/exchanges/" + name)
    
  def addExchange(self, cluster, exchange):
     return self.put("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(exchange.vhost) + "/exchanges/", exchange)
     
  def deleteExchange(self, cluster, vhost, name):
     return self.delete("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/exchanges/" + name)

  def queues(self, cluster, vhost, Filter=None, SortBy=None, Desc=False):
    queues = self.get("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/queues")
    return applyFilters(queues, Filter, SortBy, Desc)

  def queue(self, cluster, vhost, name):
    return self.get("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/queues/" + name)

  def addQueue(self, cluster, queue):
     return self.put("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(queue.vhost) + "/queues/", queue)

  def deleteQueue(self, cluster, vhost, name):
     return self.delete("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/queues/" + name)

  def allBindings(self, cluster, Filter=None, SortBy=None, Desc=False):
    bindings = self.get("/rmq/clusters/" + cluster + "/bindings")
    return applyFilters(bindings, Filter, SortBy, Desc)

  def bindings(self, cluster, vhost, Filter=None, SortBy=None, Desc=False):
    bindings =  self.get("/rmq/clusters/" + cluster + "/vhost/" + urlEncode(vhost) + "/bindings")
    return applyFilters(bindings, Filter, SortBy, Desc)

  def addBinding(self, cluster, binding):
    return self.put("/rmq/clusters/" + cluster + "/bindings", binding)

  def deleteBinding(self, cluster, binding):
    return self.delete("/rmq/clusters/" + cluster + "/bindings", data=binding)

  def getRoutingInfo(self, topic, pattern="pubsub"):
    formParams = { "cmf.bus.message.topic": topic, "cmf.bus.message.pattern": pattern }
    return self.post("/topology", formParams, form=True)

  def indexEntries(self):
    return self.get("/index/entries")

  def indexEntry(self, id):
    return self.get("/index/entries/" + id)

  def addIndexEntry(self, entry):
    return self.put("/index/entries/" + entry.id, entry)

  def updateIndexEntry(self, entry):
    return self.post("/index/entries/" + entry.id, entry)

  def deleteIndexEntry(self, id):
    return self.delete("/index/entries/" + id)

  def indexedTopics(self, query="*"):
    return self.get("/index/topics/" + query)

  def indexedPatterns(self, query="*"):
    return self.get("/index/patterns/" + query)

  def indexedClients(self, query="*"):
    return self.get("/index/clients/" + query)

  def indexedGroups(self, query="*"):
    return self.get("/index/groups/" + query)