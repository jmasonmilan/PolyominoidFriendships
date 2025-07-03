/*
 
 */
package polyominoidfriendships;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mason
 * 
 * read a file and pass the input lines to the first filter
 */
public class StdinToRepReader extends InReader {
    long count;
    ParameterAnalyzer pa;
    boolean nolineno;
    
    public StdinToRepReader(Filter in, String params)   {
        super(in);
        String[] rules = new String[] {"nolineno", "starter"};
        pa = new ParameterAnalyzer(params , rules); 
        nolineno = pa.getBooleanValue("nolineno", false);
        count = pa.getLongValue("starter", 1) - 1;
    }
    public boolean write(String s) {
        if (s.length() == 0)
            return true;
        if (s.startsWith("#"))
            return true;
        int p = s.indexOf(" ");
        BigInteger rep;
        if (s.substring(s.length() - 1).equals(" "))
            s = s.substring(0, s.length() - 1);
        if (nolineno)
            rep = new BigInteger(s);
        else
            rep = new BigInteger(s.substring(p + 1));
        count++;
        if (count % 100000 == 0)
            System.err.println(count + " " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        if (nolineno)
            return nextOne.write(count, rep);      
        else
            return nextOne.write(Integer.parseInt(s.substring(0, p)), rep);        
    }
    public void close() {
        
    }    
}
