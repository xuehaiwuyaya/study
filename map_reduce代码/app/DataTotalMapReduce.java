package com.ibeifeng.bigdata.hadoop.mapreduce.app;

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
public class DataTotalMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	public static class DataTotalMapper extends //
		Mapper<LongWritable,Text,Text,DataTotalWritable>{
		private Text mapOutputKey = new Text() ;
		private DataTotalWritable mapOutputValue = new DataTotalWritable();
		
		
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
//===========================================================================
		// TODO
		// value is validate
//===========================================================================
			
			
			
			// line value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t") ;
			
			// get value
			// key
			String phoneNum = strs[1] ;
			// up package
			int upPackNum = Integer.valueOf(strs[6]) ;
			// down package
			int downPackNum = Integer.valueOf(strs[7]) ;
			// up payload
			int upPayLoad = Integer.valueOf(strs[8]) ;
			// down payload
			int downPayLoad = Integer.valueOf(strs[9]) ;
			
			// set map output key/value
			mapOutputKey.set(phoneNum);
			mapOutputValue.set(upPackNum, downPackNum, upPayLoad, downPayLoad);
			
			// output
			context.write(mapOutputKey, mapOutputValue);
		}
	}
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class DataTotalReducer extends //
		Reducer<Text,DataTotalWritable,Text,DataTotalWritable>{
		DataTotalWritable outputValue = new DataTotalWritable() ;
		
		@Override
		public void reduce(Text key, Iterable<DataTotalWritable> values,
				Context context)
				throws IOException, InterruptedException {
			// up package
			int upPackNum = 0 ;
			// down package
			int downPackNum = 0;
			// up payload
			int upPayLoad = 0 ;
			// down payload
			int downPayLoad = 0 ;
			
			// iterator
			for(DataTotalWritable value : values){
				upPackNum += value.getUpPackNum() ;
				downPackNum += value.getDownPackNum() ;
				upPayLoad += value.getUpPayLoad() ;
				downPayLoad += value.getDownPayLoad();
			}
			
			// set 
			outputValue.set(upPackNum, downPackNum, upPayLoad, downPayLoad);
			
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
		job.setMapperClass(DataTotalMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DataTotalWritable.class);
		
		// 3.3: reducer
		job.setReducerClass(DataTotalReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DataTotalWritable.class);
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
			new DataTotalMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
