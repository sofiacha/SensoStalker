/*
 * Copyright (c) 2006 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
  import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.io.BufferedReader;
//mport java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;;
import java.sql.*;
/* Panel for drawing mote-data graphs */
class Graph extends JPanel
{
    final static int BORDER_LEFT = 40;
    final static int BORDER_RIGHT = 0;
    final static int BORDER_TOP = 10;
    final static int BORDER_BOTTOM = 10;

    final static int TICK_SPACING = 40;
    final static int MAX_TICKS = 16;
    final static int TICK_WIDTH = 10;

    final static int MIN_WIDTH = 50;

    int gx0, gx1, gy0, gy1; // graph bounds
    int scale = 2; // gx1 - gx0 == MIN_WIDTH << scale
	int count = 0;
	int cou1 = 0;
	int cou2 = 0;
	int cou3 = 0;
	int count1 = 0;
	int count2 = 0;
	double temp1 = 0;
	int tempo1 = 0;
	double temp2 = 0;
	int tempo2 = 0 ;
	double temp3 = 0;
    Window parent;

    /* Graph to screen coordinate conversion support */
    int height, width;
    double xscale, yscale;

    void updateConversion() {
    height = getHeight() - BORDER_TOP - BORDER_BOTTOM;
    width = getWidth() - BORDER_LEFT - BORDER_RIGHT;
    if (height < 1) {
        height = 1;
    }
    if (width < 1) {
        width = 1;
    }
    xscale = (double)width / (gx1 - gx0 + 1);
    yscale = (double)height / (gy1 - gy0 + 1);
    }

    Graphics makeClip(Graphics g) {
    return g.create(BORDER_LEFT, BORDER_TOP, width, height);
    }

    // Note that these do not include the border offset!
    int screenX(int gx) {
    return (int)(xscale * (gx - gx0) + 0.5);
    }

    int screenY(int gy) {
    return (int)(height - yscale * (gy - gy0));
    }

    int graphX(int sx) {
    return (int)(sx / xscale + gx0 + 0.5);
    }

    Graph(Window parent) {
    this.parent = parent;
    gy0 = 0; gy1 = 0xffff;
    gx0 = 0; gx1 = MIN_WIDTH << scale;

    }

    void rightDrawString(
            Graphics2D g, 
            String s, 
            int x, 
            int y) {
    TextLayout layout =
        new TextLayout(s, parent.smallFont, g.getFontRenderContext());
    Rectangle2D bounds = layout.getBounds();
    layout.draw(g, x - (float)bounds.getWidth(), y + (float)bounds.getHeight() / 2);
    }

    protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;

    /* Repaint. Synchronize on Oscilloscope to avoid data changing.
       Simply clear panel, draw Y axis and all the mote graphs. */
    synchronized (parent.parent) {
        updateConversion();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        drawYAxis(g2d);

        Graphics clipped = makeClip(g2d);
        int count = parent.moteListModel.size();
        for (int i = 0; i < count; i++) {
        clipped.setColor(parent.moteListModel.getColor(i));
        drawGraph(clipped, parent.moteListModel.get(i));
        }
    }
    }

    /* Draw the Y-axis */
    protected void drawYAxis(Graphics2D g) {
    int axis_x = BORDER_LEFT - 1;
    int height = getHeight() - BORDER_BOTTOM - BORDER_TOP;

    g.setColor(Color.WHITE);
    g.drawLine(axis_x, BORDER_TOP, axis_x, BORDER_TOP + height - 1);

    /* Draw a reasonable set of tick marks */
    int nTicks = height / TICK_SPACING;
    if (nTicks > MAX_TICKS) {
        nTicks = MAX_TICKS;
    }

    int tickInterval = (gy1 - gy0 + 1) / nTicks;
    if (tickInterval == 0) {
        tickInterval = 1;
    }

    /* Tick interval should be of the family A * 10^B,
       where A = 1, 2 * or 5. We tend more to rounding A up, to reduce
       rather than increase the number of ticks. */
    int B = (int)(Math.log(tickInterval) / Math.log(10));
    int A = (int)(tickInterval / Math.pow(10, B) + 0.5);
    if (A > 2) {
        A = 5;
    } else if (A > 5) {
        A = 10;
    }

    tickInterval = A * (int)Math.pow(10, B);

    /* Ticks are printed at multiples of tickInterval */
    int tick = ((gy0 + tickInterval - 1) / tickInterval) * tickInterval;
    while (tick <= gy1) {
        int stick = screenY(tick) + BORDER_TOP;
        rightDrawString(g, "" + tick, axis_x - TICK_WIDTH / 2 - 2, stick);
        g.drawLine(axis_x - TICK_WIDTH / 2, stick,
               axis_x - TICK_WIDTH / 2 + TICK_WIDTH, stick);
        tick += tickInterval;
    }
    
    }

    /* Draw graph for mote nodeId */
    protected void drawGraph(Graphics g, int nodeId) {
    SingleGraph sg = new SingleGraph(g, nodeId);
	
   if (gx1 - gx0 >= width) {
        for (int sx = 0; sx < width; sx++)
        sg.nextPoint(g, graphX(sx), sx);
    } else {
        for (int gx = gx0; gx <= gx1; gx++)
        sg.nextPoint(g, gx, screenX(gx));
    } 
    }

    /* Inner class to simplify drawing a graph. Simplify initialise it, then
       feed it the X screen and graph coordinates, from left to right. */
    private class SingleGraph {
    int lastsx, lastsy, nodeId;

    /* Start drawing the graph mote id */
    SingleGraph(Graphics g, int id) {
        nodeId = id;
        lastsx = -1;
        lastsy = -1;
    }

    /* Next point in mote's graph is at x value gx, screen coordinate sx */
    void nextPoint(Graphics g, int gx, int sx) {
        int gy = parent.parent.data.getData(nodeId, gx);
        int sy = -1;
	
        if (gy >= 0) { // Ignore missing values
        double rsy = height - yscale * (gy - gy0);
	//connectl(gy);

	if (count%10 == 0){	
		//temp1 = 2.5 * (((double)gy) / 4096.0) * 6250.0;
		tempo1 = (int)temp1;
		 connectl(gy);
		//connectl(tempo1);
		 temp1=0;
		 tempo1 =0;
		count++;
		cou1++;
	}
	else if (count%10 == 1){
		temp1 = (-39.60 + 0.01 *((double)gy));
		tempo1 = Math.abs((int)temp1);
		 connectt(tempo1);
		count++;
		cou2++;
	}
	else{
		temp2 = temp2 +((double)gy) ;
			if (count%10 == 9){
				temp2=temp2/8;
				 temp3 = -4 + 0.0405*temp2 + (-2.8 * Math.pow(10,-6) * Math.pow(temp2,2) );
				temp3 = (temp1 - 25) * (0.01 + 0.00008*temp2) + temp3;
				tempo2 = (int)temp3;
				connecth(tempo2);
				temp3 = 0;
				temp2 = 0;
				temp1 = 0;
				tempo2 = 0;
				tempo1 = 0;
				cou3++;
			}
		count++;
	}
	
	if ((cou2%240 == 0) && (cou2 != 0)){
		if (count1 % 10 == 0) {
		call_tempm();
		 }
		if (count1% 520 == 0 ){
		call_aver_temp();
		}
		count1++;
	}

	if ((cou3%240 == 0) && (cou3 != 0)){
		if (count2 % 10 == 0) {
		 call_humm();
		 }
		if (count2% 520 == 0 ){
		call_aver_hum();
		}
		count2++;
		
	}

	
        // Ignore problem values
        if (rsy >= -1e6 && rsy <= 1e6) {
            sy = (int)(rsy + 0.5);
        }

        if (lastsy >= 0 && sy >= 0) {
            g.drawLine(lastsx, lastsy, sx, sy);
        }
        }
        lastsx = sx;
        lastsy = sy;
    }
    }


