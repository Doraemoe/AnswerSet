package netp.canvas;

import java.awt.Point;
import java.awt.Rectangle;
public class NetpCanvasGeom
{
    public static double ptToPt(Point p1,Point p2)
    {
        return NetpCanvasGeom.ptToPt(p1.x,p1.y,p2.x,p2.y);
    }
    public static double ptToPt(Point p, int x, int y)
    {
        return NetpCanvasGeom.ptToPt(p.x,p.y,x,y);
    }
    public static double ptToPt(int x1,int y1,int x2,int y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public static double ptToSeg(int x1,int y1,int x2,int y2,Point p)
    {
        return NetpCanvasGeom.ptToSeg(x1,y1,x2,y2,p.x,p.y);
    }
    public static double ptToSeg(Point p1,Point p2,Point p)
    {
        return NetpCanvasGeom.ptToSeg(p1.x,p1.y,p2.x,p2.y,p.x,p.y);
    }
    public static double ptToSeg(int x1,int y1, int x2,int y2,int x,int y)
    {
        double d1, d2, d;

        d = (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
        d1 = (x1-x)*(x1-x)+(y1-y)*(y1-y);
        if((d == 0))return Math.sqrt(d1);
        d2 = (x-x2)*(x-x2)+(y-y2)*(y-y2);

        if((d+d2)<=d1) return Math.sqrt(d2);
        if((d+d1)<=d2) return Math.sqrt(d1);


        return Math.abs(((double)((x-x1) * (y2-y1)-(y-y1) * (x2-x1))) / Math.sqrt(d));
    }

    public static double ptToFilledRect(int x,int y, int x0,int y0,int x2, int y2)
    {
		if(((y2-y)*(y-y0)>=0)&&((x2-x)*(x-x0)>=0)) return 0.0;
		double d0=10000;
		double d;
        d= NetpCanvasGeom.ptToSeg(x0,y0, x0, y2,x,y);
		if(d<d0) d0=d;
        d= NetpCanvasGeom.ptToSeg(x0,y0, x2, y0,x,y);
		if(d<d0) d0=d;
        d= NetpCanvasGeom.ptToSeg(x2,y2, x2, y0,x,y);
		if(d<d0) d0=d;
        d= NetpCanvasGeom.ptToSeg(x2,y2, x0, y2,x,y);
		if(d<d0) d0=d;
		return d0;

    }

    public static double ptToFilledRect(Point p,Rectangle rect)
    {
		int x0=rect.x,y0=rect.y,x=p.x,y=p.y,x2=rect.x+rect.width,y2=rect.y+rect.height;
        return ptToFilledRect(x,y,x0,y0,x2,y2);
    }



    public static Point findUniformPoint(Point p0,Point p1,int len)
    {
        double d= ptToPt(p0,p1);
        if(d==0) return p0;
        int x,y;

        x=(int)(1.0*(p0.x+((p1.x-p0.x)*(d-len)/d)));
        y=(int) (1.0*(p0.y+((p1.y-p0.y)*(d-len)/d)));
        return new Point(x,y);
    }
    public static double findUniformPointX(Point p0,Point p1,int len) {
        double d= ptToPt(p0,p1);
        if(d==0) return p0.x;
        return p0.x + (d-len)* (p1.x-p0.x)/d;
    }

    public static double findUniformPointY(Point p0,Point p1,int len) {
        double d= ptToPt(p0,p1);
        if(d==0) return p0.y;
        return p0.y + (d-len)* (p1.y-p0.y)/d;
    }

	public static boolean hasCross(Point p0,Point p1,Point p2, Point p3){
		int d0=getDirectionValue(p0,p1,p2);
		int d1=getDirectionValue(p1,p2,p3);
		int d2=getDirectionValue(p2,p3,p0);
		int d3=getDirectionValue(p3,p0,p1);
//		if((d0==0)||(d1==0)||(d2==0)||(d3==0)) // dont' consider this status yet
		if((d0>0)&&(d1>0)&&(d2>0)&&(d3>0)) return true;
		if((d0<0)&&(d1<0)&&(d2<0)&&(d3<0)) return true;
		return false;
	}
	public static int getDirectionValue(Point p0, Point p1, Point p2) {
		int x0=p1.x-p0.x;
		int y0=p1.y-p0.y;
		int x1=p2.x-p1.x;
		int y1=p2.y-p1.y;
		return x0*y1-x1*y0;
	}
}
