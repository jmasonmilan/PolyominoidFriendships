/*
 
 */
package polyominoidfriendships;


/**
 *
 * @author mason
 * 
 * class for the reader - the first argument of the command line
 */
public abstract class InReader {
    protected Filter nextOne;
    public InReader(Filter in) {
        nextOne = in;
    }
    public abstract boolean write(String s);
    public abstract void close();
    protected void usage() {
        System.err.println("should not be here " + this.getClass().getSimpleName());
        System.exit(1);
    }
    protected void usage(String param) {
        System.err.println("should not be here " + this.getClass().getSimpleName() + " " + param);
        System.exit(1);
    }    
    public void runReader() {
        
    }
}
