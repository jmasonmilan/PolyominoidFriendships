package polyominoidfriendships;

/*
 
 */


import java.io.*;
import java.math.BigInteger;
import java.util.*;
/**
 *
 * @author mason
 * 
 * interpret the command line and satisfy the request
 */
public class Runner {
    ArrayList<Filter> filterList ;
    InReader firstOne = null;
    /**
     * @param args the command line arguments
     
     */
    public  Runner(String[] args)  {
        //Runner.printArgs("Runner", args);
        if (args.length < 2)
            usage("too few arguments");
        int n;
        run(args);
    }
    void run(String args[])  {
        InputStream ins = null;
        boolean closed = false;
        Filter lastOne = new DevNullFilter(null);
        Filter nextOne;
        String fileArray[] = null;
        int currentFile = 0;
        int n;        
        filterList = new ArrayList<Filter>();
        String fileList = args[args.length - 1];
        if (!fileList.equals("none")) {       
            fileArray = fileList.split("\\+");
            try {
                ins = new FileInputStream(fileArray[0]);
            } catch (IOException e) {
                System.err.println("An error occurred opening input " + fileArray[0]);
                //e.printStackTrace();
                System.exit(1);
            }         
        }
        
        for (int i = args.length - 2 ; i > 0 ; i--) {
            if ((n = testCMD(args[i],"ocount")) > 0) {
                nextOne = new OrderedCounterFilter(lastOne);
                filterList.add(nextOne);
                lastOne = nextOne; 
            } else if ((n = testCMD(args[i],"componentfriends"))  > 0) {
                String in = args[i].substring(n); 
                nextOne = new ComponentFriendshipsFilter(lastOne, in);
                filterList.add(nextOne);
                lastOne = nextOne;
            } else if ((n = testCMD(args[i],"repToForm")) > 0) {
                nextOne = new RepToFormFilter(lastOne, args[i].substring(n));
                filterList.add(nextOne);
                lastOne = nextOne;
            } else if ((n = testCMD(args[i],"graphics"))  > 0) {
                String in = args[i].substring(n);                 
                nextOne = new GraphicsFilter(lastOne, in );
                filterList.add(nextOne);
                lastOne = nextOne;               
            } else if ((n = testCMD(args[i],"lines")) > 0) {
                String in = args[i].substring(n);                              
                nextOne = new LinesFilter(lastOne, in);
                filterList.add(nextOne);
                lastOne = nextOne;                           
            } else if ((n = testCMD(args[0],"topology"))    > 0) {
                String in = args[0].substring(n);
                PolyominoidTopologyReader counter = new PolyominoidTopologyReader(lastOne, in);    

                firstOne = counter;
            } else if ((n = testCMD(args[i],"stdout")) > 0) {
                String in = args[i].substring(n);
                nextOne = new StdoutFilter(lastOne, in);
                filterList.add(nextOne);
                lastOne = nextOne;
            } else if ((n = testCMD(args[i],"polyominoidtopology"))  > 0) {
                String in = args[i].substring(n);                 
                nextOne = new PolyominoidTopologyFilter(lastOne, in );
                filterList.add(nextOne);
                lastOne = nextOne;               
            }   else {                
                usage("found unknown : " + args[i]);
            }

            // 
        }
        if ((n = testCMD(args[0],"stdinToRep"))   > 0) {
            String in = args[0].substring(n);
            firstOne = new StdinToRepReader(lastOne, in);                                
        }  else
            usage("unknown first parameter");       
        if (ins != null)
            out: while (true) {
                try {
                    BufferedReader b = new BufferedReader(new  InputStreamReader(ins));
                    for(String s = null; (s = b.readLine()) != null;){

                        boolean continueFlag = firstOne.write(s);
                        if (!continueFlag) {
                            close();
                            closed = true;
                            break out;
                        }  

                    }
                    
                    currentFile++;
                    if (currentFile >= fileArray.length)
                        break out;
                    ins.close();
                    ins = new FileInputStream(fileArray[currentFile]);
                    
                } catch (IOException e) {
                    System.err.println("An error occurred opening input " + fileArray[currentFile]);
                    //e.printStackTrace();
                    System.exit(1);
                }  
            }
        if (!closed)
            close();
    }
    int testCMD(String arg, String cmd) {
        if (arg.equals(cmd))
            return arg.length();
        if (arg.startsWith(cmd + ":"))
            return cmd.length() + 1;
        return 0;
    }
    void usage(String error) {
        System.err.println(error);
       
        System.exit(1);
    }
    void close() {
        if (firstOne != null)
            firstOne.close();
        for (int i = filterList.size() - 1; i >= 0; i--) 
            filterList.get(i).close();        
        
    }    
    public static void printArgs(String place, String args[]) {
        String cmd = "";
        for (int i = 0 ; i < args.length ; i++) 
            cmd += args[i] + " ";
        System.err.println(place + " : " + cmd);
    }
    public static void notImplemented(String place) {
        System.err.println(place + " not implemented");
        System.exit(1);
    }
}
