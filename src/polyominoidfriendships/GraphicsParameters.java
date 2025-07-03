/*
 
 */
package polyominoidfriendships;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 *
 * @author mason
 * used to pass parameters between polyform and graphics filter
 */
public class GraphicsParameters {
    public Graphics2D g;
    public int px, 
            //py, 
            edgeLength, edgeLength2,
            vertPixelPosition, imageWidth, imageHeight;
    public int vertMargin ,  horizMargin , colWidth, maxY, pen;
    public boolean lattice, border, markers, layer, innerSWNE, innerSENW, multiColour, makeGif;
    public boolean centre, applyCb, caption, round, filling, rep, reverse, showSym, mec, gap;
    public boolean hAxis, vAxis, swneAxis, senwAxis, connectedness, suppressInnerBorders, trivet;
    public String filename;
    public Polyomino ring;
    public boolean ringFlag, fillYellow;
    public double centreX, centreY, diameter; 
    public PolyformWC enclosed;
    public int alternative;
    public Color colour;
    public Color green, red, yellow, blue;
    public GraphicsParameters(boolean bw) {
        if (bw) {
            green = Color.GRAY;
            yellow = Color.LIGHT_GRAY;
            red = Color.DARK_GRAY;        
            blue = new Color(51, 51, 51);
        } else {
            green = Color.GREEN;
            yellow = Color.YELLOW;
            red = Color.RED;
            blue = Color.BLUE;
        }        
    }
}
