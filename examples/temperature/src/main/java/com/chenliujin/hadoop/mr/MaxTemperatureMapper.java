package com.chenliujin.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @site http://www.chenliujin.com
 * @author chenliujin <liujin.chen@qq.com>
 * @since 2016-04-25
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable>
{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
	{
		String line = value.toString();

		try {
			String year = line.substring(0, 4);

			int airTemperature = Integer.parseInt(line.substring(5));

			context.write(new Text(year), new IntWritable(airTemperature));
		} catch (Exception e) {
			System.out.print("Error in line:" + line);
		}
	}
}
