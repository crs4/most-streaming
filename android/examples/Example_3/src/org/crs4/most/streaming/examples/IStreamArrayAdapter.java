/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package org.crs4.most.streaming.examples;

import java.util.List;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.enums.StreamProperty;
import org.crs4.most.streaming.enums.StreamState;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IStreamArrayAdapter extends ArrayAdapter<IStream> {

    public IStreamArrayAdapter(Context context, int textViewResourceId,
                 List<IStream> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    public View getViewOptimize(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.istream_row, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.textName);
            viewHolder.uri = (TextView)convertView.findViewById(R.id.textUri);
            viewHolder.videoSize = (TextView) convertView.findViewById(R.id.textSize);
            viewHolder.latency = (TextView) convertView.findViewById(R.id.textLatency);
            viewHolder.status = (TextView)convertView.findViewById(R.id.textState);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        IStream myStream = getItem(position);
        viewHolder.name.setText(myStream.getName());
        viewHolder.uri.setText(myStream.getProperty(StreamProperty.URI));
        if (myStream.getVideoSize()!=null)
        	viewHolder.videoSize.setText(myStream.getVideoSize().toString());
        else
        	viewHolder.videoSize.setText("n.a");
        viewHolder.latency.setText("" + myStream.getProperty(StreamProperty.LATENCY)+ " ms");
        viewHolder.status.setText(myStream.getState().toString());
        if (myStream.getState()==StreamState.ERROR)
        	viewHolder.status.setBackgroundColor(Color.RED);
        else if (myStream.getState()==StreamState.PLAYING_REQUEST)
        	viewHolder.status.setBackgroundColor(0xFFFFA500); // ORANGE COLOR
        else
        	viewHolder.status.setBackgroundColor(Color.GREEN);
        
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView uri;
        public TextView videoSize;
        public TextView latency;
        public TextView status;
        
    }
}