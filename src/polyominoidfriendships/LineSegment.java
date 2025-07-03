/*
 
 */
package polyominoidfriendships;

/**
 *
 * @author mason
 * 
 * A line segment in 2D
 */
public class LineSegment {
    protected DCoord sc, ec;
   
    private  double justABit = 0.0000001;
    public LineSegment(DCoord insc, DCoord inec) {
        sc = insc;
        ec = inec;
                    
    }
    protected LineSegment() {
        
    }
    public LineSegment invert() {
        return new LineSegment(ec, sc);
    }
    public DCoord getS() { return sc; }
    public DCoord getE() { return ec; }
   
    public String toString() {
        return "(" + sc.toString() + ")-(" + ec.toString() + ")";
    }
    protected boolean edgeContainsPointOnLine(DCoord c) {
        if (strictlyWithin(sc.getX(), ec.getX(), c.getX()) && strictlyWithin(sc.getY(), ec.getY(), c.getY())) 
            return true;
        else
            return false;
    }
    boolean strictlyWithin(double r1, double r2, double v) {
        if (r1 < r2) {
            return v > r1 + justABit && v < r2 - justABit;
        } else {
            return v > r2 + justABit && v < r1 - justABit;
        }
    }
    public boolean intersects(LineSegment other) {
        DCoord c = new Formula(this).intersect(new Formula(other));
        if (c == null)
            return false;
        return this.edgeContainsPointOnLine(c) && other.edgeContainsPointOnLine(c);
    }
    public boolean sameSide(DCoord c1, DCoord c2) {
        Formula f = new Formula(this);
        double v1 = f.evaluate(c1.getX(), c1.getY());
        double v2 = f.evaluate(c2.getX(), c2.getY());
        return (v1 * v2 > 0);
    }    
    public boolean sameEndpoints(LineSegment other) {
        if (sc == other.sc && ec == other.ec)
            return true;
        if (sc == other.ec && sc == other.ec)
            return true;
        return false;
    }
    public boolean coincidentEndpoints(LineSegment other) {
        if (sc.coincidesWith(other.sc) && ec.coincidesWith(other.ec))
            return true;
        if (sc.coincidesWith(other.ec) && ec.coincidesWith(other.sc))
            return true;
        return false;
    }
    
    public boolean hasEndpoint(DCoord c) {
        return (sc == c) || (ec == c);
    }
    public boolean containsApprox(DCoord c) {
        if (sc.coincidesWith(c) || ec.coincidesWith(c))
            return true;
        return false;
    }
    public boolean isHoriz() {
        return Comparison.equals(sc.getY(), ec.getY());
    }
    public boolean isVert() {
        return Comparison.equals(sc.getX(), ec.getX());
    }
   
    public boolean totallyContains(DCoord c) {
        Formula f = new Formula(this);
        DCoord nc = new DCoord(c.getX(), f.deduceY(c.getX()));
        return c.coincidesWith(nc);
    }
    public double length() {       
        return distance(sc, ec);
    }
    public static double distance(DCoord c1, DCoord c2) {
        double xs = Math.pow(c1.getX() - c2.getX(), 2);
        double ys = Math.pow(c1.getY() - c2.getY(), 2);
        double sum = xs + ys;
        double ret = Math.sqrt(sum);
        return ret;        
    }
    public DCoord halfway() {
        double x = (sc.getX() + ec.getX()) / 2;
        double y = (sc.getY() + ec.getY()) / 2;
        return new DCoord(x, y);
        
    }
    public LineSegment bisector() {
        DCoord h = halfway();
        DCoord scn = new DCoord(h.getX() - (ec.getY() - h.getY()), h.getY() + (ec.getX() - h.getX()));
        DCoord ecn = new DCoord(h.getX() + (ec.getY() - h.getY()), h.getY() - (ec.getX() - h.getX()));
        return new LineSegment(scn, ecn);
    }
    /*
    return that point that is a distance of d before the endpoint
    */
    public DCoord lenghtenEndBy(double d) {
        double portion = d / length();
        DCoord ret = new DCoord(ec.getX() + (ec.getX() - sc.getX()) * portion, ec.getY() + (ec.getY() - sc.getY()) * portion);
        return ret;
    }
    public DCoord lenghtenStartBy(double d) {
        double portion = d / length();
        DCoord ret = new DCoord(sc.getX() + (sc.getX() - ec.getX()) * portion, sc.getY() + (sc.getY() - ec.getY()) * portion);
        return ret;
    }
    
}
