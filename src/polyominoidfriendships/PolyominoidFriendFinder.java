/*
 
 */
package polyominoidfriendships;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

import java.math.BigInteger;

/**
 *
 * @author jmason
 * 
 */
public class PolyominoidFriendFinder  {
    /*
    PoolSet poolSet;
    ParameterAnalyzer pa;
    PolyominoidPool array;
    int pSize;
    boolean noCrash, writePair, noWrite, newVerify, both, noPool, restricted;
    String from ,to;
    String fromRep ,toRep;
    boolean knife;
    FileWriter fw3;
    long done = 0;
    */
    boolean noCrash;
    PolyominoidPool array;
    Polyominoid pTo //, pToD, pFromD 
            ;
    //Filter filter;
    ArrayList<Polyominoid>  pair;
    public PolyominoidFriendFinder() {
        
    }
    public void setNoCrash() {
        noCrash = true;
    }
    public void setArray(PolyominoidPool array) {
        this.array = array;
    }
    public void setTo(Polyominoid to) {
        this.pTo = to;
    }
    
    public ArrayList<Polyominoid>  getFriends(Polyominoid p) {
        ArrayList<Polyominoid>  ret = new ArrayList<>();
        ret.add(p);
        ArrayList<Polyominoid>  set12 = p.get12List();
        for (Polyominoid q : set12) 
            addFriends(ret, q, p);
        if (pair != null)
            return pair;
        return ret;
    }
    /*
 * x even, y even, z odd - flat, z=0
 * x odd, y even, z even - vertical, x = 0
 * x even, y odd, z even - vertical, y = 0

    */
    void addFriends(ArrayList<Polyominoid> list, Polyominoid p, Polyominoid orig) {
        for (int i = 0; i < p.size(); i++) {
            CoordTriplet hingeElement = p.getCoords(i);
            int z = hingeElement.getZ();
            if (z % 2 == 0)
                continue;
            ArrayList<CoordTriplet> immediateElements = getImmediate(p, hingeElement);
            if (immediateElements.size() == 0)
                continue;
            ArrayList<CoordTriplet> furtherElements = propogateElements(p, immediateElements, hingeElement);
            ArrayList<ArrayList<CoordTriplet>> connectedSubsets = getConnectedSubsets(p, hingeElement, immediateElements, furtherElements);
           
            for (ArrayList<CoordTriplet> subset : connectedSubsets) {
                ArrayList<CoordTriplet> fixed = allExcept(p.getArray(), subset);
                if (subset.size() > fixed.size())
                    continue;
                for (int direction = -1; direction <= 1; direction += 2) {
                    VerifiedPolyominoid p90 = rot90(fixed, hingeElement, subset, direction);
                    
                    if (p90 != null) {
                        handle(fixed, p90,  p,  list,  hingeElement,  subset, orig, direction);
                        if (p90.ok == 0) {
                            VerifiedPolyominoid p90bis = rot90(fixed, hingeElement, p90.newCoords, direction);
                            if (p90bis != null) {
                                handle(fixed, p90bis,  p,  list,  hingeElement,  subset, orig, direction);
                            }
                        }
                    }
                    
                }
            }
        }
    }
    void handle(ArrayList<CoordTriplet> fixed, VerifiedPolyominoid p90, Polyominoid p, ArrayList<Polyominoid> list, CoordTriplet hingeElement, ArrayList<CoordTriplet> subset,
            Polyominoid orig, int direction ) {
        String s = p90.p.getUnique();
        Polyominoid p90u = new Polyominoid(new BigInteger(s));                        
        if (noCrash == false && !array.contains(p90u ) ) {
            System.err.println("current " + p.listCoords());
            System.err.println("friend  " + p90u.listCoords());
            System.err.println("friend not found in input");
            System.exit(1);
        }
        if (!list.contains(p90u) && !orig.equals(p90u)) {

            /*
            if (orig.getRep().toString().equals("10633823966279326992597943707408269312") && p90u.getRep().toString().equals("236978464")) {
                VerifiedPolyominoid p90dbg = rot90(fixed, hingeElement, subset, direction);
                innerWrite(count++, p);
                innerWrite(count++, p90.p);  
                System.err.println("direction " + direction);
                System.err.println("current " + p.listCoords());
                System.err.println("subset  " + dump(subset));
                System.err.println("friend  " + p90.p.listCoords());     
                System.err.println("hinge  " + hingeElement.toString()); 
                System.err.println("verify " + p90.ok);
                if (p90.cp != null)
                    System.err.println("object " + p90.cp);
            } */
           
            if (p90.ok == 0) {
                list.add(p90u);
                if (pTo != null && p90u.equals(pTo)) {
                    CoordTriplet realHinge = hingeElement.add(new CoordTriplet(0, 1, 0));
                    Polyominoid p90h = p90.p.addHinge(realHinge, p90.delta);
                    Polyominoid ph = new Polyominoid();
                    for (CoordTriplet ct : fixed)
                        ph.insert(ct);
                    for (CoordTriplet ct : subset) {
                        ct = ct.add(new CoordTriplet(0, 0, 0));
                        ct.setFlag();
                        ph.insert(ct);
                    }
                    ph.insert(realHinge);
                    ph.seal();
                    //Polyominoid ph = p.addHinge(realHinge);
                    ph.setError(p90.ok);
                    pair = new ArrayList<>();
                    pair.add(ph);
                    pair.add(p90h);
                    /*
                    filter.innerWrite(0, ph);
                    filter.innerWrite(0, p90h);  
                    */
                }
            }
            
        }
         
    }
    ArrayList<CoordTriplet> allExcept(ArrayList<CoordTriplet> list, ArrayList<CoordTriplet> exceptions) {
        ArrayList<CoordTriplet> ret = new ArrayList<>();
        for (CoordTriplet ct : list)
            if (!exceptions.contains(ct))
                ret.add(ct);
        return ret;
    }
    String dump(ArrayList<CoordTriplet> list) {
        String ret = "";
        for (CoordTriplet ct : list)
            ret += ct.toString();
        return ret;
    }
    boolean simple(Polyominoid p) {
        return p.flat() && (p.maxAdjacencies() <= 2) && p.sameDirectionAdjacencies();
    }
    ArrayList<ArrayList<CoordTriplet>> getConnectedSubsets(Polyominoid p, CoordTriplet cp, ArrayList<CoordTriplet> immediateElements, ArrayList<CoordTriplet> furtherElements) {
        ArrayList<ArrayList<CoordTriplet>> ret = new ArrayList<>();
        int immediateCombinationNumber = (1 << immediateElements.size()) - 1;
        for (int i = 1; i <= immediateCombinationNumber; i++) {
            generateCombination(p, ret, immediateElements, i, furtherElements);
        }
        return ret;
    }
    void generateCombination(Polyominoid p, ArrayList<ArrayList<CoordTriplet>> list, ArrayList<CoordTriplet> immediateElements, 
            int immediateCombination, ArrayList<CoordTriplet> furtherElements) {
        int furtherCombinationNumber = (1 << furtherElements.size()) - 1;
        for (int i = 0; i <= furtherCombinationNumber; i++) {
            generateCombination(p, list, immediateElements, immediateCombination, furtherElements, i);
        }        
    }
    void generateCombination(Polyominoid p, ArrayList<ArrayList<CoordTriplet>> list, ArrayList<CoordTriplet> immediateElements, 
            int immediateCombination, ArrayList<CoordTriplet> furtherElements, int furtherCombination) {
        if (!combinationIntact(p, immediateElements, immediateCombination, furtherElements, furtherCombination))
            return;
        ArrayList<CoordTriplet> subset = combine(immediateElements, immediateCombination, furtherElements, furtherCombination);
        list.add(subset);
    }
    ArrayList<CoordTriplet> combine(ArrayList<CoordTriplet> immediateElements, int immediateCombination, 
            ArrayList<CoordTriplet> furtherElements, int furtherCombination) {
        ArrayList<CoordTriplet> ret = new ArrayList<>();
        combine(ret, immediateElements, immediateCombination);
        combine(ret, furtherElements, furtherCombination);
        return ret;
    }
    void combine(ArrayList<CoordTriplet>  list, ArrayList<CoordTriplet> elements, int combination) {
        for (int i = 0; i < elements.size(); i++) {
            if ((combination & (1 << i)) != 0)
                list.add(elements.get(i));
        }
    }
    boolean combinationIntact(Polyominoid p, ArrayList<CoordTriplet> immediateElements, int immediateCombination, 
            ArrayList<CoordTriplet> furtherElements, int furtherCombination) {
        
        
        Polyominoid q = new Polyominoid();
        addElements(q, immediateElements,  immediateCombination);
        addElements(q, furtherElements, furtherCombination);
        //q.seal();
        boolean ret = q.intact();
        return ret;
        
    }
    void addElements(Polyominoid q, ArrayList<CoordTriplet> elements, int combination) {
        for (int i = 0; i < elements.size(); i++) {
            if ((combination & (1 << i)) != 0) {
                q.insert(elements.get(i));
            }
        }        
    }
    
