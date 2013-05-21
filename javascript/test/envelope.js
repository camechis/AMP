require("coffee-script");

var   vows   = require("vows")
    , chai   = require("chai")
    , Envelope = require("../envelope.coffee");

var expect = chai.expect;

vows.describe("Envelope").addBatch({
    "when initialized with sensible defaults": {
        topic: function(){
           return new Envelope()
        },
        "we get a default value for": {
            "id": function(envelope){
                expect(envelope.id()).to.exist;
                expect(envelope.id()).to.be.a("string");
            },
            "creation date": function(envelope){

                expect(envelope.created()).to.exist;
                expect(envelope.created()).to.be.a("number");
                expect(envelope.created()).to.be.above(0);
            },
            "content-type": function(envelope){

                expect(envelope.contentType()).to.exist;
                expect(envelope.contentType()).to.equal("application/json")
            }
        }
    },
    "when intialized with headers and body": {
        topic: function(){
            var headers = {};
            headers[ Envelope.HeaderConstants.MESSAGE_ID.key ] = "envelope.id";
            headers[ Envelope.HeaderConstants.CONTENT_TYPE.key ] = "base64";
            headers[ Envelope.HeaderConstants.ENVELOPE_CREATION_TIME.key ] = 23456;
            var body = { some: "object" };
            return new Envelope(headers, body);
        },
        "the defaults do not override": {
            "id": function(envelope) {

                expect(envelope.id()).to.equal("envelope.id");
            },
            "content-type": function(envelope) {

                expect(envelope.contentType()).to.equal("base64");
            },
            "creation date": function(envelope) {

                expect(envelope.created()).to.equal(23456);
            }
        },
        "body is initialized as 'payload'": function(envelope){

            expect(envelope.payload()).to.deep.equal({ some: "object" });
        }
    },
    "when setting header values,": {
       topic: function(){
            var env = new Envelope();
            env.id("id");
            env.created(12345);
            env.received(12345);
            env.correlationId("corid");
            env.sender("sender");
            env.signature("signature");
            env.topic("topic");
            env.messageType("some.Topic");
            env.contentType("text/xml");
            return env;
       },
       "we get the correct value for": {
           "id": function(envelope){

               expect(envelope.id()).to.equal("id");
           },
           "creation date": function(envelope){

               expect(envelope.created()).to.equal(12345);
           },
           "receipt date": function(envelope){

               expect(envelope.received()).to.equal(12345);
           },
           "content type": function(envelope){

               expect(envelope.contentType()).to.equal("text/xml");
           },
           "correlation id": function(envelope){

               expect(envelope.correlationId()).to.equal("corid");
           },
           "sender identity": function(envelope){

               expect(envelope.sender()).to.equal("sender");
           },
           "sender signature": function(envelope){

               expect(envelope.signature()).to.equal("signature");
           },
           "message type": function(envelope){

               expect(envelope.messageType()).to.equal("some.Topic");
           },
           "message topic": function(envelope){

               expect(envelope.topic()).to.equal("topic");
           }
       }
    },
    "when setting the payload": {
        topic: function(){
            var envelope = new Envelope();
            envelope.payload({ a: "complex", object: true });
            return envelope;
        },
        "we get the correct value for":{
            "the body property": function(envelope){

                expect(envelope.body).to.deep.equal({ a: "complex", object: true });
            },
            "the payload getter": function(envelope){

                expect(envelope.payload()).to.deep.equal({ a: "complex", object: true });
            }
        }
    },
    "fluent interface works awesome": {
        topic: function(){

            return new Envelope()
                .id("id")
                .messageType("some.Topic")
                .contentType("text/html")
                .correlationId("corid")
                .created(12345)
                .received(12345)
                .sender("me")
                .signature("my#sig")
                .topic("blah")
                .payload({ my: "body", brings: "the girls", to: "the", yard: true });
        },
        "and returns the right values": function(envelope){

            expect(envelope.id()).to.equal("id");
            expect(envelope.messageType()).to.equal("some.Topic");
            expect(envelope.contentType()).to.equal("text/html");
            expect(envelope.correlationId()).to.equal("corid");
            expect(envelope.created()).to.equal(12345);
            expect(envelope.received()).to.equal(12345);
            expect(envelope.sender()).to.equal("me");
            expect(envelope.signature()).to.equal("my#sig");
            expect(envelope.topic()).to.equal("blah");
            expect(envelope.payload()).to.deep.equal(
                { my: "body", brings: "the girls", to: "the", yard: true });
        }
    }
}).export(module);