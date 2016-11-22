package com.ibeifeng.bigdata.hadoop.mapreduce.index;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class UrlCountWritable implements Writable {
	
	private String url ;
	private int count ;
	
	public UrlCountWritable() {
		// TODO Auto-generated constructor stub
	}
	
	
	public UrlCountWritable(String url ,int count){
		this.set(url, count);
	}
	
	public void set(String url ,int count){
		this.setUrl(url);
		this.setCount(count);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub

	}

}
