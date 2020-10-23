package DataFilfer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class FeatureTitle {
    public static class FilterMapper extends Mapper<LongWritable, Text, Text, Text> {
        int lines=0;
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if(lines == 0){
                context.write(new Text("POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,SQUARE_M,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_BGP)"), new Text(""));
            }
            context.write(value, new Text(""));
            lines++;

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: wordcount <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "Removing the feature BHK");
        job.setJarByClass(FeatureTitle.class);
        job.setMapperClass(FilterMapper.class);
        //job.setCombinerClass(IntSumReducer.class);
        //job.setReducerClass(IntSumReducer.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,
                new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
