/*
 
 */
package polyominoidfriendships;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

import java.math.BigInteger;

/**
 *
 * @author jmason
 * 
 */
public class PolyominoidTopologyFilter extends OnlyPolyFilter {
    PoolSet poolSet;
    ParameterAnalyzer pa;
    PolyominoidPool array;
    int pSize;
    boolean noCrash, writePair, noWrite,  both, noPool, restricted, fromFlag;
    String from ,to;
    String fromRep ,toRep;
    boolean knife;
    FileWriter fw3;
    long done = 0;
    int stop;
    public PolyominoidTopologyFilter(Filter nextOneParam, String params) {
        super(nextOneParam);
        String[] rules = new String[] {"nocrash", "writepair", "from", "to", "fromrep", "torep", "nowrite", "knife", "both", "nopool", 
            "restricted", "size", "fromflag", "stop"};
        pa = new ParameterAnalyzer(params , rules);  
        noCrash = pa.getBooleanValue("nocrash", false);
        noWrite = pa.getBooleanValue("nowrite", false);
        writePair = pa.getBooleanValue("writepair", false);
        knife = pa.getBooleanValue("knife", true);
       
        both = pa.getBooleanValue("both", false);
        fromFlag = pa.getBooleanValue("fromflag", false);
        restricted = pa.getBooleanValue("restricted", false);
        noPool = pa.getBooleanValue("nopool", false);
        pSize = pa.getIntValue("size", 0);
        stop = pa.getIntValue("stop", 0);
        poolSet = new PoolSet();
        array = new PolyominoidPool();
        from = pa.getStringValue("from", false);
        to = pa.getStringValue("to", false);
        fromRep = pa.getStringValue("fromrep", false);
        toRep = pa.getStringValue("torep", false);
       
    }

    public boolean write(long n, Polyform inPoly) {
        PolyominoidFriendFinder finder = new PolyominoidFriendFinder();
        
            finder.setNoCrash();
               
        Polyominoid p = (Polyominoid)inPoly;
        
        pSize = p.size();
        if (noPool) {
                done++;
                if (done % 10000 == 0) {
                    System.err.println("done " + done + " of " + array.size());
                }
                
                ArrayList<Polyominoid> friends = finder.getFriends(p); // returns p too, as first element
                for (Polyominoid friend : friends) {
                    
                    try {
                
                        fw3.write(p.getRep().toString() + " " + friend.getRep().toString() + "\r\n");

                    }  catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                        System.exit(1);
                    }                        
                        
                         
                }
                             
        }
        else
            array.add(p);

