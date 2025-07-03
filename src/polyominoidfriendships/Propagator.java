/*
 
 */
package polyominoidfriendships;

import java.util.*;

/**
 *
 * @author John Mason
 * 
 * class for counting holes, ...
 * 
 * Constructor
 * Build a rectangular array to contain the polyomino
 * colour the polyomino one colour, and the background another
 * 
 * outside: change the colour (of the border squares) of a border square  and propagate to touched squares
 * Return the number of newly coloured squares. If this is less than the size of the polyomino, there 
 * are holes
 * 
 * find: find a square of a colour
 * 
 * set: set a square to a colour and propagate
 */
public class Propagator {
    int flags[][];
    int area;
    PolyformWC poly;
    int maxX, maxY, polySize;
    int outsideColour, polyColour;
    
    public Propagator(PolyformWC polyParam, int polyColour, int restColour) {
        poly = polyParam;
//        System.err.println(poly.getRep().toString());
        this.polyColour = polyColour;
        polySize = poly.size();
        maxX = poly.getWidth();
        maxY = poly.getHeight();
        flags = new int[maxX + 1][maxY + 1];
        
        for (int x = 1 ; x <= maxX; x++)
            for (int y = 1 ; y <= maxY ; y++) 
                if (poly.ifUseful(x, y))
                    area++;
        
        for (int x = 1 ; x <= maxX; x++)
            for (int y = 1 ; y <= maxY ; y++)     
                flags[x][y] = restColour;
                
        for (int i = 0 ; i < poly.size(); i++) 
            flags[poly.getCoords(i).getX()][poly.getCoords(i).getY()] = polyColour;
        //dump();
    }
    public int getArea() {
        return area;
    }
    public int outside(int from, int to) {
        int ret = this.poly.outside(flags, from, to);

        ret+= propagate(from, to, false);
        this.outsideColour = to;
        //dump();
        return ret;
    }
    public CoordPair find(int colour) {         
        for (int x = 1 ; x <= maxX; x++)
            for (int y = 1 ; y <= maxY ; y++)  {
                if (flags[x][y] == colour) {
                    return new CoordPair(x, y);
                }
            } 
        System.err.println(poly.getRep().toString());
        int i = 1 / 0;
        return null;       
    }
    public CoordPair findIf(int colour) {         
        for (int x = 1 ; x <= maxX; x++)
            for (int y = 1 ; y <= maxY ; y++)  {
                if (flags[x][y] == colour) {
                    return new CoordPair(x, y);
                }
            } 
        
        return null;       
    }
    public int set(CoordPair cp, int from, int to) {
        
        return set(cp, from, to, false);
    }
    public int set(CoordPair cp, int from, int to, boolean pseudo) {
        int ret = 1;
        flags[cp.getX()][cp.getY()] = to;
        ret += propagate(from, to, pseudo);
        return ret;
    }
    public int getColour(int x, int y) {
        return flags[x][y];
    }
    public int getOutsideColour(int x, int y) {
        if (x < 1 || x > maxX || y < 1 || y > maxY)
            return this.outsideColour;
        return flags[x][y];
    }
    int propagate(int from, int to, boolean pseudo) {
        int ret = 0;
        boolean found = false;
        while (true) {
            found = false;
            for (int x = 1 ; x <= maxX; x++)
                for (int y = 1 ; y <= maxY ; y++)  {
                    if (flags[x][y] == to ) {
                        int partial = propagate(x, y, from, to, pseudo);
                        if (partial > 0) {
                            ret += partial;
                            found = true;
                        }
                    }
                }  
            if (!found)
                break;
        }
        return ret;
    }    
    int propagate(int x, int y, int from, int to, boolean pseudo) {
        /*
        return 
                propagate2(x + 1, y, from, to) +
                propagate2(x, y + 1, from, to) +
                propagate2(x - 1, y, from, to) +
                propagate2(x, y - 1, from, to) ;
*/
        ArrayList<CoordPair> a = this.poly.getClose(new CoordPair(x, y), true, pseudo);
        int ret = 0;
        for (int i = 0 ; i < a.size() ; i++) {
            ret += propagate2(a.get(i).getX(), a.get(i).getY(), from, to);
        }
        return ret;
    }
    int propagate2(int x, int y, int from, int to) {
        //if (x < 1 || x > maxX || y < 1 || y > maxY)
        //    return 0;
        if (flags[x][y] == from) {
            flags[x][y] = to;
            return 1;
        }
        return 0;
    }

