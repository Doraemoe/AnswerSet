package netp.GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


//implements ActionListener, MouseListener
public class NetpCalendar extends Panel implements ItemListener, ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int year,month;
    int day;
    int rows;
    static final int MINYEAR=2000,MAXYEAR=2006;
    Choice sy =new Choice();
    CalApplet m_par;
    String months[]={"January","February","March","April","May", "June","July","August","September","October","November","December"};
    Choice sm =new Choice();
    String smonths[]={"Jan","Feb","Mar","Apr","May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    int []numDays={31,28,31,30,31,30,31,31,30,31,30,31};

    StLabel cell[][]=new StLabel[7][7];
    DayLabel sd;
    Panel p_top,p_mid;
    Panel p_bottom;
    Button b_ok,b_cancel;

    public NetpCalendar(CalApplet par)
    {
       super();
       m_par=par;
       init();
    }

    public String getDate() 
    {
      if(day==0) return "";
      return ""+year+"-"+(month+1)+"-"+day;
    }

    int parseMonth(String sm) throws Exception
    {
      int i;

      for(i=0;i<smonths.length;++i)
      {
        if(sm.equals(smonths[i]))
          return i;
        if(sm.equals(months[i]))
          return i;
      }
      i=Integer.parseInt(sm);
      i--;
      return i;
    }

    public void initdates() 
    {
      return;
    }

    public void initdates(String s) 
    {
      StringTokenizer st=new StringTokenizer(s,"-/, ");
      String sty,stm,std;
      int ty,tm,td;

      if(st.countTokens()!=3) 
        return;

      sty=st.nextToken();
      stm=st.nextToken();
      std=st.nextToken();

      try
      {
        ty=Integer.parseInt(sty);
        tm=parseMonth(stm);
        td=Integer.parseInt(std);
      }
      catch (Exception e) {
        return;
      }
      year=ty;
      month=++tm;
      day=td;
      initdate(year,month,day);
    }

    public void initdate(int y,int m,int d)
    {
      int i,j,num;

      if(y<MINYEAR) y=MINYEAR;
        if(y>MAXYEAR) y=MAXYEAR;
          year=y;
      m--; // remember the month is the index
      if(m<0) m=0;
      if(m>11) m=11;
      month=m;
      sy.select(year-MINYEAR);
      sm.select(m);
      num=show_constant();
      if(d<0) d=0;
      if(d>num) d=num;
      if(d>0)
      {
        for(i=1;i<7;++i) 
          for(j=0;j<7;++j)
          {
            if(cell[i][j].getDay()==d)
            {
              day=d;
              sd=(DayLabel) cell[i][j];
              sd.setStatus(DayLabel.SELECTED);
            }
          }
       }
     }

     public void setDay(DayLabel dl) 
     {
       if(sd!=null)
       {
         sd.setStatus(DayLabel.NORMAL);
       }
       if(dl==sd) 
       {
         sd=null;
         day=0;
       }
       else
       {
         sd=dl;
         day=dl.getDay();
       }
     }

     public int daysInMonth()
     {
       GregorianCalendar gc=new GregorianCalendar();
       int days=numDays[month];

       if(month==1 && gc.isLeapYear(year)) ++days;
       return days;		
     }

     public void init_main() 
     {
       GridLayout ml=new GridLayout(7,7);
       int i,j;
       p_mid.setLayout(ml);
       i=0;
       cell[0][i]=new DayTitle("S"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("M"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("T"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("W"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("T"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("F"); p_mid.add(cell[0][i]);i++;
       cell[0][i]=new DayTitle("S"); p_mid.add(cell[0][i]);i++;

       day=1;
       for(i=1;i<7;++i)
         for(j=0;j<7;++j) 
         {
           cell[i][j]=new DayLabel(this);
           day++;
           p_mid.add(cell[i][j]);
         }
     }

     public int show_constant()
     {
       int offset,i,num,d,j,cnt;
       GregorianCalendar gc=new GregorianCalendar();
       gc.set(year,month,1);
       offset=gc.get(GregorianCalendar.DAY_OF_WEEK)-1;

       num=daysInMonth();
       d=0;
       cnt=1;

       for(i=1;i<7;++i) 
         for(j=0;j<7;++j) 
         {
           if(d<offset)
           {
             cell[i][j].setDay(0);
             d++;
             continue;
           }
           if(cnt<=num) 
             cell[i][j].setDay(cnt);
           else
             cell[i][j].setDay(0);
           cnt++;

         } 
       sd=null;
       day=0;
       return num;
    }

    public void init()
    {
       Date dat=new Date();
       GregorianCalendar cad=new GregorianCalendar();
       int i;
       cad.setTime(dat);

       for(i=MINYEAR;i<=MAXYEAR;++i) 
         sy.add(""+i);

       for(i=0;i<months.length;++i)
         sm.add(months[i]);

       p_top=new Panel();
       p_mid=new Panel();
       p_top.setBackground(new Color(192,192,192));
       p_mid.setBackground(new Color(192,192,192));

       setLayout(new BorderLayout());
       init_main();
       add("North",p_top);
       add("Center",p_mid);
       sy.addItemListener(this);
       sm.addItemListener(this);
       p_top.add(sy);
       p_top.add(sm);
		
       p_bottom=new Panel();
       p_bottom.setBackground(new Color(192,192,192));
       add("South",p_bottom);

       b_ok=new Button("Ok");
       b_cancel=new Button ("Cancel");
       p_bottom.add(b_ok);
       p_bottom.add(b_cancel);

       b_ok.addActionListener(this);
       b_cancel.addActionListener(this);

       show_constant();

       year=cad.get(Calendar.YEAR);
       month=cad.get(Calendar.MONTH)+1;
       day=cad.get(Calendar.DAY_OF_MONTH);
       initdate( year, month, day);
     }

     public void itemStateChanged(ItemEvent e)
     {
       String y=sy.getSelectedItem();
       year=Integer.parseInt(y);
       month=sm.getSelectedIndex();
       initdate(year,month+1,day);
//     show_constant();
    }

    public void actionPerformed (ActionEvent e)
    {
      if(e.getSource()==b_ok) 
      {
        m_par.receiveDate(getDate());
        return;
      }
      m_par.closeCalendar();
    }
}

class DayTitle extends StLabel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DayTitle(String s) 
    {
      super(s);
      setAlignment(Label.CENTER);
    }
}

class DayLabel extends StLabel implements MouseListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    int day;
    int status;
    NetpCalendar host;
    final static int NORMAL=0,OVER=1,SELECTED=2,OVERSELECTED=3;

    public DayLabel(NetpCalendar h) 
    {
      super();
      setAlignment(Label.CENTER);
      addMouseListener(this);
      host=h;
    }

    public int getDay()
    {
      return day;
    }

    public void setDay(int d) 
    {
      day=d;

      if(d>0) 
        setText(""+d);
      else
        setText("");
      setStatus(NORMAL);
    }

    public void setStatus(int st)
    {
      status=st;
      switch (st) 
      {
        case NORMAL:
          setBackground(new Color(192,192,192));
          setForeground(Color.black);
          break;
        case OVER:
          setBackground(new Color(192,192,192));
          setForeground(Color.white);
          break;
        case SELECTED:
          setBackground(new Color(0,0,192));
          setForeground(Color.red);
          break;
        case OVERSELECTED:
          setBackground(new Color(0,0,192));
          setForeground(Color.white);
          break;
      }
    }

    public void mouseClicked(MouseEvent e)
    {
       if(day>0)
       {
         setStatus(OVERSELECTED);
         host.setDay(this);
       }
     }

     public void mouseEntered(MouseEvent e)
     {
       if(day>0) 
       {
          switch(status) 
          {
             case NORMAL:
             case OVER:
               setStatus(OVER);
               break;
            case SELECTED:
            case OVERSELECTED:
               setStatus(OVERSELECTED);
               break;
          }
        }
      }

      public void mouseExited(MouseEvent e)
      {
         if(day>0)
         {
           switch(status)
           {
             case NORMAL:
             case OVER:
               setStatus(NORMAL);
               break;
             case SELECTED:
             case OVERSELECTED:
               setStatus(SELECTED);
               break;
            }
          }
       }

       public void mousePressed(MouseEvent e)
       {
       }

       public void mouseReleased(MouseEvent e)
       {
       }
}
