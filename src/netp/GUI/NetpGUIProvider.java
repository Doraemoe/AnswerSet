package netp.GUI;

import java.awt.BasicStroke;

public class NetpGUIProvider
{
    static BasicStroke m_dashLine=null;

    static public BasicStroke getDashLine() {
        if(m_dashLine==null) {
            float sk[]={5.0f,5.0f};
            m_dashLine=new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND,
                1.0f,sk,0f);
        }
        return m_dashLine;
    }


}