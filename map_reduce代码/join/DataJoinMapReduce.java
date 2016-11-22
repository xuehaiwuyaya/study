package com.ibeifeng.bigdata.hadoop.mapreduce.join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
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
public class DataJoinMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	// map output key: cid
	// map output value: cinfo /  oinfo
	public static class DataJoinMapper extends //
		Mapper<LongWritable,Text,LongWritable,DataJoinWritable>{
		
		private LongWritable mapOutputKey = new LongWritable();
		private DataJoinWritable mapOutputValue = new DataJoinWritable() ;
		
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// line value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split(",") ;
			
			int length = strs.length;
			if((3 != length) && (4 != length)){
				return ;
			}
			
			// get cid
			Long cid = Long.valueOf(strs[0]) ;
			mapOutputKey.set(cid);
			
			// get name 
			String name = strs[1] ;
			
			// set customer
			if(3 == length){
				String phone = strs[2] ;
				// set
				mapOutputValue.set("customer", name + "," + phone);
			}
			
			// set oder
			if(4 == length){
				String price = strs[2] ;
				String date = strs[3] ;
				// set
				mapOutputValue.set("order", name + "," + price + "," + date);
			}
			// output
			context.write(mapOutputKey, mapOutputValue);
		}
	}
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class DataJoinReducer extends //
		Reducer<LongWritable,DataJoinWritable,NullWritable,Text>{
		private Text outputValue = new Text() ;
		
		@Override
		public void reduce(LongWritable key, Iterable<DataJoinWritable> values,
				Context context)
				throws IOException, InterruptedException {
			String customerInfo = null ;
			List<String> orderList = new ArrayList<String>() ;
			
			// iterator
			for(DataJoinWritable value : values){
				String tag = value.getTag();
				// 
				if("customer".equals(tag)){
					customerInfo = value.getData() ;
				}
				else if("order".equals(tag)){
					orderList.add(value.getData()) ;
				}
			}
			
			if(0 == orderList.size()){
				return ;
			}
			
			// output
			for(String order: orderList ){
				// set output value
				outputValue.set(key.toString() + "," + customerInfo + "," + order);
				// 
				context.write(NullWritable.get(), outputValue);
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
		job.setMapperClass(DataJoinMapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(DataJoinWritable.class);
		
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
		job.setReducerClass(DataJoinReducer.class);
		job.setOutputKeyClass(NullWritable.class);
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
			new DataJoinMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