    int leftmostTop() {
        
        for (int x = 1 ; x <= maxY ; x++)
            if (poly.contains(x, maxY))
                return x;
        int qq = 1 / 0;
        return qq;
    }     
    // only for polyominoes
    public Polyomino PolyominoOfColour(int colour) {
        Polyomino p = new Polyomino();
        for (int x = 1 ; x <= maxX ; x++)
            for (int y = 1 ; y <= maxY ; y++)
                if (flags[x][y] == colour)
                    p.insert(new CoordPair(x, y));
        p.seal();
        return p;
    }
    public ArrayList<CoordPair> ListOfColour(int colour) {
        ArrayList<CoordPair> ret = new ArrayList<>();
        for (int x = 1 ; x < maxX ; x++)
            for (int y = 1 ; y < maxY ; y++)
                if (flags[x][y] == colour)
                    ret.add(new CoordPair(x, y));
        
        return ret;
    }
    // used in calcualting inner loops
    public int removeOnesAndTwos(int colour, int back, int mustBeMultipleOf) {
        boolean repeat = true;
        int ret = 0;
        while (repeat) {
            repeat = false;
            for (int x = 1 ; x < maxX; x++)
                for (int y = 1 ; y < maxY; y++) 
                    if (flags[x][y] != colour) {
                        int n = countNear(x, y, colour);
                        if (n == 3) {
                            flags[x][y] = colour;
                            repeat = true;
                            ret++;
                        }
                    }
            for (int x = 2 ; x < maxX - 1; x++)
                for (int y = 2 ; y < maxY; y++) 
                    if (flags[x - 1][y] == colour && flags[x][y] != colour && flags[x + 1][y] != colour && flags[x + 2][y] == colour) {
                        for (int dy = -1; dy <= 1; dy += 2) {
                            if (flags[x][y + dy] == colour && flags[x + 1][y + dy] == colour) {
                                flags[x][y] = colour;
                                flags[x + 1][y] = colour;
                                repeat = true;
                                ret += 2;
                            }
                        }
                    } 
            for (int y = 2 ; y < maxY - 1; y++)
                for (int x = 2 ; x < maxX; x++) 
                    if (flags[x][y - 1] == colour && flags[x][y] != colour && flags[x][y + 1] != colour && flags[x][y + 2] == colour) {
                        for (int dx = -1; dx <= 1; dx += 2) {
                            if (flags[x + dx][y] == colour && flags[x + dx][y + 1] == colour) {
                                flags[x][y] = colour;
                                flags[x][y + 1] = colour;
                                repeat = true;
                                ret += 2;
                            }
                        }
                    }             
        }
        if (ret % mustBeMultipleOf != 0) {
            int qq = 1 / 0;
        }
        return ret / mustBeMultipleOf;
       
    }
    int countNear(int x, int y, int colour) {
        int ret = 0;
        ret += countNear2(x + 1, y, colour);
        ret += countNear2(x - 1, y, colour);
        ret += countNear2(x, y + 1, colour);
        ret += countNear2(x, y - 1, colour);
        return ret;
    }
    int countNear2(int x, int y, int colour) {
        if (x < 1 || y < 1)
            return 0;
        if (flags[x][y] == colour)
            return 1;
        return 0;
    }
    void dump() {
        for (int y = maxY; y >= 1 ; y--) {
            String line="";
            for (int x = 1 ; x <= maxX ; x++) {
                int v = flags[x][y];
                if (v == -1) 
                    line += "x";
                else if (v == -2) 
                    line += ".";
                else
                    line += v;
            }
            System.out.println(line);
        }
        System.out.println("\r\n");
    }
}
