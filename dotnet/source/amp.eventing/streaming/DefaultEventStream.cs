﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.eventing.patterns.streaming;
using amp.bus;
using Common.Logging;
using cmf.bus;

namespace amp.eventing.streaming
{
    public class DefaultEventStream : IEventStream 
    {
        protected ILog _log;
        private readonly IStandardStreamingEventBus _eventBus;
        private int _batchLimit = 2;
        private Queue<EventStreamQueueItem> _queuedEvents;
        
        private readonly Guid _sequenceId;
        private int _position;
        private string _topic;

        public DefaultEventStream(IStandardStreamingEventBus eventBus, string topic)
        {
            _log = LogManager.GetLogger(this.GetType());
            _eventBus = eventBus;
            _topic = topic;
            _queuedEvents = new Queue<EventStreamQueueItem>();
            _sequenceId = Guid.NewGuid();
            _position = 0;
        }

        public int BatchLimit
        {
            set { _batchLimit = value; }
        }

        public void Publish(object evt)
        {
            _log.Debug("enter publish to stream");
            string sequence = _sequenceId.ToString();
            string isLastFlag = bool.FalseString;

            Envelope env = StreamingEnvelopeHelper.BuildStreamingEnvelope(sequence, _position, isLastFlag);
            env.SetMessageTopic(Topic);

            EventContext context = new EventContext(EventContext.Directions.Out, env, evt);
            EventStreamQueueItem eventItem = new EventStreamQueueItem(context);

            _log.Debug("buffering event with sequenceId: " + sequence + ", position: " + _position + ", isLast: " + isLastFlag);
            _queuedEvents.Enqueue(eventItem);

            if (_queuedEvents.Count == (_batchLimit + 1)) {
                _log.Debug("flusshing " + _batchLimit + " event(s) to stream.");
                bool isComplete = false;
                FlushStreamBuffer(isComplete);
            }
        }

        public string Topic
        {
            get { return _topic; }
        }

        /// <summary>
        /// When processing a stream of an unknown size, it becomes a challenge to know when you have dealt with the 
        /// last object in that stream. This class utilizes the Dispose() method to indicate that stream processing 
        /// has completed.  This is necessary in order to mark the last message with the isLast header flag 
        /// set to true.  The trick here is to ensure that the streamBuffer is not entirely empty when 
        /// dispose gets called.
        /// </summary>
        /// <param name="isComplete"></param>
        private void FlushStreamBuffer(bool isComplete) 
        {
            int boundary = (isComplete) ? 0 : 1;
            //We'll flush out the batch of messages == _batchLimit and leave one left in the queue to either
            //be sent with the next batch or to be sent when dispose gets called.
            while (_queuedEvents.Count > boundary) {
                EventStreamQueueItem eventItem = _queuedEvents.Dequeue();
                _eventBus.ProcessEvent(eventItem.EventContext, _eventBus.OutboundProcessors, () =>
                {
                    _eventBus.EnvelopeBus.Send(eventItem.Envelope);
                });
            }
        }

        public void Dispose()
        {
            bool isComplete = true;
            try
            {
                MarkLastElementInQueue();
                FlushStreamBuffer(isComplete);
            }
            catch (Exception e) 
            {
                _log.Error("Unable to send last batch of messages in event stream.", e);
            }
        }

        private void MarkLastElementInQueue() {
            int counter = 0;
            foreach (EventStreamQueueItem item in _queuedEvents)
            { 
                if (counter == (_queuedEvents.Count - 1))
                {
                    item.Envelope.Headers[StreamingEnvelopeConstants.IS_LAST] = bool.TrueString;
                }
                counter++;
            }
        }
    }
}