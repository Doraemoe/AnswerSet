package netp.GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


//implements ActionListener, MouseListener
public class NetpTime extends Panel implements ItemListener, ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int hour,minute;
    int time;
    int rows;
    static final int MINHOUR=00, MAXHOUR=23;
    static final int MINMINUTE=00, MAXMINUTE=59;
    Choice sh = new Choice();
    TimeApplet m_par;
    String hours[]={"00","01","02","03","04","05","06","07","08","09","10","11",
                    "12","13","14","15","16","17","18","19","20","21","22","23"};
    Choice si = new Choice();
    String minutes[]={"00","01","02","03","04","05","06","07","08","09","10","11", "12","13","14","15","16","17","18","19","20","21","22","23", "24","25","26","27","28","29","30","31","32","33","34","35", "36","37","38","39","40","41","42","43","44","45","46","47", "48","49","50","51","52","53","54","55","56","57","58","59"};

    Panel p_top,p_mid;
    Panel p_bottom;
    Button b_ok,b_cancel;

    public NetpTime(TimeApplet par)
    {
       super();
       m_par=par;
       init();
    }

    public String getTime()
    {
       return ""+hour+":"+minute;
    }

    public void inittimes() 
    {
       return;
    }

    public void inittimes(String s)
    {
       StringTokenizer st=new StringTokenizer(s,"-:, ");
       String sth,sti;
       int th,ti;
	
       if(st.countTokens()!=2) return;
       sth=st.nextToken();
       sti=st.nextToken();
       try
       {
         th=Integer.parseInt(sth);
         ti=Integer.parseInt(sti);
       }
       catch (Exception e) {
          return;
       }
       hour=th;
       minute=ti;
       inittime(hour,minute);
    }

    public void inittime(int h,int i) 
    {
       if (h<MINHOUR) h=MINHOUR;
       if (h>MAXHOUR) h=MAXHOUR;
       if (i<MINMINUTE) i=MINMINUTE;
       if (i>MAXMINUTE) i=MAXMINUTE;
       hour=h;
       minute=i;
       sh.select(hour-MINHOUR);
       si.select(minute-MINMINUTE);	
     }

     public void init() 
     {
        Date dat=new Date();
        GregorianCalendar cad=new GregorianCalendar();
        int i;
        cad.setTime(dat);

        for(i=MINHOUR;i<=MAXHOUR;++i) 
          sh.add(""+i);

        for(i=0;i<minutes.length;++i)
          si.add(minutes[i]);

        p_top=new Panel();
        p_mid=new Panel();
        p_top.setBackground(new Color(192,192,192));
        p_mid.setBackground(new Color(192,192,192));

        setLayout(new BorderLayout());
        add("North",p_top);
        add("Center",p_mid);
        sh.addItemListener(this);
        si.addItemListener(this);
        p_top.add(sh);
        p_top.add(si);
		
        p_bottom=new Panel();
        p_bottom.setBackground(new Color(192,192,192));
        add("South",p_bottom);

        b_ok=new Button("Ok");
        b_cancel=new Button ("Cancel");
        p_bottom.add(b_ok);
        p_bottom.add(b_cancel);

        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);

        hour=cad.get(Calendar.HOUR);
        minute=cad.get(Calendar.MINUTE);	
        inittime(hour,minute);
     }

     public void itemStateChanged(ItemEvent e)
     {
        String h=sh.getSelectedItem();
        hour=Integer.parseInt(h);
        String i=si.getSelectedItem();
        minute=Integer.parseInt(i);
        inittime(hour,minute);
      }

      public void actionPerformed (ActionEvent e)
      {
        if(e.getSource()==b_ok)
        {
           m_par.receiveTime(getTime());
           return;
        }
        m_par.closeTime();
      }
}
