package com.ibeifeng.bigdata.hadoop.mapreduce;

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
public class SecondarySortMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	public static class SecondarySortMapper extends //
		Mapper<LongWritable,Text,PairWritable,IntWritable>{
		
		private PairWritable mapOutputKey = new PairWritable() ;
		private IntWritable mapOutputValue = new IntWritable() ;
		
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// line value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split(",") ;
			// invalidate
			if(2 != strs.length){
				return ;
			}
			
			// set map  output key and value
			mapOutputKey.set(strs[0], Integer.valueOf(strs[1]));
			mapOutputValue.set(Integer.valueOf(strs[1]));
			
			// output
			context.write(mapOutputKey, mapOutputValue);
		}
	}
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class SecondarySortReducer extends //
		Reducer<PairWritable,IntWritable,Text,IntWritable>{
		private Text outputKey = new Text() ;
		
		@Override
		public void reduce(PairWritable key, Iterable<IntWritable> values,
				Context context)
				throws IOException, InterruptedException {
			// set output key
			outputKey.set(key.getFirst());
			
			// iterator
			for(IntWritable value : values){
				// output
				context.write(outputKey, value);
			}
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
		job.setMapperClass(SecondarySortMapper.class);
		job.setMapOutputKeyClass(PairWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		
// ===========================Shuffle======================================		
		// 1) partitioner
				job.setPartitionerClass(FirstPartitioner.class);
		// 2) sort
//				job.setSortComparatorClass(cls);
		// 3) combine
//				job.setCombinerClass(cls);
		// 4) compress
			// set by configuration
		// 5) group
		job.setGroupingComparatorClass(FirstGroupingComparator.class);
// ===========================Shuffle======================================		
		
		
		// 3.3: reducer
		job.setReducerClass(SecondarySortReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		// set reducer number
		job.setNumReduceTasks(2);
		
		// 3.4: output
		Path outPath = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outPath);
		
		// 4: submit job 
		boolean isSuccess = job.waitForCompletion(true);
		
		return isSuccess ? 0 : 1 ;
	}
	
	public static void main(String[] args) throws Exception {
		args = new String[]{
				"hdfs://bigdata-senior01.ibeifeng.com:8020/user/beifeng/mapreduce/sort/input" ,//
				"hdfs://bigdata-senior01.ibeifeng.com:8020/user/beifeng/mapreduce/sort/output"
			};		
		
		// create configuration
		Configuration configuration = new Configuration();
		
		// run job
		int status = ToolRunner.run(//
			configuration, //
			new SecondarySortMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
