package cool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ButtonMenu extends JPanel{

	protected static final int defaultVal=20;
	
	private boolean ImIn;
	
	public ButtonMenu(int width,int height){
		ImIn=false;
		setPreferredSize(new Dimension(width,height));
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				click(e);
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				setImIn(false);
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}		
		});
		addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent arg0) {}
			@Override
			public void mouseMoved(MouseEvent me) {
				setImIn(me.getX()>(getWidth()-getHeight()));
			}
		});
	}
	
	private void setImIn(boolean i){
		this.ImIn=i;
		repaint();
	}
	
	protected int getAlpha(){
		return ImIn?250:50;
	}
	
	protected Color getColor(int r, int g, int b){
		return new Color(r,g,b,ImIn?250:50);
	}
	
	protected Color getColor(Color c){
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),ImIn?250:50);
	}
	
	public abstract void click(MouseEvent e);
	
}
