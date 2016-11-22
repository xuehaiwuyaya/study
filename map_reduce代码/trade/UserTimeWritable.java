package com.ibeifeng.bigdata.hadoop.mapreduce.trade;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class UserTimeWritable implements WritableComparable<UserTimeWritable> {
	private String userId;
	private String time;

	public UserTimeWritable() {
	}

	public UserTimeWritable(String userId, String time) {
		this.set(userId, time);
	}

	public void set(String userId, String time) {
		this.setUserId(userId);
		this.setTime(time);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.getUserId());
		out.writeUTF(this.getTime());
	}

	public void readFields(DataInput in) throws IOException {
		this.setUserId(in.readUTF());
		this.setTime(in.readUTF());
	}

	public int compareTo(UserTimeWritable o) {
		int comp = this.getUserId().compareTo(o.getUserId());

		if (0 != comp) {
			return comp;
		}

		return this.getTime().compareTo(o.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserTimeWritable other = (UserTimeWritable) obj;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return userId + "\t" + time;
	}

}
