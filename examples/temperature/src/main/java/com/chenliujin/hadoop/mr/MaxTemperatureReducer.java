package com.chenliujin.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @site http://www.chenliujin.com
 * @author chenliujin <liujin.chen@qq.com>
 * @since 2016-04-25
 */
public class MaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable>
{
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
	{
		int maxValue = Integer.MIN_VALUE;

		for (IntWritable value: values) {
			maxValue = Math.max(maxValue, value.get());
		}

		context.write(key, new IntWritable(maxValue));
	}
}
