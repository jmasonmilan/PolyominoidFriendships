/*

 */
package polyominoidfriendships;

import java.awt.Color;
import java.math.BigInteger;
import java.util.ArrayList;

import java.awt.*;

/**
 *
 * @author mason
 * 
 * In this class a polyedge is represented by a polyomino.
 * 
 * For example, this polyedge
 * 
 * +-+
 * |
 * +
 * |
 * +
 * 
 * is represented by this polyomino
 * 
 * OOO
 * O
 * O
 * O
 * O
 * 
 * So each node of the polyedge is a square of the polyomino such that x+y mod 2 = 0,
 * and each edge is a square such that x+y mod 2 = 1; if x is odd then the edge is vertical, if even then horizontal
 */
public class Polyedge extends Polyomino {
    public Polyedge(BigInteger inValue) {
        super(inValue);  
    }    
   
    public Polyedge() {
        super();
    }  
    public Polyedge(String inValue) {
        array = new ArrayList<CoordPair>();
        int y = 1, x = 1;
        for (int i = 0; i < inValue.length() ; i++) {
            String t = inValue.substring(i, i + 1);
            if (t.equals( "/")) {
                x = 1; 
                y++;
                continue;
            }
            if (t.equals("O"))
                this.insert(new CoordPair(x, y));
            if (t.equals("X")) {
                CoordPair cp = new CoordPair(x, y);
                cp.setFlag();
                insert(cp);
            }
            
            x++;
        }
        
        seal();  
    }
    public Polyedge maker(String srep) {
        return new Polyedge(new BigInteger(srep));
    }  
    public Polyedge maker() {
        return new Polyedge();
    }    
    public void addAdjacentElement( ArrayList<CoordPair> list, CoordPair cp, int dx, int dy) {
        CoordPair ncp = new CoordPair(cp.getX() + dx, cp.getY() + dy);
        if (contains(ncp) && !list.contains(ncp))
            list.add(ncp);
    } 
    public void complete() {
        int s = array.size();
        for (int i = 0; i < s ; i++) {
            CoordPair cp = array.get(i);
            if (cp.isPolyedgeNode())
                continue;
            if (cp.isPolyedgeHorizontal()) {
                complete(cp, -1, 0);
                complete(cp, 1, 0);
            } else {
                complete(cp, 0, 1);
                complete(cp, 0, -1);
            }
        }
    }
    void complete(CoordPair cp, int dx, int dy) {
        CoordPair newCp = cp.add(dx, dy);
        if (!contains(newCp))
            insert(newCp);
    }
    public ArrayList<Polyedge> get4List() {
        ArrayList<Polyedge> ret = new ArrayList<>();
        ret.add(this);
        Polyedge p = (Polyedge)(this.rotate90());
        ret.add(p);
        p = (Polyedge)(p.rotate90());
        ret.add(p);
        p = (Polyedge)(p.rotate90());
        ret.add(p);
        return ret;
    }
    public Polyedge addHinge(CoordPair other, CoordPair delta) {
        Polyedge np = new Polyedge();
        for (CoordPair cp : array) {
            if (cp.equals(other.add(delta))) {
                CoordPair tcp = cp.add(0, 0);
                tcp.setFlag();
                np.insert(tcp);
            } else
                np.insert(cp);
        }
        //np.insert(other.add(delta));
        np.complete();
        np.seal();
        if (np.size() != size()) {
            int qq = 1  / 0;
        }
        return np;
    }
    public PolySet genNew(int limit) {
        PolySet ps = new PolySet();
        if (this.array.size() == 0) {
            ps.adds("1");
            return ps;
        }
        for (int i = 0 ; i < this.array.size() ; i++) {
            if (array.get(i).parity() == 0) {
                tryDiag(i, ps, 1, 0);
                tryDiag(i, ps, -1, 0);
                tryDiag(i, ps, 0, 1);
                tryDiag(i, ps, 0, -1);
            }
            
        }
        return ps;
    }   
    void tryDiag(int i, PolySet ps, int dx, int dy) {
        int x = array.get(i).getX() + dx;
        int y = array.get(i).getY() + dy;    

        Polyomino p1 = this.addOneCoords(x, y);
        if (x < 1)
            x++;
        if (y < 1)
            y++;        
        if (p1 != null) { // if it was possible to add an edge, then p1 contains it
            int n = p1.locate(x, y);
            if (!p1.tryOne(n, ps, dx, dy)) { // try to add to ps p2, where p2 is p1 + a node
                ps.addUniques(p1); // the node already exists; add p1 to ps
            }
           
        }       
    }   
    public int logicalSize() {
        return dashes();
    }
    public int length() {
        int ret = this.getHeight();
        int w = this.getWidth();
        if (w > ret)
            ret = w;
        return ret;
    }
    public int dots() {
        int ret = 0;
        for (CoordPair cp : array) {
            if (cp.parity() == 0)
                ret++;
        }
        return ret;
    }
    public int dashes() {
        return size() - dots();
    }
    public boolean treelike() {
        return dots() == dashes() + 1;
    }
    
