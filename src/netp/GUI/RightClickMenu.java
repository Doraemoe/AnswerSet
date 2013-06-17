package netp.GUI;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RightClickMenu extends Window implements MouseMotionListener,MouseListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public Frame m_f=new Frame();
    static RightClickMenu m_menu = null;
    static int TOP_MARGIN = 3, LEFT_MARGIN = 3, RIGHT_MARGIN = 3, BOTTOM_MARGIN=15;

    static public Color bgColor; // configured by the configure file

    RightClickMenuEnabled m_parent;
    Vector m_items;
    int m_item_num, appWidth = 0, appHeight = 0;
        
    boolean itemChanged = true,created=false;    
	Graphics outg;
	Image outimg;

    public void paint(Graphics g) 
    {
        int i;

        if(appWidth > 0) 
        {
	        outimg = createImage(appWidth, appHeight);

	        outg = outimg.getGraphics();

		    outg.setColor(bgColor);
		    outg.fillRect(0,0,appWidth,appHeight);

            RightClickMenuItem itm;
            for(i=0;i<m_item_num;++i) 
            {
                itm=(RightClickMenuItem)m_items.elementAt(i);
                itm.draw(outg);
            }
    		g.drawImage(outimg,0,0,this);
        }
    }

    public void clear() 
    {
        m_items.removeAllElements();
        itemChanged=true;
    }
    
    public void addMenuItem(RightClickMenuItem itm) 
    {
        m_items.addElement(itm);
        itemChanged=true;
    }
    
    void calItems()
    {
        int p_wd = 0;
        int p_ht=TOP_MARGIN;
        int i;
        RightClickMenuItem itm;
        if(!itemChanged) return;
        m_item_num = m_items.size();
        for(i = 0; i < m_item_num; ++i) 
        {
            itm = (RightClickMenuItem)m_items.elementAt(i);
            itm.setY(p_ht);
            p_ht+=itm.getHeight()+1; // 1 pixel margin
            if(itm.getMinWidth()>p_wd) 
            {
                p_wd=itm.getMinWidth();
            }
        }
        for(i = 0; i < m_item_num; ++i) 
        {
            itm = (RightClickMenuItem) m_items.elementAt(i);
            itm.setWidth(p_wd);
        }
        p_wd+=RIGHT_MARGIN+LEFT_MARGIN;
        p_ht+=BOTTOM_MARGIN;

        appWidth = p_wd;
        appHeight = p_ht;

        created = true;
        setSize(p_wd, p_ht);
        itemChanged = false;

    }

    public RightClickMenu(Frame f,RightClickMenuEnabled p)
    {
        super(f);
        m_parent = p;
        m_items = new Vector();
        setBackground(bgColor);
        addMouseMotionListener(this);
        addMouseListener(this);
    }
    public RightClickMenu(RightClickMenuEnabled p)
    {
        this(m_f,p);
    }
    
    void hide_other()
    {
        if(m_menu != null)
        {
            RightClickMenu tmp;
            tmp = m_menu;
            m_menu = null;
            tmp.hide();
        }
    }

    public void hide()
    {
        if(m_menu != null) 
        {
            hide_other();
        }
        super.setVisible(false);
//        super.setVisible(false);
    }

    public void show()
    {
        hide_other();
        m_menu = this;
        super.setVisible(true);
//        super.setVisible(true);

    }
    
    public void show(Point p)
    {
        Dimension d,md;
        Toolkit tk;
        int s_h, s_w, m_h, m_w;
        int p_x, p_y;
        Point lc;
        calItems();
        tk = m_parent.getToolkit();
        d = tk.getScreenSize();
        s_h = d.height;
        s_w = d.width;
        md = getSize();
        m_h = md.height;
        m_w = md.width;
        lc = m_parent.getLocationOnScreen();
        p_x = p.x + lc.x;
        p_y = p.y + lc.y;
        if((p_x + m_w) > (s_w - 5))
            p_x = s_w - m_w - 5;
        if((p_y + m_h) > (s_h - 5))
            p_y = s_h - m_h - 5;
        setLocation(new Point(p_x,p_y));
        show();
        toFront();
    }

    public void mouseDragged(MouseEvent e) 
    {   }
    
    public void mouseMoved(MouseEvent e) 
    {
        int i,changed=0;
        Point p=e.getPoint();
        RightClickMenuItem itm;

        for(i=0;i<m_item_num;++i) 
        {
            itm=(RightClickMenuItem)m_items.elementAt(i);
            changed+=itm.checkChangeStatus(p);
        }
        if(changed>0)
            repaint();
    }

    public void mouseClicked(MouseEvent e) 
    {
        int i;
        Point p=e.getPoint();
        RightClickMenuItem itm;
        String cmd=new String();

        for(i=0;i<m_item_num;++i) 
        {
            itm=(RightClickMenuItem)m_items.elementAt(i);
            if(itm.isClicked(p)) 
            {
                cmd=itm.getIndex();
                break;
            }
        }
        hide();
        m_parent.receiveCmd(cmd);
    }

    public void mouseEntered(MouseEvent e) 
    {
        mouseMoved(e);
    }
    
    public void mouseExited(MouseEvent e) 
    {
        mouseMoved(e);
    }

    public void mousePressed(MouseEvent e) 
    {   }

    public void mouseReleased(MouseEvent e) 
    {   }

    public void update(Graphics g) 
    {
		paint(g);
    }

}
