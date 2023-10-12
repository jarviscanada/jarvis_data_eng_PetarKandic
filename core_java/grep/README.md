# Introduction
The Java Grep App simulates grep, a Linux command line tool used to find lines in files which match specified patterns. By implementing this functionality in Java, we can effectively use grep on all operating systems, and not just Linux. Core Java was used to develop the app. Maven was used to obtain and store dependencies, and to compile the completed app. There are two implementations of the app. One uses normal features of the language, including for loops. The other uses lambda functions, which replace the loops. The lambda implementation is shorter and uses less memory; both implementations are included, allowing the user to choose which one to use. Both implementations were tested with jUnit. 

# Quick Start


The usage of the app is as follows:

```bash
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp [regex] [source directory] [output file]
```
The app will search the source directory for files, and will find lines in these files which match the regex. The matching lines are written to the output file.

# Implemenation
## Pseudocode
The app executes a method known as "process". This method controls the app's overall workflow. The following pseudocode illustrates its operation:

```bash
process()
  create matchedLines
  fileList finds files recursively
      add each line in fileList to lineList using readLines
        add matching lines to matchedLines using containsPattern
  write matchedLines to output file using writeToFile
```


## Performance Issue
This app uses a large amount of memory. A large number of files may be searched, which may yield a large number of lines. To combat the performance issues this causes, it is necessary to be economical with memory. We may use buffers to break down data into encoded chunks, reducing memory demands. We may also use streams more frequently. These do not store data, unlike ArrayLists, and are used merely for computations.

# Test
jUnit was used to test this application. Different values were used for all three arguments. Different regexes were tested, as were different directories containing different folders and files. 

# Deployment
After creating a docker profile (including a username and password), I created a Dockerfile in the app's repository. This file defines which commands can be used on the Docker image containing the app. These are ```java```, and ```jar```. I then packaged the app using maven (```mvn clean package```), created a local Docker image, and pushed the image to the Docker hub.

# Improvement
1) As previously stated, the memory overuse problem should be addressed. Buffers and streams should be implemented.
2) The non-lambda implementation should have more documentation comparing it to the lambda implementation.
3) Additional methods should be added to replace certain functionalities in the process method.