    public Polyominoid makePolyominoid() {
        Polyominoid p = new Polyominoid();
        for (CoordPair cp : array) {
            if ((cp.getX() + cp.getY()) % 2 != 0)
                p.insert(new CoordTriplet(cp.getX(), cp.getY(), 2));
        }
        p.seal();
        return p;
    }
    public int weight() {
        int ret = 0;
        
        
        for (CoordPair ct : array) {
            if (ct.isPolyedgeNode())
                continue;
            int n = this.getAdjacent(ct).size();
            if (n > 2)
                ret += n;
        }

       
        return ret;
    }    
    public ArrayList<CoordPair> getAdjacent(CoordPair cp) {
        return getAdjacent(array, cp);
    }
    public ArrayList<CoordPair> getAdjacent(ArrayList<CoordPair> a, CoordPair cp) {
        if (cp.isPolyedgeNode()) {
            int qq = 1 / 0;
        }
        ArrayList<CoordPair> ret = new ArrayList<>();
        if (cp.isPolyedgeHorizontal()) {
            listAdd(a, ret, cp, -2, 0);
            listAdd(a, ret, cp, 2, 0);
            listAdd(a, ret, cp, -1, 1);
            listAdd(a, ret, cp, -1, -1);
            listAdd(a, ret, cp, 1, 1);
            listAdd(a, ret, cp, 1, -1);
            
        } else {
            listAdd(a, ret, cp, 0, -2);
            listAdd(a, ret, cp, 0, 2);
            listAdd(a, ret, cp, -1, 1);
            listAdd(a, ret, cp, -1, -1);
            listAdd(a, ret, cp, 1, 1);
            listAdd(a, ret, cp, 1, -1);            
        }
        return ret;
    }
    void listAdd(ArrayList<CoordPair> a, ArrayList<CoordPair> list, CoordPair cp, int dx, int dy) {
        int x = cp.getX() + dx, y = cp.getY() + dy;
        if (a.contains(new CoordPair(x, y)))
            list.add(new CoordPair(x, y));
    }
    public int stripness() {
       
        ArrayList<List> listOfLists = getListOfLists();
        StripSet ss = findAllStrips(listOfLists);
        int ret = 0;
        for (Strip s : ss.strips)
            ret += s.score;
        return ret;
       
    }
    
