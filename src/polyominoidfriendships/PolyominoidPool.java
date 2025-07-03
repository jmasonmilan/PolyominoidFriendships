/*

 */
package polyominoidfriendships;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 *
 * @author mason
 */
public class PolyominoidPool {
    
        int minWeight = 9999999;
        int maxStripness = 0;
        ArrayList<BasePolycube> pool;
        Hashtable<String, BasePolycube> h;
        PolyominoidPool() {
            pool = new ArrayList<>();
            h = new Hashtable<>();
        }
        void addWithWeight(Polyominoid p) {
            if (contains(p))
                return;
            add(p);
            int w = p.weight();
            if (w < minWeight)
                minWeight = w;
            int s = p.stripness();
            if (s > maxStripness)
                maxStripness = s;
        }
        
        void add(BasePolycube p) {
            if (contains(p))
                return;
            h.put(p.getRep().toString(), p);
            pool.add(p);
        }
        boolean contains(BasePolycube p) {
            if (h.get(p.getRep().toString()) != null)
                return true;
            return false;
        }
        int size() {
            return h.size();
        }
       
}
