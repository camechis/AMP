define [
  '../../util/Logger'
  'jquery'
],
(Logger)->
    class RoutingInfoRetriever
      constructor: (@hostname, @port, @serviceUrlExpression, @serializer)->
      retrieveRoutingInfo: (topic)->
        Logger.log.info "RoutingInfoRetriever.retrieveRoutingInfo >> Getting routing info for topic: #{topic}"
        deferred = $.Deferred()
        req = $.ajax
          url: "https://#{@hostname}:#{@port}#{@serviceUrlExpression}/#{topic}?c=true"
          dataType: 'jsonp'
        req.done (data, textStatus, jqXHR)->
            Logger.log.info "RoutingInfoRetriever.retrieveRoutingInfo >> retrieved topic info"
            deferred.resolve(data)
        req.fail (jqXHR, textStatus, errorThrown)->
            Logger.log.error "RoutingInfoRetriever.retrieveRoutingInfo >> failed to retrieve topic info"
            deferred.reject()
        return deferred.promise()

    return RoutingInfoRetriever