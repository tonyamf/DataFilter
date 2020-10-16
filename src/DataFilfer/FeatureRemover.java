package DataFilfer;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * @author antonio
 *
 */
public class FeatureRemover {
    public static class FilterMapper
            extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,BHK_OR_RK,SQUARE_FT,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_LACS)
            String line = value.toString();
            String[] words = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if(key.toString().equals("0")){
                words[5] = "SQUARE_M";
                words[11] = "TARGET(PRICE_IN_GBP)";
            }else{
                Double GBP = (Double.parseDouble(words[11])/94.94)*100000;
                Double m = Double.parseDouble(words[5])/10.7639104;
                words[5] = m.toString();
                words[11] = GBP.toString();
            }
            String wordOut = words[0] + "," + words[1] + "," + words[2] + "," + words[3] + "," + words[5] + "," + words[6] +
                    "," + words[7] + "," + words[8] + "," + words[9] + "," + words[10] + "," + words[11] ;
            context.write(new Text(wordOut), new Text(""));
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
        job.setJarByClass(FeatureRemover.class);
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
