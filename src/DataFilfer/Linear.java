package DataFilfer;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression;

import java.io.IOException;

public class Linear {
    public static class FilterMapper extends Mapper<LongWritable, Text, Text, Text> {
        int lines=0;
        int i =0;
        double[][] x = new double[596][8];
        double[] y = new  double[596];
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if(i > 0) {
                String line = value.toString();
                String[] words = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                x[lines][0] = Double.parseDouble(words[0]);
                x[lines][1] = Double.parseDouble(words[1]);
                x[lines][2] = Double.parseDouble(words[2]);
                x[lines][3] = Double.parseDouble(words[3]);
                x[lines][4] = Double.parseDouble(words[4]);
                x[lines][5] = Double.parseDouble(words[5]);
                x[lines][6] = Double.parseDouble(words[6]);
                x[lines][7] = Double.parseDouble(words[7]);
                y[lines] = Double.parseDouble(words[8]);

                if (i == 596){
                    OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
                    regression.newSampleData(y, x);

                    double[] beta = regression.estimateRegressionParameters();
                    double ARto2 = regression.calculateAdjustedRSquared();

                    double Rto2 = regression.calculateRSquared();
                    regression.calculateResidualSumOfSquares();

                    String betas = beta[0]+", "+beta[1]+", "+beta[2]+", "+beta[3]+", "+beta[4]+", "+beta[5]+", "+beta[6]+", "+beta[7]+", "+beta[8];
                    context.write(new Text("estimateRegressionParameters"), new Text(betas));
                    context.write(new Text("length"), new Text(String.valueOf(beta.length)));
                    context.write(new Text("AdjustedRSquared"), new Text(String.valueOf(ARto2)));
                    context.write(new Text("RSquared"), new Text(String.valueOf(Rto2)));

                }
            lines++;
            }
            i++;

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
        job.setJarByClass(Linear.class);
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
