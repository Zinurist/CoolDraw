package cool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EmptyWindow extends JFrame{
	
	private DrawCool p;
	private ExitButton exit;
	private ColorPalette color;
	private JPanel contentPane;
	
	public EmptyWindow(DrawCool panel){
		setUndecorated(true);
		setAlwaysOnTop(true);
		//setBackground(new Color(0, true));
		setBackground(new Color(250,250,250,1));
		
		p=panel;
		p.setOpaque(false);
		p.addMouseMotionListener(new MouseMotionListener() {
			private int xOld,yOld,xOldP,yOldP;
			@Override
			public void mouseMoved(MouseEvent me) {
				xOld=me.getXOnScreen();
				yOld=me.getYOnScreen();
				xOldP=me.getX();
				yOldP=me.getY();
				
			}
			@Override
			public void mouseDragged(MouseEvent me) {
				if(p.isLeftButton()){
					if(!me.isShiftDown()){
						p.drawLine(me.getX(),me.getY(),xOldP,yOldP);
					}
				}else{
					addPos(me.getXOnScreen()-xOld,me.getYOnScreen()-yOld);
				}
				xOld=me.getXOnScreen();
				yOld=me.getYOnScreen();
				xOldP=me.getX();
				yOldP=me.getY();
			}
		});
		
		color=new ColorPalette(p.getPreferredSize().height,p.getFH());
		color.setOpaque(false);
		p.setColorPalette(color);
		
		exit=new ExitButton(p.getPreferredSize().width);
		exit.setOpaque(false);
		
		contentPane=new JPanel(new BorderLayout());
		contentPane.setOpaque(false);
		contentPane.add(exit,BorderLayout.NORTH);
		contentPane.add(color,BorderLayout.WEST);
		contentPane.add(p,BorderLayout.CENTER);
		setContentPane(contentPane);
		pack();
		
		setVisible(true);
	}
	
	public void addPos(int xc, int yc){
		setLocation(getLocationOnScreen().x+xc, getLocationOnScreen().y+yc);
	}
}
