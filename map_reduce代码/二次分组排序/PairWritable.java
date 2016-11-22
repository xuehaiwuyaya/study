package com.ibeifeng.bigdata.hadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class PairWritable implements WritableComparable<PairWritable> {

	private String first;
	private int second;

	public PairWritable() {
	}

	public PairWritable(String first, int second) {
		this.set(first, second);
	}

	public void set(String first, int second) {
		this.first = first;
		this.setSecond(second);
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public int getSecond() {
		return second - Integer.MAX_VALUE;
	}

	public void setSecond(int second) {
		this.second = second + Integer.MAX_VALUE;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(first);
		out.writeInt(second);
	}

	public void readFields(DataInput in) throws IOException {
		this.first = in.readUTF();
		this.second = in.readInt();
	}

	public int compareTo(PairWritable o) {
		// compare first
		int comp =this.first.compareTo(o.getFirst()) ;
		
		// eqauls
		if(0 != comp){
			return comp ;
		}
		
		// compare
		return Integer.valueOf(this.getSecond()).compareTo(Integer.valueOf(o.getSecond())) ;
	}

}
