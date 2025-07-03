/*

 */
package polyominoidfriendships;

/**
 *
 * @author mason
 * 
 * generic class for polyforms having a list of coordinates
 */
public abstract class WCCommon extends Polyform {
    public abstract boolean strip();
    public abstract boolean treelike();
    public abstract int perimeterOrSurface();
    public abstract int minPerimeterOrSurface();
    public boolean minimalPerimeter() {
        return this.perimeterOrSurface() == this.minPerimeterOrSurface();
    }
    public WCCommon getPointed(int i) {
        return null;
    }   
    public boolean testAsym() {
        int qq = 1 / 0;
        return false;
    }
    protected Integer pointedCell;
    public void setPointedCell(int i) {
        pointedCell = new Integer(i);
    }
}
