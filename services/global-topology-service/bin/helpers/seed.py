#!/user/bin/env python

from gtsclient import *
from gtsmodel import *

gts = GTS()

accessEventDirectKey = RoutingContextDefinition("security.AccessEvent")

print gts.addRoutingContextDefinition(accessEventDirectKey)

securityEventTopicKey = RoutingContextDefinition("security.#")

print gts.addRoutingContextDefinition(securityEventTopicKey)

localCluster = ClusterDefinition("localhost", description="Local Cluster")

print gts.addClusterDefinition(localCluster)

defaultExchange = ExchangeDefinition(
    "amp.default", description="Default Exchange")

print gts.addExchangeDefinition(defaultExchange)

clientQueue = QueueDefinition(
    description="Unique Client Queue",
    name='@{clientName}#@{increment}#@{eventType}')

print gts.addQueueDefinition(clientQueue)

proute = ProducingRouteDefinition(localCluster, defaultExchange)
proute.addRoutingContextReference(accessEventDirectKey)
proute.addRoutingContextReference(securityEventTopicKey)

print gts.addProducingRouteDefinition(proute)

croute = ConsumingRouteDefinition(localCluster, defaultExchange, clientQueue)
croute.addRoutingContextReference(accessEventDirectKey)
croute.addRoutingContextReference(securityEventTopicKey)

print gts.addConsumingRouteDefinition(croute)

routingInfoDefinition = RoutingInfoDefinition()
routingInfoDefinition.addProducingRouteReference(proute)
routingInfoDefinition.addConsumingRouteReference(croute)

print gts.addRoutingInfoDefinition(routingInfoDefinition)

