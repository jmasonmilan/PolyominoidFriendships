/*
 
 */
package polyominoidfriendships;
import java.util.ArrayList;

/**
 *
 * @author jmason
 * x,y,z coordinates for 3D polyforms
 */
public class CoordTriplet extends Coord {
    
    private int x, y, z;
    private int colour;
   
    public CoordTriplet(int inX, int inY, int inZ) {
        x = inX; 
        y = inY;
        z = inZ;
    }
    public CoordTriplet(int inX, int inY, int inZ, int inColour) {
        x = inX; 
        y = inY;
        z = inZ;
        colour = inColour;
    }
    public CoordTriplet(int inX, int inY, int inZ, CoordTriplet inCp) {
        x = inX; 
        y = inY;
        z = inZ;
        colour = inCp.colour;
    }
    public CoordTriplet(int c1, int v1, int c2, int v2, int c3, int v3) {
        setCoord(c1, v1);
        setCoord(c2, v2);
        setCoord(c3, v3);
        
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public int getCoord(int c) {
        if (c == 0) return x;
        if (c == 1) return y;
        return z;
    }
    private void setCoord(int c, int v) {
        if (c == 0)  
            x = v; 
        else        if (c == 1) 
            y = v; 
        else
            z = v;
    }
    public CoordTriplet swap2(int c) {
        int nx = x, ny = y, nz = z;
        if (c == 0) {
            ny = z;
            nz = y;
        } else if (c == 1) {
            nx = z;
            nz = x;
        } else {
            nx = y;
            ny = x;
        }
        CoordTriplet ncp = new CoordTriplet(nx, ny, nz);
        return ncp;
    }
    public CoordTriplet newValues(int x, int y, int z) {
        CoordTriplet nct = new CoordTriplet(x, y, z, colour);
        if (this.flaggedForDisplay)
            nct.setFlag();
        return nct;
    }
    public boolean equals(Object other) {
        CoordTriplet o = (CoordTriplet)other;
        return (x == o.getX()) && (y == o.getY()) && (z == o.getZ()) ;
    }
    
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
    public void incrementX(int dx) {
        x += dx;
    }
    public void incrementY(int dy) {
        y += dy;
    }
    public void incrementZ(int dz) {
        z += dz;
    }
    public void setColour(int value) {
        this.colour = value;
    }
    public int getColour() {
        return this.colour;
    }
    public int parity() {
        return (x + y + z) % 2;
    }
    // for polyominoids
    public boolean isPolyominoidHinge() {
        return isPolyominoidHingeParallelToX() || isPolyominoidHingeParallelToY()  || isPolyominoidHingeParallelToZ();
    }
    public boolean isPolyominoidHingeParallelToX() {
        return ((x % 2 == 0) && (y % 2 != 0) && (z % 2 != 0));
    }
    public boolean isPolyominoidHingeParallelToY() {
        return ((x % 2 != 0) && (y % 2 == 0) && (z % 2 != 0));
    }
    public boolean isPolyominoidHingeParallelToZ() {
        return ((x % 2 != 0) && (y % 2 != 0) && (z % 2 == 0));
    }
    public boolean isHoriz() {
        check();
        if ((z % 2 != 0) && (x % 2 == 0) && (y % 2 == 0))
            return true;
        else
            return false;
    }
    public boolean isVertParaYE0() {
        check();
        if ((x % 2 != 0) && (y % 2 == 0) && (z % 2 == 0))
            return true;
        else
            return false;
    }
    public boolean isVertParaXE0() {
        check();
        if ((y % 2 != 0) && (x % 2 == 0) && (z % 2 == 0))
            return true;
        else
            return false;
    }
    // for polyominoids
    public void check() {
        if ((z % 2 != 0) && (x % 2 == 0) && (y % 2 == 0))
            return;
        if ((x % 2 != 0) && (y % 2 == 0) && (z % 2 == 0))
            return;
        if ((y % 2 != 0) && (x % 2 == 0) && (z % 2 == 0))
            return;
        System.err.println("bad coord triplet " + this.toString());
        StackTraceElement[] a = Thread.currentThread().getStackTrace();
        for (StackTraceElement b : a)
            System.err.println(b.toString());
        int qq = 1 / 0;
    }
    public CoordPair flatten(int c) {
        if (c == 0)
            return new CoordPair(y, z);
        else if (c == 1)
            return new CoordPair(x, z);
        else
            return new CoordPair(x, y);
    }
        /*
 * x even, y even, z odd - flat, z=0
 * x odd, y even, z even - vertical, x = 0
 * x even, y odd, z even - vertical, y = 0
 
    */
    // For polyominoids
    public int parallelTo() {
        if (z % 2 != 0)
            return 2;
        else if (y % 2 != 0)
            return 1;
        else
            return 0;
                    
    }
    public CoordTriplet add(CoordTriplet delta) {
        return new CoordTriplet(x + delta.getX(), y + delta.getY(), z + delta.getZ());
    }
    public CoordTriplet add(int dx, int dy, int dz) {
        return new CoordTriplet(x + dx, y + dy, z + dz);
    }
    public CoordTriplet subtract(CoordTriplet other) {
        return new CoordTriplet(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }
    public int whichIsZero() {
        if (x == 0)
            return 0;
        if (y == 0)
            return 1;
        if (z == 0)
            return 2;
        return -1;
    }
    public CoordTriplet changeSingle(int coord, int val) {
        CoordTriplet nct = new CoordTriplet(x, y, z);
        nct.setCoord(coord, val);
        return nct;
    }
    public String utpFaceDirectionString() {
        if (this.utpHorizontal())
            return "z";
        else if (this.utpVerticalParaX0())
            return "x";
        else 
            return "y";
    }
    public int utpFaceDirection() {
        if (this.utpHorizontal())
            return 2;
        else if (this.utpVerticalParaX0())
            return 0;
        else 
            return 1;
    }
    public int getUTPEdge(int currDir, int currEdge, CoordTriplet prevFace, CoordTriplet nextFace) {
        if (prevFace == null) {
            System.err.println("getUTPEdge prevFace null");
        }
        if (nextFace == null) {
            System.err.println("getUTPEdge nextFace null");
        }
        for (int c = 0; c < 3; c++) {
            if (c == this.utpFaceDirection())
                continue;
            if (prevFace.getCoord(c) == getCoord(c) && getCoord(c) == nextFace.getCoord(c))
                return Polyform.oppEdge(currEdge);
        }
        for (int c = 0; c < 3; c++) {
            int ret;
            if (c == this.utpFaceDirection())
                continue;
            if (prevFace.getCoord(c) == getCoord(c))
                continue;
            if (nextFace.getCoord(c) < getCoord(c))
                ret = Polyform.nextEdge(currEdge);
            else
                ret = Polyform.prevEdge(currEdge);
            if (currDir < 0)
                ret = Polyform.oppEdge(currEdge);
            return ret;
        }
        int qq = 1 / 0;
        return -1;
    }
    public int getUTPDir(int currDir, CoordTriplet nextFace) {
        int ret = 1000;
        if (nextFace.utpFaceDirection() == utpFaceDirection())
            return currDir;
        for (int c = 0; c < 3; c++) {
            if (c == utpFaceDirection())
                continue;
            if (getCoord(c) == nextFace.getCoord(c))
                continue;
            String morv = this.getUTPMV(currDir, nextFace);
            if (getCoord(c) < nextFace.getCoord(c)) {
                ret = c; // assume morv is m
            } else {
                ret = -c;
            }
            if (morv.equals("v"))
                ret = -ret;
            if (currDir < 0)
                ret = -ret;
            return ret;
        }
        int qq = 1 / 0;
        return -1;        
    }
    /*
    methods for untearable polyominoids
    */
    /*
    object is a face parallel to X = 0
    */
    public boolean utpVerticalParaX0() { 
        return mod6String().equals("144");
    }
    public boolean utpVerticalParaY0() {
        return mod6String().equals("414");
    }
    public boolean utpHorizontal() {
        return mod6String().equals("441");
    }
    public String utpType() {
        if (utpFace()) 
            return "face";
        if (utpConnector()) 
                return "connector";
        if (utpOriginallyHole())
            return "hole";
        int qq = 1 / 0;
        return null;
    }
    public boolean hasTwoEqualCoordinates(CoordTriplet other) {
        int eq = 0;
        for (int i = 0; i < 3; i++)
            if (this.getCoord(i) == other.getCoord(i))
                eq++;
        return eq == 2;
    }
    /*
    object, aasumed face, parallel to specificed coordinate = 0
    */
    public boolean utpFaceParallelTo(int c) {
        return mod6String().substring(c, c + 1).equals("1");
    }
    public boolean utpFace() {
        return utpVerticalParaX0() || utpVerticalParaY0()  || utpHorizontal();
    }
    public boolean utpOriginallyHole() {
        String s = mod6String();
        boolean ret = false;
        ret |= (s.equals("145") || s.equals("143") || s.equals("154") || s.equals("134")); // face vert para x0
        ret |= (s.equals("415") || s.equals("413") || s.equals("514") || s.equals("314")); // face vert para y0
        ret |= (s.equals("451") || s.equals("431") || s.equals("341") || s.equals("541")); // face horiz
        return ret;
    }
    public CoordTriplet utpGetFaceNearHole() {
        int nx = getFaceNearHole(x);
        int ny = getFaceNearHole(y);
        int nz = getFaceNearHole(z);
        CoordTriplet ret = new CoordTriplet(nx, ny, nz);
        return ret;
    }
    int getFaceNearHole(int v) {
        int m = mod6(v);
        switch(m) {
            case 3: return v + 1;
            case 5: return v - 1;
            default: return v;
        }
    }
    /*
    public String utpType() {
        if (utpFace())
            return "face";
        if (this.utpStraightConnector())
            return "straight connector";
        if (this.utpOrthogonalConnector())
            return "orthogonal connector";
        if (this.utpOriginallyHole())
            return "hole";
        int qq = 1 / 0;
        return null;
    }
    */
    /*
    return an array of the 3 faces adjacent to an edge that was originally adjacent to a hole
    */
    public CoordTriplet[] utpGetFacesAdjacentHole() {
        CoordTriplet[] ret = new CoordTriplet[3];
        String s = mod6String();
        switch (s) {
        // face vert para x0
            case "145": 
                ret[0] = this.add(0, 0, -1 + 6); 
                ret[1] = this.add(-3, 0, 2);
                ret[2] = this.add(3, 0, 2);
                break;
            case "143": 
                ret[0] = this.add(0, 0, 1 - 6);
                ret[1] = this.add(-3, 0, -2);
                ret[2] = this.add(3, 0, -2);
                break;                
            case "154": 
                ret[0] = this.add(0, -1 + 6, 0);
                ret[1] = this.add(-3, 2, 0);
                ret[2] = this.add(3, 2, 0);           
                break;
            case "134": 
                ret[0] = this.add(0, 1 - 6, 0);
                ret[1] = this.add(-3, -2, 0);
                ret[2] = this.add(3, -2, 0);           
                break;                
        // face vert para y0
            case "415": 
                ret[0] = this.add(0, 0, -1 + 6);
                ret[1] = this.add(0, -3, 2);
                ret[2] = this.add(0, 3, 2);
                break;                
            case "413": 
                ret[0] = this.add(0, 0, 1 - 6);
                ret[1] = this.add(0, -3, -2);
                ret[2] = this.add(0, 3, -2);
                break;                                
            case "514": 
                ret[0] = this.add(-1 + 6, 0, 0);
                ret[1] = this.add(2, -3, 0);
                ret[2] = this.add(2, 3, 0);
                break;                 
            case "314": 
                ret[0] = this.add(1 - 6, 0, 0);
                ret[1] = this.add(-2, -3, 0);
                ret[2] = this.add(-2, 3, 0);
                break;                                
        // face horiz 
            case "451": 
                ret[0] = this.add(0, -1 + 6, 0);
                ret[1] = this.add(0, 2, -3);
                ret[2] = this.add(0, 2, 3);
                break;                  
            case "431": 
                ret[0] = this.add(0, 1 - 6, 0);
                ret[1] = this.add(0, -2, -3);
                ret[2] = this.add(0, -2, 3);
                break;                 
            case "541": 
                ret[0] = this.add(-1 + 6, 0, 0);
                ret[1] = this.add(2, 0, -3);
                ret[2] = this.add(2, 0, 3);
                break;                  
            case "341":    
                ret[0] = this.add(1 - 6, 0, 0);
                ret[1] = this.add(-2, 0, -3);
                ret[2] = this.add(-2, 0, 3);
                break;                  
            default: int qq = 1 / 0;
        }
        return ret;
    }
    public boolean utpHinge() {
        String s = this.mod6String();
        switch(s) {
            case "411":
            case "141":
            case "114":
                return true;
        }
        return false;
    }
    public boolean utpConnectorNearHinge(CoordTriplet realHinge) {
        
        if (Math.abs(y - realHinge.y) > 1)
            return false;
        if (Math.abs(z - realHinge.z) > 1)
            return false;
        return true;
    }
    public boolean utpOrthogonalConnector() {
        String s = mod6String();
        boolean ret = false;
        ret |= (s.equals("042") || s.equals("242") || s.equals("040") || s.equals("240"));  // face vert para x0
        ret |= (s.equals("024") || s.equals("224") || s.equals("004") || s.equals("204"));
        
        ret |= (s.equals("402") || s.equals("422") || s.equals("400") || s.equals("420"));  // face vert para y0
        ret |= (s.equals("204") || s.equals("224") || s.equals("004") || s.equals("024"));
        
        ret |= (s.equals("240") || s.equals("242") || s.equals("040") || s.equals("042"));  // face horiz
        ret |= (s.equals("420") || s.equals("422") || s.equals("400") || s.equals("402"));
        
        return ret;
    }
    public CoordTriplet[] utpFacesNearConnector() {
        if (utpOrthogonalConnector())
            return utpFacesNearOrthogonalConnector();
        else
            return utpFacesNearStraightConnector();
    }
    public CoordTriplet[] utpFacesNearOrthogonalConnector() {
        String s = mod6String();
        CoordTriplet[] ret = new CoordTriplet[2];
        switch(s) {
            case "042": 
                ret[0] = new CoordTriplet(x - 2, y, z - 1);
                ret[1] = new CoordTriplet(x + 1, y, z + 2);
                break;
            case "242": 
                ret[0] = new CoordTriplet(x + 2, y, z - 1);
                ret[1] = new CoordTriplet(x - 1, y, z + 2);
                break;
            case "040": 
                ret[0] = new CoordTriplet(x - 2, y, z + 1);
                ret[1] = new CoordTriplet(x + 1, y, z - 2);
                break;
            case "240": 
                ret[0] = new CoordTriplet(x + 2, y, z + 1);
                ret[1] = new CoordTriplet(x - 1, y, z - 2);
                break;
                
            case "024": 
                ret[0] = new CoordTriplet(x - 2, y - 1, z);
                ret[1] = new CoordTriplet(x + 1, y + 2, z);
                break;
            case "224": 
                ret[0] = new CoordTriplet(x + 2, y - 1, z);
                ret[1] = new CoordTriplet(x - 1, y + 2, z);
                break;
            case "004": 
                ret[0] = new CoordTriplet(x - 2, y + 1, z);
                ret[1] = new CoordTriplet(x + 1, y - 2, z);
                break;
            case "204": 
                ret[0] = new CoordTriplet(x + 2, y + 1, z);
                ret[1] = new CoordTriplet(x - 1, y - 2, z);
                break;
            
            case "402": 
                ret[0] = new CoordTriplet(x, y - 2, z - 1);
                ret[1] = new CoordTriplet(x, y + 1, z + 2);
                break;
            case "422": 
                ret[0] = new CoordTriplet(x, y + 2, z - 1);
                ret[1] = new CoordTriplet(x, y - 1, z + 2);
                break;
            case "400": 
                ret[0] = new CoordTriplet(x, y - 2, z + 1);
                ret[1] = new CoordTriplet(x, y + 1, z - 2);
                break;
            case "420": 
                ret[0] = new CoordTriplet(x, y + 2, z + 1);
                ret[1] = new CoordTriplet(x, y - 1, z - 2);
                break;
            
            default:
                int qq = 1 / 0;
        }
        return ret;
    }
    public boolean utpConnector() {
        return this.utpOrthogonalConnector() || this.utpStraightConnector();
    }
    public boolean utpStraightConnector() {
        String s = mod6String();
        boolean ret = false;
        ret |= (s.equals("140") || s.equals("142") || s.equals("104") || s.equals("124")); // face vert para x0
        ret |= (s.equals("410") || s.equals("412") || s.equals("014") || s.equals("214")); // face vert para y0
        ret |= (s.equals("401") || s.equals("421") || s.equals("241") || s.equals("041")); // face horiz
        return ret;        
    }
    /*
    public boolean utpSkippableStraightConnector() {
        String s = mod6String();
        return utpStraightConnector() && s.contains("2");
    }
*/
    public CoordTriplet[] utpFacesNearStraightConnector() {
        
        CoordTriplet[] ret = new CoordTriplet[2];
        ret[0] = new CoordTriplet(x + utpFacesNearStraightConnector(x, 1), y + utpFacesNearStraightConnector(y, 1), z + utpFacesNearStraightConnector(z, 1));
        ret[1] = new CoordTriplet(x + utpFacesNearStraightConnector(x, -1), y + utpFacesNearStraightConnector(y, -1), z + utpFacesNearStraightConnector(z, -1));
        if (!ret[0].utpFace()) {
            System.err.println(this.toString());
            System.err.println(ret[0].toString());
            int qq = 1 / 0;
        }
        if (!ret[1].utpFace()) {
            int qq = 1 / 0;
        }
        return ret;
    }
    int utpFacesNearStraightConnector(int v, int d) {
        switch (mod6(v)) {
            case 1: return 0;
            case 4: return 0;
            case 0:
                return 1 + -3 * d;
            case 2:
                return -1 + -3 * d;
            default:
                int qq = 1 / 0;
                return 0;
        }
    }
    public CoordTriplet utpNearestFaceToStraightConnector() {
        CoordTriplet[] list = this.utpFacesNearStraightConnector();
        int d0 = totalDistance(list[0]);
        int d1 = totalDistance(list[1]);
        if (d0 < d1)
            return list[0];
        else
            return list[1];
                    
    }
    public CoordTriplet[] utpGetConnectors(CoordTriplet ocp) {
        CoordTriplet[] ret;
        int count = 0;
        int diff = -1, eq = -1;
        int[] others = new int[2]; 
        int numOthers = 0;
        for (int dir = 0; dir <= 2; dir++)
            if (this.getCoord(dir) == ocp.getCoord(dir)) {

                count++; 
                eq = dir;
            } else {
                diff = dir;
                if (numOthers == 2) {
                    int qq = 1 / 0;
                }
                others[numOthers] = dir;
                numOthers++;
                
            }
        
        if (count == 2) {
            ret = new CoordTriplet[2];
            ret[0] = this.changeSingle(diff, this.getCoord(diff) + (ocp.getCoord(diff) - this.getCoord(diff)) / 3);
            ret[1] = this.changeSingle(diff, this.getCoord(diff) + (ocp.getCoord(diff) - this.getCoord(diff)) * 2 / 3);
            //System.err.println(this.toString() + " " + ocp.toString() + " " + ret[0].toString() + " " + ret[1].toString());
        } else {
            ret = new CoordTriplet[1];
            CoordTriplet possible = this.changeSingle(others[0], this.getCoord(others[0]) + (ocp.getCoord(others[0]) - this.getCoord(others[0])) * 2 / 3)
                    .changeSingle(others[1], this.getCoord(others[1]) + (ocp.getCoord(others[1]) - this.getCoord(others[1])) / 3);
            //System.err.println("A: " + this.toString() + " " + ocp.toString() + " " + possible.toString() );
            if (!possible.utpOrthogonalConnector()) {
                possible = this.changeSingle(others[0], this.getCoord(others[0]) + (ocp.getCoord(others[0]) - this.getCoord(others[0])) / 3)
                    .changeSingle(others[1], this.getCoord(others[1]) + (ocp.getCoord(others[1]) - this.getCoord(others[1])) * 2 / 3);
            }
            ret[0] = possible;
                    /*
            boolean found = false;
            if (!possible.utpOrthogonalConnector()) {
                int qq = 1 / 0;
            }
            CoordTriplet[] faces = possible.utpFacesNearOrthogonalConnector();
            if (ocp.equals(faces[0]) || ocp.equals(faces[1])) {
                ret[0] = possible;
                found = true;
                
            }
            if (!found) {
                possible = this.changeSingle(others[0], this.getCoord(others[0]) + (ocp.getCoord(others[0]) - this.getCoord(others[0])) / 3)
                    .changeSingle(others[1], this.getCoord(others[1]) + (ocp.getCoord(others[1]) - this.getCoord(others[1])) * 2 / 3);
                System.err.println("B: " + this.toString() + " " + ocp.toString() + " " + possible.toString() );
                ret[0] = possible;
                if (!possible.utpOrthogonalConnector()) {
                    int qq = 1 / 0;
                }
            }
*/
        }
        return ret;
    }
    public boolean utpIsNearestFaceToStraightConnectorInList(ArrayList<CoordTriplet> list) {
        CoordTriplet ct = utpNearestFaceToStraightConnector();
        return list.contains(ct);
    }
    int totalDistance(CoordTriplet other) {
        int dist = coordSum(other) - coordSum(this);
        return Math.abs(dist);
    }
    int coordSum(CoordTriplet ct) {
        return ct.getX() + ct.getY() + ct.getZ();
    }
    int mod6(int v) {
        return (v + 60000) % 6;
    }
    String mod6String() {
        String ret = mod6(x) + "" + mod6(y) + "" + mod6(z);
        return ret;
    }
    public String getUTPMV(int direction, CoordTriplet other) {
        int otherDir = other.utpFaceDirection(); 
        int thisDir = this.utpFaceDirection();
        if (otherDir == thisDir)
            return "";
        if (sameSign(direction, other.getCoord(thisDir) - this.getCoord(thisDir)))
            return "v";
        else
            return "m";
    }
    boolean sameSign(int v1, int v2) {
        return v1 * v2 > 0;
    }
}
