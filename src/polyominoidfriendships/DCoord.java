/*
 
 */
package polyominoidfriendships;

import java.text.DecimalFormat;

/**
 *
 * @author mason
 * 
 * a point on the 2D plane, with double precision
 */
public class DCoord {
    private double x, y;
    //private double nextToNothing = 0.00000001;
    public DCoord(double inX, double inY) {
        x = inX;
        y = inY;
    }
    public String toString() {
        return toString(x) + " , " + toString(y);
    }
    String toString(double v ) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(v)) + "";            
    }  
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean coincidesWith(DCoord other) {
        //if ((Math.abs(x - other.x) < nextToNothing) && (Math.abs(y - other.y) < nextToNothing))
        if (Comparison.equals(x, other.x) && Comparison.equals(y, other.y))
            return true;
        return false;
    }   
    public DCoord addDelta(double dx, double dy) {
        return new DCoord(x + dx, y + dy);
    }
}
