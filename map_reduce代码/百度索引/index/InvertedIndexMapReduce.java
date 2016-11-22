package com.ibeifeng.bigdata.hadoop.mapreduce.index;

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
public class InvertedIndexMapReduce extends Configured implements Tool{
	
	// step 1: mapper class
	/**
	 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	 */
	/**
	 * data-sample:
	 *		url$$title$$content
	 */
	public static class InvertedIndexMapper extends //
		Mapper<LongWritable,Text,KeyUrlWritable,IntWritable>{
		private KeyUrlWritable mapOutputKey = new KeyUrlWritable();
		private IntWritable mapOutputValue = new IntWritable(1) ;

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("$$") ;
			String url = strs[0] ;
			String title = strs[1] ;
			String content = strs[2] ;
			
			// split title
			String[] sts = title.split(" ") ;
			for(String st : sts){
				mapOutputKey.set(st, url);
				context.write(mapOutputKey, mapOutputValue);
			}
			// split content
			String[] scs = content.split(" ") ;
			for(String sc : scs){
				mapOutputKey.set(sc, url);
				context.write(mapOutputKey, mapOutputValue);
			}
		}
		/**
		 * url-01:spark ->  1
		 * url-01:hadoop -> 1
		 * url-01:spark ->  1
		 * 		|
		 * 		|
		 * url-01:spark -> 2
		 * 		->    spark ->  url-01:2
		 * 						url-08:10
		 * 						url-05:12
		 * 
		 * 
		 * 
		 */
	}
	
//================================================================
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	public static class  InvertedIndexCombiner extends //
		Reducer<KeyUrlWritable,IntWritable,Text,UrlCountWritable>{
		
		private Text combinerOutoutKey = new Text() ;
		private UrlCountWritable combinerOutputValue = new UrlCountWritable();
		
		@Override
		public void reduce(KeyUrlWritable key, Iterable<IntWritable> values,
				Context context)
				throws IOException, InterruptedException {
			// set combiner Output Key
			combinerOutoutKey.set(key.getKey());
			
			int sum = 0 ;
			for(IntWritable value : values){
				sum += value.get() ;
			}
			//
			combinerOutputValue.set(key.getUrl(), sum);
		
			// output
			context.write(combinerOutoutKey, combinerOutputValue);
		}
	}
	
//================================================================
	
	// step 2: reducer class
	/**
	 * public class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
	 */
	// TODO
	public static class InvertedIndexReducer extends //
		Reducer<Text,UrlCountWritable,Text,UrlCountWritable>{
		
		// TODO
		@Override
		public void reduce(Text key, Iterable<UrlCountWritable> values,
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
		job.setMapperClass(InvertedIndexMapper.class);
		// TODO
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
// ===========================Shuffle======================================		
		// 1) partitioner
//				job.setPartitionerClass(cls);
		// 2) sort
//				job.setSortComparatorClass(cls);
		// 3) combine
				job.setCombinerClass(InvertedIndexCombiner.class);
		// 4) compress
			// set by configuration
		// 5) group
//				job.setGroupingComparatorClass(cls);
// ===========================Shuffle======================================		
		
		
		// 3.3: reducer
		job.setReducerClass(InvertedIndexReducer.class);
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
			new InvertedIndexMapReduce(), //
			args
		) ;
		
		// exit program
		System.exit(status);
	}
}
