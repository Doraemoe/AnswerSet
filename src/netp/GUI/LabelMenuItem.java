package netp.GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class LabelMenuItem extends RightClickMenuItem 
{
    static public Font ft;
    static public FontMetrics fm;
    static public Color disableColor,actBackColor,normalColor,overColor;

    String m_label;

    public LabelMenuItem (String lab, String idx) 
    {
        super(idx);
        m_label = lab;
        m_status = 1;
        calSize();
    }
    
    void calSize() 
    {
        m_height = fm.getHeight() + 0;
        m_min_width = fm.stringWidth(m_label) + 10;
    }
    
    public void draw(Graphics g) 
    {
        g.setFont(ft);

        switch(m_status) 
        {
            case DISABLE:
                g.setColor(disableColor);
                break;
            case NORMAL:
                g.setColor(normalColor);
                break;
            case MOUSE_ON:
                g.setColor(new Color(204,204,204));
                g.fill3DRect(m_x, m_y, m_width, m_height, true);
                g.setColor(overColor);
                break;
        }
        g.drawString(m_label, m_x, m_y + fm.getAscent() + fm.getLeading());
    }
}
