/*
 
 */
package polyominoidfriendships;

/**
 *
 * @author mason
 * 
 * The formula of a line segment
 */
public class Formula {
    private boolean onlyX, onlyY;
    private double a, k; // ax + y = k
    public Formula(LineSegment e) {
        if (e.isHoriz()) {
            onlyY = true;
            k = e.getS().getY();
            return;
        }
        if (e.isVert()) {
            onlyX = true;
            k = e.getS().getX();
            a = 1;
            return;
        }  
        a = (e.getE().getY() - e.getS().getY()) / (e.getS().getX() - e.getE().getX());
        k = a * e.getS().getX() + e.getS().getY();
    }
    public Formula(boolean onlyX, boolean onlyY, double a, double k) {
        this.onlyX = onlyX;
        this.onlyY = onlyY;
        this.a = a;
        this.k = k;
    }
    public String toString() {
        if (onlyX)
            return "x = " + k;
        if (onlyY)
            return "y = " + k;
        return a + " * x + y = " + k;
    }
    public Formula addConstant(double c) {
        return new Formula(onlyX, onlyY, a, k + c);
    }
    double deduceX(double inY) {
        if (onlyX)
            return k;
        return (k - inY) / a;
    }
    double deduceY(double inX) {
        if (onlyY)
            return k;
        return k - (a * inX);
    }
    public DCoord intersect(Formula other) {
        if (parallel(other))
            return null;
        if (this.onlyX && other.onlyX) { int k = 1 / 0; }
        if (this.onlyY && other.onlyY) { int k = 1 / 0; }
        if (this.onlyX) {
            return new DCoord(this.k, other.deduceY(this.k));
        }
        if (this.onlyY) {
            return new DCoord(other.deduceX(this.k), this.k);                
        }
        if (other.onlyX) {
            return new DCoord(other.k, this.deduceY(other.k));
        }
        if (other.onlyY) {
            return new DCoord(this.deduceX(other.k), other.k);                
        }
        double tx = (this.k - other.k) / (this.a - other.a);
        return new DCoord(tx, this.deduceY(tx));
    }
    double evaluate(double x, double y) {
        if (onlyX )
            return x - k;
        if (onlyY )
            return y - k;
        return a * x + y - k;
    }    
    double evaluate(DCoord d) {
        return evaluate(d.getX(), d.getY());
    }    
    public boolean parallel(Formula other) {
        if (this.onlyX && other.onlyX )
            return true;
        if (this.onlyY && other.onlyY )
            return true;
        if (this.onlyX || this.onlyY || other.onlyX || other.onlyY)
            return false;
        if (Comparison.equals(a, other.a))
            return true;
        return false;
    }
    public boolean passesThrough(DCoord point) {
        double v = evaluate(point.getX(), point.getY());
        if (Comparison.equals(v, 0))
            return true;
        else
            return false;
    }
}
