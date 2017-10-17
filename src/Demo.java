import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Demo {
    public static void main(String[] args) throws Exception {

        // exit if no command line argument present or invalid filename
        if (args.length == 0 || !new File(args[0]).exists()) {
            System.out.println("Please enter a valid filename as argument!");
            return;
        }

        // open file for reading
        // if the file is not found the method will throw an exception and exit
        BufferedReader b = new BufferedReader(new FileReader(args[0]));

        // read first line from file
        String line = b.readLine();

        // as long as there are lines in the file
        while (line != null) {
            // read next line from file
            line = b.readLine();
        }
    }
}