    VerifiedPolyominoid rot90(ArrayList<CoordTriplet> fixed, CoordTriplet hingeCp, ArrayList<CoordTriplet> subset, int direction) {
        int ok = 0;
        boolean diff = false;
        CoordTriplet badCp = null;
        Polyominoid np = new Polyominoid();
        for (int i = 0; i < fixed.size(); i++) {
            //CoordTriplet xcp = p.getCoords(i);
            CoordTriplet xcp = fixed.get(i);
            //if (!subset.contains(xcp))
                np.insert(xcp);
       }
       if (!np.intact())
           return null;
       ArrayList<CoordTriplet> soh = getStraightOnHinges(np.getArray());
       if (conflict(soh, getStraightOnHinges(subset)))
           return null;
       ArrayList<CoordTriplet> xp = new ArrayList<>();
       for (CoordTriplet xcp : subset) {
            CoordTriplet xcp90 = rot90(np, xcp, hingeCp, direction);
            if (xcp90 == null)
               return null;
            if (xp.contains(xcp90)) {
                int qq = 1 / 0;
            }
            if (np.contains(xcp90))
               return null;
           
            int ver;

            ver = newVerifyPassage(np, xcp.getX(), xcp.getY(), xcp.getZ(), hingeCp.getX(), hingeCp.getY(), hingeCp.getZ(), xcp90, subset);
            if (ver != 0) {
                ok = ver;
                badCp = xcp;
            }
            
            xcp90.setFlag();
            xp.add(xcp90);
       }
       if (conflict(soh, getStraightOnHinges(xp)))
           return null;
       for (CoordTriplet xcp : xp) {
           np.insert(xcp);
       }
       if (fixed.size() + subset.size() != np.size()) {
           int qq = 1 / 0;
       }
       int minX = 9999, minY = 9999, minZ = 9999;
       for (int i = 0; i < np.size(); i++) {
           CoordTriplet ct = np.getCoords(i);
           int x = ct.getX(), y = ct.getY(), z = ct.getZ();
          
           if (x < minX)
               minX = x;
           
           if (y < minY)
               minY = y;
           
           if (z < minZ)
               minZ = z;
           
       }
       int dx = 0, dy = 0, dz = 0;
       dx = calcDelta(minX);
       dy = calcDelta(minY);
       dz = calcDelta(minZ);
       CoordTriplet delta = new CoordTriplet(dx, dy, dz);
       if (fixed.size() + subset.size() != np.size()) {
           int qq = 1 / 0;
       }       
       Polyominoid np2 = new Polyominoid();
       for (int i = 0; i < np.size(); i++) {
           CoordTriplet ct = np.getCoords(i);
           CoordTriplet ctNew = new CoordTriplet(ct.getX() + dx, ct.getY() + dy, ct.getZ() + dz);
           if (ct.getFlag())
               ctNew.setFlag();
           np2.insert(ctNew);
       }
       np2.seal();
       if (fixed.size() + subset.size() != np2.size()) {
           int qq = 1 / 0;
       }       
       if (!np2.intact()) {
           //int qq = 1 / 0;
           return null;
       }
       
       return new VerifiedPolyominoid(np2, ok, badCp, xp, diff, delta);
    }
    boolean conflict(ArrayList<CoordTriplet> one, ArrayList<CoordTriplet> two) {
        for (CoordTriplet ct : one)
            if (two.contains(ct))
                return true;
        return false;
    }
    int calcDelta(int v) {
        int ret = 0;
        while (v < 1) {
            v += 2;
            ret += 2;
        }
        while (v > 2) {
            v -= 2;
            ret -= 2;
        }
        return ret;
    } 
    /*
    Suppose H   element
    direction 1 means clockwise, -1 anti-clockwise
    */
    CoordTriplet  rot90(Polyominoid remainder, CoordTriplet ocp, CoordTriplet cp, int direction) {
        int ox = ocp.getX(), oy = ocp.getY(), oz = ocp.getZ();
        int x = cp.getX(), y = cp.getY(), z = cp.getZ();
        
        int dy = oy - y, dz = oz - z;
        int ny, nz;
        
        if (direction > 0) {
           
            ny = y + dz + 1;
            nz = z - dy + 1;
        } else {
            
            nz = z + dy - 1;
            ny = y - dz + 1;            
        }
        
        /*
        if (!verifyPassage(remainder, ox, ny, nz, x, y, z, -direction))
            return null;
        */
        CoordTriplet ret = new CoordTriplet(ox, ny, nz);
       
        return ret;
    }
    /*
    
    */
    int newVerifyPassage(Polyominoid remainder, int ox, int oy, int oz, int hx, int hy, int hz, CoordTriplet destination, ArrayList<CoordTriplet> subset) {
        for (CoordTriplet collision : remainder.getArray()) {
            int ret = newVerifyPassage( remainder,  ox,  oy,  oz,  hx,  hy,  hz,  destination, collision, subset);
            if (ret != 0)
                return ret;
        }
        return 0;
    }

