package netp.GUI;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;

public class ColorDialog extends Dialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FixColorCanvas canvas;
	SlidBar sb;
	CDLabel lr,lg,lb,lh,ls,lbr;
	TextField tr,tg,tb,th,ts,tbr;
	Button bok,bcancel;
//	SmallColorCanvas sold,snew;
	ColorText sold,snew;
	Color bkcolor=new Color(192,192,192);
	int hue,sat,lum=64,cr,cg,cb;
	RGBtoHSL rgbtohsl = new RGBtoHSL(32,128,128);
	RGBkeyInput rgbinp;
	int r_old,g_old,b_old;
	boolean normalRGBchange=true;

	Panel pup,pdown;

	ColorChangable master;
	Frame par;
	public ColorDialog(Frame parent,Color currentColor,ColorChangable master) 
        {
          super(parent,"Select a color:",true);
          r_old=currentColor.getRed();
          g_old=currentColor.getGreen();
          b_old=currentColor.getBlue();
          init();
          this.master=master;
          pack();
          addWindowListener(new WindowEventHandler());
          par=parent;
	}

	public void initControl() 
        {
          GridLayout gl=new GridLayout(4,4);
          gl.setHgap(5);
          gl.setVgap(10);
          pdown.setLayout(gl);

          lh=new CDLabel("Hue:");
          ls=new CDLabel("Sat:");
          lbr=new CDLabel("Lum:");
          th=new TextField(""+hue,3);
          ts=new TextField(""+sat,3);
          tbr=new TextField(""+lum,3);

          lr=new CDLabel("Red:");
          lg=new CDLabel("Green:");
          lb=new CDLabel("Blue:");
          tr=new TextField(""+r_old,3);
          tg=new TextField(""+g_old,3);
          tb=new TextField(""+b_old,3);
          rgbinp=new RGBkeyInput(this);
          tr.addTextListener(rgbinp);
          tg.addTextListener(rgbinp);
          tb.addTextListener(rgbinp);

          sold=new ColorText(r_old,g_old,b_old);
          snew=new ColorText(r_old,g_old,b_old);
          sold.addMouseListener(new RestoreColor());
          bok=new Button("Ok");
          bcancel=new Button("Cancel");

          bok.addActionListener(this);
          bcancel.addActionListener(this);
          pdown.add(lr);pdown.add(tr);pdown.add(lh);pdown.add(th);
          pdown.add(lg);pdown.add(tg);pdown.add(ls);pdown.add(ts);
          pdown.add(lb);pdown.add(tb);pdown.add(lbr);pdown.add(tbr);
          pdown.add(sold);pdown.add(snew);pdown.add(bok);pdown.add(bcancel);

          normalRGBchange=false;
	}
	public void actionPerformed (ActionEvent e) {
		String s=e.getActionCommand();
		if("Ok".equals(s)) {
			master.setColor(new Color(cr,cg,cb));
		}
		master.releaseColorHold();

		ColorDialog.this.par.dispose();
		ColorDialog.this.dispose();
	}
	public void init () {
		setLayout(new GridLayout(2,1));
		pup=new Panel();
		pdown=new Panel();
		pup.setBackground(bkcolor);
		pdown.setBackground(bkcolor);
		add(pup);
		add(pdown);
		initControl();
		canvas=new FixColorCanvas(this);
		pup.add(canvas);
		sb=new SlidBar(this);
		pup.add(sb);

	}
	public void setHueSat(int h,int s) {
		hue=h;
		sat=s;
		changeHSLtoRGB();
		setRGBText();
		setHSLText();
		sb.setHSL(hue,sat,lum);
	}
	public void setLum(int l) {
		lum=l;
		changeHSLtoRGB();
		setRGBText();
		setHSLText();
	}

	public void changeHSLtoRGB() {
		int rgb[]=new int[3];
		if(hue<0) hue=0;
		if(hue>=192) hue=191;
		if(sat<0) sat=0;
		if(sat>=128) sat=128;
		if(lum<0) lum=0;
		if(lum>=128) lum=127;
		canvas.hueToRGB( hue, rgb);
		cr=rgb[0]*sat/128+128-sat;
		cg=rgb[1]*sat/128+128-sat;
		cb=rgb[2]*sat/128+128-sat;

		if(lum>64) {
			cr+=(255-cr)*(lum-64)/64;
			cg+=(255-cg)*(lum-64)/64;
			cb+=(255-cb)*(lum-64)/64;
		}
		else {
			cr=cr*lum/64;
			cg=cg*lum/64;
			cb=cb*lum/64;
		}
	}
	public void setRGBText() {
		normalRGBchange=true;
		tr.setText(""+cr);
		tg.setText(""+cg);
		tb.setText(""+cb);
		snew.setRGB(cr,cg,cb);
		normalRGBchange=false;
	}
	public void restoreColor() {
		cr=r_old;
		cg=g_old;
		cb=b_old;
		setRGBText();
	}

	public void setHSLText() {
		th.setText(""+hue);
		ts.setText(""+sat);
		tbr.setText(""+lum);
	}

	public void RGBtoHSL(int r,int g,int b) {
		cr=r;
		cg=g;
		cb=b;
/*		rgbtohsl.setRGB(r,g,b);
		hue=rgbtohsl.getH();
		sat=rgbtohsl.getS();
		lum=rgbtohsl.getL();
		canvas.setHS(hue,sat);
		sb.setHSL(hue,sat,lum);
*/
		snew.setRGB(cr,cg,cb);
	}
    class WindowEventHandler extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
          master.releaseColorHold();
          ColorDialog.this.par.dispose();
          ColorDialog.this.dispose();
        }
     }

     class RestoreColor extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
          ColorDialog.this.restoreColor();
        }
     }
}

