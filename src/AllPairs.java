import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.lang.management.ManagementFactory;

public class AllPairs {

    public static void main(String[] args) throws Exception {
        
        final double NANOSECONDS_PER_SECOND = 1000000000;
        final ThreadMXBean threadTimer= ManagementFactory.getThreadMXBean();
        final long start;

            

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

        // sort set
        set.sort(Comparator.comparingInt(ArrayList::size));
        for (ArrayList<Integer> i: set)
            Collections.sort(i);

        start = threadTimer.getCurrentThreadCpuTime();

        int allpairs = allPairs(set, threshold);
        
        long now = threadTimer.getCurrentThreadCpuTime();

        double time= (now - start)/ NANOSECONDS_PER_SECOND;

        System.out.println(allpairs);
        System.out.println(time);
    }

    // indexing prefix length, pseudo code (pi_r)^I
    private static int eqo(ArrayList<Integer> r, ArrayList<Integer> s, double t) {
        return (int)Math.ceil(t/(t + 1) * (r.size() + s.size()));
    }

    // size lower bound on join partners for r
    private static double lb(ArrayList<Integer> r, double t){
        return r.size()*t;
    }

    // size upper bound on join partners for r
    private static int ub(ArrayList<Integer> r, double t){
        return (int)Math.floor(r.size()/t);
    }

    // probing prefix length, pseudo code pi_r
    private static int ppl(ArrayList<Integer> r, double t){
        return (r.size() - (int)Math.ceil(lb(r, t)) + 1);
    }

    // indexing prefix length, pseudo code (pi_r)^I
    private static int ipl(ArrayList<Integer> r, double t){
        return (r.size() - eqo(r, r, t) + 1);
    }

    //verify
    private static int verify(ArrayList<Integer> r, HashMap<ArrayList<Integer>, Integer> M, double t){
        int outputsize = 0;
        for (Map.Entry<ArrayList<Integer>, Integer> e : M.entrySet()) {
            ArrayList<Integer> s = e.getKey();
            int minoverlap = eqo(r, s, t);
            int ps = 0;//ipl(s, t);
            int pr = 0;
            int overlap = 0;//e.getValue();
            int maxr = r.size();
            int maxs = s.size();
            while (maxr >= minoverlap && maxs >= minoverlap && minoverlap > overlap) {
                if (Objects.equals(r.get(pr), s.get(ps))) {
                    pr++;
                    ps++;
                    overlap++;
                } else {
                    if (r.get(pr).compareTo(s.get(ps))<0) {
                        pr++;
                        maxr--;
                    } else {
                        ps++;
                        maxs--;
                    }
                }
            }
            if(minoverlap <= overlap){
                outputsize++;
            }
        }
        return outputsize;
    }

    // allPairs
    private static int allPairs(ArrayList<ArrayList<Integer>> R, double t){
        int res = 0;
        HashMap<Integer, ArrayList<Integer>> I = new HashMap<>();   // inverted list, key tokens, value sets containing key
        HashMap<Integer, Integer> start = new HashMap<>();  // start Index for Inverted list instead removing token
        for (int k = 0; k<R.size(); k++) {
            ArrayList<Integer> r = R.get(k);
            HashMap<ArrayList<Integer>, Integer> M = new HashMap<>();   // dictionary for candidate sets, key set, value number of intersecting tokens
            for (int p = 0; p < ppl(r, t); p++) {
                Integer key = r.get(p);
                if (I.get(key)!=null) {
                    int j = start.getOrDefault(key, 0);
                    // storage optimisation
//                    if (j>=I.get(key).size()) {
//                        I.remove(key);
//                        start.remove(key);
//                    }
                    for (int i = j; i < I.get(key).size(); i++) {
                        int idx = I.get(key).get(i);
                        ArrayList<Integer> s = R.get(idx);
                        if (s.size() < lb(r, t)) {
                            if (start.containsKey(key))
                                start.put(key, start.get(key) + 1);
                            else
                                start.put(key, 1);
                        } else {
                            if (!M.containsKey(s))
                                M.put(s, 0);
                            int x = M.get(s) + 1;
                            M.put(s, x);
                        }
                    }
                }
            }
            for (int p = 0; p < ipl(r, t); p++) {
                ArrayList<Integer> x = I.get(r.get(p));
                if (x == null)
                    x = new ArrayList<>();
                x.add(k);
                I.put(r.get(p), x);
            }
            //debug
            //System.out.println("start: " + start);
            //System.out.println("I: " + I);
            //System.out.println("M: " + M);
            if(M.size()>0)
                res += verify(r, M, t);
        }
        return res;
    }
}