    int newVerifyPassage(Polyominoid remainder, int ox, int oy, int oz, int hx, int hy, int hz, CoordTriplet destination, 
            CoordTriplet collision, ArrayList<CoordTriplet> subset) {
        int realHingeY = hy + 1;
        int cx = collision.getX(), cy = collision.getY(), cz = collision.getZ();
        int  dy = destination.getY(), dz = destination.getZ();
    /*
    Lateral
1. C has the same orientation as F, parallel to x=0
2. C has the same x coordinate as F
3. C lies in the quadrant between the 2 lines L1 & L2.
4. Define minC to be the minimum distance between H and any vertex of C; likewise for maxC,
minF, maxF. Then collision will occur if there is some overlap in the ranges minC-maxC, minVmaxV.
    */
        if (parallelXeq0(ox) && parallelXeq0(cx) && ox == cx) {
            LineSegment line1 = getLineHingeToFace(realHingeY, hz, oy, oz);
            LineSegment line2 = getLineHingeToFace(realHingeY, hz, dy, dz);
            DCoord centreF = getCentreOfFaceCoordinates(oy, oz);
            DCoord centreD = getCentreOfFaceCoordinates(dy, dz);
            DCoord centreC = getCentreOfFaceCoordinates(cy, cz);
            if (line1.sameSide(centreC, centreD) && line2.sameSide(centreC, centreF)) {
                Range rC = getRangeOfDistancesHingeToFace(realHingeY, hz, cy, cz);
                Range rF = getRangeOfDistancesHingeToFace(realHingeY, hz, oy, oz);
                if (rC.overlaps(rF))
                    return 30;
            }
        }
        /*
        Frontal
1.	C and F are not parallel to x=0
2.	C has the same x coordinate as F
3.	C lies in the quadrant between the 2 lines L1 & L2. 
4.	Define minC to be the minimum distance between H and each vertex of C; likewise for maxC, minF, maxF. Then collision will occur if there is some overlap in the ranges minC-maxC, minF-maxF.

        */
        if (!parallelXeq0(ox) && !parallelXeq0(cx) && ox == cx) {
            LineSegment line1 = getLineHingeToEdge(realHingeY, hz, oy, oz);
            LineSegment line2 = getLineHingeToEdge(realHingeY, hz, dy, dz);
            DCoord centreF = getCentreOfEdgeCoordinates(oy, oz);
            DCoord centreD = getCentreOfEdgeCoordinates(dy, dz);
            DCoord centreC = getCentreOfEdgeCoordinates(cy, cz);
            if (line1.sameSide(centreC, centreD) && line2.sameSide(centreC, centreF)) {
                Range rC = getRangeOfDistancesHingeToEdge(realHingeY, hz, cy, cz);
                Range rF = getRangeOfDistancesHingeToEdge(realHingeY, hz, oy, oz);
                if (rC.overlaps(rF))
                    return 31;
            }
        }    
        /*
        Moving knife
1.	C is not parallel to x=0, but F is.
2.	C is a pair of adjacent parallel faces bonded by a line segment parallel to x=0 and with the same x as F.
3.	C lies in the quadrant between the 2 lines L1 & L2. 
4.	Define minC to be the minimum distance between H and each vertex of C; likewise for maxC, minF, maxF. Then collision will occur if there is some overlap in the ranges minC-maxC, minF-maxF.

        */
        for (int side = -1; side <= 1; side += 2)
            if (parallelXeq0(ox) && !parallelXeq0(cx) && (cx == ox - 1 * side) && remainder.contains(ox + 1 * side, cy, cz)) {            
                LineSegment line1 = getLineHingeToFace(realHingeY, hz, oy, oz);
                LineSegment line2 = getLineHingeToFace(realHingeY, hz, dy, dz);
                DCoord centreF = getCentreOfFaceCoordinates(oy, oz);
                DCoord centreD = getCentreOfFaceCoordinates(dy, dz);
                DCoord centreC = getCentreOfEdgeCoordinates(cy, cz);
                if (line1.sameSide(centreC, centreD) && line2.sameSide(centreC, centreF)) {
                    Range rC = getRangeOfDistancesHingeToEdge(realHingeY, hz, cy, cz);
                    Range rF = getRangeOfDistancesHingeToFace(realHingeY, hz, oy, oz);
                    if (rC.overlaps(rF))
                        return 32;
                }
            }        
        /*
        Moving pair
1.	C is parallel to x=0, but F is not.
2.	F is a pair of adjacent parallel faces bonded by a line segment parallel to x=0 and with the same x as C.
3.	C lies in the quadrant between the 2 lines L1 & L2. 
4.	Define minC to be the minimum distance between H and each vertex of C; likewise for maxC, minF, maxF. Then collision will occur if there is some overlap in the ranges minC-maxC, minF-maxF.

        */
        for (int side = -1; side <= 1; side += 2)
            if (!parallelXeq0(ox) && parallelXeq0(cx) && (cx == ox + 1 * side) && subset.contains(new CoordTriplet(ox + 2 * side, oy, oz))) {            
                LineSegment line1 = getLineHingeToEdge(realHingeY, hz, oy, oz);
                LineSegment line2 = getLineHingeToEdge(realHingeY, hz, dy, dz);
                DCoord centreF = getCentreOfEdgeCoordinates(oy, oz);
                DCoord centreD = getCentreOfEdgeCoordinates(dy, dz);
                DCoord centreC = getCentreOfFaceCoordinates(cy, cz);
                if (line1.sameSide(centreC, centreD) && line2.sameSide(centreC, centreF)) {
                    Range rC = getRangeOfDistancesHingeToFace(realHingeY, hz, cy, cz);
                    Range rF = getRangeOfDistancesHingeToEdge(realHingeY, hz, oy, oz);
                    if (rC.overlaps(rF))
                        return 33;
                }
            }                
        return 0;
    }
    Range  getRangeOfDistancesHingeToFace(int realHingeY, int hz, int fy, int fz) {
        double half = 1D / 2D;
        double min = 9999, max = -9999;
        DCoord hinge = getPointCoordinates(realHingeY,  hz);
        DCoord centreOfFace = getCentreOfFaceCoordinates(fy, fz);
        DCoord bottomLeft = centreOfFace.addDelta(-half, -half);
        DCoord topLeft = centreOfFace.addDelta(-half, half);
        DCoord bottomRight = centreOfFace.addDelta(half, -half);
        DCoord topRight = centreOfFace.addDelta(half, half);
        double blLen = (new LineSegment(hinge, bottomLeft)).length();
        double brLen = (new LineSegment(hinge, bottomRight)).length();
        double tlLen = (new LineSegment(hinge, topLeft)).length();
        double trLen = (new LineSegment(hinge, topRight)).length();
        if (blLen > max)
            max = blLen;
        if (blLen < min)
            min = blLen;
        
        if (brLen > max)
            max = brLen;
        if (brLen < min)
            min = brLen;
        
        if (tlLen > max)
            max = tlLen;
        if (tlLen < min)
            min = tlLen;
        
        if (trLen > max)
            max = trLen;
        if (trLen < min)
            min = trLen;
        
        return new Range(min, max);
    }
    Range  getRangeOfDistancesHingeToEdge(int realHingeY, int hz, int fy, int fz) {
        double half = 1D / 2D;
        double min = 9999, max = -9999;
        DCoord hinge = getPointCoordinates(realHingeY,  hz);
        DCoord centreOfEdge = getCentreOfEdgeCoordinates(fy, fz);
        DCoord end1, end2;
        if (parallelZeq0(fz)) {
            end1 = centreOfEdge.addDelta(-half, 0); 
            end2 = centreOfEdge.addDelta(half, -0);
        } else {
            end1 = centreOfEdge.addDelta(0, -half); 
            end2 = centreOfEdge.addDelta(0, half);            
        }
        
       
        double end1Len = (new LineSegment(hinge, end1)).length();
        double end2Len = (new LineSegment(hinge, end2)).length();
       
        if (end1Len > max)
            max = end1Len;
        if (end1Len < min)
            min = end1Len;
        
        if (end2Len > max)
            max = end2Len;
        if (end2Len < min)
            min = end2Len;
        
      
        
        return new Range(min, max);
    }
    LineSegment  getLineHingeToFace(int realHingeY, int hz, int oy, int oz) {
        return new LineSegment(getPointCoordinates(realHingeY,  hz), getCentreOfFaceCoordinates(oy, oz));
    }
    LineSegment  getLineHingeToEdge(int realHingeY, int hz, int oy, int oz) {
        return new LineSegment(getPointCoordinates(realHingeY,  hz), getCentreOfEdgeCoordinates(oy, oz));
    }
    DCoord getPointCoordinates(int x , int y) {
        return new DCoord(convertToCoordinate(x), convertToCoordinate(y));
    }
    DCoord getCentreOfFaceCoordinates(int x , int y) {
        return new DCoord(convertToCoordinate(x), convertToCoordinate(y));
    }
    DCoord getCentreOfEdgeCoordinates(int x , int y) {
        return new DCoord(convertToCoordinate(x), convertToCoordinate(y));
    }
    double convertToCoordinate(int v) {
        double dv = v;
        return (dv - 1D) / 2D;
    }
    class Range {
        double nextToNothing = 0.00000001;
        double min, max;
        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
        boolean overlaps(Range other) {
            if (le(max, other.min))
                return false;
            if (ge(min, other.max))
                return false;
            return true;
        }
        