class ColorCanvas extends Canvas {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image img=null;
	int oldw=0,oldh=0;

	public void  hueToRGB(int hue,int total, int rgb[]) {
		int step=total/6;
		if(hue<step) {rgb[0]=255;rgb[1]=255*hue/step;rgb[2]=0; return;}
		hue-=step;
		if(hue<step) { rgb[0]=255-(255*hue)/step; rgb[1]=255;rgb[2]=0; return;}
		hue-=step;
		if(hue<step) { rgb[0]=0; rgb[1]=255; rgb[2]=(255*hue)/step; return;}
		hue-=step;
		if(hue<step){ rgb[0]=0; rgb[1]=255-(255*hue)/step; rgb[2]=255; return;}
		hue-=step;
		if(hue<step) { rgb[0]=255*hue/step; rgb[1]=0; rgb[2]=255; return;}
		hue-=step;
		if(hue<step){ rgb[0]=255; rgb[1]=0; rgb[2]=255-255*hue/step; return;}
	}

    public void paint(Graphics g) {
		int w = getSize().width;
		int h = getSize().height;
		if((w==0)||(h==0)) {super.paint(g);return;}
		if((oldw!=w)||(oldh!=h)) {
			calculateImage(w,h);
			oldw=w;
			oldh=h;
		}
        g.drawImage(img, 0, 0, w, h, this);
    }

    public void update(Graphics g) {
		paint(g);
    }

    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    public Dimension getPreferredSize() {
	return new Dimension( 192,128);
    }


    public Image getImage() {
	return img;
    }

    public void setImage(Image img) {
	this.img = img;
        paint(getGraphics());
    }

	public void calculateImage(int w,int h) {
		int pix[] = new int[w * h]; 
		int index = 0; 
		int hue,sat;
		int r,g,b,rr,gg,bb,rgb[]= new int[3];
		int grey;

		for ( hue  = 0 ; hue < w; hue++) {
			hueToRGB(hue,w,rgb);
			rr=rgb[0];
			gg=rgb[1];
			bb=rgb[2];
			index=hue;
			for (sat = h-1; sat >= 0 ; sat--) { 
				grey=128-sat*128/h;
				r=rr*sat/h+grey;
				g=gg*sat/h+grey;
				b=bb*sat/h+grey;
				pix[index] = (255 << 24) | (r << 16) | (g<<8)|b; 
				index+=w;

			} 
		} 

		img = createImage(new MemoryImageSource(w, h, pix, 0, w)); 
	}
}



