/*

 */
package polyominoidfriendships;

import java.io.*;
import java.math.BigInteger;
/**
 *
 * @author jmason
 * input polyform, output representation
 */
public class RepFilter extends OnlyPolyFilter {
    
    public RepFilter(Filter nextOneParam) {
        super(nextOneParam);
       
    }

    public boolean write(long n, Polyform inPoly) {
        
        
        return innerWrite(count++, inPoly.getRep());
        
        
          
    }     
    public void close() {
        
    }           
  
}

    
 
