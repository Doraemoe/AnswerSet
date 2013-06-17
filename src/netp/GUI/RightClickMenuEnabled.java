package netp.GUI;

import java.awt.Point;
import java.awt.Toolkit;


/**
 * This interface is used for a Component can have a pop up menu 
 *
 * @version     1.0, 2001-01-28
 * @since       JDK1.0
 */

public interface RightClickMenuEnabled 
{
    public void receiveCmd(String cmd);
    public Point getLocationOnScreen();
    public Toolkit getToolkit();
}