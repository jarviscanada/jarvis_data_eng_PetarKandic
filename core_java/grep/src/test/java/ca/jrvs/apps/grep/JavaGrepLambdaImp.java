package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp
{

   /**
    * This now replaces the functionality of process().
    * Lambdas are used to iterate through fileList and lineList.
    * @param args 3 arguments (same as superclass)
    */ 

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            throw new IllegalArgumentException("USAGE: ca.jrvs.apps.grep.JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

       try
       {
            javaGrepLambdaImp.process();
            List<File> fileList = new ArrayList<>();
            List<String> lineList = new ArrayList<>();
            List<String> matchedLines = new ArrayList<>();
            fileList = javaGrepLambdaImp.listFiles(javaGrepLambdaImp.getRootPath());
           fileList.forEach(aFile -> {
               List<String> lines = javaGrepLambdaImp.readLines(aFile);
               lines.forEach(line -> lineList.add(line));
           });
        matchedLines = lineList.stream()
                .filter(javaGrepLambdaImp::containsPattern)
                .collect(Collectors.toList());
            javaGrepLambdaImp.writeToFile(matchedLines);
        }
        catch (IOException caughtException)
        {
            javaGrepLambdaImp.logger.error("Error: Unable to process", caughtException);
        }
    }
   
    /*
     * Lambdas replace the original for loop.
     * Collectors is used to add all lines to the list.
     * @param input file
     * @return the list of lines
     */ 
    @Override
    public List<String> readLines(File inputFile)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
        {
            return reader.lines()
                    .collect(Collectors.toList());
        }
        catch (IOException caughtException)
        {
            logger.error("Error: Unable to process", caughtException);
            return null;
        }
    }

    /*
     * Lambdas are used here.
     * flatmap is used to ensure that the result of each recursive call is added.
     */ 
    @Override 
    public List<File> listFiles(String rootDir)
    {
        List<File> fileList = new ArrayList<>();
        File directory = new File(rootDir);

        if (directory.exists() && directory.isDirectory())
        {
            File[] filesAndDirs = Objects.requireNonNull(directory.listFiles());

            fileList.addAll(
                    Stream.of(filesAndDirs)
                            .filter(File::isFile)
                            .collect(Collectors.toList())
            );

            fileList.addAll(
                    Stream.of(filesAndDirs)
                            .filter(File::isDirectory)
                            .flatMap(subDirectory -> listFiles(subDirectory.getAbsolutePath()).stream())
                            .collect(Collectors.toList())
            );
        }
        return fileList;
    }
}
