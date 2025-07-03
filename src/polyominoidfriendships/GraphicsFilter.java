
/*

 */
package polyominoidfriendships;

import java.io.*;
import java.math.BigInteger;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;


/**
 *
 * @author jmason
 * build a jpeg of the input polyform; 
 */
public class GraphicsFilter extends OnlyPolyFilter {
    ArrayList<Polyform> array[];
    ArrayList<String> imageFiles;
    String seqName;
    int numHoriz, rows;
    GraphicsParameters gp;
    String enclosedList, form;
    ArrayList<PolyformWC> enclosedArray;
    Color[] colourSet = {Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE};
    int pen, space, vspace;
    boolean bw;
    String imageType;
    boolean html, loop;
    int time;
    int delay;
    /*
    boolean lattice;
    boolean centre, applyCb;
    boolean hAxis, vAxis, swneAxis, senwAxis;
    */
    public GraphicsFilter(Filter nextOneParam, String param) {
        super(nextOneParam);
        int squareSize = 40;
        String[] rules = new String[] {"prefix", "horiz", "lattice", "centre", "multicolour", "trivet", "gif", "delay", "loop",
            "border", "connectedness", "rep", "layer", "reverse", "ring", "showsym", "mec", "pen", "vspace",
            "haxis", "vaxis", "swneaxis", "senwaxis", "applycb", "caption", "form", "yellow", "space", "edge2",
            "round", "filling", "rows", "filename", "suppressinnerborders", "innerswne", "innersenw", "type", "gap", "time",
        "centrex", "centrey", "diameter", "edge", "vmargin", "hmargin", "enclosedlist", "alternative", "bw", "maxy", "html"};
        ParameterAnalyzer pa = new ParameterAnalyzer(param , rules);
        bw = pa.getBooleanValue("bw");
        gp = new GraphicsParameters(bw);
        seqName = pa.getStringValue("prefix", true);
        numHoriz = pa.getIntValue("horiz", 3);
        delay = pa.getIntValue("delay", 5);
        loop = pa.getBooleanValue("loop", false);
        imageType = pa.getStringValue("type", false);
        if (imageType == null)
            imageType = "png";
        gp.lattice = pa.getBooleanValue("lattice");
        gp.makeGif = pa.getBooleanValue("gif");
        gp.mec = pa.getBooleanValue("mec");
        gp.ringFlag = pa.getBooleanValue("ring");
        gp.centre = pa.getBooleanValue("centre");
        gp.layer = pa.getBooleanValue("layer");
        gp.multiColour = pa.getBooleanValue("multicolour");
        gp.caption = pa.getBooleanValue("caption");
        gp.fillYellow = pa.getBooleanValue("yellow");
        gp.gap = pa.getBooleanValue("gap");
        gp.filling = pa.getBooleanValue("filling", true);
        gp.hAxis = pa.getBooleanValue("haxis");
        gp.vAxis = pa.getBooleanValue("vaxis");
        gp.swneAxis = pa.getBooleanValue("swneaxis");
        gp.senwAxis = pa.getBooleanValue("senwaxis");
        gp.rep = pa.getBooleanValue("rep");
        gp.applyCb = pa.getBooleanValue("applycb");
        gp.round = pa.getBooleanValue("round");
        gp.innerSWNE = pa.getBooleanValue("innerswne");
        gp.innerSENW = pa.getBooleanValue("innersenw");
        gp.reverse = pa.getBooleanValue("reverse");
        gp.showSym = pa.getBooleanValue("showsym");
        gp.markers = pa.getBooleanValue("markers");
        gp.border = pa.getBooleanValue("border");
        
        html = pa.getBooleanValue("html", false);
        
        gp.connectedness = pa.getBooleanValue("connectedness");
        gp.trivet = pa.getBooleanValue("trivet");
        gp.suppressInnerBorders = pa.getBooleanValue("suppressinnerborders");
        gp.filename = pa.getStringValue("filename");
        gp.centreX = pa.getDoubleValue("centrex", 0);
        gp.centreY = pa.getDoubleValue("centrey", 0);
        gp.diameter = pa.getDoubleValue("diameter", 0);
        rows = pa.getIntValue("rows", 8);
        gp.vertMargin = pa.getIntValue("vmargin", 20);
        gp.horizMargin = pa.getIntValue("hmargin", 10);
        gp.edgeLength = pa.getIntValue("edge", squareSize);
        gp.edgeLength2 = pa.getIntValue("edge2", gp.edgeLength);
        gp.alternative = pa.getIntValue("alternative", 0);
        gp.maxY = pa.getIntValue("maxy", 0);
        pen = pa.getIntValue("pen", 1);
        array = new ArrayList[1000]; // inelegant but sufficient
        enclosedList = pa.getStringValue("enclosedlist", false);
        form = pa.getStringValue("form", false);
        space = pa.getIntValue("space", 20);
        vspace = pa.getIntValue("vspace", 0);
        time = pa.getIntValue("time", 2);
        imageFiles = new ArrayList<>();
    }

    public boolean write(long n, Polyform poly) {  
        int size = poly.logicalSize();
        
        if (array[size] == null)
            array[size] = new ArrayList<>();
        
        array[size].add(poly);
        
        return innerWrite(count++, poly);          
    }  
    public void close() {
        
        for (int i = 1; i < 1000; i++) {
            if (array[i] != null)
                display1(array[i], i, numHoriz, seqName);
            
        }
        
    }    
    
