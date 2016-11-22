package com.ibeifeng.bigdata.hadoop.mapreduce.trade;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class TimeTypeWritable implements Writable {
	// time
	private String time;
	// type
	private String type;

	public TimeTypeWritable() {
	}

	public TimeTypeWritable(String time, String type) {
		this.set(time, type);
	}

	public void set(String time, String type) {
		this.setTime(time);
		this.setType(type);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.getTime());
		out.writeUTF(this.getType());
	}

	public void readFields(DataInput in) throws IOException {
		this.setTime(in.readUTF());
		this.setType(in.readUTF());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeTypeWritable other = (TimeTypeWritable) obj;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return time + "\t" + type;
	}

}
