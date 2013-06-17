package netp.canvas;

import javax.swing.*;
import java.awt.*;

public class NetpPopupMenu extends JPopupMenu
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void show(Component invoker,int x,int y) {
        super.show(invoker,x,y);
        int nx,ny;
        Point p=getLocationOnScreen();
        Rectangle rt=getBounds();
        Dimension d=getToolkit().getScreenSize();
        nx=x;
        ny=y;
        if((rt.width+p.x)>d.width) nx=nx-(rt.width+p.x)+d.width;
        if((rt.height+p.y)>d.height) ny=ny-(rt.height+p.y)+d.height;
        if((nx!=x)||(ny!=y))
            super.show(invoker,nx,ny);
//            setLocation (nx,ny);
    }
}
