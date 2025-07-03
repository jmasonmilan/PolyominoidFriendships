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
 * common class for polyforms in 3D
 */
public abstract class PolyformWC3D extends WCCommon {
    ArrayList<CoordTriplet> array ;
    CoordTriplet fcp;
   
    int maxX, maxY, maxZ;
    
    public PolyformWC3D() {
        
    }
     
 
    protected void calcMax() {
        for (int i = 0 ; i < array.size(); i++) {
            if (array.get(i).getX() > maxX)
                maxX = array.get(i).getX();
            if (array.get(i).getY() > maxY)
                maxY = array.get(i).getY();       
            if (array.get(i).getZ() > maxZ)
                maxZ = array.get(i).getZ();             
        }
        
    
        
       
    }
    public void nonCBSeal() {
          
        if (rep == null) {
            rep = new BigInteger("0");
            for (int i = 0 ; i < array.size() ; i++) {
                int n = getBit(array.get(i).getX(), array.get(i).getY(), array.get(i).getZ());
                //System.out.println(ret);
                if (n < 0 ) {
                    int k = 1 / 0;
                }
                rep = rep.setBit(n);
                //System.out.println(ret);
            }
        }
        //setFirstSquare();   
        calcMax();
        sealed = true;        
    }
    /**
     * 
     * @return width of the polyform
     */
    public int getWidth() {
        if (!sealed) {
            int i = 1 / 0;
        }
        return maxX;
    }
    /**
     * 
     * @return height of the polyform
     */
    public int getHeight() {
        if (!sealed) {
            int i = 1 / 0;
        }
        return maxY;
    }
    public int getDepth() {
        if (!sealed) {
            int i = 1 / 0;
        }
        return maxZ;
    }
    public void add(CoordTriplet cp) {
        array.add(cp);
    }
    /**
     * 
     * @param x
     * @param y
     * @return the number of the bit that will represent the specificed cooedinate position
     */
    public abstract int getBit(int x, int y, int z)   ;
    /**
     * 
     * @param cp
     * @return whether cp is a cell in the polyform
     */
    public boolean contains(CoordTriplet cp) {
        
        return contains(cp.getX(), cp.getY(), cp.getZ());
        
    }   
    /**
     * 
     * @param x
     * @param y
     * @return whether x,y is a cell in the polyform
     */
    public boolean contains(int x, int y, int z) {
        
        
        //if (x < 1 || y < 1 || z < 1) would be quicker but does not give correct result on unaligned polyform
        //    return false;
        //if (sealed)   speed mprovement that does not work for polyiamonds
        //    return rep.testBit(getBit(x, y));
        
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY()  && z == array.get(i).getZ() )
                return true;
        return false;
    }   
    /**
     * 
     * @param x
     * @param y
     * @return the coordinate pair corresponding to the input coordinates, if it exists
     */
    public CoordTriplet find(int x, int y, int z) {
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() && z == array.get(i).getZ() )
                return array.get(i);
        return null;
    }   
    /**
     * 
     * @param x
     * @param y
     * @return the position in the array of the coordinate pair, if it exists
     */
    public int locate(int x, int y, int z) {
        for (int i = 0 ; i < array.size(); i++)
            if (x == array.get(i).getX() && y == array.get(i).getY() && z == array.get(i).getZ() )
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
    public CoordTriplet getCoords(int i) {
        return array.get(i);
    }
    
   
    public void insert(CoordTriplet cp) {
        array.add(cp);
    } 
    public void insert(int x, int y, int z) {
        array.add(new CoordTriplet(x, y, z));
    } 
    public boolean unconditionallyInsert(int x, int y, int z) {
        if (!contains(x, y, z)) {
            insert(new CoordTriplet(x, y, z));
            return true;
        }
        int qq = 1 / 0;
        return false;
    }     
    public boolean unconditionallyInsert(CoordTriplet cp) {
        if (!contains(cp)) {
            insert(cp);
            return true;
        }
        int qq = 1 / 0;
        return false;
    }     
   
    /**
     * 
     * @param cp
     * @return if input cell can be added
     */
    protected boolean canAdd(CoordTriplet cp) {
        return !this.contains(cp);
    }    
   
   
    public String dumpCoords( ) {
        String ret = "";
        boolean first = true;
        for (CoordTriplet ct : array) {
            if (!first)
                ret += " ";
            else
                first = false;
            ret += ct.toString();
        }
        return ret;
    } 
  
}