    private void display1(ArrayList<Polyform> list, int polySize, int numHoriz, String name)  {
        if (rows * numHoriz >= list.size()) {
            display(list,    numHoriz,  name + "-" + polySize);
            return;
        }
        for (int i = 0; i < list.size(); i += (rows * numHoriz)) {
            ArrayList<Polyform> newList = copy(list, i, rows * numHoriz);
            display(newList,   numHoriz,  name + "-" + polySize + "-" + i);
            if (html) {
                String htm = "html\\" + name + "-" + i + ".htm";
                try {
                    if (i < list.size() - 1)
                        whtml(name, name, name , i, i + 1, polySize);
                    else
                        whtml(name, name, name + "b" , i, i - 1, polySize);
                    if (i > 1)
                        whtml(name, name + "b", name + "b" , i, i - 1, polySize);
                    else
                        whtml(name, name + "b", name  , i, i - 1, polySize);
                    
                } catch(Exception e) {
                    
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }
    void whtml(String name, String current, String other, int i, int j, int polySize) throws Exception {
        String htm = "html\\" + current + "-" + i + ".htm";
                    PrintWriter writer = new PrintWriter(htm, "UTF-8");
                    
                    writer.println("<html>");
                    writer.println("<head>");
                    //if (i < list.size() -1 ) {
                        writer.println("<meta http-equiv=\"refresh\" content=\"" + time + ";url=" + other + "-" + (j) + ".htm\" />");
                    //}
                    writer.println("</head>");
                    writer.println("<body>");
                    //writer.println("<audio src=\"..\\midinotes\\a3.mid\" autoplay=\"autoplay\" loop=\"loop\"></audio>");
                    writer.println("<img src=\"..\\png\\" + name + "-" + polySize + "-" + i + "-1.png\" />");
                    writer.println("</body>");
                    writer.println("</html>");

                    writer.close();          
    }
    private ArrayList<Polyform> copy(ArrayList<Polyform> inList, int from, int forMax) {
        ArrayList<Polyform> ret = new ArrayList<>();
        for (int i = 0 ; i < forMax && i + from < inList.size(); i++) {
            ret.add(inList.get(i + from));
        }
        return ret;
    }
    private void display(ArrayList<Polyform> list,  int numHoriz, String name) {
        /*
            vertMargin
            max height in pixel of list[0] list[1]
            vertMargin
            max height in pixel  of list[2] list[3]
            vertMargin
        */
        int heightInPixel[], posnInPixel[], totHeightInPixel;
        
        //int posn[];        
        if (list.size() < numHoriz)
            numHoriz = list.size();  
        
        // how many rows?
        int rows = (list.size() + numHoriz - 1) / numHoriz;
        heightInPixel = new int[rows];
        
        for (int row = 0 ; row < rows ; row++) {
            int maxPH = 0;
            for (int i = row * numHoriz; i < (row + 1) * numHoriz && i < list.size(); i++) {
                int PH = list.get(i).getPixelHeight(gp);
                if (PH > maxPH)
                    maxPH = PH;
            }
            heightInPixel[row] = maxPH;
        }
        posnInPixel = new int[rows];
        posnInPixel[0] = gp.vertMargin;
        for (int row = 1 ; row < rows ; row++) {
            posnInPixel[row] = posnInPixel[row - 1] + heightInPixel[row - 1] + gp.vertMargin + (row ) * vspace;
        }
        totHeightInPixel = posnInPixel[rows - 1] + heightInPixel[rows - 1] + gp.vertMargin + 1;
        

        int maxWidth = 0;
        for (int i = 0 ; i < list.size(); i++) {
            int tw = list.get(i).getPixelWidth(gp);
            if (tw > maxWidth)
                maxWidth = tw;
        }
        
        gp.colWidth = maxWidth + space;
        int w = ((numHoriz * gp.colWidth) - space)  + (2 *  gp.horizMargin) + 1;
        
        BufferedImage image = new BufferedImage(
           w, totHeightInPixel, BufferedImage.TYPE_INT_RGB);
        gp.imageWidth = w;
        gp.imageHeight = totHeightInPixel;
        gp.g = (Graphics2D)image.getGraphics();
        gp.pen = pen;
        gp.g.setStroke(new BasicStroke(pen));
        gp.g.setColor( Color.WHITE );
        gp.g.fillRect( 0, 0, w, totHeightInPixel );
        for (int i = 0 ; i < list.size(); i++) {
        //    gp.py = i / numHoriz;
            gp.vertPixelPosition = posnInPixel[i / numHoriz]; // top of where you should draw
            gp.px = i % numHoriz;
           
            Polyform p = list.get(i);
            if (enclosedArray != null)
                gp.enclosed = enclosedArray.get(i);
            if (gp.multiColour) {
                int tmp = i % numHoriz;
                
                if ((i / numHoriz) % 2 != 0)
                    tmp += 2;
                tmp = tmp % 4;
                gp.colour = colourSet[tmp];
            }
            p.displayOnJpeg(gp);
        }
        try {
            String filename = name  + "-" + numHoriz;
            if (gp.filename != null)
                filename = gp.filename;
            String dest = imageType + "\\" + filename  + "." + imageType;
           
                ImageIO.write(image, imageType, new File(dest));
                imageFiles.add(dest);
        }
        catch (Exception je) {
            System.err.println(je.getMessage());
            je.printStackTrace();
            System.exit(1);
        }  
       
    }
    
    
}
