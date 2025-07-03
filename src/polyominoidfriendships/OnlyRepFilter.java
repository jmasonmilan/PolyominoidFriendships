/*
 
 */
package polyominoidfriendships;

import java.io.*;
import java.math.BigInteger;

/**
 *
 * @author jmason
 * abstract class for filters that only read representations
 */
public abstract class OnlyRepFilter extends Filter {
    public OnlyRepFilter(Filter nextOneParam) {
        super(nextOneParam);
    }
    public boolean write(long n, Polyform poly) {
        
        usage();
        return true;
          
    }     
             
  
}

    
 
