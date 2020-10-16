package DataFilfer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class EmptyFeatues {
    public static class FilterMapper extends Mapper<LongWritable, Text, Text, Text> {
        int lines=0;
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,BHK_OR_RK,SQUARE_FT,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_LACS)
            String line = value.toString();
            String[] words = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            int i=0;
            lines++;
            while (i<words.length){
                if(words[i].trim().isEmpty()){
                    context.write(new Text(String.valueOf(lines)), new Text(String.valueOf(i)) );
                }i++;
                //"Vikhroli (East),Lalitpur" 5191
            }

           /* if(value.toString().equals("POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,BHK_OR_RK,SQUARE_FT,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_LACS)")){
            }else{
                if(key.toString().equals("0")){
                    context.write(new Text("POSTED_BY,UNDER_CONSTRUCTION,RERA,BHK_NO.,BHK_OR_RK,SQUARE_FT,READY_TO_MOVE,RESALE,ADDRESS,LONGITUDE,LATITUDE,TARGET(PRICE_IN_LACS)"), new Text(""));
                }else{
                    context.write(value, new Text(""));
                }
            }
            String line = value.toString();
            String[] words = line.split("\t");
            int replication = Integer.parseInt(words[1]) - 1;*/
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
        job.setJarByClass(EmptyFeatues.class);
        job.setMapperClass(FilterMapper.class);
        job.setNumReduceTasks(0);
        //job.setCombinerClass(IntSumReducer.class);
        //job.setReducerClass(IntSumReducer.class);

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
