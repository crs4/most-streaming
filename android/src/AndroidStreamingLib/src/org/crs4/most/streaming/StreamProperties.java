package org.crs4.most.streaming;

import java.util.Properties;

import org.crs4.most.streaming.enums.StreamProperty;

import android.util.Property;

/**
 * This class collects a set of stream properties a user intend to apply to a stream.
 *  @see IStream#commitProperties(StreamProperties)
 */
public class StreamProperties {
	
	private Properties props = new Properties();
	
	/**
	 * Add a new property 
	 * @param streamProperty the stream property to update
	 * @param value the value to be set for this property
	 * @return this StreamProperties so you can chain more properties to add
	 */
	public StreamProperties add(StreamProperty streamProperty, String value)
	{
		this.props.setProperty(streamProperty.toString(), value);
		return this;
	}
	
	/**
	 * Remove the specified property
	 * @param streamProperty the property to remove
	 * @return value of the removed property, or null if this property was not found
	 */
	public String remove(StreamProperty streamProperty)
	{
		return (String) this.props.remove(streamProperty);
	}
	
	/**
	 * Get the specified property value 
	 * @param property the stream property key
	 * @return the specified property value(or null if the key was not found)
	 */
	public String get(StreamProperty property)
	{
		return this.props.getProperty(property.toString());
	}
	
	/**
	 * Get all added properties
	 * @return the added properties
	 */
	public Properties getAll()
	{
		return this.props;
	}
   
}
