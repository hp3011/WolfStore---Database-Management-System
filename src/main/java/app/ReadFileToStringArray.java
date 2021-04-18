package main.java.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadFileToStringArray {

    //Read file content into the string with - Files.lines(Path path, Charset cs)
 
    public static String[] readLineByLine() 
    {
        String filePath = "main/sql/demo_tables.txt";

        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        String content = contentBuilder.toString();
        String[] arrContent = content.split("\\r?\\n\\n");
        return arrContent;
    }
}
