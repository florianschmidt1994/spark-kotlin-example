package me.florianschmidt.sparkExample;

import org.apache.spark.SparkConf;
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

        context.textFile(args[0])
                .flatMap(elem -> Arrays.asList(elem.split(" "))) // Stem text on whitespace
                .mapToPair(elem -> new Tuple2<>(elem, 1))        // Create pairs of the type (word, 1)
                .reduceByKey((a, b) -> a + b)                    // Add up value of same words (word, n)
                .mapToPair(Tuple2::swap)                         // Swap pairs to (n, word)
                .sortByKey()
                .saveAsTextFile("output");
    }
}
