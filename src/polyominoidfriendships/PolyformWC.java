/*
 
 */
package polyominoidfriendships;
import java.util.*;
import java.math.BigInteger;
import java.awt.Color;
import java.math.*;

import java.awt.Color;

/**
 *
 * @author jmason
 * Polyform with 2D coordinate set
 */
public abstract class PolyformWC extends  WCCommon  {
    PolyformWC[] colourArray;
    int colourArraySize;
    ArrayList<CoordPair> array ;
    CoordPair fcp;
    static boolean debug = false;
    int maxX, maxY;
    static Color[] colourTable = {Color.BLUE, Color.GREEN, Color.MAGENTA,Color.YELLOW, Color.PINK, Color.ORANGE, Color.RED, Color.CYAN};
    public PolyformWC() {
        
    }
   
    public ArrayList<CoordPair> getArray() {
        return array;
    }  
    public String listCoords() {
        String ret = "";
        for (CoordPair ct : array)
            ret += ct.toString();
        return ret;
    }    
    public void initColourArray() {
        colourArray = new PolyformWC[size()];
    }
    public void clearColourArray() {
        colourArray = null;
    }
    public void addToColourArray(PolyformWC p, int pos) {
        colourArray[pos] = p;
        
    }
    public PolyformWC getColourArray(int i) {
        return this.colourArray[i];
    }
    public PolyformWC maker(int dx, int dy) {
        PolyformWC np = (PolyformWC)(maker());
        for (CoordPair cp : array) {
            np.insert(cp.getX() + dx, cp.getY() + dy);
        }
        return np;
    }
    public int getMinX() {
        int ret = 9999;
        for (int i = 0 ; i < array.size(); i++)
            if (array.get(i).getX() < ret)
                ret = array.get(i).getX();  
        return ret;
    }
    public int getMinY() {
        int ret = 9999;
        for (int i = 0 ; i < array.size(); i++)
            if (array.get(i).getY() < ret)
                ret = array.get(i).getY();  
        return ret;
    }
    public int getMaxX() {
        int ret = 0;
        for (int i = 0 ; i < array.size(); i++)
            if (array.get(i).getX() > ret)
                ret = array.get(i).getX();  
        return ret;
    }
    public int getMaxY() {
        int ret = 0;
        for (int i = 0 ; i < array.size(); i++)
            if (array.get(i).getY() > ret)
                ret = array.get(i).getY();  
        return ret;
    }
    protected void calcMax() {
        if (maxX > 0)
            return;
        for (int i = 0 ; i < array.size(); i++) {
            if (array.get(i).getX() > maxX)
                maxX = array.get(i).getX();
            if (array.get(i).getY() > maxY)
                maxY = array.get(i).getY();  
        }
       
      
    }
    public void nonCBSeal() {
          
        if (rep == null) {
            rep = new BigInteger("0");
            for (int i = 0 ; i < array.size() ; i++) {
                int n = getBit(array.get(i).getX(), array.get(i).getY());
                //System.out.println(ret);
                if (n < 0 ) {
                    System.err.println(array.get(i).toString());
                    int k = 1 / 0;
                }
                rep = rep.setBit(n);
                //System.out.println(ret);
            }
        }
        setFirstSquare();   
        calcMax();
        sealed = true;        
    }
    /**
     * 
     * @return width of the polyform
     */
    public int getWidth() {
        if (!sealed) {
            calcMax();
        }
        return maxX;
    }
    /**
     * 
     * @return height of the polyform
     */
    public int getHeight() {
        if (!sealed) {
            calcMax();
        }
        return maxY;
    }
    public int leftmostTop() {
        int h = maxY;
        for (int x = 1 ; x <= maxX ; x++)
            if (contains(x, h))
                return x;
        System.err.println(this.drawing());
        int qq = 1 / 0;
        return qq;
    }      
    /**
     * 
     * @param x
     * @param y
     * @return the number of the bit that will represent the specificed cooedinate position
     */
    public abstract int getBit(int x, int y)   ;
    /**
     * 
     * @param cp
     * @return whether cp is a cell in the polyform
     */
    public boolean contains(CoordPair cp) {
        
        return contains(cp.getX(), cp.getY());
        
    }   
    /**
     * 
     * @param x
     * @param y
     * @return whether x,y is a cell in the polyform
     */
    public boolean contains(int x, int y) {
        
        
        if (x < 1 || y < 1)
            return false;
        //if (sealed)   speed mprovement that does not work for polyiamonds
        //    return rep.testBit(getBit(x, y));
        
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() )
                return true;
        return false;
    }   
    public boolean easyContains(int x, int y) {
        
        
       
        
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() )
                return true;
        return false;
    }   
    /**
     * 
     * @param x
     * @param y
     * @return the coordinate pair corresponding to the input coordinates, if it exists
     */
    public CoordPair find(int x, int y) {
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() )
                return array.get(i);
        return null;
    }   
    /**
     * 
     * @param x
     * @param y
     * @return the position in the array of the coordinate pair, if it exists
     */
    public int locate(CoordPair cp) {
        return locate(cp.getX(), cp.getY());
    }   
    public int locate(int x, int y) {
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() )
                return i;
        return -1;
    }   
    /**
     * 
     * @return the size of the polyform
     */
    public int size() {
        return array.size();
    }
    /**
     * 
     * @param i
     * @return the i'th coordinate pair
     */
    public CoordPair getCoords(int i) {
        return array.get(i);
    }
    
    /**
     * 
     * @return list of fixed polyforms buildable from this polyform
     */
    public abstract PolySet getFixed();

    /**
     * 
     * @return list of one-sided polyforms buildable from this polyform
     */
    public abstract PolySet getOnesided();    
    /**
     * Insert a cell
     * @param cp 
     */
    public void insert(CoordPair cp) {
        if (cp.getX() < 1 || cp.getY() < 1) {
            //int qq = 1 / 0;
        }
        array.add(cp);
    }  
    public void insert(int x, int y) {
        insert(new CoordPair(x, y));
    }    
    public boolean conditionallyInsert(int x, int y) {
        if (!contains(x, y)) {
            insert(new CoordPair(x, y));
            return true;
        }
        return false;
    }    
    public boolean unconditionallyInsert(int x, int y) {
        if (!contains(x, y)) {
            insert(new CoordPair(x, y));
            return true;
        }
        int qq = 1 / 0;
        return false;
    }    
    public void strictInsert(CoordPair cp) {
        if (cp.getX() < 1 || cp.getY() < 1) {
            int qq = 1 / 0;
        }
        array.add(cp);
    }  
    public void strictInsert(int x, int y) {
        PolyformWC.this.strictInsert(new CoordPair(x, y));
    }    
   
    /**
     * 
     * @return whether this polyform is a loop
     */
    public boolean loop() {
        if (this.countHoles() == 0)
            return false;
        for (int i = 0 ; i < size(); i++) {
            int adj = getAdjacent(array.get(i)).size();
            if (adj != 2)
                return false;           
        }
        return true;
    }
    /**
     * 
     * @return whether this polyform is a strip
     */
    public boolean strip() {
        if (size() <= 1)
            return true;
        
        int adj1 = 0;
        for (int i = 0 ; i < size(); i++) {
            if (array.get(i).isPolyedgeNode())
                continue;
            int adj = getAdjacent(array.get(i)).size();
            if (adj > 2)
                return false;
            if (adj == 1)
                adj1++;
        }
        return adj1 == 2;
    }    
    /**
     * 
     * @param cp
     * @return list of coordinate pairs near the input pair and really belonging to the polyform
     */
    public ArrayList<CoordPair> getAdjacent(CoordPair cp) {
        ArrayList<CoordPair> list = getClose(cp, false, false);
        for (int i = list.size() - 1; i >= 0; i--)
            if (this.contains(list.get(i)))
                list.set(i, this.find(list.get(i).getX(), list.get(i).getY()));
        else
                list.remove(i);
        return list;
    }
    /**
     * 
     * @param cp
     * @param within
     * @return list of coordinate pairs near the input pair; exclusively 
     * for cells within the bordering polygon if "within" is set
     */
    public abstract ArrayList<CoordPair> getClose(CoordPair cp, boolean within, boolean pseudo); // get close within borders
    
    protected void getClose(ArrayList<CoordPair> a, CoordPair cp, int dx, int dy, boolean within) {
        int x = cp.getX() + dx;
        int y = cp.getY() + dy;
        if (within && (x < 1 || x > maxX || y < 1 || y > maxY))
            return;
        a.add(new CoordPair(x, y));
    }    
    /**
     * 
     * @return if polyform is properly connected
     */
    public boolean intact() {
        if (size() == 0)
            return true;
        Propagator prop = new Propagator(this, 1, 2);
        CoordPair cp = prop.find(1);
        int n = prop.set(cp, 1, 3);
        return n == this.size();
    }    
    /**
     * 
     * @param cp
     * @return if input cell can be added
     */
    protected boolean canAdd(CoordPair cp) {
        return !this.contains(cp);
    }    
    /**
     * 
     * @return number of holes in polyform
     */
    public int countHoles() {
        Propagator prop = new Propagator(this, -1, 0);
        int total = prop.getArea() - this.size();
        int holes = 0;
        total -= prop.outside(0, -2); // set outside edges and propagate
        
        /*
        0 unknown
        -2 outside
        -1 body
        1 first hole
        2 second hole
        ...
        */
        
        
        while (total > 0) {
            holes++;
            CoordPair cp = prop.find(0);
            total -= prop.set(cp, 0, holes);
                     
        }
        return holes;
    }   
    public boolean onlySmallHoles() {
        Propagator prop = new Propagator(this, -1, 0);
        int size = size();
        int total = prop.getArea() - size;
       
        
        int holes = 0;
        total -= prop.outside(0, -2); // set outside edges and propagate
        int holeSize = total;
        /*
        0 unknown
        -2 outside
        -1 body
        1 first hole
        2 second hole
        ...
        */
        
        
        while (total > 0) {
            holes++;
            CoordPair cp = prop.find(0);
            total -= prop.set(cp, 0, holes);
                     
        }
        return holes == holeSize;
    }   
    /**
     * 
     * @return size including holes
     */
    public int totalSize() {
        Propagator prop = new Propagator(this, -1, 0);
        int total = prop.getArea() ;
        
        total -= prop.outside(0, -2); // set outside edges and propagate
        
        
        return total;
    }   
    public abstract int outside(int[][] flags, int from, int to);
    public boolean ifUseful(int x, int y) { return true; }
    public int perimeterOrSurface() {
        int ret = 0;
        for (int i = 0; i < array.size(); i++) {
            ret += perimeter(array.get(i));
        }
        return ret;
    }
    protected abstract int perimeter(CoordPair cp);
    int perimeter(CoordPair cp, int dx, int dy) {
        int x = cp.getX() + dx;
        int y = cp.getY() + dy;
        if (x < 1 || y < 1)
            return 1;
        if (this.contains(x, y))
            return 0;
        return 1;
    }    
    void setFirstSquare() {
        if (array.size() == 0)
            return ;
        int x = 1, y = 1;
        
        
        for ( ;  ; ) {
            fcp = new CoordPair(x, y);
            if (this.contains(fcp)) {
                return ;
            }
            
            x--;
            y++;
            if (x < 1) {
                x = y;
                y = 1;
            }
        }            
    }
    public CoordPair getFirstSquare() {
        return fcp;         
    }       
    public abstract boolean needs2CB() ;
    public boolean simplyConnected() {
        //System.err.println("border length " + this.calcBorder().toString().length());
        //System.err.println("perimeterOrSurface " + this.perimeterOrSurface());
        int per = this.perimeterOrSurface();
        int bor = this.calcBorder().toString().length();
        if (per != bor) {
            int dbg = 0;
            //bor = this.calcBorder().toString().length();
        }
        return (bor == per);
    }
    
    public abstract BigInteger calcBorder();
    public boolean wellconnected() {
        if (size() <= 1)
            return true;
        for (int i = 0; i < size(); i++) {
            CoordPair cp = this.getCoords(i);
            BigInteger smaller = this.getRep().clearBit(getBit(cp.getX(), cp.getY()));
            if (!((PolyformWC)(this.maker(smaller.toString()))).intact())
                return false;
        }
        return true;
    }   
    public boolean maxbiased() {
        int pred = calcPredominant();
        int maxBias = calcMaxPred();
        return pred == maxBias;
    }
    public boolean unbiased() {
        int pred = calcPredominant();
        return pred + pred == this.size();
    }
    // return number >= size/2
    int calcPredominant() {
        int ret = countSameColourAsFirst();
        if (ret + ret < size())
            ret = size() - ret;
        return ret;
    }
    protected abstract int calcMaxPred();
    int countSameColourAsFirst() {
        int ret = 0;
        for (int i = 0 ; i < array.size(); i++) {
            if (array.get(i).parity() == fcp.parity())
                ret++;
        }  
        return ret;
    }   
    public boolean treelike() {
        
        return (perimeterOrSurface() == maximalPerimeter())
              //  && simplyConnected()
                ;
    }
    protected abstract int maximalPerimeter();
    public  int internalPoints() {
        Runner.notImplemented("internalPoints");
        return 1 / 0;
    }
    public  PolyformWC core() {
        Runner.notImplemented("core");
        return null;
    }
    public  int internalEdges() {
        Runner.notImplemented("internalEdges");
        return 1 / 0;
    }
    public  int reallyInternalEdges() {
        Runner.notImplemented("veryInternalEdges");
        return 1 / 0;
    }
    protected int internalEdges(int x, int y) {
        if (contains(x, y))
            return 1;
        else
            return 0;
    }    
    public  BigInteger canonise() {
        Runner.notImplemented("canonise");
        return null;
    }
    public PolyformWC copy() {
        
        PolyformWC np = copy(0, 0); // thsi avoids problems with alignment
        if (this.colourArray != null) {
            np.initColourArray();
            for (int i = 0; i < size(); i++)
                np.colourArray[i] = this.colourArray[i];
        }
        return np;
    }
    public PolyformWC copyExcept(CoordPair e) {
        PolyformWC np = (PolyformWC)(maker());
        for (CoordPair cp : array)
            if (!e.equals(cp))
                np.insert(cp);
        return np;
    }
    public CoordPair findLeaf() {
        for (CoordPair cp: array) {
            ArrayList<CoordPair> a = getAdjacent(cp);
            if (a.size() == 1)
                return cp;
        }
        return null;
    }
    public PolyformWC copy(int dx, int dy) {
        PolyformWC np = (PolyformWC)(maker());
        for (CoordPair cp : array)
            np.insert(new CoordPair(cp.getX() + dx, cp.getY() + dy, cp.getColour()));
        return np;
    }
    int countIf(int x, int y) {
        if (contains(x, y))
            return 1;
        else
            return 0;
    }    
    // returns contiguous element if it exists and is distinct from input existing element
    CoordPair tryCP(CoordPair cp, int dx, int dy, CoordPair prevCP) {
        int x = cp.getX() + dx, y = cp.getY() + dy;
        CoordPair ncp = new CoordPair(x, y);
        if (!contains(ncp))
            return null;
        if (ncp.equals(prevCP))
            return null;
        return ncp;
    }    
    //protected abstract int minPerimeter();
    public boolean armfull() {
        System.err.println("armfull not implemented");
        System.exit(1);
        return false;
    }
    
    private ArrayList<PolyformWC> forEnclosureTest;
    public boolean freeEncloses(PolyformWC in) {
        if (forEnclosureTest  == null)
            forEnclosureTest = getFixedArray();
        for (PolyformWC p : forEnclosureTest) {
            if (p.fixedEncloses(in))
                return true;
        }
        return false;
    }
    public boolean fixedEncloses(PolyformWC in) {
        CoordPair cp = findEnclosurePosition(in);
        return (cp != null);
    }
    public CoordPair findEnclosurePosition(PolyformWC in) {
        int thisWidth = getWidth(), thisHeight = getHeight(), otherWidth = in.getWidth(), otherHeight = in.getHeight();
        for (int x = 0; x <= thisWidth - otherWidth; x++)
            for (int y = 0; y <= thisHeight - otherHeight; y++) {
                if (this.fixedEncloses(in, x, y))
                    return new CoordPair(x, y);
            }
        return null;
    }
    public boolean fixedEncloses(PolyformWC in, int x, int y) {
        for (CoordPair cp : in.array) {
            if (!this.contains(cp.getX() + x, cp.getY() + y))
                return false;
        }
        return true;
    }
    public PolyformWC remove(PolyformWC in, int x, int y) {
        PolyformWC np = (PolyformWC)(this.maker());
        for (CoordPair cp : array) {
            if (!in.contains(cp.getX() - x, cp.getY() - y)) {
                np.insert(cp);
            }
        }
        //np.seal();
        return np;
    }
    public ArrayList<PolyformWC> getFixedArray() {
        
        ArrayList<PolyformWC> ret = new ArrayList<>();
        PolySet ps = getFixed();
        for (int i = 0; i < ps.size(); i++) 
            ret.add((PolyformWC)(maker(ps.gets(i))));
        return ret;
    }       
    
}
