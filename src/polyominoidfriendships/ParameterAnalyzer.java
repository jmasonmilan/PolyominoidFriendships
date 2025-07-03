/*
 
 */
package polyominoidfriendships;

/**
 *
 * @author mason
 * used to analyze parameters passed to filters and readers
 */
public class ParameterAnalyzer {
    String resultsParameters[];
    String resultValues[];
    String rules[];
    int number = 0;
    public ParameterAnalyzer(String input, String rules[]) {
        this.rules = rules;
        resultsParameters = new String[rules.length];
        resultValues = new String[rules.length];
        while (input.length() > 0)
            input = doOne(input);
    }
    String doOne(String input) {
        int n = input.indexOf(",");
        String param = input;
        if (n >= 0) {
            param = input.substring(0, n);
            input = input.substring(n + 1);
        }
        else
            input = "";
        int m = param.indexOf("=");
        if (m < 0) {
            System.err.println("erroneous \"" + param + "\"");
            System.exit(1);            
        }
        String paramName = param.substring(0, m);
        String paramValue = param.substring(m + 1);
        for (int i = 0; i < rules.length ; i++) {
            if (paramName.equals(rules[i])) {
                resultsParameters[number] = paramName;
                resultValues[number] = paramValue;
                number++; 
                return input;
            }
        }
        System.err.println("erroneous \"" + paramName + "\"");
        String list = "";
        for (int i = 0; i < rules.length ; i++) {
            list += rules[i] + " ";
        }
        System.err.println("looking for one of " + list);
        System.exit(1);
        return null;
    }
    public String getStringValue(String paramName) {
        for (int i = 0 ; i < number; i++) {
            if (paramName.equals(this.resultsParameters[i]))
                return this.resultValues[i];
        }
        return null;
    }
    public String getStringValue(String paramName, boolean obligatory) {
        for (int i = 0 ; i < number; i++) {
            if (paramName.equals(this.resultsParameters[i]))
                return this.resultValues[i];
        }
        if (obligatory) {
            System.err.println("missing " + paramName);
            System.exit(1);            
        }
        return null;
    }
    public int getIntValue(String paramName, int def) {
        String t = getStringValue(paramName);
        if (t == null)
            return def;
        return Integer.parseInt(t);
    }
    public double getDoubleValue(String paramName, double def) {
        String t = getStringValue(paramName);
        if (t == null)
            return def;
        return Double.parseDouble(t);
    }
    public int getIntValue(String paramName) {
        String t = getStringValue(paramName);
        if (t == null) {
            System.err.println("missing " + paramName);
            System.exit(1);            
        }
        return Integer.parseInt(t);
    }
    
    public long getLongValue(String paramName, long def) {
        String t = getStringValue(paramName);
        if (t == null) {
            return def;           
        }
        return Long.parseLong(t);
    }
    
    public boolean getBooleanValue(String paramName) {
        String t = getStringValue(paramName);
        if (t == null || t.equals("false"))
            return false;
        if (t.equals("true"))
            return true;
        System.err.println("erroneous " + t);
        System.exit(1);
        return false;        
    }
    public boolean getBooleanValue(String paramName, boolean def) {
        String t = getStringValue(paramName);
        if (t == null)
            return def;
        if (t.equals("false"))
            return false;
        if (t.equals("true"))
            return true;
        System.err.println("erroneous " + t);
        System.exit(1);
        return false;        
    }
    public Boolean getTrooleanValue(String paramName, boolean obbli) {
        Boolean ret = getTrooleanValue( paramName);
        if (ret == null && obbli) {
            System.err.println("missing " + paramName);
            System.exit(1);            
        }
        return ret;
    }
    public Boolean getTrooleanValue(String paramName) {
        String t = getStringValue(paramName);
        if (t == null)
            return null;
        if (t.equals("false"))
            return Boolean.FALSE;
        if (t.equals("true"))
            return Boolean.TRUE;
        System.err.println("erroneous " + t);
        System.exit(1);
        return false;        
    }
    
}
