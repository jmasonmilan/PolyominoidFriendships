/*
 
 */
package polyominoidfriendships;


import java.io.*;
import java.math.BigInteger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
/**
 *
 * @author jmason
 * convert representation to polyform
 */
public  class RepToFormFilter extends RepFilter {
    String form;
    boolean multi;
    int[] ss;
    public RepToFormFilter(Filter nextOneParam, String params) {
        super(nextOneParam);
        String[] rules = new String[] {"form", "sizeset", "multi"};
        ParameterAnalyzer pa = new ParameterAnalyzer(params , rules);
        this.form = pa.getStringValue("form", true);
        this.multi = pa.getBooleanValue("multi");
        String sss = pa.getStringValue("sizeset");
        if (sss != null) {
            String ssss[] = sss.split("/");
            ss = new int[ssss.length];
            for (int i = 0; i < ssss.length; i++) {
                ss[i] = Integer.parseInt(ssss[i]);
            }
        }
    }
    public boolean write(long n, BigInteger rep) {
        Polyform poly = null;
        if (rep.toString().equals("1144056238563172736")) {
            int dbgg = 1;
        }
        
        Object object = null;
        try {
            Class<?> c = Class.forName("polyominoidfriendships." + form);
            Constructor<?> cons;
            if (ss == null) {
                cons = c.getConstructor(BigInteger.class);
                object = cons.newInstance(rep); 
            }
            else {
                cons = c.getConstructor(BigInteger.class, int[].class);
                object = cons.newInstance(rep, ss); 
            }
              
        } catch (Exception e) {
            System.err.println("Could not create instance of " + form);
            System.exit(1);
        }
        Polyform p = (Polyform)object;
        if (multi) {
            p = p.multiMaker();
        }
        if (rep.toString().equals("1144056238563172736")) {
            int dbg = 1;
        }
        return nextOne.write(count++, p);
          
    }     
             
    public void close() {
        
    }    
}

    
 
