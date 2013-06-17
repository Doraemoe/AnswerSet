package netp.GUI;

import java.awt.*;

import javax.swing.*;

public class NetpSplash {
    static SplashFrame splashFrame=null;

    static public void ShowSplash(String imgFileName) {
        splashFrame = new SplashFrame();
        splashFrame.setImageName(imgFileName);
        splashFrame.setVisible(true);
/*
        try {
            Thread.sleep(50);
        }
        catch (InterruptedException e) {
            System.out.println("Error:"+e.toString());
        }
*/
    }


    static public void HideSplash() {
        if(splashFrame==null) return;
        splashFrame.dispose();
    }

}
class SplashFrame extends JWindow {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image img;
    int w,h;
    public void setImageName(String name) {
//        setBackground(new Color(0,0,0,0));
        img=getToolkit().getImage(name);
        prepareImage(img,this);
        w=0;h=0;
        setBounds(0,0,1,1);
    }
    public void paint (Graphics g) {
        int sW,sH;
        if(w<=0) {
            w=img.getWidth(this);
            h=img.getHeight(this);
            Dimension screenSize = getToolkit().getScreenSize();
            sW=screenSize.width;
            sH=screenSize.height;
//            System.out.println("sW:"+sW+"sH:"+sH+"w:"+w+"h:"+h);
		    setBounds ( (sW-w)/2, (sH-h)/2, w, h );
            toFront();
        }
        g.drawImage(img,0,0,this);

    }
    public void update(Graphics g) {
        paint(g);
    }

}