class SlidBar extends Canvas implements Runnable,MouseListener,MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int oldhue=-1,oldsat=-1,oldlum=-1;
	int hue,sat,lum;
	int w=10,h=128;
	int pix[]=new int[128*15];;
	Image img;
	ColorDialog par;
	Color bkcolor=new Color(192,192,192);
	public SlidBar(ColorDialog p) {
		super();
		par=p;
		hue=par.hue;
		sat=par.sat;
		lum=64;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
    public void paint(Graphics g) {
		int x[]=new int[3];
		int y[]=new int[3];
		if((oldhue!=hue)||(oldsat!=sat)) {
			calculateImage();
			oldhue=hue;
			oldsat=sat;
		}
        g.drawImage(img, 0, 0, w, h, this);
		if((oldlum!=lum)) {
			g.setColor(bkcolor);
			g.fillRect(11,0,10,128);
			g.setColor(Color.black);
			x[0]=11;y[0]=128-lum-1;
			x[1]=20; x[2]=20;
			y[1]=y[0]-5;
			y[2]=y[0]+5;
			g.fillPolygon(x,y,3);
		}
    }
	public void setHSL(int h,int s,int l) {
		hue=h;
		sat=s;
		lum=l;
		if(lum<0) lum=0;
		if(lum>127) lum=127;

		Thread th=new Thread(this);
		th.start();
		par.setLum(lum);

	}
	
	public void run () {
		repaint();
	}

    public void update(Graphics g) {
		paint(g);
    }

    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    public Dimension getPreferredSize() {
	return new Dimension( 25,128);
    }


	public void calculateImage() {
		int index = 0,wd;
		int r,g,b,cr,cg,cb,rgb[]= new int[3];

		par.canvas.hueToRGB( hue, rgb);
		cr=rgb[0]*sat/128+128-sat;
		cg=rgb[1]*sat/128+128-sat;
		cb=rgb[2]*sat/128+128-sat;

		for ( int l=127; l>=0; l--) {
			if(l>64) {
				r=cr+(((255-cr)*(l-64))>>6);
				g=cg+(((255-cg)*(l-64))>>6);
				b=cb+(((255-cb)*(l-64))>>6);
			}
			else {
				r=(cr*l)>>6;
				g=(cg*l)>>6;
				b=(cb*l)>>6;
			}
			for (wd = 0; wd <w ; ++wd) {
				pix[index++] = (255 << 24) | (r << 16) | (g<<8)|b; 
			}
		}
		img = createImage(new MemoryImageSource(w, h, pix, 0, w)); 
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		lum=128-e.getY();
		setHSL(hue,sat,lum);
	}
	public void mouseReleased(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseDragged(MouseEvent e) {
		lum=128-e.getY();
		setHSL(hue,sat,lum);

	}
	public void mouseMoved(MouseEvent e) {}

}


class FixColorCanvas extends ColorCanvas implements MouseListener, MouseMotionListener,
	Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int mhue=0,msat=0;
	final static int mheight=128,mwidth=192;
	boolean firsttime;

	ColorDialog par;

	Thread engine;
	Graphics outg;
	Image outimg;
	public void setHS(int h,int s) {
		mhue=h;
		msat=s;
		if(mhue<0) mhue=0; if(mhue>=mwidth) mhue=mwidth-1;
		if(msat<0) msat=0; if(msat>mheight) msat=mheight;

		engine=new Thread(this);
		engine.start();
		par.setHueSat(mhue,msat);

	}
	public FixColorCanvas (ColorDialog p) {
		super();
		calculateImage();
		addMouseListener(this);
		addMouseMotionListener(this);
		firsttime=true;
		par=p;

	}
    public void paint(Graphics g) {
		if(firsttime) {
		    outimg = createImage(mwidth, mheight);
			outg = outimg.getGraphics();
		}

	    outg.drawImage(img, 0, 0, mwidth, mheight, this);
		outg.setColor(Color.white);
		outg.drawOval(mhue-5,mheight-msat-5,10,10);
		g.drawImage(outimg,0,0,this);

    }
	public void run() {
		repaint();
	}
	public void  hueToRGB(int hue, int rgb[]) {
		int step=32;
		if(hue<step) {rgb[0]=255;rgb[1]=(hue<<3)+(hue>>5);rgb[2]=0; return;}
		hue-=step;
		if(hue<step) { rgb[0]=255-(hue<<3)+(hue>>5); rgb[1]=255;rgb[2]=0; return;}
		hue-=step;
		if(hue<step) { rgb[0]=0; rgb[1]=255; rgb[2]=(hue<<3)+(hue>>5); return;}
		hue-=step;
		if(hue<step){ rgb[0]=0; rgb[1]=255-(hue<<3)+(hue>>5); rgb[2]=255; return;}
		hue-=step;
		if(hue<step) { rgb[0]=(hue<<3)+(hue>>5); rgb[1]=0; rgb[2]=255; return;}
		hue-=step;
		if(hue<step){ rgb[0]=255; rgb[1]=0; rgb[2]=255-(hue<<3)+(hue>>5); return;}
	}

	public void calculateImage() {
		int pix[] = new int[mwidth * mheight]; 
		int index = 0; 
		int hue,sat;
		int r,g,b,rr,gg,bb,rgb[]= new int[3];
		int rd,gd,bd;
		int grey;
		int w=mwidth,h=mheight;
		for ( hue  = 0 ; hue < mwidth; hue++) {
			hueToRGB(hue,rgb);
			rd=rgb[0];gd=rgb[1];bd=rgb[2];
			rr=rd<<7;
			gg=gd<<7;
			bb=bd<<7;
			index=hue;

			for (sat = h-1,grey=0; sat >= 0 ; sat--,grey++) { 
				rr-=rd;
				gg-=gd;
				bb-=bd;
				r=(rr>>7)+grey;
				g=(gg>>7)+grey;
				b=(bb>>7)+grey;
				pix[index] = (255 << 24) | (r << 16) | (g<<8)|b; 
				index+=w;

			} 
		} 
		img = createImage(new MemoryImageSource(w, h, pix, 0, w)); 
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
  		mhue=e.getX();
		msat=mheight-e.getY()-1;
		setHS(mhue,msat);
	}
	public void mouseReleased(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseDragged(MouseEvent e) {
		mhue=e.getX();
		msat=mheight-e.getY()-1;
		setHS(mhue,msat);

	}
	public void mouseMoved(MouseEvent e) {}
}

class ColorText extends TextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int r,g,b;
	public ColorText(int r, int g, int b) {
		super();
		setEditable(false);
		setRGB(r,g,b);
	}
	public void setRGB(int r,int g, int b) {
		this.r=r;
		this.g=g;
		this.b=b;
		setBackground(new Color(r,g,b));

	}
	public ColorText() {
		super();
		setEditable(false);
		setRGB(0,0,0);
	}

}
class SmallColorCanvas extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int r,g,b;
	Color bk;
	public SmallColorCanvas (int r,int g,int b) {
		super();
		this.r=r;
		this.g=g;
		this.b=b;
	}
    public void paint(Graphics gg) {
		bk=new Color(r,g,b);
		setBackground(bk);
    }
	public void setRGB(int r,int g,int b) {
		this.r=r;
		this.g=g;
		this.b=b;
		repaint();
	}
    public void update(Graphics g) {
		paint(g);
    }

    public Dimension getMinimumSize() {
	return new Dimension(30, 20);
    }

    public Dimension getPreferredSize() {
	return new Dimension( 30,20);
    }
}

