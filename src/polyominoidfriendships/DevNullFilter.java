/*
 
 */
package polyominoidfriendships;

import java.math.BigInteger;

/**
 *
 * @author John Mason
 * loses input data
 */
public  class DevNullFilter extends Filter{
    
    public  DevNullFilter(Filter nextOneParam) {

        super(null);
    }
    public  boolean write(long n, BigInteger rep) {return true;}
    public  boolean write(long n, Polyform poly) {return true;}
    public  void close() {}
    protected void memoryFlush() {}
    
}
