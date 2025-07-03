/*
 
 */
package polyominoidfriendships;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import java.math.BigInteger;


/**
 *
 * @author John Mason
 * identify components in set of polyominoids of a specific size
 */
public class ComponentFriendshipsFilter extends OnlyRepFilter {
    
    ParameterAnalyzer pa;
    String friendsFile;
    Pool component;
    String exclString;
    ArrayList<String> exclusionList;
    public ComponentFriendshipsFilter(Filter nextOneParam, String params) {
        super(nextOneParam);
        String[] rules = new String[] {"friends", "excl"};
        pa = new ParameterAnalyzer(params , rules);  
        friendsFile = pa.getStringValue("friends", true);
        exclString = pa.getStringValue("excl", false);
        component = new Pool();
        exclusionList = new ArrayList<>();
        if (exclString != null) {
            String a[] = exclString.split("/");
            for (String b : a) {
                exclusionList.add(b);
            }
        }
    }
 
    public boolean write(long n, BigInteger rep) {
        component.add(rep.toString());
        return true;
    }
    
    public void close() {
        long counter = 0;
            try {
                BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(friendsFile)));
                while (true) {
                    String line = b.readLine();
                    if (line == null)
                        break;
                    String a[] = line.split(" ");
                    if (component.contains(a[0]) && a[0].compareTo(a[1]) < 0 && !exclusionList.contains(a[0]) && !exclusionList.contains(a[1])) {
                        System.out.println(line);
                        counter++;
                        this.innerWrite(count++, new BigInteger(a[0]));
                        this.innerWrite(count++, new BigInteger(a[1]));
                        
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(1);
            }    
        System.err.println("friendship count " + counter + " for component size " + component.size());
    }
    class Pool {
        ArrayList<String> pool;
        Hashtable<String, String> h;
        Pool() {
            pool = new ArrayList<>();
            h = new Hashtable<>();
        }
        void add(String p) {
            if (contains(p))
                return;
            h.put(p, p);
            pool.add(p);
        }
        boolean contains(String p) {
            if (h.get(p) != null)
                return true;
            return false;
        }
        int size() {
            return h.size();
        }
    }    
}
