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
    }

    // indexing prefix length, pseudo code (pi_r)^I
    private static int eqo(ArrayList<Integer> r, ArrayList<Integer> s, double t) {
        return (int) Math.ceil(t/(t + 1) * (r.size() + s.size()));
    }

    // size lower bound on join partners for r
    private static int lb(ArrayList<Integer> r, double t){
        return (int)Math.ceil(r.size()*t);
    }

    // size upper bound on join partners for r
    private static int ub(ArrayList<Integer> r, double t){
        return (int)Math.floor(r.size()/t);
    }
    // probing prefix length, pseudo code pi_r
    private static int ppl(ArrayList<Integer> r, double t){
        return r.size() - lb(r, t) + 1;
    }

    // indexing prefix length, pseudo code (pi_r)^I
    private static int ipl(ArrayList<Integer> r, double t){
        return r.size() - eqo(r, r, t) + 1;
    }

    // inverted list, contains index of lists which contain p
    private static ArrayList<Integer> invList(Integer p, ArrayList<ArrayList<Integer>> list){
        ArrayList<Integer> invList = new ArrayList<>();
        for (int i=0; i<list.size(); i++)
            if (list.get(i).contains(p))
                invList.add(i);
        return invList;
    }

    // verify
    private static ArrayList<Integer> verify(ArrayList<Integer> r, ArrayList<Integer> M, double t){
        return null;
    }

    // allPairs
    private static ArrayList<ArrayList<Integer>> allPairs(ArrayList<ArrayList<Integer>> R, double t){
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();  // resolution
        for (ArrayList<Integer> r: R){
            ArrayList<Integer> M = new ArrayList<>();   // dictionary for candidate sets
            for (int p=0; p<ppl(r, t)-1; p++){
                ArrayList<Integer> I = invList(p, R);   // inverted list, all sets of R containing p
                for (int s=0; s<I.size(); s++){
                    if(R.get(s).size()<lb(r, t))    // delete too short sets
                        I.remove(s);
                    else {
                        int index = I.get(s);
                        if (!M.contains(index))
                            M.add(index, 0);
                        M.add(index, M.get(index) + 1);
                    }
                }
            }
            res.add(M);
        }
        return res;
    }

}

