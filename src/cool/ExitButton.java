package cool;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ExitButton extends ButtonMenu{
	
	public ExitButton(int width){
		super(width,defaultVal);
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(getColor(200,50,50));
		g.fillRect(getWidth()-getHeight(), 0, getHeight(), getHeight());
		g.setColor(getColor(0,0,0));
		g.drawRect(getWidth()-getHeight(), 0, getHeight()-1, getHeight());
	}

	@Override
	public void click(MouseEvent e) {
		if(e.getX()>(getWidth()-getHeight())){
			System.exit(0);
		}
	}
	
	
}

