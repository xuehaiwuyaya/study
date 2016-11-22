package com.ibeifeng.bigdata.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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
public class ModuleMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	// TODO
	public static class ModuleMapper extends //
		Mapper<LongWritable,Text,LongWritable,Text>{

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// TODO
		}
	}
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	// TODO
	public static class ModuleReducer extends //
		Reducer<LongWritable,Text,LongWritable,Text>{
		
		// TODO
		@Override
		public void reduce(LongWritable key, Iterable<Text> values,
				Context context)
				throws IOException, InterruptedException {
			// TODO
		}
	}
	
	
	// step 3: driver
	public int run(String[] args) throws Exception {
		// 1: get configuration
		Configuration configuration = super.getConf() ;
				
		// 2: create job
		Job job = Job.getInstance(//
			configuration, //
			this.getClass().getSimpleName()//
		);
		job.setJarByClass(this.getClass());
		
		// 3: set job
		// input  -> map  -> reduce -> output
		// 3.1: input
		Path inPath = new Path(args[0]) ;
		FileInputFormat.addInputPath(job, inPath);
		
		// 3.2: mapper
		job.setMapperClass(ModuleMapper.class);
		// TODO
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
// ===========================Shuffle======================================		
		// 1) partitioner
//				job.setPartitionerClass(cls);
		// 2) sort
//				job.setSortComparatorClass(cls);
		// 3) combine
//				job.setCombinerClass(cls);
		// 4) compress
			// set by configuration
		// 5) group
//				job.setGroupingComparatorClass(cls);
// ===========================Shuffle======================================		
		
		
		// 3.3: reducer
		job.setReducerClass(ModuleReducer.class);
		// TODO
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		// set reducer number
//		job.setNumReduceTasks(3);
		
		// 3.4: output
		Path outPath = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outPath);
		
		// 4: submit job 
		boolean isSuccess = job.waitForCompletion(true);
		
		return isSuccess ? 0 : 1 ;
	}
	
	public static void main(String[] args) throws Exception {
		// create configuration
		Configuration configuration = new Configuration();
		
		// run job
		int status = ToolRunner.run(//
			configuration, //
			new ModuleMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
