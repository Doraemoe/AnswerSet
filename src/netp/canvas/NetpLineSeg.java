package netp.canvas;
public class NetpLineSeg 
{
    public int startX,startY,endX,endY;
    public int width;
    public NetpLineSeg(int stx,int sty, int endx,int endy) {
        startX=stx;
        startY=sty;
        endX=endx;
        endY=endy;
        width=1;
    }
}
