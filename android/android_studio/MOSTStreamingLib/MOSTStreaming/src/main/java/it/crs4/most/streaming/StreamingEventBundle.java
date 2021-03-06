/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package it.crs4.most.streaming;

import it.crs4.most.streaming.enums.StreamingEvent;
import it.crs4.most.streaming.enums.StreamingEventType;


/**
 * This class represents a container of all the informations of the events triggered by the Streaming Library.
 */
public class StreamingEventBundle {


    private StreamingEventType eventType = null;
    private StreamingEvent event = null;
    private String info = null;
    private Object data = null;

    /**
     * This object contains all the informations of any event triggered by the Streaming Library.
     *
     * @param eventType the type of this event
     * @param event     the event
     * @param info      a textual information describing this event
     * @param data      a generic object containing event-specific informations (the object type depends on the type of the event). In particular:
     *                  <ul>
     *                  <li> events of type {@link StreamingEventType#STREAM_EVENT} contain the {@link IStream} object that triggered this event </li>
     *                  </ul>
     */
    public StreamingEventBundle(StreamingEventType eventType, StreamingEvent event, String info, Object data) {
        this.eventType = eventType;
        this.event = event;
        this.info = info;
        this.data = data;
    }

    /**
     * Get the event type
     *
     * @return the event type
     */
    public StreamingEventType getEventType() {
        return eventType;
    }

    /**
     * Get the triggered event
     *
     * @return the event triggered by the library
     */
    public StreamingEvent getEvent() {
        return event;
    }

    /**
     * Get a textual description of this event
     *
     * @return a textual description of this event
     */
    public String getInfo() {
        return info;
    }

    /**
     * Get a generic object containing event-specific informations (the object type depends on the type of the event). </br> Note that events of type {@link StreamingEventType#STREAM_EVENT} contain the {@link IStream} object that triggered this event
     *
     * @return a generic object containing event-specific informations
     */
    public Object getData() {
        return data;
    }
}
