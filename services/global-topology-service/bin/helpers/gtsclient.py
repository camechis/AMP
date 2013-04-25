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
    
    response = self.session.put(self.servicesUrl + partialUrl, data=json.dumps(data, default=objectToJson))
    
    if response.status_code >= 200 and response.status_code < 300:
      return True
    
    return False
  
  def post(self, partialUrl, data):

    response = self.session.post(self.servicesUrl + partialUrl, data=json.dumps(data, default=objectToJson))

    if response.status_code >= 200 and response.status_code < 300:
      return True

    return False
  
  def delete(self, partialUrl):
    
    response = self.session.delete(self.servicesUrl + partialUrl)

    if response.status_code >= 200 and response.status_code < 300:
      return True

    return False
  
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
  
  def whoami(self):
    return self.get("/whoami")
  
  def routingInfoDefinitions(self):
    return self.get("/definitions/routing-info")
    
  def routingInfoDefinition(self, id):
    return self.get("/definitions/routing-info/" + id)
  
  def addRoutingInfoDefinition(self, definition):
    return self.put("/definitions/routing-info/" + getId(definition), definition)
  
  def updateRoutingInfoDefinition(self, definition):
    return self.post("/definitions/routing-info/" + getId(definition), definition)
    
  def deleteRoutingInfoDefinition(self, id):
    return self.delete("/definitions/routing-info/" + id)
  
  def producingRouteDefinitions(self):
    return self.get("/definitions/producing-route")
  
  def producingRouteDefinition(self, id):
    return self.get("/definitions/producing-route/" + id)
  
  def addProducingRouteDefinition(self, definition):
    return self.put("/definitions/producing-route/" + getId(definition), definition)
  
  def updateProducingRouteDefinition(self, definition):
    return self.post("/definitions/producing-route/" + getId(definition), definition)
  
  def deleteProducingRouteDefinition(self, id):
    return self.delete("/definitions/producing-route/" + id)
  
  def consumingRouteDefinitions(self):
    return self.get("/definitions/consuming-route")
  
  def consumingRouteDefinition(self, id):
    return self.get("/definitions/consuming-route/" + id)
  
  def addConsumingRouteDefinition(self, definition):
    return self.put("/definitions/consuming-route/" + getId(definition), definition)
  
  def updateConsumingRouteDefinition(self, definition):
    return self.post("/definitions/consuming-route/" + getId(definition), definition)
  
  def deleteConsumingRouteDefinition(self, id):
    return self.delete("/definitions/consuming-route/" + id)
  
  def exchangeDefinitions(self):
    return self.get("/definitions/exchange")
  
  def exchangeDefinition(self, id):
    return self.get("/definitions/exchange/" + id)
  
  def addExchangeDefinition(self, definition):
    return self.put("/definitions/exchange/" + getId(definition), definition)
  
  def updateExchangeDefinition(self, definition):
    return self.post("/definitions/exchange/" + getId(definition), definition)
  
  def deleteExchangeDefinition(self, id):
    return self.delete("/definitions/exchange/" + id)  
  
  def queueDefinitions(self):
    return self.get("/definitions/queue")

  def queueDefinition(self, id):
    return self.get("/definitions/queue/" + id)

  def addQueueDefinition(self, definition):
    return self.put("/definitions/queue/" + getId(definition), definition)

  def updateQueueDefinition(self, definition):
    return self.post("/definitions/queue/" + getId(definition), definition)

  def deleteQueueDefinition(self, id):
    return self.delete("/definitions/queue/" + id)
  
  def clusterDefinitions(self):
    return self.get("/definitions/cluster")

  def clusterDefinition(self, id):
    return self.get("/definitions/cluster/" + id)

  def addClusterDefinition(self, definition):
    return self.put("/definitions/cluster/" + getId(definition), definition)

  def updateClusterDefinition(self, definition):
    return self.post("/definitions/cluster/" + getId(definition), definition)

  def deleteClusterDefinition(self, id):
    return self.delete("/definitions/cluster/" + id)
  
  def routingContextDefinitions(self):
    return self.get("/definitions/routing-context")

  def routingContextDefinition(self, id):
    return self.get("/definitions/routing-context/" + id)

  def addRoutingContextDefinition(self, definition):
    return self.put("/definitions/routing-context/" + getId(definition), definition)

  def updateRoutingContextDefinition(self, definition):
    return self.post("/definitions/routing-context/" + getId(definition), definition)

  def deleteRoutingContextDefinition(self, id):
    return self.delete("/definitions/routing-context/" + id)
  