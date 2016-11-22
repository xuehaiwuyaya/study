package com.ibeifeng.bigdata.hadoop.mapreduce;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
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
public class WebPvMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	public static class WebPvMapper extends //
		Mapper<LongWritable,Text,IntWritable,IntWritable>{
		// map output value
		private final static IntWritable mapOutputValue = new IntWritable(1) ;
		// map output key
		private IntWritable mapOutputKey = new IntWritable();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// line value
			String lineValue = value.toString();
			
			// split
			String[] values = lineValue.split("\\t") ;
			
			if(30 > values.length){
				context.getCounter("WEBPVMAPPER_COUNTERS", "LENGTH_LT30_COUNTER")//
					.increment(1L) ;
				return ;
			}
			
			// province id 
			String provinceIdValue = values[23] ;
			// url
			String url = values[1] ;
			
			// validate provinceIdValue
			if(StringUtils.isBlank(provinceIdValue)){
				context.getCounter("WEBPVMAPPER_COUNTERS", "PROVINCEID_BLANK_COUNTER")//
				.increment(1L) ;
				return ;
			}
			// validate url
			if(StringUtils.isBlank(url)){
				context.getCounter("WEBPVMAPPER_COUNTERS", "URL_BLANK_COUNTER")//
				.increment(1L) ;
				return ;
			}
			
			int provinceId = Integer.MAX_VALUE;
			
			try{
				provinceId = Integer.valueOf(provinceIdValue) ;
			}catch(Exception e){
				context.getCounter("WEBPVMAPPER_COUNTERS", "PROVINCEID_NOTTONUMBER_COUNTER")//
				.increment(1L) ;
				return ;
			}
			
			if(Integer.MAX_VALUE == provinceId){
				context.getCounter("WEBPVMAPPER_COUNTERS", "PROVINCEID_VALIDATE_COUNTER")//
				.increment(1L) ;
				return ;
			}
			// set
			mapOutputKey.set(provinceId);
			// output
			context.write(mapOutputKey, mapOutputValue);
		}
	}
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class WebPvReducer extends //
		Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
		
		private IntWritable outputValue = new IntWritable();
		
		@Override
		public void reduce(IntWritable key, Iterable<IntWritable> values,
				Context context)
				throws IOException, InterruptedException {
			// temp : sum 
			int sum = 0 ;
			
			// iterator
			for(IntWritable value : values){
				// total
				sum += value.get() ;
			}
			// set
			outputValue.set(sum);
			
			// output
			context.write(key, outputValue);
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
		job.setMapperClass(WebPvMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		
// ===========================Shuffle======================================		
		// 1) partitioner
//				job.setPartitionerClass(cls);
		// 2) sort
//				job.setSortComparatorClass(cls);
		// 3) combine
			job.setCombinerClass(WebPvReducer.class);
		// 4) compress
			// set by configuration
		// 5) group
//				job.setGroupingComparatorClass(cls);
// ===========================Shuffle======================================		
		
		
		// 3.3: reducer
		job.setReducerClass(WebPvReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
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
		
		args = new String[]{
				"hdfs://bigdata-senior01.ibeifeng.com:8020/user/beifeng/mapreduce/webpv/input" ,//
				"hdfs://bigdata-senior01.ibeifeng.com:8020/user/beifeng/mapreduce/webpv/output"
			};
		
		// create configuration
		Configuration configuration = new Configuration();
		
		// run job
		int status = ToolRunner.run(//
			configuration, //
			new WebPvMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
