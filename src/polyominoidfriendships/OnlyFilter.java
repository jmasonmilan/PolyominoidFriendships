/*
 
 */
package polyominoidfriendships;

/**
 *
 * @author jmason
 * passes polyforms satisfying size restrictions
 */
public class OnlyFilter extends OnlyPolyFilter {
    int from, to, white;
    boolean perimeter, noStop;
    ParameterAnalyzer pa;
    public OnlyFilter(Filter nextOneParam, String params) {
        super(nextOneParam);
        String[] rules = new String[] {"from", "to", "perimeter", "nostop", "white"};
        pa = new ParameterAnalyzer(params , rules);  
        to = pa.getIntValue("to", 0);
        from = pa.getIntValue("from");
        white = pa.getIntValue("white", -1);
        perimeter = pa.getBooleanValue("perimeter");
        noStop = pa.getBooleanValue("nostop"); 
        
    }

    public boolean write(long n, Polyform inPoly) {
        //PolyformWC poly = (PolyformWC) inPoly;
        if (perimeter) {
            int value = ((PolyformWC)(inPoly)).perimeterOrSurface();
            if (value >= from && value <= to)
                return innerWrite(count++, inPoly);
            return true;
        }
        
        if (inPoly.logicalSize() >= from && inPoly.logicalSize() <= to)
            return innerWrite(count++, inPoly);
        if (noStop)
            return true;
        return inPoly.logicalSize() <= to;
          
    }     
    public void close() {
        
    }           
  
}

    
 
