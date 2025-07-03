/*
 
 */
package polyominoidfriendships;
import java.io.*;
import java.math.BigInteger;

/**
 *
 * @author jmason
 * abstract class for filters that only read polyforms
 */
public abstract class OnlyPolyFilter extends Filter {
    public OnlyPolyFilter(Filter nextOneParam) {
        super(nextOneParam);
    }
    public boolean write(long n, BigInteger rep) {
        
        usage();
        return true;
          
    }     
             
  
}

    
 
