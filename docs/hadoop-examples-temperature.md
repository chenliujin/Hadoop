我们以简化版的气温统计为例，演示如何开发一个MapReduce程序。

## mvn 构建一个新项目
```
$ mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=com.chenliujin.hadoop.mr -DartifactId=temperature
```

## MaxTemperatureDriver.java
```java
package com.chenliujin.hadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @site http://www.chenliujin.com
 * @author chenliujin <liujin.chen@qq.com>
 * @since 2016-04-25
 */
public class MaxTemperatureDriver extends Configured implements Tool
{
	public int run( String[] args ) throws Exception
	{
		if (args.length != 2) {
			System.err.printf("Usage: %s <input> <output>", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		Configuration conf = getConf();

		Job job = new Job(getConf());

		job.setJobName("Max Temperature");
		job.setJarByClass(getClass());

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(MaxTemperatureMapper.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

    public static void main( String[] args ) throws Exception
    {
		int exitcode = ToolRunner.run( new MaxTemperatureDriver(), args );

		System.exit(exitcode);
    }
}
```

## MaxTemperatureMapper.java
```java
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
```

## MaxTemperatureReducer.java
```java
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
```

## 打包 jar 文件
将项目导出为 jar 文件，注意选择 MaxTemperatureDriver 作为包含 main 的类。
```
$ mvn package
```

## 要处理的数据
* temperature1
* temperature2
* temperature3
```
# 一个文件包含多行数据，前4位为年份，第5位为空格，第6位起为对应的温度
2015 25
2016 30
2014 28
```
* 导入 hadoop
```
$ /opt/hadoop/bin/hdfs dfs -put /input/* /chenliujin/input/
```

## 执行 MapReduce
* 权限：检查 temperature-1.0-SNAPSHOT.jar 所属的用户和用户组（hadoop:hadoop）
```
$ /opt/hadoop/bin/hdfs dfs -rm -r /output
$ /opt/hadoop/bin/hadoop jar temperature-1.0-SNAPSHOT.jar hdfs://master:9000/chenliujin/input hdfs://master:9000/chenliujin/output
```

## 查看结果
```
$ /opt/hadoop/bin/hdfs dfs -ls /chenliujin/output
```

## 参考文献
* [Hadoop MapReduce Examples - 最高气温统计](http://my.oschina.net/yanjianhai/blog/261728?fromerr=nwDNQmfz)
