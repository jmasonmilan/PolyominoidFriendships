/*

 */
package polyominoidfriendships;

/**
 *
 * @author jmason
 * Pair of x,y coordinates for 2D polyforms
 */
public class CoordPair extends Coord {
    
    private int x, y;
    private int colour;
    private String letter = "O";
    
    //private CoordPair previous, next; // use in rings
    public CoordPair(int inX, int inY) {
        x = inX; 
        y = inY;
    }
    public CoordPair(int inX, int inY, int inColour) {
        x = inX; 
        y = inY;
        colour = inColour;
    }
    public CoordPair(CoordPair cp, int inColour) {
        x = cp.getX(); 
        y = cp.getY();
        colour = inColour;
    }
    public CoordPair(int inX, int inY, CoordPair inCp) {
        x = inX; 
        y = inY;
        colour = inCp.colour;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getCoord(int c) {
        if (c == 0)
            return x;
        else 
            return y;
    }
    /*
    public int getBit() {
        
        return ((x + y - 1)*(x + y - 2)/2) + y - 1;
    }
    */
    public void setLetter(String letter) {
        this.letter = letter;
    }
    public String getLetter() {
        if (colour != 0)
            return "X";
        return letter;
    }
    public int parity() {
        return (x + y) % 2;
    }
    public int mod4() {
        return (40000000 + x + y) % 4;
    }
    public boolean equals(Object o) {
        CoordPair other = (CoordPair)o;
        return (x == other.getX()) && (y == other.getY()) ;
    }
    public CoordPair nextAlong(int side) {
        int nx = this.x;
        int ny = this.y;
        if (side == 1) {
            nx++;
        } else if (side == 2) {
            ny++;
        } else if (side == 3) {
            nx++;
        } else { // side == 4
            ny++;
        }
        return new CoordPair(nx, ny);
    }
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    public void incrementX(int dx) {
        x += dx;
    }
    public void incrementY(int dy) {
        y += dy;
    }
    public void setColour(int value) {
        this.colour = value;
    }
    public int getColour() {
        return this.colour;
    }
    public CoordPair add(CoordPair dc) {
        return new CoordPair(x + dc.x , y + dc.y);
    }
    public CoordPair add(int dx, int dy) {
        return new CoordPair(x + dx , y + dy);
    }
   
    public boolean isPolyedgeNode() {
        return (x + y) % 2 == 0;
    }
    public boolean isPolyedgeEdge() {
        return !isPolyedgeNode();
    }
    public boolean isPolyedgeVertical() {
        return isPolyedgeEdge() && (x % 2 != 0);
    }
    public boolean isPolyedgeHorizontal() {
        return isPolyedgeEdge() && (x % 2 == 0);
    }
    public CoordPair newValues(int x, int y) {
        CoordPair nct = new CoordPair(x, y, colour);
        if (this.flaggedForDisplay)
            nct.setFlag();
        return nct;
    }    
    // both this and ocp are assumed polyedge edges
    public boolean aligned(CoordPair ocp) {
        return (this.isPolyedgeHorizontal() == ocp.isPolyedgeHorizontal());
    }
   
   
    
}