class CDLabel extends Label {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Color bkcolor=new Color(192,192,192);
	public CDLabel(String s) {
		super(s);
		setBackground(bkcolor);
		setAlignment(Label.RIGHT);
	}
}

class RGBtoHSL {
	int r,g,b;
	int h,s,l;
	int maxh;
	int maxs;
	int maxl;
	int min,max;
	public RGBtoHSL(int mh,int ms,int ml) {
		maxh=mh;
		maxs=ms;
		maxl=ml;
	}
	public void setRGB(int r,int g, int b) {
		this.r=r;
		this.g=g;
		this.b=b;
		calHSL();
	}
	private int calH() {
		int r2,g2,b2;
		min=r;
		if(g<min) min=g;
		if(b<min) min=b;
		max=r;
		if(g>max) max=g;
		if(b>max) max=b;
		if(min==max) return 0;
		r2=(r-min)*maxh/(max-min);
		g2=(g-min)*maxh/(max-min);
		b2=(b-min)*maxh/(max-min);
		if((r2==maxh)&&(g2==0)) return b2;
		if((g2==0)&&(g2==maxh)) return 2*maxh-r2;
		if((r2==0)&&(b2==maxh)) return 2*maxh+g2;
		if((r2==0)&&(g2==maxh)) return 4*maxh-b2;
		if((b2==0)&&(g2==maxh)) return 4*maxh+r2;
		if((b2==0)&&(r2==maxh)) return 6*maxh-g2;
		return 0;
	}
	private int calS() {
		if(max==0) return 0;
		return maxs*(max-min)/max;
	}
	private int calL() {
		return maxl+(max+min-512)*maxl/512;
	}
	private void calHSL() {
		h=calH();
		s=calS();
		l=calL();
	}
	public int getH() {
		return h;
	}
	public int getS() {
		return s;
	}
	public int getL() {
		return l;
	}
}

class RGBkeyInput implements TextListener {
	ColorDialog par;
	int mr,mg,mb;
	public RGBkeyInput (ColorDialog p) {
		par=p;
	}
	public int getInt(String str) {
		int ret;
		try {
			ret=Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return 0;
		}
		if(ret>255) ret=255;
		if(ret<0) ret=0;
		return ret;
	}
	public void textValueChanged(TextEvent e)  {
		String tr,tg,tb;
		if(par.normalRGBchange) return;
		par.normalRGBchange=true;
		tr=par.tr.getText();
		tg=par.tg.getText();
		tb=par.tb.getText();
		mr=getInt(tr);
		mg=getInt(tg);
		mb=getInt(tb); 
//		if(!tr.equals(""+mr)) {if(mr==0) tmp=""; else tmp=""+mr; par.tr.setText(tmp); }
//		if(!tg.equals(""+mg)) {if(mg==0) tmp=""; else tmp=""+mg; par.tg.setText(tmp); }
//		if(!tb.equals(""+mb)) {if(mb==0) tmp=""; else tmp=""+mb; par.tb.setText(tmp); }
		if((mr!=par.cr)||(mg!=par.cg)||(mb!=par.cb))
			par.RGBtoHSL(mr,mg,mb);
		par.normalRGBchange=false;
	}
	
}
