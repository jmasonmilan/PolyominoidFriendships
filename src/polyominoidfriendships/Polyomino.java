/*
 
 */
package polyominoidfriendships;
import java.awt.Color;
import java.util.*;
import java.math.BigInteger;

/**
 *
 * @author jmason
 * 
 * polyomino, expressed as an array of x,y coordinates
 */
public class Polyomino extends BasePolyomino {
    
    public Polyomino(BigInteger inValue) {
        rep = inValue;
        build(inValue);
        
        seal();  
    }    
    public Polyomino(String inValue) {
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
            x++;
        }
        
        seal();  
    }    
   
    public Polyomino() {
        array = new ArrayList<CoordPair>();
    }  
    
    public Polyomino maker(BigInteger rep) {
        return new Polyomino(rep);
    }
    public Polyomino maker(String srep) {
        return new Polyomino(new BigInteger(srep));
    }
    public Polyomino maker() {
        return new Polyomino();
    }
    
    public void seal() {
        setFirstSquare();   // seems repreated in nonVBSeal 
        nonCBSeal();
      
    }
    
    void build(BigInteger rep) {
        innerBuild(rep);
    }
    public int maxDiagonalCells() {
        int ret = 0;
        for (int dx = 1; dx < getWidth(); dx++)
            for (int dy = 1; dy  < getHeight(); dy++) {
                int v = maxDiagonalCells(dx, dy);
                if (v > ret)
                    ret = v;
                v = maxDiagonalCells(dx, -dy);
                if (v > ret)
                    ret = v;
            }
        return ret;
    }
    public int boundingBoxRegions() {
        int ret = 0;
        Propagator prop = new Propagator(this, 0, 1);
        int last = 1;
        while (true) {
            CoordPair cp = prop.findIf(1);
            if (cp == null)
                return last - 1;
            prop.set(cp  ,   1, last + 1);
            last++;
        }
    }
    public int debugMax() {
        return Math.max(debugMaxX(), debugMaxY());
    }
    int debugMaxX() {
        int ret = 0;
        for (int y = 1; y <= getHeight(); y++) {
            int v = debugMaxX(y);
            if (v > ret)
                ret = v;
        }
        return ret;
    }
    int debugMaxX(int y) {
        int ret = 0;
        for (int x = 1; x <= getWidth(); x++)
            if (contains(x, y))
                ret++;
        return ret;
    }
    int debugMaxY() {
        int ret = 0;
        for (int x = 1; x <= getWidth(); x++) {
            int v = debugMaxY(x);
            if (v > ret)
                ret = v;
        }
        return ret;
    }
    int debugMaxY(int x) {
        int ret = 0;
        for (int y = 1; y <= getHeight(); y++)
            if (contains(x, y))
                ret++;
        return ret;
    }
    public int maxCellsInLine() {
        return Math.max(maxDiagonalCells(), maxOrthogonalCells());
    }
    int maxDiagonalCells(int dx, int dy) {
        int ret = 0;
        for (CoordPair cp : array) {
            int v =  maxDiagonalCells(dx, dy, cp);
            if (v > ret)
                    ret = v;
        }
        return ret;
    }
    int maxDiagonalCells(int dx, int dy, CoordPair cp) {
        int ret = 1;
        for (int x = cp.getX() + dx, y = cp.getY() + dy; x <= getWidth() && y >= 1 && y <= getHeight(); x += dx, y += dy) {
            if (contains(x, y))
                ret++;
        }
        return ret;
    }
    public int maxOrthogonalCells() {
        int maxXcells = maxCells(0);
        int maxYcells = maxCells(1);
        return Math.max(maxXcells, maxYcells);
    }
    /*
    if c == 0, get maximum number of cells in a row, ...
    */
    int maxCells(int c) {
        int ret = 0;
        for (int z = 1; z <= this.getMax(1 - c); z++) {
            int v = getMax(c, z);
            if (v > ret)
                ret = v;
        }
        return ret;
    }
    public int getMaxDimension() {
        return Math.max(getWidth(), getHeight());
    }
    public int getMinDimension() {
        return Math.min(getWidth(), getHeight());
    }
    int getMax(int c) {
        if (c == 0)
            return getMaxX();
        else
            return getMaxY();
    }
    /*
    if c = 0, get number of cells in row(y) z, ...
    */
    int getMax(int c, int z) {
        int ret = 0;
        for (CoordPair cp : array) {
            if (cp.getCoord(1 - c) == z)
                ret++;           
        }
        return ret;
    }
    public boolean kissing() {
        return kissing(1) || kissing(-1);
    }
    boolean kissing(int dir) {
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            if (contains(x + 1, y + dir) && !contains(x + 1, y) && !contains(x, y + dir))
                return true;
        }
        return false;
    }
    public PolyformWC getPointed(int i) {
    
        Polyomino ret = new Polyomino();
        for (int j = 0; j < this.size(); j++) {
            CoordPair cp = this.getCoords(j);
            int x = cp.getX(), y = cp.getY();
            ret.insert(3 * x - 2, 3 * y - 2);
            ret.insert(3 * x - 1, 3 * y - 2);
            ret.insert(3 * x - 0, 3 * y - 2);
            ret.insert(3 * x - 2, 3 * y - 0);
            ret.insert(3 * x - 1, 3 * y - 0);
            ret.insert(3 * x - 0, 3 * y - 0);
            ret.insert(3 * x - 2, 3 * y - 1);
            ret.insert(3 * x - 0, 3 * y - 1);
            
        }
        CoordPair icp = this.getCoords(i);
        int x = icp.getX(), y = icp.getY();
        ret.insert(3 * x - 1, 3 * y - 1);
        ret.seal();
        return ret;
           
    }
    int[] deltaXpp = {0, 0, 1, -1, 1, 1, -1, -1};
    int[] deltaYpp = {1, -1, 0, 0, 1, -1, 1, -1};
    public boolean isPerfectHalo() {
        //System.err.println(drawing());
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            int adj = 0;
            for (int j = 0; j < 8; j++) {
                int nx = x + deltaXpp[j], ny = y + deltaYpp[j];
                if (contains(nx, ny) ) {
                    adj++;
    
                }
            }
            if (adj != 2)
                return false;
            
        }
        return true;
    } 
    public boolean hasPerfectHalo() {
        Polyomino halo = getHalo();
        return halo.isPerfectHalo();
    }
    int deltaX[] =  {0, 0, 1, -1};
    int deltaY[] =  {1, -1, 0, 0};    
    public Polyomino getHalo() {
        Polyomino halo = new Polyomino();
        for (int i = 0; i < size(); i++) {
            CoordPair cp = getCoords(i);
            int x = cp.getX(), y = cp.getY();
            for (int j = 0; j < 4; j++) {
                int nx = x + deltaX[j], ny = y + deltaY[j];
                if (contains(nx, ny))
                    continue;
                nx++;
                ny++;
                if (halo.contains(nx, ny))
                    continue;
                halo.insert(nx, ny);
            }
            
        }
        halo.seal();    
        return halo;
    }
    public boolean lShaped() {
        for (int i = 0; i < size() - 1; i++)
            for (int j = i + 1; j < size() ; j++)
                if (!lPath(array.get(i), array.get(j)))
                    return false;
        return true;
    }
    boolean lPath(CoordPair from, CoordPair to) {
        int fX = from.getX(), fY = from.getY(), tX = to.getX(), tY = to.getY();
        int dx = 1, dy = 1;
        if (tX < fX)
            dx = -1;
        if (tY < fY)
            dy = -1;
        if (allX(fX, dx, tX, fY) && allY(fY, dy, tY, tX))
            return true;
        if (allX(fX, dx, tX, tY) && allY(fY, dy, tY, fX))
            return true;
        return false;
    }
    boolean allX(int fX, int dx, int tX, int y) {
        for (int x = fX; ; x += dx) {
            if (!contains(x, y))
                return false;
            if (x == tX)
                return true;
        }
    }
    boolean allY(int fY, int dy, int tY, int x) {
        for (int y = fY; ; y += dy) {
            if (!contains(x, y))
                return false;
            if (y == tY)
                return true;
        }
    }
    
    
    public Polyomino rotate90() {
        Polyomino ret = maker();
        int h = this.getHeight();
        for (int i = 0 ; i < array.size(); i++) {
            ret.insert(array.get(i).newValues(h + 1 - array.get(i).getY(), array.get(i).getX()));
        }
        ret.seal();
        return ret;
    }
    public Polyomino reflectH() {
        Polyomino ret = maker();
        int h = this.getHeight();
        for (int i = 0 ; i < array.size(); i++) {
            ret.insert(array.get(i).newValues(array.get(i).getX(), h + 1 - array.get(i).getY()));
        }
        ret.seal();
        return ret;
    }
    public Polyomino reflectV() {
        Polyomino ret = maker();
        int w = this.getWidth();
        for (int i = 0 ; i < array.size(); i++) {
            ret.insert(array.get(i).newValues(w + 1 - array.get(i).getX(), array.get(i).getY()));
        }
        ret.seal();
        return ret;
    }
    public Polyomino reflectD() {
        Polyomino ret = maker();
        
        for (int i = 0 ; i < array.size(); i++) {
            ret.insert(array.get(i).newValues(array.get(i).getY(), array.get(i).getX()));
        }
        ret.seal();
        return ret;
    }
   

   
    public boolean pile() {
        for (int i = 0 ; i < array.size() ; i++) {
            if (array.get(i).getY() == 1)
                continue;
            if (this.contains(array.get(i).getX(), array.get(i).getY() - 1))
                continue;
            return false;
        }
        return true;
    }
    public boolean board() {
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        for (int y = 1 ; y <= maxY ; y++) {
            int flag = 0;
            for (int x = 1 ; x <= maxX ; x++) {
               if (this.contains(x, y)) {
                   if (flag == 0)
                       flag = 1;
                   if (flag == 2)
                       return false;
               } else {
                   if (flag == 1)
                       flag = 2;
               }
            }
        }
        return true;
    
    }

   
    
    public void correct() {
        Polyomino ret = this;
        for (int i = 0 ; i < array.size() ; i++) {
            CoordPair cp = array.get(i);
            if (cp.getX() < 1) {
                System.err.println("x < 1");
                System.exit(1);
            }
            if (cp.getY() < 1) {
                System.err.println("x < 1");
                System.exit(1);
            }
        }
        
    }
    public ArrayList<Polyomino> getFixedList() {
        ArrayList<Polyomino> ret = new ArrayList<>();
        PolySet ps = this.getFixed();
        for (int i = 0; i < ps.size(); i++)
            ret.add(new Polyomino(new BigInteger(ps.gets(i))));
        return ret;
    }
    public boolean hasFactor(Polyomino divisor) {
        ArrayList<Polyomino> dList = divisor.getFixedList();
        return hasFixedFactor(dList);
       
    }
    public boolean hasFixedFactor(ArrayList<Polyomino> dList) {
        mainloop: for (Polyomino divisor : dList) {
            CoordPair thisTopLeft = this.findTopLeft();
            CoordPair divisorTopLeft = divisor.findTopLeft();
            int tx = thisTopLeft.getX(), ty = thisTopLeft.getY();
            int dx = divisorTopLeft.getX(), dy = divisorTopLeft.getY();
            for (CoordPair cp : divisor.array) {
                int x = cp.getX(), y = cp.getY();
                if (!contains(tx + x - dx, ty + y - dy))
                    continue mainloop;
            }
            if (this.size() == divisor.size())
                return true;
            Polyomino remainder = this.subtract(divisor, tx, ty, dx, dy);
            if (remainder.hasFixedFactor(dList))
                return true;
        }
        return false;
    }
    Polyomino subtract(Polyomino divisor, int tx, int ty, int dx, int dy) {
        Polyomino ret = new Polyomino();
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            if (!divisor.contains(x - tx + dx, y - ty + dy))
                ret.insert(cp);
        }
        ret = (Polyomino)(ret.align());
        return ret;
    }
    public CoordPair findTopLeft() {
        int y = getHeight();
        int x = 99999999;
        for (CoordPair cp : array) {
            if (cp.getY() == y)
                if (cp.getX() < x)
                    x = cp.getX();
            
        }
        return new CoordPair(x, y);
    }
    int twoPower(int n) {
        int ret = 1;
        while (n > 0) {
            ret *=2;
            n--;
        }
        return ret;
    }

 


    
    // return high value of predominant
    /*
Size of polyomino	predominant squares	minor squares	bias
4n                      3n                      n               2n
4n+1                    3n+1                    n               2n+1
4n+2                    3n+1                    n+1             2n
4n+3                    3n+2                    n+1             2n+1
4n+4                    3n+3                    n+1             2n+2
    
    */

    public String getUnique() {
        
        BigInteger rep1 = this.getRep();
        // for debugging
        
        Polyomino x1 = maker(rep1);
        Polyomino y1 = x1.rotate90();
        BigInteger z2 = y1.getRep();
        Polyomino x2 = maker(z2);
        Polyomino y2 = x2.rotate90();
        BigInteger z3 = y2.getRep();
        
        
        BigInteger rep2 = maker(rep1).rotate90().getRep();
        BigInteger rep3 = maker(rep2).rotate90().getRep();
        BigInteger rep4 = maker(rep3).rotate90().getRep();
        BigInteger rep5 = maker(rep4).reflectH().getRep();
        BigInteger rep6 = maker(rep5).rotate90().getRep();
        BigInteger rep7 = maker(rep6).rotate90().getRep();
        BigInteger rep8 = maker(rep7).rotate90().getRep();
        
        if (rep1.compareTo(rep2) > 0)
            rep1 = rep2;
        if (rep1.compareTo(rep3) > 0)
            rep1 = rep3;
        if (rep1.compareTo(rep4) > 0)
            rep1 = rep4;
        if (rep1.compareTo(rep5) > 0)
            rep1 = rep5;
        if (rep1.compareTo(rep6) > 0)
            rep1 = rep6;
        if (rep1.compareTo(rep7) > 0)
            rep1 = rep7;
        if (rep1.compareTo(rep8) > 0)
            rep1 = rep8;
        
        return rep1.toString();
    }   
    public String getVUnique() {
        
        BigInteger rep1 = this.getRep();
        BigInteger rep2 = new Polyomino(rep1).reflectV().getRep();
        
        
        if (rep1.compareTo(rep2) > 0)
            rep1 = rep2;
        
        
        return rep1.toString();
    } 
    public boolean tExtreme() {
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            if (y == 1 && x == 1)
                return false;
            if (y == 1 && x == getWidth())
                return  false;
        }
        for (int x = 1; x <= getWidth(); x++)
            if (!contains(x, getHeight()))
                return false;
        return true;
    }
    public String drawing() {
        int h = this.getHeight();
        int w = this.getWidth();
        String ret = "\r\n";
        for (int y = h ; y >= 1 ; y--) {
            String buf = "";
            for (int x = 1; x <= w ; x++) {
                CoordPair cp = this.find(x, y);
                if (this.contains(x, y))
                    buf += cp.getLetter();
                else 
                    buf += "_";
            }
            ret += buf + "\r\n";
        }
               
        return ret;
               
          
    } 
    public String drawing(int showX, int showY) {
        int h = this.getHeight();
        int w = this.getWidth();
        String ret = "\r\n";
        for (int y = h ; y >= 1 ; y--) {
            String buf = "";
            for (int x = 1; x <= w ; x++)
                if (this.contains(x, y)) {
                    if (x == showX && y == showY)
                        buf = "X";
                    else
                        buf += "O";
                }
                else 
                    buf += "_";
            ret += buf + "\r\n";
        }
               
        return ret;
               
          
    } 
    public String drawing(int minX, int minY, int maxX, int maxY) {
       
        String ret = "\r\n";
        for (int y = maxY ; y >= minY ; y--) {
            String buf = "";
            for (int x = minX; x <= maxX ; x++)
                if (this.contains(x, y))
                    buf += "O";
                else 
                    buf += "_";
            ret += buf + "\r\n";
        }
               
        return ret;
               
          
    } 
    
  
    public PolySet getFixed() {
        
        PolySet ps = new PolySet();
        Polyomino poly;
        ps.adds(this.getRep().toString());
        poly = this.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.reflectH(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());      
        return ps;
    } 
    public PolySet getRotations() {
        
        PolySet ps = new PolySet();
        Polyomino poly;
        ps.adds(this.getRep().toString());
        poly = this.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        poly = poly.rotate90(); ps.adds(poly.getRep().toString());
        
        return ps;
    } 
    
 
    public PolySet getOnesided() {
        
        PolySet ps = new PolySet();
        ps.adds(this.getRep().toString());
        Polyomino polyH = this.reflectH(); 
        Polyomino polyV = this.reflectV(); 
        Polyomino polyD1 = this.reflectD(); 
        Polyomino polyD2 = this.rotate90().rotate90().reflectD(); 
        if (this.equals(polyH))
            return ps;
        if (this.equals(polyV))
            return ps;
        if (this.equals(polyD1))
            return ps;
        if (this.equals(polyD2))
            return ps;
        ps.adds(polyH.getRep().toString());
        return ps;
    }    
    public Polyomino spread() {
        Polyomino np = new Polyomino();
        for (CoordPair cp : array) {
            int x = cp.getX() + 1, y = cp.getY() + 1;
            np.conditionalInsert(x, y);
            np.conditionalInsert(x + 1, y);
            np.conditionalInsert(x - 1, y);
            np.conditionalInsert(x, y + 1);
            np.conditionalInsert(x, y - 1);
            
        }
        np.seal();
        return np;
    }
    public boolean conditionalInsert(int x, int y) {
        if (contains(x, y))
            return false;
        this.strictInsert(x, y);
        return true;
    }
    public Polyomino makeBorderPoly() {
        return makeBorderPoly(1);
    }
    public Polyomino makeBorderPoly(int delta) {
        BigInteger rep = BigInteger.ZERO;
        for (CoordPair cp : array) {
            rep = makeBorder(rep, cp, delta);
        }
        return new Polyomino(rep);
    }
    BigInteger makeBorder(BigInteger rep, CoordPair cp, int delta) {
        int x = cp.getX(), y = cp.getY();
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                int nx = x + dx, ny = y + dy;
                if (contains(nx, ny))
                    continue;
                rep = rep.setBit(getBit(nx + delta, ny + delta));
            }
        return rep;
    }
    public boolean loopBorder() {
        return this.makeBorderPoly().loop();
    }
    // only for 2M90 polyominoes
    public boolean dominoCentered() {
        int width = getWidth();
       
        int height = getHeight();
        
        int x1, y1, x2 , y2;
        if (width % 2 == 1) {
            if (height % 2 == 1)
                return false;
            x1 = (width + 1) / 2;
            x2 = x1;
            y1 = height / 2;
            y2 = y1 + 1;
        } else        if (height % 2 == 1) {
            if (width % 2 == 1)
                return false;
            y1 = (height + 1) / 2;
            y2 = y1;
            x1 = width / 2;
            x2 = x1 + 1;
        } else
            return false;
        return (contains(x1, y1) && contains(x2, y2));
        
    }    
  
    public Polyomino quadruple() {
        Polyomino np = new Polyomino();
        int w = getWidth(), h = getHeight();
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            np.insert(x + w, y + h);
            np.insert(x + w, h - y + 1);
            np.insert(w - x + 1, y + h);
            np.insert(w - x + 1, h - y + 1);
            
        }
        np.seal();
        return np;
    }
    public Polyomino duplicateBelow() {
        Polyomino np = new Polyomino();
        int  h = getHeight();
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            np.insert(x , y + h);
            np.insert(x , h - y + 1);
            
            
        }
        np.seal();
        return np;
    }
  
    
    
    Hashtable<String, String> equiv;
    String substitute(String in) {
        String out = equiv.get(in);
        if (out == null)
            return in;
        else
            return out;
    }       
   
    boolean beyondCol(int x, int otherX, int startY, int deltaY) {
        startY = endOfCol(x, otherX, startY, deltaY) + 2 * deltaY;
        for (int y = startY; y >= 1 && y <= getHeight(); y += deltaY) {
            if (contains(x, y))
                return true;
        }
        return false;
    }
    boolean isCol(int x, int otherX, int startY, int deltaY) {
        int y = endOfCol(x,  otherX,  startY,  deltaY);
        return y != 0;
    }
    int endOfCol(int x, int otherX, int startY, int deltaY) {
        for (int y = startY; y >= 1 && y <= getHeight(); y += deltaY) {
            if (contains(x, y)) {
                if (contains(otherX, y))
                    return 0;
            } else {
                return y - 1;
            }
        }
        int qq = 1 / 0;
        return getHeight();
    }
    public int countX(int x) {
        int ret = 0;
        for (int y = 1; y <= getHeight(); y++)
            if (contains(x, y))
                ret++;
        return ret;
    }
    public Polyomino extractRows(int startY, int endY) {
        Polyomino ret = new Polyomino();
        for (int y = startY; y <= endY; y++) {
            for (int x = 1; x <= getWidth(); x++) {
                if (contains(x, y)) {
                    ret.insert(x, y - startY + 1);
                }
            }
        }
        ret.seal();
        return ret;
    }
    String calcCoeff(int partial, int half) {
        if (partial > half) {
            int qq = 1 / 0;
        }
        if (partial == half)
            return "m";
        return "m - " + (half - partial);
    }
    int numberOnY3(int y) {
        int sum = 0;
        for (int x = 1; x <= 3; x++)
            if (contains(x, y))
                sum++;
        return sum;
    }
    
    private void add(ArrayList<Vertex> list, int x, int y) {
        Vertex v = new Vertex(x, y);
        /*
        if (!list.contains(v))
            list.add(v);*/
        for (Vertex vv : list) 
            if (vv.equals(v))
                return;
        list.add(v);
    }
    public String top() {
        return footprint4(maxY);
    }
    public String bottom() {
        return footprint4(1);
    }
    public String footprint4( int row) {
        String ret = "";
        String letters = " abcd";
        for (int i = 1 ; i <= 4; i++)
            if (this.contains(i, row))
                ret += letters.substring(i, i + 1);
        return ret;
    }    
    public String rowToLetters(int row) {
        String ret = "";
        String letters = " abcd";
        for (int i = 1 ; i <= getWidth(); i++)
            if (this.contains(i, row))
                ret += letters.substring(i, i + 1);
        return ret;
    }    
    private class Vertex { // could be CoordPair ?
        int x, y;
        Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public boolean equals(Vertex other) {
            return x == other.x && y == other.y;
        }
    }
    public int empty() {
        int ret = getWidth() * getHeight() - size();
        return ret;
    }
    public int topHeight() {
        int ret = 0;
        for (int y = getHeight(); y >= 1; y--)
            if (completeRow(y))
                ret++;
            else
                break;
        return ret;
    }
    public boolean completeRow(int y) {
        for (int x = 1 ; x <= getWidth() ; x++)
            if (!contains(x, y))
                return false;
        return true;
    }
    /*
    public Polyomino extract(int bottom, int top) {
        Polyomino ret = new Polyomino();
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            if (y >= bottom && y <= top)
                ret.insert(x, y - bottom + 1);
        }
        ret.seal();
        return ret;
    } */
   
   
 
    /*
    Assumes convexity
    */
    public boolean minimallyRealLifeConcave() {
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            if (contains(x, y - 1) && !contains(x + 1, y) && contains(x + 1, y + 1) && contains(x + 2, y + 1))
                return false;
            if (contains(x, y + 1) && !contains(x + 1, y) && contains(x + 1, y - 1) && contains(x + 2, y - 1))
                return false;
            if (contains(x, y - 1) && !contains(x - 1, y) && contains(x - 1, y + 1) && contains(x - 2, y + 1))
                return false;
            if (contains(x, y + 1) && !contains(x - 1, y) && contains(x - 1, y - 1) && contains(x - 2, y - 1))
                return false;
            
        }
        return true;
    }
    public int internalPoints() {
        int ret = 0;
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            
            if (y < 2)
                continue;
            if (contains(x + 1, y)  && contains(x , y - 1) && contains(x + 1, y - 1) )
                ret++;
        }
        return ret;
    }   
    public  int internalEdges() {
        int ret = 0;
        for (CoordPair cp : array) {
            int x = cp.getX(), y = cp.getY();
            
                ret += internalEdges(x - 1, y);
                ret += internalEdges(x + 1, y);
                ret += internalEdges(x , y + 1);                
                ret += internalEdges(x , y - 1);                
                
        }
        return ret / 2;
    }    
    public int findTransformation(Polyomino other) {
        ArrayList<Polyomino> list = listAllTransformations();
        for (int i = 0; i < list.size() ; i++)
            if (other.equals(list.get(i)))
                return i;
        System.err.println("cannot transform " + other.drawing() + " to " + this.drawing());
        return 1 / 0;
            
    }
    public Polyomino getTransformation(int n) {
        ArrayList<Polyomino> list = listAllTransformations();
        return list.get(n);
    }
    ArrayList<Polyomino> listAllTransformations() {
        ArrayList<Polyomino> ret = new ArrayList<>();
        Polyomino p = this;
        ret.addAll(p.listAllRotations());
        p = p.reflectH();
        ret.addAll(p.listAllRotations());
        for (Polyomino q : ret) {
            if (q.size() != size()) {
                int qq = 1 / 0;
            }
        }
        return ret;
    }
    ArrayList<Polyomino> listAllRotations() {
        ArrayList<Polyomino> ret = new ArrayList<>();
        Polyomino p = this;
        ret.add(p);
        p = p.rotate90();
        ret.add(p);
        p = p.rotate90();
        ret.add(p);
        p = p.rotate90();
        ret.add(p);
       
        return ret;
    }    
    public boolean isTCP() {
        
        outer: for (int x = getMinX() ; x <= getMaxX(); x++) {
            int state = 0;
            /* state 1 : found at least one cell
               state 2 : after cell, found space
            */
            for (int y = getMinY(); y <= getMaxY() ; y++) {
                if (contains(x, y)) {
                    switch (state) {
                        case 0: state = 1; break;
                        case 2: continue outer;
                    }
                } else {
                    switch (state) {
                        case 1: state = 2; break;
                    }                    
                }
            }
            return false;
        }
        outer: for (int y = getMinY() ; y <= getMaxY(); y++) {
            int state = 0;
            /* state 1 : found at least one cell
               state 2 : after cell, found space
            */
            for (int x = getMinX(); x <= getMaxX() ; x++) {
                if (contains(x, y)) {
                    switch (state) {
                        case 0: state = 1; break;
                        case 2: continue outer;
                    }
                } else {
                    switch (state) {
                        case 1: state = 2; break;
                    }                    
                }
            }
            return false;
        }        
        return true;
    }       
}
