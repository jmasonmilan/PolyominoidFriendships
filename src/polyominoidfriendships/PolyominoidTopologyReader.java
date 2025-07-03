/*
 
 */
package polyominoidfriendships;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author mason
 * 
 * read input polyominoids, of specific size, and write them out into their component files
 * 
 */
public class PolyominoidTopologyReader extends InReader {
    long count;
    ParameterAnalyzer pa;
    int column;
    long done = 0;
    String sep = " ";
    PoolSet poolSet;
    boolean restricted;
    int pSize;
    Pool current = null;
    public PolyominoidTopologyReader(Filter in, String params)   {
        super(in);
        String[] rules = new String[] {"size", "separator", "restricted"};
        pa = new ParameterAnalyzer(params , rules); 
        restricted = pa.getBooleanValue("restricted", false);
        poolSet = new PoolSet();
        pSize = pa.getIntValue("size");
    }
    public boolean write(String s) {
        String partial[] = s.split(sep);
        done++;
        if (done % 10000 == 0) {
            System.err.println("done " + done );
        }
        String p = partial[0];
        String friend = partial[1];
        if (p.equals(friend))
            current = null;


        Pool poolf = poolSet.find(friend);
        if (poolf != null) {
            if (current == null) {
                current = poolf;
            } else {
                if (current == poolf) {
                    // noop
                } else {
                    current = poolSet.absorb(current, poolf);
                }
            }
        } else {
            if (current == null) {
                current = poolSet.newPool(friend);
            } else {
                current.add(friend);
            }
        }
          
            

        return true;
    }
    public void close() {
        int poolNumber = 0;
        for (Pool pool : poolSet.pools) {
            int number = 0;
            poolNumber++;
            System.out.println("" + pool.size());
            String fileName = "various\\topologyr-" + pSize + "-" + poolNumber + ".txt";
            if (restricted)
                fileName = "various\\rest-topologyr-" + pSize + "-" + poolNumber + ".txt";
            try {
                FileWriter fw = new FileWriter(fileName);
                fw = new FileWriter(fileName); 
            
                for (String q : pool.pool) {
                    fw.write(number + " " + q + "\r\n");
                }
                fw.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                System.exit(1);
            }   
        }              
    }   
    class PoolSet {
        ArrayList<Pool> pools;
        PoolSet() {
            pools = new ArrayList<>();
        }
        Pool find(String p) {
            for (Pool pool : pools) {
                if (pool.contains(p))
                    return pool;
            }
            return null;
        }
        int findThru(int rank, String p) {
            for (int i = 0; i <= rank; i++) {
                if (pools.get(i).contains(p))
                    return i;
            }
            return -1;
        }
        boolean contains(String p) {
            Pool pool = find(p);
            return pool != null;
        }
        Pool absorb(Pool pool1, Pool pool2) {
            
            Pool absorber, absorbed;
            if (pool1.size() < pool2.size()) {
                absorbed = pool1;
                absorber = pool2;
            } else {
                absorbed = pool2;
                absorber = pool1;                
            }
            for (String q : absorbed.pool) {
                absorber.add(q);
            } 
            pools.remove(absorbed);
            return absorber;
        }
        Pool newPool(String p) {
            Pool pool = new Pool();
            pool.add(p);
            pools.add(pool);
            return pool;
        }
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
