package me.florianschmidt.sparkExample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide the input file full path as argument");
            System.exit(0);
        }

        SparkConf conf = new SparkConf().setAppName("me.florianschmidt.sparkExample.WordCount").setMaster("local");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> file = context.textFile(args[0]);
        JavaRDD<String> words = file.flatMap(elem -> Arrays.asList(elem.split(" ")));
        JavaPairRDD<String, Integer> pairs = words.mapToPair(elem -> new Tuple2<>(elem, 1));
        JavaPairRDD<String, Integer> counter = pairs.reduceByKey((a, b) -> a + b);

        counter.saveAsTextFile("output.txt");
    }
}
