package com.ibeifeng.bigdata.hadoop.mapreduce.trade;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * @author beifeng
 * 
 */
public class OrderTypeMapReduce extends Configured implements Tool {

	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	public static class OrderTypeMapper extends //
			Mapper<LongWritable, Text, UserTimeWritable, TimeTypeWritable> {

		private UserTimeWritable mapOutputKey = new UserTimeWritable();
		private TimeTypeWritable mapOutputValue = new TimeTypeWritable();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//
			String lineValue = value.toString();

			// split
			String[] values = lineValue.split("\t");

			// validate
			// length

			// userid time type
			String time = values[0];
			String userId = values[1];
			String type = values[2];

			// set
			mapOutputKey.set(userId, time);
			mapOutputValue.set(time, type);

			// output
			context.write(mapOutputKey, mapOutputValue);

		}
	}

	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class OrderTypeReducer extends //
			Reducer<UserTimeWritable, TimeTypeWritable, Text, IntWritable> {

		private int orderDanChengTotal = 0;
		private int orderWangFanTotal = 0;

		@Override
		public void reduce(UserTimeWritable key,
				Iterable<TimeTypeWritable> values, Context context)
				throws IOException, InterruptedException {
			// validate
			// .....

			//
			String searchType = "xxx";
			if ("search-dancheng".equals(searchType)) {
				orderDanChengTotal++;
			}
			if ("search-wangfan".equals(searchType)) {
				orderWangFanTotal++;
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			context.write(new Text("SearchDanCheng"), new IntWritable(orderDanChengTotal));
			context.write(new Text("SearchWangFan"), new IntWritable(orderWangFanTotal));

		}

	}

	// step 3: driver
	public int run(String[] args) throws Exception {
		// 1: get configuration
		Configuration configuration = super.getConf();

		// 2: create job
		Job job = Job.getInstance(//
				configuration, //
				this.getClass().getSimpleName()//
				);
		job.setJarByClass(this.getClass());

		// 3: set job
		// input -> map -> reduce -> output
		// 3.1: input
		Path inPath = new Path(args[0]);
		FileInputFormat.addInputPath(job, inPath);

		// 3.2: mapper
		job.setMapperClass(OrderTypeMapper.class);
		job.setMapOutputKeyClass(UserTimeWritable.class);
		job.setMapOutputValueClass(TimeTypeWritable.class);

		// ===========================Shuffle======================================
		// 1) partitioner
		job.setPartitionerClass(UserIdPartitioner.class);
		// 2) sort
		// job.setSortComparatorClass(cls);
		// 3) combine
		// job.setCombinerClass(cls);
		// 4) compress
		// set by configuration
		// 5) group
		job.setGroupingComparatorClass(UseIdGroupingComparator.class);
		// ===========================Shuffle======================================

		// 3.3: reducer
		job.setReducerClass(OrderTypeReducer.class);
		// TODO
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		// set reducer number
		// job.setNumReduceTasks(3);

		// 3.4: output
		Path outPath = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outPath);

		// 4: submit job
		boolean isSuccess = job.waitForCompletion(true);

		return isSuccess ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		// create configuration
		Configuration configuration = new Configuration();

		// run job
		int status = ToolRunner.run(//
				configuration, //
				new OrderTypeMapReduce(), //
				args);

		// exit program
		System.exit(status);
	}
}
