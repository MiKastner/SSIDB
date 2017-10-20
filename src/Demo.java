import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Demo {
    public static void main(String[] args) throws Exception {

        // exit if no command line argument present or invalid filename
        if (args.length == 0 || !new File(args[0]).exists()) {
            System.out.println("Please enter a valid filename as argument!");
            return;
        }

        double threshold = Double.parseDouble(args[1]);

        // open file for reading
        // if the file is not found the method will throw an exception and exit
        BufferedReader b = new BufferedReader(new FileReader(args[0]));

        // read first line from file
        String line = b.readLine();

        // as long as there are lines in the file


        ArrayList<ArrayList<Integer>> set = new ArrayList<>();

        while (line != null) {
            ArrayList<Integer> item = new ArrayList<>();
            StringBuilder s = new StringBuilder();

            for (int j=0; j<line.length(); j++) {
                if (line.charAt(j) != ' ')
                    s.append(line.charAt(j));
                if (line.charAt(j) == ' ' || j==line.length()-1){
                    item.add(Integer.parseInt(s.toString()));
                    s = new StringBuilder();
                }
            }
            set.add(item);
            // read next line from file
            line = b.readLine();
        }

        System.out.println(set);
        System.out.println(threshold);


        System.out.println(jacSim(set.get(1), set.get(2)));

    }

    private static <E> double jacSim(ArrayList<E> r, ArrayList<E> s){
        HashSet<E> intersect = new HashSet<>();
        HashSet<E> union = new HashSet<>();

        // get union of r and s
        union.addAll(r);
        union.addAll(s);

        // get intersect
        for (E item: union){
            if (s.contains(item) && r.contains(item))
                intersect.add(item);
        }

        return (double)intersect.size()/union.size();

    }

    private static <E, F> boolean verify(ArrayList<E> r, ArrayList<F> M, double t){
        return false;
    }
}

