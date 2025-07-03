/*
 
 */
package polyominoidfriendships;


import java.math.BigInteger;


/**
 *
 * @author John Mason
 * writes selected lines to next filter
 */
public class LinesFilter extends Filter {
    int from, to;
    boolean perimeter;
    ParameterAnalyzer pa;
    public LinesFilter(Filter nextOneParam, String params) {
        super(nextOneParam);
        String[] rules = new String[] {"from", "to"};
        pa = new ParameterAnalyzer(params , rules);  
        to = pa.getIntValue("to", 0);
        from = pa.getIntValue("from");
    }
 
    public boolean write(long n, BigInteger rep) {
        count++;
        if (count >= from && (count <= to || to == 0))
            return innerWrite(count - from + 1, rep);
        
        return count <= to  || to == 0;
    }
    public boolean write(long n, Polyform inPoly) {
        //PolyformWC poly = (PolyformWC) inPoly;
        count++;
        if (count >= from && (count <= to || to == 0))
            return innerWrite(count - from + 1, inPoly);
        
        return count <= to  || to == 0;
          
    }     
    public void close() {
        System.err.println("write " + (count) + " lines");
    }
}