        public String toString() {
            return "(" + min + "-" + max + ")";
        }
    }
    boolean almostEquals(double v1, double v2) {
        double nextToNothing = 0.00000001;
        if (v1 - nextToNothing < v2 && v2 < v1 + nextToNothing)
            return true;
        return false;
    }
    boolean le(double v1, double v2) {
        return (v1 < v2) || almostEquals(v1, v2);
    }
    boolean ge(double v1, double v2) {
        return (v1 > v2) || almostEquals(v1, v2);
    }
    
    
    
    /*
 * x even, y even, z odd - flat, z=0
 * x odd, y even, z even - vertical, x = 0
 * x even, y odd, z even - vertical, y = 0    
    */
    boolean parallelZeq0( int z) {
        return (z % 2) != 0;
    }
    boolean parallelXeq0(int x) {
        return (x % 2) != 0;
    }
    boolean parallelYeq0(int y ) {
        return (y % 2) != 0;
    }
    ArrayList<CoordTriplet> getImmediate(Polyominoid p, CoordTriplet cp) {
        ArrayList<CoordTriplet>  ret = new ArrayList<>();
        p.addAdjacentElement(ret, cp, 0, 1, 1);
        p.addAdjacentElement(ret, cp, 0, 2, 0);
        p.addAdjacentElement(ret, cp, 0, 1, -1);
        return ret;
    }

