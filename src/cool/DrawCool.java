package cool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawCool extends JPanel implements Runnable{

	public BufferedImage img;
	private int pw,ph,fw,fh;
	private int[] border;
	private boolean focus,leftButton;
	private ColorPalette color;
	
	private int drawWidth,blurWidth,mx,my,alpha;
	
	public DrawCool(){
		pw=ph=500;
		fw=fh=30;
		drawWidth=0;
		blurWidth=0;
		alpha=250;
		border=new int[]{50,50,250,150};
		setPreferredSize(new Dimension(pw+fw+fw,ph+fh+fh));
		img=new BufferedImage(pw,ph,BufferedImage.TYPE_4BYTE_ABGR);
		focus=false;
		leftButton=false;
		addMouseWheelListener(new MouseWheelListener(){
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent me) {
				if(me.isShiftDown()){
					alpha-=me.getWheelRotation()*10;
					if(alpha<0){
						alpha=0;
					}else if(alpha>250){
						alpha=250;
					}
				}else if(me.isControlDown()){
					int old=blurWidth;
					blurWidth-=me.getWheelRotation();
					if(blurWidth<3){
						blurWidth=old<blurWidth?3:0;
					}else if(blurWidth>100){
						blurWidth=100;
					}
				}else{
					
					drawWidth-=me.getWheelRotation();
					if(drawWidth<1){
						drawWidth=0;
					}else if(drawWidth>100){
						drawWidth=100;
					}
				}
				/*
				border[1]+=me.getWheelRotation()*2;
				border[2]-=me.getWheelRotation()*2;
				
				if(border[1]<0){
					border[1]=0;
				}
				
				if(border[1]>250){
					border[1]=250;
				}
				
				if(border[2]<0){
					border[2]=0;
				}
				
				if(border[2]>250){
					border[2]=250;
				}
				repaint();*/
			}
		});
		
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1){
					if(e.isShiftDown()){		
						new Thread(){
							public void run(){
								fill(e.getX(),e.getY());
							}
						}.start();		
					}else if(e.isControlDown()){
						new Thread(){
							public void run(){
								fillAni(e.getX(),e.getY());
							}
						}.start();	
					}else{
						drawLine(e.getX(), e.getY(), e.getX(), e.getY());
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				focus=true;
			}
			@Override
			public void mouseExited(MouseEvent e) {
				focus=false;
			}
			@Override
			public void mousePressed(MouseEvent e) {
				switch(e.getButton()){
				case MouseEvent.BUTTON1:
					leftButton=true;
					break;
				case MouseEvent.BUTTON2:
					img=new BufferedImage(pw,ph,BufferedImage.TYPE_4BYTE_ABGR);
					repaint();
					leftButton=false;
					break;
				case MouseEvent.BUTTON3:
					leftButton=false;
					break;
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});

		addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent me) {
				mx=me.getX();
				my=me.getY();
			}
			@Override
			public void mouseMoved(MouseEvent me) {
				mx=me.getX();
				my=me.getY();
			}
		});
	}
	
	public int getFH(){
		return fh;
	}
	
	public void setColorPalette(ColorPalette color){
		this.color=color;
	}
	
	public boolean isLeftButton(){
		return leftButton;
	}
	
	@Override
	public void paintComponent(Graphics g){
		//g.clearRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(img,fw,fh,null);
		if(focus && drawWidth>0){
			g.setColor(Color.BLACK);
			g.drawOval(mx-drawWidth/2, my-drawWidth/2, drawWidth, drawWidth);
		}
		if(focus && blurWidth>0){
			g.setColor(Color.GRAY);
			g.drawRect(mx-blurWidth/2, my-blurWidth/2, blurWidth-1, blurWidth-1);
		}
		
		g.setColor(new Color(border[0],border[1],border[2],border[3]));
		g.fillRect(0,0,getWidth(),fh);
		g.fillRect(0,fh,fw,getHeight());
		g.fillRect(fw,fh+ph,pw+fw,fh);
		g.fillRect(fw+pw,fh,fw,ph);
		g.setColor(new Color(0,0,0,alpha));
		g.fillRect(0, 0, fw, fh);
		g.fillRect(getWidth()-fw, getHeight()-fh, fw, fh);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		g.drawRect(fw-1, fh-1, getWidth()-(fw+fw-1), getHeight()-(fh+fh-1));
		g.drawLine(0, getHeight()-1, fw-1, getHeight()-fh);
		g.drawLine(getWidth()-1,0, getWidth()-fw,fh-1);
		
	}
	
	public void drawLine(int mx, int my,int oldX,int oldY){
		mx-=fw;
		my-=fh;
		oldX-=fw;
		oldY-=fh;
		
		Graphics g=img.getGraphics();
		g.setColor(color.getSelectedColor(alpha));
		if(blurWidth>0){//either blur or draw, not both
			Color colorAvg[][] = new Color[blurWidth][blurWidth];
			int startX = mx-blurWidth/2 +1;
			int startY = my-blurWidth/2 +1;
			if(startX<1) startX=1;
			if(startY<1) startY=1;
			int tmpWidth=blurWidth;
			int difToRight=img.getWidth()-1-(mx+blurWidth/2);
			int difToBottom=img.getHeight()-1-(my+blurWidth/2);
			difToBottom=Math.min(difToBottom, difToRight);
			if(difToBottom<0){
				tmpWidth+=difToBottom;
			}
			for(int x=0; x<tmpWidth-2; x++){
				for(int y=0; y<tmpWidth-2; y++){
					int avgr = 0; int avgg = 0; int avgb = 0;
					Color c;
					for(int k=0; k<9; k++){
						c = new Color(img.getRGB(startX+x+(k%3)-1, startY+y+(k/3)-1));
						avgr += c.getRed();
						avgg += c.getGreen();
						avgb += c.getBlue();
					}
					colorAvg[x][y] = new Color(avgr/9, avgg/9, avgb/9);
				}
			}
			for(int x=0; x<tmpWidth-2; x++){
				for(int y=0; y<tmpWidth-2; y++){
					g.setColor( colorAvg[x][y] );
					g.drawLine(startX+x, startY+y, startX+x, startY+y);
				}
			}
		}else if(drawWidth>0){
			g.fillOval(mx-drawWidth/2, my-drawWidth/2, drawWidth, drawWidth);
		}else{
			g.drawLine(mx, my, oldX, oldY);
		}
		
		
		repaint();
	}
	
	private static final int[][] fillVecs=new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
	
	public void fill(int mx, int my){
		mx-=fw;
		my-=fh;
		int h=img.getHeight();
		int w=img.getWidth();
		boolean[][] checked=new boolean[h][w];
		ArrayList<int[]> growersOld=new ArrayList<int[]>(1);
		ArrayList<int[]> growersNew=new ArrayList<int[]>(4);

		int argb=img.getRGB(mx,my);
		int b = (argb)&0xFF;
		int g = (argb>>8)&0xFF;
		int r = (argb>>16)&0xFF;
		int a = (argb>>24)&0xFF;
		
		Graphics gr=img.getGraphics();
		gr.setColor(color.getSelectedColor(alpha));
		gr.drawLine(mx, my, mx, my);
		checked[my][mx]=true;
		growersOld.add(new int[]{mx,my});

		int dif,nx,ny;
		while(true){
			for(int[] p:growersOld){
				for(int i=0; i<4;i++){
					nx=p[0]+fillVecs[i][0];
					ny=p[1]+fillVecs[i][1];
					
					if(nx>=0 && nx<w && ny>=0 && ny<h && !checked[ny][nx]){
						checked[ny][nx]=true;
						argb=img.getRGB(nx,ny);	
						dif= Math.abs(b - (argb)&0xFF) + Math.abs(g - (argb>>8)&0xFF) + Math.abs(r - (argb>>16)&0xFF) + Math.abs(a-(argb>>24)&0xFF);
						
						if(dif==0){
							gr.drawLine(nx,ny,nx,ny);
							growersNew.add(new int[]{nx,ny});
						}
					}
				}
			}
			
			growersOld=growersNew;
			growersNew=new ArrayList<int[]>(growersOld.size()*2);
			
			if(growersOld.size()==0){
				break;
			}
		}
		
		repaint();
	}
	
	public void fillAni(int mx, int my){
		mx-=fw;
		my-=fh;
		int h=img.getHeight();
		int w=img.getWidth();
		boolean[][] checked=new boolean[h][w];
		ArrayList<int[]> growersOld=new ArrayList<int[]>(1);
		ArrayList<int[]> growersNew=new ArrayList<int[]>(4);

		int argb=img.getRGB(mx,my);
		int b = (argb)&0xFF;
		int g = (argb>>8)&0xFF;
		int r = (argb>>16)&0xFF;
		int a = (argb>>24)&0xFF;
		
		Graphics gr=img.getGraphics();
		gr.setColor(color.getSelectedColor(alpha));
		gr.drawLine(mx, my, mx, my);
		checked[my][mx]=true;
		growersOld.add(new int[]{mx,my});

		int dif,nx,ny;
		while(true){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			repaint();
			
			for(int[] p:growersOld){
				for(int i=0; i<4;i++){
					nx=p[0]+fillVecs[i][0];
					ny=p[1]+fillVecs[i][1];
					
					if(nx>=0 && nx<w && ny>=0 && ny<h && !checked[ny][nx]){
						checked[ny][nx]=true;
						argb=img.getRGB(nx,ny);	
						dif= Math.abs(b - (argb)&0xFF) + Math.abs(g - (argb>>8)&0xFF) + Math.abs(r - (argb>>16)&0xFF) + Math.abs(a-(argb>>24)&0xFF);
						
						if(dif==0){
							gr.drawLine(nx,ny,nx,ny);
							growersNew.add(new int[]{nx,ny});
						}
					}
				}
			}
			
			growersOld=growersNew;
			growersNew=new ArrayList<int[]>(growersOld.size()*2);
			
			if(growersOld.size()==0){
				break;
			}
		}
		
		repaint();
	}


	@Override
	public void run() {
		//int addC=5;
		//int add=addC;

		int borderIndexU=0;
		int borderIndexD=2;
		int vec=2;
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
			
			/*
			if(focus || border[3]!=100){
				border[3]+=add;
				
				if(border[3]<100){
					border[3]=100;
					add=addC;
				}
				
				if(border[3]>200){
					border[3]=200;
					add=-addC;
				}
				
			}*/

			border[borderIndexU]+=vec;
			border[borderIndexD]-=vec;
			if(border[borderIndexU]==250 || border[borderIndexD]==50){
				borderIndexU++;
				borderIndexD++;
				if(borderIndexU>=3){
					borderIndexU=0;
				}else if(borderIndexD>=3){
					borderIndexD=0;
				}
			}
			//System.out.println(border[0]+" "+border[1]+" "+border[2]);
			
			repaint();
		}
	}
	
}
