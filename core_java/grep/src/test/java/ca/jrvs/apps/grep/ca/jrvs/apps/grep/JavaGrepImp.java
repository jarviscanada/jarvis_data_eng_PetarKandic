package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements the methods found in ca.jrvs.apps.grep.JavaGrep.
 * There are three instance fields, as well as a Logger instance.
 * The instance variables store command-line arguments:
 * These are a regex, a root path, and an output file.
 * The regex is used to find matching lines in a given directory (root path)
 * The matching lines are written to the output file.
 * The logger is used to write error messages for caught exceptions.
 * More detailed descriptions may be found below.
 */

public class JavaGrepImp implements JavaGrep
{

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    /**
     * The main method of the program.
     * Three command line arguments are expected.
     * Each are assigned to the relevant instance fields (using the setter methods).
     * The process methods is invoked, and a try-catch block is used to catch IOExceptions.
     * @param args 3 arguments (regex, rootPath, outFile) are expected.
     */
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            throw new IllegalArgumentException("USAGE: ca.jrvs.apps.grep.JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();
        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try
        {
            javaGrepImp.process();
        }
        catch (IOException caughtException)
        {
            javaGrepImp.logger.error("Error: Unable to process", caughtException);
        }

    }

    /**
     * This method controls the overall workflow.
     * A list of Strings, matchedLines is created.
     * We invoke listFiles to find files in the root path.
     * Then, we invoke readLines to find all lines in these files.
     * We then invoke containsPattern to search these lines, and lines which match the regex.
     * These are added to matchedLines.
     * Finally, matchedLines is written to the outFile, using writeToFile.
     * IOExceptions thrown here are caught in the main method.
     * @throws IOException
     */
    @Override
    public void process() throws IOException
    {
        List<String> matchedLines = new ArrayList<>();
        List<File> fileList = listFiles(getRootPath());
        List<String> lineList = new ArrayList<>();
        for (File aFile: fileList)
        {
            lineList.add(readLines(aFile).toString());
        }
        for (String line: lineList)
        {
            if(containsPattern(line))
            {
                matchedLines.add(line);
            }
        }
        writeToFile(matchedLines);
    }

    /**
     * Recursively searches through rootDir, and finds all files "under" it.
     * We convert rootDir to a File, which allows for searching.
     * We explore the current directory using a for loop.
     * If we find a file, we add it to fileList, which will be returned.
     * If we find a directory, we recursively call listFiles, passing the current absolute path.
     * This allows us to explore subdirectories.
     * After the directory has been fully explored, we return the fileList.
     * @param rootDir input directory
     * @return the list of all files under rootDir
     */
    @Override
    public List<File> listFiles(String rootDir)
    {
        List<File> fileList = new ArrayList<>();
        File directory = new File(rootDir);
        if (directory.exists() && directory.isDirectory())
        {
            File[] filesAndDirs = directory.listFiles();
            if (filesAndDirs != null)
            {
                for (File fileOrDir : filesAndDirs)
                {
                    if (fileOrDir.isFile())
                    {
                        fileList.add(fileOrDir);
                    }
                    else if (fileOrDir.isDirectory())
                    {
                        fileList.addAll(listFiles(fileOrDir.getAbsolutePath()));
                    }
                }
            }
        }
        else
        {
            logger.info("The directory does not exist.");
        }
        return fileList;
    }


    /**
     * We find all lines in the files (returned above).
     * They will be stored in lineList.
     * FileReader is used to read from the input file.
     * BufferedReader stores FileReader's stream in a buffer.
     * Lines are encoded (each int corresponds to a character) and entered into the buffer.
     * BufferedReader allows for a flexible buffer size, and allows us to handle IOExceptions.
     * The encoding also allows for efficient reading of characters.
     * Otherwise, FileReader would convert each character into bytes, which would be costly.
     * We add each line to lineList, then return it.
     * @param inputFile file to be read
     * @return lines in the files
     */
    @Override
    public List<String> readLines(File inputFile)
    {
        List<String> lineList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                lineList.add(line);
            }
        }
        catch (IOException caughtException)
        {
            logger.error("Error: Unable to process", caughtException);
        }

        return lineList;
    }

    /**
     * Two libraries are used to match the regex with a given line.
     * Pattern is used to compile the regex.
     * Matcher is used to match the Pattern with the line.
     * If there is a match, true is returned.
     * Otherwise, false is returned.
     * @param line input string
     * @return true if pattern is found, false otherwise.
     */
    @Override
    public boolean containsPattern(String line)
    {
        String regex = getRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * Writes to the user-provided output file.
     * Much like BufferedReader, BufferedWriter places characters from FileWriter's output stream into a buffer.
     * The encoded lines are more efficiently written than they would be with FileWriter.
     * Each line is followed by a new line.
     * @param lines matched line
     * @throws IOException
     */
    @Override
    public void writeToFile(List<String> lines) throws IOException
    {
        String filePath = getOutFile();
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            for (String line : lines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException caughtException)
        {
            logger.error("Error: Unable to process", caughtException);
        }
    }

    /**
      * Getter for the root path.
      * @return the root path
      */
    @Override
    public String getRootPath()
    {
        return this.rootPath;
    }

    /**
      * Setter for the root path.
      * @param rootPath
      */
    @Override
    public void setRootPath(String rootPath)
    {
        this.rootPath=rootPath;
    }

    /**
      * Getter for the regex.
      * @return the regex
      */
    @Override
    public String getRegex()
    {
        return this.regex;
    }

    /**
      * Setter for the regex.
      * @param regex
      */
    @Override
    public void setRegex(String regex)
    {
        this.regex=regex;
    }

    /**
      * Getter for the output file.
      * @return the output file
      */
    @Override
    public String getOutFile()
    {
        return this.outFile;
    }

    /**
      * Setter for the output file.
      * @param outFile
      */
    @Override
    public void setOutFile(String outFile)
    {
        this.outFile=outFile;
    }
}