    ArrayList<CoordTriplet> propogateElements(Polyominoid p, ArrayList<CoordTriplet> immediateElements, CoordTriplet cp) {
        //System.err.println("enter propogateElements");
        ArrayList<CoordTriplet>  ret = new ArrayList<>();
        ArrayList<CoordTriplet> inputElements = immediateElements;
        while (true) {
            boolean flag = false;
            ArrayList<CoordTriplet> newElements = propogateElements(p, immediateElements, inputElements,  cp);
            if (newElements.size() == 0)
                break;
            for (CoordTriplet e : newElements) {
                if (!ret.contains(e)) {
                    ret.add(e);
                    flag = true;
                }
            }
            inputElements = newElements;
            if (!flag)
                break;
        }
        //System.err.println("exit propogateElements");
        return ret;
    }
    ArrayList<CoordTriplet> propogateElements(Polyominoid p,  ArrayList<CoordTriplet> immediateElements, ArrayList<CoordTriplet> inputElements,  CoordTriplet cp) {
        ArrayList<CoordTriplet>  ret = new ArrayList<>();
        for (CoordTriplet e : inputElements) {
            ArrayList<CoordTriplet>  adj = p.getAdjacent( e);
            for (CoordTriplet ae : adj) {
                if (ae.equals(cp))
                    continue;
                if (ret.contains(ae))
                    continue;
                if (immediateElements.contains(ae))
                    continue;
                if (inputElements.contains(ae))
                    continue;
                ret.add(ae);
            }
        }
        return ret;
    }

