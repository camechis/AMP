require("coffee-script");

var   vows   = require("vows")
    , chai   = require("chai")
    , sinon  = require("sinon")
    , _      = require("lodash")
    , Envelope    = require("../envelope.coffee")
    , EnvelopeBus = require("../envelope-bus.coffee");

var expect = chai.expect;

/**
 * Get a Stub that will call a continuation when
 * it's done, if that continuation is present.
 * @returns {*}
 */
function getStubWithContinuationSupport(){

    var stub = sinon.stub();
    // If the method has a callback function,
    // call it!  This is conditional since we
    // use argument matchers to ensure a function
    // is present.
    stub.withArgs(sinon.match.any, sinon.match.func).callsArg(1);
    return stub;
}

/**
 * Generate a spied, NoOp transport.
 * @returns TransportProvider Object
 */
function getSpiedTransport(){

   return {
      send: getStubWithContinuationSupport(),
      register: getStubWithContinuationSupport(),
      unregister: getStubWithContinuationSupport(),
      onEnvelopeReceived: sinon.spy(),
      close: getStubWithContinuationSupport()
   };
}


function getStubbedProcessors(){
    var stub = sinon.stub().returns(true);
    return [ stub ];
}

/**
 * Get an instance of the EnvelopeBus for testing purposes.
 * @param configuration Any desired settings for overriding
 * the default configuration.
 * @returns EnvelopeBus instance.
 */
function getEnvelopeBus(configuration){

    if (_.isUndefined(configuration)){
        configuration = {};
    }

    if (_.isUndefined(configuration.transportProvider)){
        configuration.transportProvider = getSpiedTransport();
    }
    return new EnvelopeBus( configuration );
}

vows.describe("EnvelopeBus").addBatch({
  "when initialized with defaults": {
    topic: function(){

      return getEnvelopeBus();
    },
    "the inbound processor chain initialized as empty array": function(eb){

        expect(eb.inboundProcessors).to.deep.equal([]);
    },
    "the outbound processor chain initialized as empty array": function(eb){

        expect(eb.outboundProcessors).to.deep.equal([]);
    },
    "the transport provider has a callback registered": function(eb){

        expect(eb.transportProvider.onEnvelopeReceived.calledOnce).to.be.true;
    }
  },
  "when sending an envelope": {
      "with a callback": {
          topic: function(){

              var conf = { outboundProcessors: getStubbedProcessors() };
              eb = getEnvelopeBus(conf);
              var envelope = new Envelope();
              var callback = sinon.spy();
              eb.send(envelope, callback);
              return { envelope: envelope, bus: eb, callback: callback };
          },
          "outbound chain gets a chance to manipulate envelope": function(context){

              var processor = context.bus.outboundProcessors[0];

              expect(processor.calledWithExactly(
                  context.envelope, {})).to.be.true;
          },
          "transport provider called to send processed envelope": function(context){

              var transport = context.bus.transportProvider;

              expect(transport.send.calledWithExactly(
                  context.envelope, context.callback)).to.be.true;
          },
          "callback is executed after message is sent": function(context){

              var callback = context.callback;
              var transport = context.bus.transportProvider;

              expect(callback.calledOnce).to.be.true;
              expect(callback.calledAfter(transport.send)).to.be.true;
          }
      },
      "with a NULL envelope": {
          topic: function(){

              var eb = getEnvelopeBus();
              var envelope = null;
              var ex = null;

              try {

                  eb.send(envelope);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, envelope: envelope, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "without an envelope": {
          topic: function(){

              var eb = getEnvelopeBus();
              var ex = null;

              try {

                  eb.send();
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "with a function (and forgot envelope)": {
          topic: function(){

              var eb = getEnvelopeBus();
              var callback = function(){ return true; };
              var ex = null;

              try {

                  eb.send(callback);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, callback: callback, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      }
  },
  "when registering a handler": {
      "and the registration object is null": {
          topic: function(){

              var eb = getEnvelopeBus();
              var ex = null;

              try {

                  eb.register(null);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "and the registration object is undefined": {
          topic: function(){

              var eb = getEnvelopeBus();
              var ex = null;

              try {

                  eb.register();
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "and the registration object is a function (callback, no registration)": {
          topic: function(){

              var eb = getEnvelopeBus();
              var ex = null;
              var callback = function(){ return true; };

              try {

                  eb.register(callback);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, callback: callback, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "with a valid registration": {
          topic: function(){

              var eb = getEnvelopeBus();
              var registration = {};

              eb.register(registration);

              return { bus: eb, registration: registration };
          },
          "the registration is delegated to the transport layer": function(context){

              var transport = context.bus.transportProvider;

              expect(transport.register.calledWithExactly(
                  context.registration, undefined)).to.be.true;
          }
      },
      "with a valid registration and callback": {
          topic: function(){

              var eb = getEnvelopeBus();
              var registration = {};
              var callback = sinon.spy();

              eb.register(registration, callback);

              return { bus: eb, registration: registration, callback: callback };
          },
          "the registration is delegated to the transport layer and callback fired": function(context){

              var transport = context.bus.transportProvider;

              expect(transport.register.calledWithExactly(
                  context.registration, context.callback)).to.be.true;

              expect(context.callback.calledAfter(transport.register)).to.be.true;
          }
      }
  },
  "when unregistering a handler": {
      "and the registration object is NULL": {
          topic: function(){

              var eb = getEnvelopeBus();
              var registration = null;
              var ex = null;

              try {

                  eb.unregister(registration);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, registration: registration, ex: ex };
          },
          "expect an exception" : function(context) {

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "and the registration object is undefined": {
          topic: function(){

              var eb = getEnvelopeBus();
              var ex = null;

              try {

                  eb.unregister();
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      },
      "and the registration object is a function": {
          topic: function(){

              var eb = getEnvelopeBus();
              var registration = function(){ return true; }
              var ex = null;

              try {

                  eb.unregister(registration);
              }
              catch (e){

                  ex = e;
              }

              return { bus: eb, registration: registration, ex: ex };
          },
          "expect an exception": function(context){

              expect(context.ex).to.exist;
              expect(context.ex).to.not.be.null;
          }
      }
  }
}).export(module);