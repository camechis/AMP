import json, uuid
from gtscommon import *

def ID():
  return str(uuid.uuid4())

def getRef(Obj):
  return Obj.getRef() if hasattr(Obj, "getRef") else Obj

'''
  Realized Model Classes
'''

class RoutingContext(Referable):
  
  def __init__(self, value, id=None, description=None):
    
    self.id = ID() if id == None else id
    self.description = description
    self.value = value
    self.refFn = RCRef

'''
  RabbitMQ Model Classes
'''

class User(Referable):

  def __init__(self, name, password, tags):

    self.name = name
    self.password = password
    self.tags = tags

class Permission(Referable):

  def __init__(self, user, vhost="/", configure=".*", read=".*", write=".*"):

    self.user = user
    self.vhost = vhost
    self.configure = configure
    self.read = read
    self.write = write

class Exchange(Referable):
  
  def __init__(self, name, etype="direct", vhost="/", durable=True, autoDelete=False, internal=False, arguments={}):
    
    self.name = name
    self.type = etype
    self.vhost = vhost
    self.durable = durable
    self.autoDelete = autoDelete
    self.internal = internal
    self.arguments = arguments
    self.refFn = ExRef

class Queue(Referable):

  def __init__(self, name, vhost="/", exclusive=False, durable=True, autoDelete=False, arguments={}):

    self.name = name
    self.vhost = vhost
    self.durable = durable
    self.autoDelete = autoDelete
    self.exclusive = exclusive
    self.arguments = arguments
    self.refFn = QRef

class Binding(Referable):

  def __init__(self, source, destination, routingKey, destinationType="queue", vhost="/", arguments={}):

    self.source = source
    self.destination = destination
    self.destinationType = destinationType
    self.routingKey = routingKey
    self.vhost = vhost
    self.arguments = arguments

class VirtualHost(Referable):

  def __init__(self, name):

    self.name = name

'''
  Definition Model Classes
'''

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

'''
  Helper methods for constructing generic references (Cluster Key or Factory References)
'''

def CK(cluster, vhost, otype, name):
  """Generate a cluster key refering to some topology object."""
  return { "cluster": cluster, "vhost": vhost, "type": otype, "name": name }

def FR(factoryName, context):
  """Generate a factory reference to a topology object."""
  contextStr = context if type(context) == str else json.dumps(context)
  return { "factoryName": factoryName, "context": contextStr }

'''
  Helpers for constructing references to Exchanges (either real or definition)
'''

def ExRef(cluster, vhost, name):
  """Generate a factory reference to an exchange object"""
  ck = CK(cluster, vhost, "exchange", name)
  return FR("amp.topology.core.factory.rmq.RmqExchangeFactory", ck)

def ExDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicExchangeFactory", id)

'''
  Helpers for constructing references to Queues (either real or definition)
'''

def QRef(cluster, vhost, name):
  """Generate a factory reference to an queue object"""
  ck = CK(cluster, vhost, "queue", name)
  return FR("amp.topology.core.factory.rmq.RmqQueueFactory", ck)

def QDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicQueueFactory", id)

'''
  Helpers for constructing references to RoutingContexts (either real or definition)
'''

def RCRef(id):
  return FR("amp.topology.core.factory.impl.DefaultRoutingContextFactory", id)

def RCDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicRoutingContextFactory", id)


'''
  Helpers for constructing references to Clusters (either real or definition)
'''

def ClRef(id):
  return FR("amp.topology.core.factory.impl.DefaultClusterFactory", id)

def ClDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicClusterFactory", id)

'''
  Helpers for constructing references for Routes and RoutingInfo definitions
'''

def PRouteDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicProducingRouteFactory", id)

def CRouteDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicConsumingRouteFactory", id)

def RInfoDefRef(id):
  return FR("amp.topology.core.factory.dynamic.DynamicRoutingInfoFactory", id)
