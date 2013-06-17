package netp.absshape;
import java.awt.Point;
import java.util.Vector;

public class AbsPolygon extends AbsShape
{
    public Vector v;
    public AbsPolygon() {
        super();
        v=new Vector();
    }
    public void upsideDown(int y0) // 
    {
        int i,s=v.size();
        Point p;
        for(i=0;i<s;++i) {
            p=(Point )v.elementAt(i);
            p.y=2*y0-p.y;
        }
    }
    public void shift(int dx,int dy) {
        int i,s=v.size();
        Point p;
        for(i=0;i<s;++i) {
            p=(Point )v.elementAt(i);
            p.translate(dx,dy);
        }
    }
    public void addPoint(int x,int y) {
        Point p=new Point (x,y);
        v.addElement(p);
    }
}