    public ArrayList<CoordTriplet> getStraightOnHinges(ArrayList<CoordTriplet> in) {
        ArrayList<CoordTriplet> ret = new ArrayList<>();
        for (CoordTriplet ct : in) {
            ArrayList<CoordTriplet> adj = getAdjacent(in, ct);
            for (CoordTriplet oct : adj) {
                CoordTriplet hinge = getStraightOnHinge(ct, oct);
                if (hinge != null ) {
                    if (!ret.contains(hinge))
                        ret.add(hinge);
                }
            }
        }
        return ret;
    } 
    CoordTriplet  getStraightOnHinge(CoordTriplet ct, CoordTriplet oct) {
        int x = ct.getX(), y = ct.getY(), z = ct.getZ(), ox = oct.getX(), oy = oct.getY(), oz = oct.getZ();
        if (x == ox && y == oy)
            return new CoordTriplet(x, y, (z + oz) / 2);
        if (y == oy && z == oz)
            return new CoordTriplet((x + ox) / 2, y, z);
        if (x == ox && z == oz)
            return new CoordTriplet(x, (y + oy) / 2, z);
        return null;
    }
    public ArrayList<CoordTriplet> getAdjacent(ArrayList<CoordTriplet> in, CoordTriplet cp) {
        ArrayList<CoordTriplet>  ret = new ArrayList<>();
        if (cp.getZ() % 2 != 0) {
            addAdjacentElement(ret, in, cp, 0, 1, 1);
            addAdjacentElement(ret, in, cp, 0, 2, 0);
            addAdjacentElement(ret, in, cp, 0, 1, -1);
            addAdjacentElement(ret, in, cp, 0, -1, 1);
            addAdjacentElement(ret, in, cp, 0, -2, 0);
            addAdjacentElement(ret, in, cp, 0, -1, -1);
            addAdjacentElement(ret, in, cp, 1, 0, 1);
            addAdjacentElement(ret, in, cp, 2, 0, 0);
            addAdjacentElement(ret, in, cp, 1, 0, -1);
            addAdjacentElement(ret, in, cp, -1, 0, 1);
            addAdjacentElement(ret, in, cp, -2, 0, 0);
            addAdjacentElement(ret, in, cp, -1, 0, -1);            
        } else if (cp.getX() % 2 != 0) {
            addAdjacentElement(ret, in, cp, 1, 0, -1);
            addAdjacentElement(ret, in, cp, -1, 0, -1);
            addAdjacentElement(ret, in, cp, 0, 0, -2);
            addAdjacentElement(ret, in, cp, 1, 0, 1);
            addAdjacentElement(ret, in, cp, -1, 0, 1);
            addAdjacentElement(ret, in, cp, 0, 0, 2);
            addAdjacentElement(ret, in, cp, 1, 1, 0);
            addAdjacentElement(ret, in, cp, -1, 1, 0);
            addAdjacentElement(ret, in, cp, 0, 2, 0);
            addAdjacentElement(ret, in, cp, 1, -1, 0);
            addAdjacentElement(ret, in, cp, -1, -1, 0);
            addAdjacentElement(ret, in, cp, 0, -2, 0);       
        } else  { 
            addAdjacentElement(ret, in, cp, 0, 1, -1);
            addAdjacentElement(ret, in, cp, 0, -1, -1);
            addAdjacentElement(ret, in, cp, 0, 0, -2);
            addAdjacentElement(ret, in, cp, 0, 1, 1);
            addAdjacentElement(ret, in, cp, 0, -1, 1);
            addAdjacentElement(ret, in, cp, 0, 0, 2);
            addAdjacentElement(ret, in, cp, -1, -1, 0);
            addAdjacentElement(ret, in, cp, -1, 1, 0);
            addAdjacentElement(ret, in, cp, -2, 0, 0);
            addAdjacentElement(ret, in, cp, 1, -1, 0);
            addAdjacentElement(ret, in, cp, 1, 1, 0);
            addAdjacentElement(ret, in, cp, 2, 0, 0);            
        }
        return ret;
    }
    public void addAdjacentElement( ArrayList<CoordTriplet> list, ArrayList<CoordTriplet> in, CoordTriplet cp, int dx, int dy, int dz) {
        CoordTriplet ncp = new CoordTriplet(cp.getX() + dx, cp.getY() + dy, cp.getZ() + dz);
        if (in.contains(ncp) && !list.contains(ncp))
            list.add(ncp);
    }    

    class VerifiedPolyominoid {
        Polyominoid p;
        int ok;
        CoordTriplet cp;
        ArrayList<CoordTriplet>  newCoords;
        boolean diff;
        CoordTriplet delta;
        VerifiedPolyominoid(Polyominoid p, int ok, CoordTriplet cp, ArrayList<CoordTriplet>  nc, boolean diff, CoordTriplet delta) {
            this.p = p;
            this.ok = ok;
            this.cp = cp;
            newCoords = nc;
            this.diff = diff;
            this.delta = delta;
        }
    }
}

    
 
