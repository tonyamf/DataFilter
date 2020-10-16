package DataFilfer;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * @author antonio
 *
 */
public class DublicateChecker {

    public static class FilterMapper extends Mapper<Object, Text, Text, IntWritable>{
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                //POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,BHK_OR_RK,SQUARE_FT,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_LACS)
                String line = value.toString();
                String[] words = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String wordOut = words[0] + "," + words[1] + "," + words[2] + "," + words[3] + "," + words[4] + "," + words[5] + "," + words[6] +
                        "," + words[7] + "," + words[8] + "," + words[9] + "," + words[10] + "," + words[11] ;
                context.write(new Text(wordOut), new IntWritable(1));
            }
        }

    public static class IntSumReducer extends Reducer<Text,IntWritable,Text, IntWritable> {
       private IntWritable result = new IntWritable();
       public void reduce(Text key, Iterable<IntWritable> values,
                          Context context
                          ) throws IOException, InterruptedException {
         int sum = 0;
         for (IntWritable val : values) {
           sum += val.get();
         }
         result.set(sum);
         if(sum >= 2){
             context.write(key, result);
         }
       }
     }

        public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
            String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
            if (otherArgs.length < 2) {
                System.err.println("Usage: wordcount <in> [<in>...] <out>");
                System.exit(2);
            }
            Job job = Job.getInstance(conf, "");
            job.setJarByClass(DublicateChecker.class);
            job.setMapperClass(FilterMapper.class);
            job.setNumReduceTasks(1);
            job.setCombinerClass(IntSumReducer.class);
            job.setReducerClass(IntSumReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            for (int i = 0; i < otherArgs.length - 1; ++i) {
                FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
            }
            FileOutputFormat.setOutputPath(job,
                    new Path(otherArgs[otherArgs.length - 1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}
