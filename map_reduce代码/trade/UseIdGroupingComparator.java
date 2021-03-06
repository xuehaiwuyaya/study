package com.ibeifeng.bigdata.hadoop.mapreduce.trade;

import java.io.IOException;

import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.RawComparator;

public class UseIdGroupingComparator implements RawComparator<UserTimeWritable> {

	public int compare(UserTimeWritable o1, UserTimeWritable o2) {
		return o1.getUserId().compareTo(o2.getUserId());
	}

	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
		    UserTimeWritable key1 = new UserTimeWritable();
		    UserTimeWritable key2  = new UserTimeWritable() ;
		    DataInputBuffer buffer = new DataInputBuffer();
		
		try {
		      buffer.reset(b1, s1, l1);                   // parse key1
		      key1.readFields(buffer);
		      
		      buffer.reset(b2, s2, l2);                   // parse key2
		      key2.readFields(buffer);
		      
		    } catch (IOException e) {
		      throw new RuntimeException(e);
		    }
		    
		    return this.compare(key1, key2);                   // compare them
		
	}

}
