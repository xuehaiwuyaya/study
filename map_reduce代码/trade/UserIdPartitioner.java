package com.ibeifeng.bigdata.hadoop.mapreduce.trade;

import org.apache.hadoop.mapreduce.Partitioner;

public class UserIdPartitioner extends Partitioner<UserTimeWritable,TimeTypeWritable> {

	@Override
	public int getPartition(UserTimeWritable key, TimeTypeWritable value,
			int numPartitions) {
		return (key.getUserId().hashCode() & Integer.MAX_VALUE) % numPartitions;
	}

}
