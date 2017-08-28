import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Contains various utility methods used by other classes.
 *
 * @author Yash Sharma
 */
public class Util {

    /**
     * Reads the file given its name and returns its contents as a string.
     * @param fileName The name of the file.
     * @return The contents of the file as string.
     */
    public static String readFile(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Cannot read the file.");
            System.exit(0);
        }
        return "";
    }

    /**
     * Writes the given string to a file with the given name.
     * @param fileName The name of the file.
     * @param content Contents to be written to the file.
     */
    public static void writeFile(String fileName, String content){
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
            out.println(content);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Unable to write to file.");
            System.exit(0);
        }
    }
}
