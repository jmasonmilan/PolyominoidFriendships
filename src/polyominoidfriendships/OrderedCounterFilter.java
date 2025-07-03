/*
 
 */
package polyominoidfriendships;

import java.io.*;
import java.math.BigInteger;

/**
 *
 * @author jmason
 * this will count input polyforms, according to size, but requires they are ordered according to size
 */
public class OrderedCounterFilter extends OnlyPolyFilter {
    int currentSize = 0;
    long currentCount = 0;
    int writtenSize = -1;
    public OrderedCounterFilter(Filter nextOneParam) {
        super(nextOneParam);
    }
       

    public boolean write(long n, Polyform inPoly) {
        //PolyformWC poly = (PolyformWC) inPoly;
        boolean ret = true;
        if (inPoly.logicalSize() == currentSize)
            currentCount++;
        else {
            if (currentCount > 0) {
                flush();
                ret = innerWrite(currentSize, new BigInteger("" + currentCount));
                writtenSize = currentSize;
            }
            currentSize = inPoly.logicalSize();
            currentCount = 1;
        }
        return ret;               
    }  
    public void close() {
        flush();
        nextOne.write(currentSize, new BigInteger("" + currentCount));
    }     
    void flush() {
        while (writtenSize < currentSize - 1) {
            for (int i = writtenSize + 1 ; i < currentSize; i++) 
                nextOne.write(i, new BigInteger("0"));
            writtenSize = currentSize - 1;
        }
    }
}
