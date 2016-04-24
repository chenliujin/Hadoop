package com.chenliujin.hadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @site http://www.chenliujin.com
 * @author chenliujin <liujin.chen@qq.com>
 * @since 2016-04-24
 */
public class MaxTemperatureDriver extends Configured implements Tool
{
	public int run( String[] args) throws Exception
	{
		if (args.length != 2) {
			System.err.printf("Usage: %s <input> <output>", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
	}

    public static void main( String[] args ) throws Exception
    {
		int exitcode = ToolRunner.run( new MaxTemperatureDriver(), args);

		System.exit(exitcode);
    }
}
