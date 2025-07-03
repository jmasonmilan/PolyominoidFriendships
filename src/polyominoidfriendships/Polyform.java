/*
 
 */
package polyominoidfriendships;
import java.util.*;
import java.math.BigInteger;
import java.awt.Color;
import java.math.*;

/**
 *
 * @author jmason
 * 
 * generic class for polyforms
 */
public abstract class Polyform  {
    
    public static final int NORTH =1, EAST = 2, SOUTH = 3, WEST = 4;
    public static int MINNESW = NORTH, MAXNESW = WEST;
    public static String NORTHS ="1", EASTS = "2", SOUTHS = "3", WESTS = "4";
    
    public static String UPRIGHTS = "1", DOWNLEFTS = "4";
    public static String RIGHTS = "2", LEFTS = "5";
    public static String DOWNRIGHTS = "3", UPLEFTS = "6";
    
    public static int UPRIGHT = 1, DOWNLEFT = 4;
    public static int RIGHT = 2, LEFT = 5;
    public static int DOWNRIGHT = 3, UPLEFT = 6;
    
    BigInteger rep;
    boolean sealed;
    public boolean isSealed() {
        return sealed;
    }
    
    
    public Polyform() {
        
    }
     
    public abstract void seal();
    public abstract Polyform maker(String in);
    public abstract Polyform maker();
    public abstract int size();
    public abstract String drawing();
    public abstract PolySet genNew(int limit); // genNew may terminate early if limit is exceeded
    public abstract String getUnique();
    public abstract int getWidth() ;
    public abstract int getHeight() ;   
  
    // 1 -> 3
    // 2 -> 4
    // 3 -> 1
    // 4 -> 2
    public static int oppEdge(int e) {
        e += 2;
        if (e == 6)
            e = 2;
        return e;
    }    
    public static int nextEdge(int e) {
        e += 1;
        if (e == 5)
            e = 1;
        return e;
    }    
    public static int prevEdge(int e) {
        e--;
        if (e == 0)
            e = 4;
        return e;
    }    
    public BigInteger getRep() {
        if (!sealed || rep == null) {
            try {
                throw new Exception("polyform is not sealed (has no representation)");
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
            System.exit(1);
        }
        
        return rep;
    }
    public String toString() {
        return rep.toString();
    }
    
    public int logicalSize() {
        return size();
    }
    public boolean equals(Object other) {
        Polyform o = (Polyform)other;
        return (this.getRep().compareTo(o.getRep()) == 0);
    }  
    public int compareTo(Polyform other) {
        if (this.size() != other.size()) {
            if (this.size() < other.size())
                return -1;
            else 
                return 1;
        }
        return this.rep.compareTo(other.rep);
    }  
    public PolySet getFixed() {
        Runner.notImplemented("getFixed");
        return null;
    }
    
    
    public Polyform getUniquePolyform() {
        return maker(getUnique());
    }
    
    
    public void dontTestForCollision() {}
    public int minChildren() {
        int qq = 1 / 0;
        return qq;
    }
    public int maxChildren() {
        int qq = 1 / 0;
        return qq;
    }
    public int countVertices() {
        int qq = 1 / 0;
        return qq;        
    }
    public Polyform multiMaker() {
        int qq = 1 / 0;
        return null;
    }
    public int countHexagons() {
        return 0;
    }
    public int countAdjacencies() {
        return 0;
    }
    public int surroundedTriangles() {
        return 0;
    }
    public int maxHexagonAdjacencies() {
        return 0;
    }
    public  void displayOnJpeg(GraphicsParameters gp) {}
    public int getPixelWidth(GraphicsParameters gp) { return getWidth() * gp.edgeLength; }
    public  int getPixelHeight(GraphicsParameters gp) { 
        if (gp.maxY > 0 && this.getHeight() > gp.maxY)
            return gp.edgeLength * gp.maxY; 
        return gp.edgeLength * this.getHeight(); 
    }
}