public static void call_tempm(){
try {
		//150.140.210.194   192.168.196.44      192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/tempm.php?temp=0";
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}


public static void call_humm(){
try {
		//150.140.210.160   192.168.196.44      192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/humm.php?hum=0";
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}


public static void call_aver_temp(){
try {
		//150.140.210.160   192.168.196.44      192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/avrgvrnc.php?temp=0";
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}

public static void call_aver_hum(){
try {
		//150.140.210.160   192.168.196.44      192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/avrghum.php?hum=0";
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}


public static void connectl(int gyx){
	try {
		//150.140.210.160   192.168.196.44      192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/islum.php?lighth=" + Integer.toString(gyx);
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}

public static void connectt(int gyx){
	try {
		//150.140.210.160         192.168.2.147
		String urlL = "http://150.140.210.194/sensostalker/istemp.php?temp=" + Integer.toString(gyx);
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		} 
}

public static void connecth(int gyx){
	try {
		//150.140.210.160
		String urlL = "http://150.140.210.194/sensostalker/ishum.php?hum=" + Integer.toString(gyx);
		URL url = new URL(urlL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = in.readLine()) != null){
			sb.append(line);
		}
		in.close();
		System.out.println(sb.toString());
		}
		catch ( Exception e ){
		System.out.println("Database not connected");
		System.err.println("Got an exception! ");
		System.err.println(e.getMessage());
		}
}

    /* Update X-axis range in GUI */
    void updateXLabel() {
    parent.xLabel.setText("X: " + gx0 + " - " + gx1);
    }

    /* Ensure that graph is nicely positioned on screen. max is the largest 
       sample number received from any mote. */
    private void recenter(int max) {
    // New data will show up at the 3/4 point
    // The 2nd term ensures that gx1 will be >= max
    int scrollby = ((gx1 - gx0) >> 2) + (max - gx1);
    gx0 += scrollby;
    gx1 += scrollby;
    if (gx0 < 0) { // don't bother showing negative sample numbers
        gx1 -= gx0;
        gx0 = 0;
    }
    updateXLabel();
    }

    /* New data received. Redraw graph, scrolling if necessary */
    void newData() {
    int max = parent.parent.data.maxX();

    if (max > gx1 || max < gx0) {
        recenter(max);
    }
    repaint();
    }

    /* User set the X-axis scale to newScale */
    void setScale(int newScale) {
    gx1 = gx0 + (MIN_WIDTH << newScale);
    scale = newScale;
    recenter(parent.parent.data.maxX());
    repaint();
    }

    /* User attempted to set Y-axis range to newy0..newy1. Refuse bogus
       values (return false), or accept, redraw and return true. */
    boolean setYAxis(int newy0, int newy1) {
    if (newy0 >= newy1 || newy0 < 0 || newy0 > 65535 ||
        newy1 < 0 || newy1 > 65535) {
        return false;
    }
    gy0 = newy0;
    gy1 = newy1;
    repaint();
    return true;
    }
}