    ArrayList<List>  getListOfLists() {
        ArrayList<List> ret = new ArrayList<>();
    
        for (CoordPair ct : array) {
            if (ct.isPolyedgeNode())
                continue;
            ret.add(new List(ct));
        }
        return ret;
    }
    class List {
        CoordPair ct;
        ArrayList<CoordPair> list;
        List(CoordPair ct) {
            this.ct = ct;
            list = getAdjacent(ct);
        }
    }
    StripSet findAllStrips(ArrayList<List> listOfLists) {
       
        StripSet ss = new StripSet();
        for (List list : listOfLists) {
            if (ss.contains(list.ct))
                continue;
            
            if (list.list.size() != 2) {
                if (list.list.size() < 3)
                    continue;
                CoordPair adj = findAdjacentSingleton(list.ct);
                if (adj == null)
                    continue;
                Strip s = new Strip(list.ct);
                ss.strips.add(s);
                
                s.propagate(adj, false);
                s.conclude();
                CoordPair n3 = null;
                //if (s.freeEnd || s.freeStart)
                    n3 = s.findNearest3Node(listOfLists);
                s.score(n3);       
                continue;
            }
                
            if (areAdjacent(list.list.get(0), list.list.get(1)))
                continue;
            Strip s = new Strip(list.ct);
            ss.strips.add(s);
            s.propagate(list.list.get(0), true);
            s.propagate(list.list.get(1), false);
            s.conclude();
            CoordPair n3 = null;
            if (s.freeEnd || s.freeStart)
                n3 = s.findFurthest3Node(listOfLists);
            s.score(n3);            
        }
        /*
        Strip ret = ss.strips.get(0);
        for (int i = 1 ; i < ss.strips.size(); i++)
            if (ss.strips.get(i).elementSet.size() > ret.elementSet.size())
                ret = ss.strips.get(i);
        */
        return ss;
    }
    CoordPair findAdjacentSingleton(CoordPair ct) {
        ArrayList<CoordPair> list = getAdjacent(ct);
        for (CoordPair possible : list) {
            if (!adjInList(ct, list))
                return possible;
        }
        return null;
    }
    boolean adjInList(CoordPair ct, ArrayList<CoordPair> list) {
        for (CoordPair other : list) {
            if (ct.equals(other))
                continue;
            if (areAdjacent(ct, other))
                return true;
        }
        return false;
    }
    public boolean areAdjacent(CoordPair one, CoordPair other) {
        if (one.isPolyedgeNode()) {
            int qq = 1 / 0;
        }
        if (other.isPolyedgeNode()) {
            int qq = 1 / 0;
        }
        boolean ret = false;
        if (one.isPolyedgeHorizontal()) {
            ret |= testAdj(one, other, -2, 0);
            ret |= testAdj(one, other, 2, 0);
            ret |= testAdj(one, other, -1, 1);
            ret |= testAdj(one, other, -1, -1);
            ret |= testAdj(one, other, 1, 1);
            ret |= testAdj(one, other, 1, -1);
            
        } else {
            ret |= testAdj(one, other, 0, -2);
            ret |= testAdj(one, other, 0, 2);
            ret |= testAdj(one, other, -1, 1);
            ret |= testAdj(one, other, -1, -1);
            ret |= testAdj(one, other, 1, 1);
            ret |= testAdj(one, other, 1, -1);            
        }
        return ret;        
    }
    boolean testAdj(CoordPair one, CoordPair other, int dx, int dy) {
        int x = one.getX() + dx, y = one.getY() + dy;
        return other.equals(new CoordPair(x, y));
    }
    class Strip {
        boolean freeStart, freeEnd;
        int score;
        ArrayList<CoordPair> elementSet;
        Strip(CoordPair ct) {
            elementSet = new ArrayList<>();
            elementSet.add(ct);
        }
        CoordPair findNearest3Node(ArrayList<List> listOfLists) {
            CoordPair ret = null;
            int dist = 9999;
            for (List list : listOfLists) {
                if (elementSet.contains(list.ct))
                    continue;
                if (list.list.size() < 2)
                    continue;
                if (list.list.size() > 2 || areAdjacent(list.list.get(0), list.list.get(1))) {
                    if (distance(this.freeNode(), list.ct) < dist) {
                        dist = distance(this.freeNode(), list.ct);
                        ret = list.ct;
                    }
                }
            }
            return ret;
        }
        CoordPair findFurthest3Node(ArrayList<List> listOfLists) {
            CoordPair ret = null;
            int dist = 0;
            for (List list : listOfLists) {
                if (elementSet.contains(list.ct))
                    continue;
                if (list.list.size() < 2)
                    continue;
                if (list.list.size() > 2 || areAdjacent(list.list.get(0), list.list.get(1))) {
                    if (distance(this.freeNode(), list.ct) > dist) {
                        dist = distance(this.freeNode(), list.ct);
                        ret = list.ct;
                    }
                }
            }
            return ret;
        }
        void propagate(CoordPair ct, boolean insert) {
            
            while(true) {
                boolean loop = false;
                if (insert) {
                    elementSet.add(0,ct);
                } else {
                    elementSet.add(ct);
                }
                ArrayList<CoordPair> list = getAdjacent(ct);
                if (list.size() > 2)
                    return;
                if (list.size() == 1)
                    return;
                for (int i = 0; i < 2; i++) {
                    CoordPair ctNext = list.get(i);
                    if (list.contains(ctNext))
                        continue;

                    ct = ctNext;
                    loop = true;
                    break;
                }
                if (!loop)
                    break;
            }
        }
        void conclude() {
            ArrayList<CoordPair> startAdjList = getAdjacent(elementSet.get(0));
            if (startAdjList.size() == 1)
                freeStart = true;
            ArrayList<CoordPair> endAdjList = getAdjacent(elementSet.get(elementSet.size() - 1));
            if (endAdjList.size() == 1)
                freeEnd = true;
            
        }
        void score(CoordPair n3) {
            int ret = 2 * elementSet.size() * elementSet.size();
            if (freeEnd || freeStart)
                ret--;
            ret *= 100;
            if (n3 != null)
                ret -= distance(n3, freeNode());
            score = ret;
        }
        CoordPair freeNode() {
            if (freeStart)
                return elementSet.get(0);
            else
                return elementSet.get(elementSet.size() - 1);
        }
        int distance(CoordPair node1, CoordPair node2) {
            return Math.abs(node2.getX() - node1.getX()) + Math.abs(node2.getY() - node1.getY()) ;
        }
    }
    class StripSet {
        ArrayList<Strip> strips;
        StripSet() {
            strips = new ArrayList<>();
        }
        boolean contains(CoordPair ct) {
            for (Strip strip : strips) {
                if (strip.elementSet.contains(ct))
                    return true;
            }
            return false;
        }
    } 
    public boolean intact() {
        ArrayList<CoordPair> build = new ArrayList<>();
        build.add(this.getCoords(0));
        for (int i = 0; i < build.size(); i++) {
            ArrayList<CoordPair>  list = super.getAdjacent( build.get(i));
            for (CoordPair ct : list)
                if (!build.contains(ct))
                    build.add(ct);
        }
        boolean ret = (build.size() == this.size());
        int z = getWidth() % 2;
        if (z == 0) {
            int qq = 1 / 0;
        }
        return ret;
    }    
    public boolean edgeIntact() {
        
        boolean ret = edgeIntact(array);
        return ret;
    }    
    public boolean edgeIntact(ArrayList<CoordPair> a) {
        ArrayList<CoordPair> build = new ArrayList<>();
        build.add(a.get(0));
        for (int i = 0; i < build.size(); i++) {
            ArrayList<CoordPair>  list = getAdjacent(a, build.get(i));
            for (CoordPair ct : list)
                if (!build.contains(ct))
                    build.add(ct);
        }
        boolean ret = (build.size() == a.size());
        return ret;
    }    
    public int nodeWeight(int m3, int m4) {
        int n3 = 0, n4 = 0;
        for (CoordPair cp : array) {
            if (!cp.isPolyedgeNode())
                continue;
            int count = countEdges(cp);
            if (count == 3)
                n3++;
            else if (count == 4)
                n4++;
        }
        int ret = n3 * m3 + n4 * m4;
        return ret;
    }
    int countEdges(CoordPair cp) {
        int ret = countEdges(cp, 0, 1);
        ret += countEdges(cp, 0, -1);
        ret += countEdges(cp, 1, 0);
        ret += countEdges(cp, -1, 0);
        return ret;
    }
    int countEdges(CoordPair cp, int dx, int dy) {
        if (contains(cp.getX() + dx, cp.getY() + dy))
            return 1;
        return 0;
    }
    public int straightness() {
        int ret = 0;
        for (CoordPair cp : array)
            if (cp.isPolyedgeEdge())
                ret += straightness(cp);
        return ret;
    }
    int straightness(CoordPair cp) {
        int ret = 10;
        ArrayList<CoordPair> list = getAdjacent(cp);
        for (CoordPair ocp : list) {
            if (!cp.aligned(ocp))
                ret--;
        }
        return ret;
    }
}
