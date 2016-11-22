package com.ibeifeng.bigdata.hadoop.mapreduce.index;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class KeyUrlWritable implements WritableComparable<KeyUrlWritable> {
	private String key ;
	private String url ;
	
	public KeyUrlWritable() {	}
	
	public KeyUrlWritable(String key, String url){
		this.set(key, url);
	}
	
	public void set(String key, String url){
		this.setKey(key);
		this.setUrl(url);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public int compareTo(KeyUrlWritable o) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
