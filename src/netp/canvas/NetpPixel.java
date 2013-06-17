package netp.canvas;

import java.awt.Graphics;
import java.awt.Point;

public class NetpPixel extends NetpCanvasObject
{
    public NetpPixel() {
        super();
        type="pixel";
    }

    public void draw(Graphics g)
    {
        int x,y;
        x=getDrawX(p0.x);
        y=getDrawY(p0.y);
        g.setColor(getColor());
//        if(m_par.m_zoom==1) 
            g.fillRect(x-1,y-1,2,2);
//        else
//            g.fillRect(x,y,1,1);
        if(isSelected) 
        {
            g.drawRect(x-3,y-3,5,5);
        }
    }

    public int toPointDistance(Point po)
    {
        Point p=getOriPoint(po);
        return (int) NetpCanvasGeom.ptToPt(p.x,p.y,p0.x,p0.y);
    }
}

