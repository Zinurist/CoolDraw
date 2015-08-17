package cool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ColorPalette extends ButtonMenu implements Runnable{

	protected static final int defaultVal=60;
	protected static final int arrowWidth=14;
	
	private static Color[] clist={
		new Color(0,0,0),
		new Color(50,50,50),							
		new Color(100,100,100),
		new Color(150,150,150),
		new Color(200,200,200),
		new Color(250,250,250),
		new Color(250,250,200),
		new Color(250,250,150),
		new Color(250,250,100),
		new Color(250,250,50),
		new Color(250,250,0),
		new Color(250,200,0),
		new Color(250,150,0),
		new Color(250,100,0),
		new Color(250,50,0),
		new Color(250,0,0),
		new Color(200,0,0),
		new Color(150,0,0),
		new Color(100,0,0),
		new Color(50,0,0),
		new Color(0,50,0),
		new Color(0,100,0),
		new Color(0,150,0),
		new Color(0,200,0),
		new Color(0,250,0),
		new Color(0,250,50),
		new Color(0,250,100),
		new Color(0,250,150),
		new Color(0,250,200),
		new Color(0,250,250),
		new Color(0,200,250),
		new Color(0,150,250),
		new Color(0,100,250),
		new Color(0,50,250),
		new Color(0,0,250),
		new Color(0,0,200),
		new Color(0,0,150),
		new Color(100,0,250),
		new Color(150,0,250),
		new Color(200,0,250),
		new Color(250,0,250),
	};
	
	private int sel;
	private int columns,rows;
	private int cWidth;
	private int startY;
	private int realHeight;
	private int xOffset;
	private int maxOffset;
	
	private boolean opened;
	
	public ColorPalette(int height, int fh){
		super(defaultVal,height);
		maxOffset=defaultVal-arrowWidth;
		xOffset=maxOffset;
		opened=false;
		sel=0;
		columns=1;
		cWidth=defaultVal;
		realHeight=height-fh;
		rows=realHeight/defaultVal;
		int fits=rows;
		while(fits<clist.length){
			columns++;
			cWidth=defaultVal/columns;
			rows=realHeight/cWidth;
			fits=rows*columns;
		}
		
		startY=fh;
	}
	
	public Color getSelectedColor(int alpha){
		if(alpha==250){//Color without alpha is so much faster
			return clist[sel];
		}else{
			return new Color(clist[sel].getRed(),clist[sel].getGreen(),clist[sel].getBlue(),alpha);
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		//background
		g.setColor(getColor(210,210,210));
		g.fillRect(xOffset, startY, getWidth(), 1+((1+clist.length)/columns)*cWidth);
		
		int x,y;
		for(int i=0; i<clist.length;i++){
			x=(i%columns)*cWidth    +xOffset;//column*cWidth
			y=startY+(i/columns)*cWidth;//row*cWidth
			
			if(sel==i){
				g.setColor(getColor(0,0,0));
			}else{
				g.setColor(getColor(150,150,150));
			}
			g.drawRect(x+1, y+1, cWidth-2, cWidth-2);
			g.setColor(getColor(clist[i]));
			g.fillRect(x+3,y+3,cWidth-5,cWidth-5);
		}
		
		//the arrow button
		int w=arrowWidth;
		x=xOffset;
		y=startY-w;
		g.setColor(getColor(210,210,210));
		g.fillRect(x, y, w, w);
		x+=2;
		y+=2;
		w-=4;
		g.setColor(getColor(250,250,250));
		if(opened){
			g.fillPolygon(new int[]{x,x,x+w}, new int[]{y,y+w,y+w/2}, 3);
		}else{
			g.fillPolygon(new int[]{x+w,x+w,x}, new int[]{y,y+w,y+w/2}, 3);
		}
		/*
		 * 
		 * //the arrow button
			int w=arrowWidth;
			x=defaultVal-w;
			y=startY-w;
			g.setColor(getColor(0,0,0));
			g.fillRect(x, y, w, w);
			x+=2;
			y+=2;
			w-=4;
			g.setColor(getColor(250,250,250));
		 */
	}

	@Override
	public void click(MouseEvent e) {
		if(opened && e.getY()>=startY){
			int c=e.getX()/cWidth;
			int r=(e.getY()-startY)/cWidth;
			int index=c+r*columns;
			if(index<clist.length){
				sel=index;
				repaint();
			}
		}else if(e.getY()>=startY-defaultVal/2){
			if(opened){
				if(e.getX()<=defaultVal/2){
					new Thread(this).start();
					repaint();
				}
			}else{
				if(e.getX()>=defaultVal/2){
					new Thread(this).start();
					repaint();
				}
			}
		}
	}

	@Override
	public void run() {
		int limit,vec;
		if(xOffset==maxOffset){
			limit=0;
			vec=-1;
		}else{
			limit=maxOffset;
			vec=1;
		}
		int mult=2;
		while(xOffset!=limit){
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {}
			xOffset+=vec*mult;
			if(mult<20){
				mult+=2;
			}
			
			if(xOffset<0){
				xOffset=0;
			}else if(xOffset>maxOffset){
				xOffset=maxOffset;
			}
			repaint();
		}
		opened=xOffset!=maxOffset;
	}
}
