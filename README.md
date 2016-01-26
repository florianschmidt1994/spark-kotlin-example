# Apache Spark WordCount Example with Kotlin
 
1. Download Spark Distribution from http://spark.apache.org/downloads.html. For this example I was using the `Prebuild
Version 1.6 with Hadoop 2.6`
  
2. Unzip the downloaded file
 
3. Create new Gradle Project for Kotlin with the following build.gradle file

        buildscript {

            ext.kotlin_version = '1.0.0-beta-4584'

            repositories {
                mavenCentral()
            }
            dependencies {
                classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
            }
        }

        apply plugin: 'java'
        apply plugin: 'kotlin'
        apply plugin: 'idea'

        sourceCompatibility = 1.8
        targetCompatibility = 1.8

        repositories {
            mavenCentral()
        }

        dependencies {
            compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
            compile "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
            compile 'org.apache.spark:spark-core_2.10:1.6.0'
        }

        task wrapper(type: Wrapper) {
            gradleVersion = '2.3'
        }

        sourceSets {
            main.java.srcDirs += 'src/main/kotlin'
        }

        // Create a fat jar with kotlin runtime
        jar {
            dependsOn configurations.runtime
            from {
                (configurations.runtime - configurations.provided).collect {
                    it.isDirectory() ? it : zipTree(it)
                }
            } {
                exclude "META-INF/*"
            }
        }

        configurations {
            provided
            compile.extendsFrom provided
        }


4. Create WordCount.kt File with main function

        context.textFile(args[0]).flatMap { elem -> Arrays.asList(elem.split(" ")) }
            .mapToPair { elem -> Tuple2(elem, 1) }  // Create Tuple of form (word, 1)
            .reduceByKey { a, b -> a!! + b!! }      // Sum up identical words (word, n)
            .mapToPair { it.swap() }                // Swap tuple (n, word)
            .sortByKey()                            // Sort by n (default = ascending)
            .saveAsTextFile("output")               // Save output in folder "output"

5. Download sample input file (e.g. https://www.gutenberg.org/ebooks/4300)

6. Run with

        ./gradlew clean jar

        /path/to/spark-distribution/bin/spark-submit --class com.example.sparkExample.WordCount
        /path/to/project/build/libs/spark-examples.jar /path/to/input/spark-example/input.txt


## LICENSE

The MIT License (MIT)
Copyright (c) 2015 Florian Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