        return true;
    }   
    public void close() {
        if (noPool) {
                    try {
                
                        fw3.close();

                    }  catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                        System.exit(1);
                    }   
            return;
        }
        if (from == null && fromRep == null && !fromFlag)
            ordinary();
        else if (to == null && toRep == null)
            from();
        else
            fromTo();
    }
    void fromTo() {
        PolyominoidFriendFinder finder = new PolyominoidFriendFinder();
        if (noCrash) 
            finder.setNoCrash();
        else
            finder.setArray(array);
        Polyominoid pTo; 
        if (to != null)
            pTo = new Polyominoid(to);
        else 
            pTo = new Polyominoid(new BigInteger(toRep));
        Polyominoid pFrom; 
        if (from != null)
            pFrom = new Polyominoid(from);
        else
            pFrom = new Polyominoid(new BigInteger(fromRep));
        String sTo = pTo.getUnique();
        String sFrom = pFrom.getUnique();
        BigInteger biTo = new BigInteger(sTo);
        BigInteger biFrom = new BigInteger(sFrom);
        pTo = new Polyominoid(biTo);
        pFrom = new Polyominoid(biFrom);
        poolSet.newPool(pTo);
        PolyominoidPool current = poolSet.pools.get(0);
        main: while (true) {
            PolyominoidPool next = null;
            for (BasePolycube p : current.pool) {
                ArrayList<Polyominoid> friends = finder.getFriends((Polyominoid)p);
                for (Polyominoid friend : friends) {
                    if (poolSet.contains(friend))
                        continue;
                    if (next == null) {
                        next = poolSet.newPool(friend);
                    } else {
                        next.add(friend);
                    }
                    if (friend.equals(pFrom))
                        break main;
                }
            }
            if (next == null) {
                System.err.println("no chain found");
                System.exit(1);
            }     
            current = next;
        }
        int rank = poolSet.pools.size() - 2;
        Polyominoid curr = pFrom;
        innerWrite(count++, pFrom);
        while (true) {
            ArrayList<Polyominoid> friends = finder.getFriends(curr);
            for (Polyominoid friend : friends) {
                int newRank = poolSet.findThru(rank, friend);
                if (newRank < 0)
                    continue;
                innerWrite(count++, friend);
                rank = newRank - 1;
                if (rank < 0) {
                    System.out.println(curr.getRep().toString() + " " + friend.getRep().toString());
                }
                curr = friend;
                break;
            }
            if (rank < 0) {
                
                break;
            }
        }
    }
    void from() {
        PolyominoidFriendFinder finder = new PolyominoidFriendFinder();
        if (noCrash) 
            finder.setNoCrash();
        else
            finder.setArray(array);        
        Polyominoid pFrom; 
        Polyominoid strip;
        if (from != null)
            pFrom = new Polyominoid(from);
        else if (fromFlag)
            pFrom = (Polyominoid)(array.pool.get(0));
        else
            pFrom = new Polyominoid(new BigInteger(fromRep));
        
        String sFrom = pFrom.getUnique();
        
        BigInteger biFrom = new BigInteger(sFrom);
        
        pFrom = new Polyominoid(biFrom);
        poolSet.newPool(pFrom);
        PolyominoidPool current = poolSet.pools.get(0);
        //boolean tryOnceMore = false;
        main: while (true) {
            System.err.println("sizes : " + poolSet.size() + " " + poolSet.totalSize() + " "+ current.maxStripness);
            PolyominoidPool next = null;
            for (BasePolycube p : current.pool) {
                if (
                        //!tryOnceMore && 
                        //p.weight() > current.minWeight
                        ((Polyominoid)p).stripness() < current.maxStripness
                        )
                    continue;
                ArrayList<Polyominoid> friends = finder.getFriends((Polyominoid)p);
                for (Polyominoid friend : friends) {
                    if (poolSet.contains(friend))
                        continue;
                    if (next == null) {
                        next = poolSet.newPoolWithWeight(friend);
                    } else {
                        next.addWithWeight(friend);
                    }
                    if (friend.strip() || (stop > 0 && poolSet.size() > stop)) {
                        strip = friend;
                        break main;
                    }
                }
            }
            if (next == null) {
                //if (tryOnceMore) {
                    System.err.println("no chain found");
                    System.exit(1);
                //} else {
                //    tryOnceMore = true;
                //    continue;
                //}
            }     
            current = next;
        }
        ArrayList<Polyominoid> sequence = new ArrayList<>();
        int rank = poolSet.pools.size() - 2;
        Polyominoid curr = strip;
        //innerWrite(count++, strip);
        sequence.add(strip);
        while (true) {
            ArrayList<Polyominoid> friends = finder.getFriends(curr);
            for (Polyominoid friend : friends) {
                int newRank = poolSet.findThru(rank, friend);
                if (newRank < 0)
                    continue;
                //innerWrite(count++, friend);
                sequence.add(friend);
                rank = newRank - 1;
                if (rank < 0) {
                    System.out.println(curr.getRep().toString() + " " + friend.getRep().toString());
                }
                curr = friend;
                break;
            }
            if (rank < 0) {
                
                break;
            }
        }
        Polyominoid previousRight = null;
        for (int i = sequence.size() - 1; i > 0; i--) {
            PolyominoidFriendFinder f = new PolyominoidFriendFinder();
            Polyominoid left = sequence.get(i);
            
            Polyominoid right = sequence.get(i - 1);
            
            f.setNoCrash();

            f.setTo(right);
            
            ArrayList<Polyominoid> pair = f.getFriends(left); 
            if (pair.size() != 2) {
                int qq = 1 / 0;
            }
            Polyominoid newLeft = pair.get(0);
            Polyominoid newRight = pair.get(1);
         
            if (previousRight != null) {
                //newLeft = newLeft.reorientate(newLeft.removeHinge(), previousRight.removeHinge());
                //newRight = newRight.reorientate(left.removeHinge(), newLeft.removeHinge());
                int distance = newLeft.removeHinge().findTransformation(previousRight.removeHinge());
                newLeft = (Polyominoid)(newLeft.getTransformation(distance));
                newRight = (Polyominoid)(newRight.getTransformation(distance));
            }        
          
            innerWrite(0, newLeft);
            innerWrite(0, newRight);              
            previousRight = newRight;
        }
    }    
    void ordinary() {
        PolyominoidFriendFinder finder = new PolyominoidFriendFinder();
        if (noCrash) 
            finder.setNoCrash();
        else
            finder.setArray(array);        
        //Enumeration f = array.h.keys();
        try {
            String f1 = "various\\friends-" + pSize + "-1.txt";
            String f2 = "various\\friends-" + pSize + "-2.txt";
            FileWriter fw1 = null;
            FileWriter fw2 = null;
            if (!noWrite) {
             fw1 = new FileWriter(f1);
             fw2 = new FileWriter(f2);
            }
                
                             
            for (BasePolycube p : array.pool) {
            //while (f.hasMoreElements()){
                  //System.out.println(e.nextElement());
                //Object key = f.nextElement();
                //Polyominoid p = array.h.get(key);
                done++;
                if (done % 10000 == 0) {
                    System.err.println("done " + done + " of " + array.size());
                }
                PolyominoidPool current = null;
                ArrayList<Polyominoid> friends = finder.getFriends((Polyominoid)p); // returns p too, as first element
                for (Polyominoid friend : friends) {
                    if (!noWrite) {
                        fw1.write(p.getRep().toString() + " " + friend.getRep().toString() + "\r\n");
                        fw2.write(friend.getRep().toString() + " " + p.getRep().toString() + "\r\n");
                    }
                    
                    PolyominoidPool pool = poolSet.find(friend);
                    if (pool != null) {
                        if (current == null) {
                            current = pool;
                        } else {
                            if (current == pool) {
                                // noop
                            } else {
                                poolSet.absorb(current, pool);
                            }
                        }
                    } else {
                        if (current == null) {
                            current = poolSet.newPool(friend);
                        } else {
                            current.add(friend);
                        }
                    }
                }     
            }
            if (!noWrite) {
                fw1.close();
                fw2.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(1);
        }            
        int poolNumber = 0;
        for (PolyominoidPool pool : poolSet.pools) {
            int number = 0;
            poolNumber++;
            System.out.println("" + pool.size());
            String fileName = "topology-" + pSize + "-" + poolNumber + ".txt";
            
            try {
                FileWriter fw = new FileWriter(fileName);
                fw = new FileWriter(fileName); 
             /*
                Enumeration e = pool.h.keys();

                while (e.hasMoreElements()){
              
                    Object key = e.nextElement();
                    Polyominoid p = pool.h.get(key);
                    fw.write(number + " " + p.getRep() + "\r\n");
                    //innerWrite(count++, p);
                } */
                for (BasePolycube p : pool.pool) {
                    fw.write(number + " " + p.getRep() + "\r\n");
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
        ArrayList<PolyominoidPool> pools;
        PoolSet() {
            pools = new ArrayList<>();
        }
        PolyominoidPool find(Polyominoid p) {
            for (PolyominoidPool pool : pools) {
                if (pool.contains(p))
                    return pool;
            }
            return null;
        }
        int findThru(int rank, Polyominoid p) {
            for (int i = 0; i <= rank; i++) {
                if (pools.get(i).contains(p))
                    return i;
            }
            return -1;
        }
        boolean contains(Polyominoid p) {
            PolyominoidPool pool = find(p);
            return pool != null;
        }
        void absorb(PolyominoidPool absorber, PolyominoidPool absorbed) {
            //Enumeration e = absorbed.h.keys();
/*
            while (e.hasMoreElements()){
              //System.out.println(e.nextElement());
                Object key = e.nextElement();
                Polyominoid q = absorbed.h.get(key);   
                absorber.add(q);
            }     
            */
            
            for (BasePolycube q : absorbed.pool) {
                absorber.add(q);
            } 
            pools.remove(absorbed);
        }
        PolyominoidPool newPool(Polyominoid p) {
            PolyominoidPool pool = new PolyominoidPool();
            pool.add(p);
            pools.add(pool);
            return pool;
        }
        PolyominoidPool newPoolWithWeight(Polyominoid p) {
            PolyominoidPool pool = new PolyominoidPool();
            pool.addWithWeight(p);
            pools.add(pool);
            return pool;
        }
        int size() {
            return pools.size();
        }
        int totalSize() {
            int ret = 0;
            for (PolyominoidPool pool : pools)
                ret += pool.size();
            return ret;
        }
    }
 
    class VerifiedPolyominoid {
        Polyominoid p;
        int ok;
        CoordTriplet cp;
        ArrayList<CoordTriplet>  newCoords;
        boolean diff;
        CoordTriplet delta;
        VerifiedPolyominoid(Polyominoid p, int ok, CoordTriplet cp, ArrayList<CoordTriplet>  nc, boolean diff, CoordTriplet delta) {
            this.p = p;
            this.ok = ok;
            this.cp = cp;
            newCoords = nc;
            this.diff = diff;
            this.delta = delta;
        }
    }
}

    
 
