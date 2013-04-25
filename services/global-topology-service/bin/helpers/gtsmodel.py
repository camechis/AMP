import json, uuid
from gtscommon import *

def ID():
  return str(uuid.uuid4())

def getRef(Obj):
  return Obj.getRef() if hasattr(Obj, "getRef") else Obj

class RoutingContext(Referable):
  
  def __init__(self, value, id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.value = value
    self.refFn = RCRef

class RoutingContextDefinition(Referable):
  
  def __init__(self, value, id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.valueExpression = value
    self.refFn = RCDefRef

class ClusterDefinition(Referable):
  
  def __init__(self, clusterId, id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.clusterIdExpression = clusterId
    self.refFn = ClDefRef

class ExchangeDefinition(Referable):
  
  def __init__(self, name, id=None, etype="direct", vhost="/", autoDelete="true",
    durable="true", shouldDeclare="true", arguments={}, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.nameExpression = name
    self.exchangeTypeExpression = etype
    self.virtualHostExpression = vhost
    self.isAutoDeleteExpression = autoDelete
    self.isDurableExpression = durable
    self.shouldDeclareExpression = shouldDeclare
    self.argumentExpressions = arguments
    self.refFn = ExDefRef
  
class QueueDefinition(Referable):

  def __init__(
    self, id=None, description=None, name="", exclusive="true", vhost="/", 
    autoDelete="true", durable="true", shouldDeclare="true", arguments={}):

    self.id = ID() if id == None else id
    self.description = description
    self.nameExpression = name
    self.isExclusiveExpression = exclusive
    self.virtualHostExpression = vhost
    self.isAutoDeleteExpression = autoDelete
    self.isDurableExpression = durable
    self.shouldDeclareExpression = shouldDeclare
    self.argumentExpressions = arguments
    self.refFn = QDefRef

class ProducingRouteDefinition(Referable):
  
  def __init__(self, clusterReference, exchangeReference, routingContextReferences=[], id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.clusterReference = getRef(clusterReference)
    self.exchangeReference = getRef(exchangeReference)
    self.routingContextReferences = map(getRef, routingContextReferences)
    self.refFn = PRouteDefRef

  def addRoutingContextReference(self, routingContextReference):
    self.routingContextReferences.append(routingContextReference)

class ConsumingRouteDefinition(Referable):
  
  def __init__(self, clusterReference, exchangeReference, queueReference, routingContextReferences=[],id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.clusterReference = getRef(clusterReference)
    self.exchangeReference = getRef(exchangeReference)
    self.routingContextReferences = map(getRef, routingContextReferences)
    self.queueReference = getRef(queueReference)
    self.refFn = CRouteDefRef
  
  def addRoutingContextReference(self, routingContextReference):
    self.routingContextReferences.append(routingContextReference)

class RoutingInfoDefinition(Referable):
  
  def __init__(self, producingRouteReferences = [], consumingRouteReferences = [], id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.producingRouteReferences = map(getRef, producingRouteReferences)
    self.consumingRouteReferences = map(getRef, consumingRouteReferences)
    self.refFn = RInfoDefRef
    
  def addProducingRouteReference(self, producingRouteReference):
    self.producingRouteReferences.append(producingRouteReference)
  
  def addConsumingRouteReference(self, consumingRouteReference):
    self.consumingRouteReferences.append(consumingRouteReference)


def CK(cluster, vhost, otype, name):
  """Generate a cluster key refering to some topology object."""
  return { "cluster": cluster, "vhost": vhost, "type": otype, "name": name }

def FR(factoryName, context):
  """Generate a factory reference to a topology object."""
  contextStr = context if type(context) == str else json.dumps(context)
  return { "factoryName": factoryName, "context": contextStr }



def ExRef(cluster, vhost, name):
  """Generate a factory reference to an exchange object"""
  ck = CK(cluster, vhost, "exchange", name)
  return FR("amp.topology.core.factory.rmq.RmqExchangeFactory", ck)

def ExDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicExchangeFactory", id)



def QRef(cluster, vhost, name):
  """Generate a factory reference to an queue object"""
  ck = CK(cluster, vhost, "queue", name)
  return FR("amp.topology.core.factory.rmq.RmqQueueFactory", ck)

def QDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicQueueFactory", id)



def RCRef(id):
  return FR("amp.topology.core.factory.impl.DefaultRoutingContextFactory", id)

def RCDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicRoutingContextFactory", id)



def ClRef(id):
  return FR("amp.topology.core.factory.impl.DefaultClusterFactory", id)

def ClDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicClusterFactory", id)



def PRouteDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicProducingRouteFactory", id)

def CRouteDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicConsumingRouteFactory", id)

def RInfoDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicRoutingInfoFactory", id)
