package ASPFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;


public class OptionBox extends JDialog implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField textProj,textDesigner,textPapWid,textPapHid;
    JTextArea textDesc;
    JButton btApply;

    public void setVisible(boolean b) {
        InitGeneralInfo();
        super.setVisible(b);
    }
    public OptionBox(JFrame f) {
        super(f, "Preferences", true);
		JPanel container = new JPanel();
		container.setLayout( new BorderLayout() );

 		JTabbedPane tabs = new JTabbedPane();
		JPanel scheme = buildSchemePanel();
		JPanel conn = buildConnectingPanel();

        JPanel geninfo = buildGeneralInfoPanel();
        tabs.addTab("General",null, geninfo);
		tabs.addTab( "Schemes", null, scheme );
		tabs.addTab( "Connecting", null, conn );


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout ( new FlowLayout(FlowLayout.RIGHT) );
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelPressed();
		}});

		buttonPanel.add( close );
		getRootPane().setDefaultButton(close);

		container.add(tabs, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(container);
		pack();
		centerDialog();
		UIManager.addPropertyChangeListener(new UISwitchListener(container));
    }

    void addToGridBag(JComponent con,GridBagLayout gb,
        GridBagConstraints c, int cx,int cy, int cw, int ch,JPanel jp)
        {
            c.gridwidth=cw;
            c.gridheight=ch;
            c.gridx=cx;
            c.gridy=cy;
            if(cx==0)
                c.anchor=GridBagConstraints.EAST;
            else
                c.anchor=GridBagConstraints.WEST;
            c.ipadx=3;
            gb.setConstraints(con, c);
            jp.add(con);
        }
    JPanel buildGeneralInfoPanel(){
        JPanel genP=new JPanel();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        genP.setLayout(gridbag);

        JLabel lb1=new JLabel("Project Root:");
        addToGridBag(lb1,gridbag,c,0,0,1,1,genP);
        textProj=new JTextField(40);
        addToGridBag(textProj,gridbag,c,1,0,2,1,genP);

        
        btApply=new JButton("Apply");
        addToGridBag(btApply,gridbag,c,2,7,1,1,genP);
        btApply.addActionListener(this);
        return genP;
    }

    JPanel buildGeneralInfoPanelOld(){
        JPanel genP=new JPanel();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        genP.setLayout(gridbag);

        JLabel lb1=new JLabel("Project:");
        addToGridBag(lb1,gridbag,c,0,0,1,1,genP);
        textProj=new JTextField(20);
        addToGridBag(textProj,gridbag,c,1,0,2,1,genP);

        JLabel lb2=new JLabel("Designer:");
        addToGridBag(lb2,gridbag,c,0,1,1,1,genP);
        textDesigner=new JTextField(20);
        addToGridBag(textDesigner,gridbag,c,1,1,2,1,genP);

        JLabel lb3=new JLabel("Description:");
        addToGridBag(lb3,gridbag,c,0,2,1,1,genP);
        textDesc=new JTextArea(5,20);//("The description of this proj");

        textDesc.setWrapStyleWord(true);
        textDesc.setLineWrap(true);
        JScrollPane sp2 = new JScrollPane();
        sp2.getViewport().add(textDesc);

        addToGridBag(sp2,gridbag,c,1,2,2,3,genP);



        JLabel lb4=new JLabel("Paper Width:");
        addToGridBag(lb4,gridbag,c,0,5,1,1,genP);
        textPapWid=new JTextField(8);
        addToGridBag(textPapWid,gridbag,c,1,5,2,1,genP);

        JLabel lb5=new JLabel("Paper Height:");
        addToGridBag(lb5,gridbag,c,0,6,1,1,genP);
        textPapHid=new JTextField(8);
        addToGridBag(textPapHid,gridbag,c,1,6,2,1,genP);
        
        btApply=new JButton("Apply");
        addToGridBag(btApply,gridbag,c,2,7,1,1,genP);
        btApply.addActionListener(this);
        return genP;
    }
    
    
    public JPanel buildSchemePanel() {
		JPanel scheme = new JPanel();
		scheme.setLayout( new GridLayout(1, 0) );

		JPanel firstPanel = new JPanel();

		firstPanel.setLayout(new ColumnLayout());
		firstPanel.setBorder( new TitledBorder("Theme Group") );

		ButtonGroup themeGroup = new ButtonGroup();
		MetalTheme themes[];
		themes=ASPFrame.getThemes();
		String name=ASPFrame.getThemeName();
		for (int i = 0; i < themes.length; i++) {
			JRadioButton item = new 
				JRadioButton( themes[i].getName() );
			themeGroup.add(item);
			firstPanel.add( item );
			item.setActionCommand("thm"+i+"");
			item.addActionListener(this);
			if(name.equals(themes[i].getName()))
				item.setSelected(true);
		}
		scheme.add(firstPanel);
	
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new ColumnLayout());
		secondPanel.setBorder(new TitledBorder("Drag Group"));
		ButtonGroup dragGroup=new ButtonGroup();
		
		JDesktopPane dsk = ASPFrame.getDesktop();
		int p_dragmode = dsk.getDragMode();

		JRadioButton live=new JRadioButton("Live");
		JRadioButton outline=new JRadioButton("Outline");
		JRadioButton slow=new JRadioButton("Old Slow Way");
		dragGroup.add(live);
		dragGroup.add(outline);
		dragGroup.add(slow);
		secondPanel.add(live);
		secondPanel.add(outline);
		secondPanel.add(slow);

		if(p_dragmode==JDesktopPane.LIVE_DRAG_MODE)
			live.setSelected(true);
		if(p_dragmode==JDesktopPane.OUTLINE_DRAG_MODE)
			outline.setSelected(true);
		if(p_dragmode==-1)
			slow.setSelected(true);
		live.addActionListener(this);
		slow.addActionListener(this);
		outline.addActionListener(this);
		live.setActionCommand("drg"+JDesktopPane.LIVE_DRAG_MODE);
		outline.setActionCommand("drg"+JDesktopPane.OUTLINE_DRAG_MODE);
		slow.setActionCommand("drg"+(-1));

		scheme.add(secondPanel);
		return scheme;
    }

    public JPanel buildConnectingPanel() {
		JPanel connectPanel = new JPanel();
        connectPanel.setLayout (new GridLayout(1,0));

		JPanel firstPanel = new JPanel();
        connectPanel.add(firstPanel);
		firstPanel.setLayout(new ColumnLayout());
		firstPanel.setBorder( new TitledBorder("Link Style") );

		ButtonGroup linkGroup = new ButtonGroup();
        JRadioButton vertStyle=new JRadioButton("Vertical Style");
        JRadioButton diagStyle = new JRadioButton("Diagonal Style");

        linkGroup.add(diagStyle);
        linkGroup.add(vertStyle);
        if(ASPFrame.m_linkStyle==ASPFrame.STYLE_VERTICAL)
            vertStyle.setSelected(true);
        if(ASPFrame.m_linkStyle==ASPFrame.STYLE_DIAGONAL)
            diagStyle.setSelected(true);

        vertStyle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	ASPFrame.m_linkStyle=ASPFrame.STYLE_VERTICAL;
            }
        });
        diagStyle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	ASPFrame.m_linkStyle=ASPFrame.STYLE_DIAGONAL;
            }
        });
        firstPanel.add(diagStyle);
        firstPanel.add(vertStyle);

		return connectPanel;
    }



    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
		Dimension size = this.getSize();
		screenSize.height = screenSize.height/2;
		screenSize.width = screenSize.width/2;
		size.height = size.height/2;
		size.width = size.width/2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		this.setLocation(x,y);
    }

    public void CancelPressed() {
        this.setVisible(false);
    }
	private void changeDrag(String cmd)
	{
		JDesktopPane dsk = ASPFrame.getDesktop();
		try {
			dsk.setDragMode(Integer.parseInt(cmd));
		} catch (Exception ex) {
			System.out.println("Failed set drag mode");
			System.out.println(ex);
		}
	}


	private void changeTheme(String cmd)
	{
		MetalTheme themes[];
		themes=ASPFrame.getThemes();

		MetalTheme selectedTheme = themes[ Integer.parseInt(cmd) ];
		MetalLookAndFeel.setCurrentTheme(selectedTheme);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			ASPFrame.setThemeName(selectedTheme.getName());
			pack();
			centerDialog();
		} catch (Exception ex) {
			System.out.println("Failed loading Metal");
			System.out.println(ex);
		}
	}
    public void InitGeneralInfo() {
//        GseGeneralInfo gi=GseData.getGeneralInfo();
//        textProj.setText(gi.m_projName);
//        textDesigner.setText(gi.m_author);
//        textDesc.setText(gi.m_desc);
//        textPapWid.setText(""+gi.m_pageWidth);
//        textPapHid.setText(""+gi.m_pageHeight);
    }
    public void UpdateGeneralInfo() {
//        GseGeneralInfo gi=GseData.getGeneralInfo();
//        gi.m_projName=textProj.getText();
//        gi.m_author=textDesigner.getText();
//        gi.m_desc=textDesc.getText();
//        gi.m_pageWidth=NetpGeneral.stringToInt(textPapWid.getText());
//        gi.m_pageHeight=NetpGeneral.stringToInt(textPapHid.getText());
//        BerpFrame.getComponentFrame().repaint();

    }
	public void actionPerformed(ActionEvent e) {
		String numStr = e.getActionCommand();
        Object o = e.getSource();
        if(o==btApply) {
            UpdateGeneralInfo();
            return;
        }
		if((numStr!=null)&&(numStr.length()>3)&&(numStr.substring(0,3).equals("thm"))){
			changeTheme(numStr.substring(3));
		}
		if((numStr!=null)&&(numStr.length()>3)&&(numStr.substring(0,3).equals("drg"))){
			changeDrag(numStr.substring(3));
		}

	}
  

}

class ColumnLayout implements LayoutManager {

  int xInset = 5;
  int yInset = 5;
  int yGap = 2;

  public void addLayoutComponent(String s, Component c) {}

  public void layoutContainer(Container c) {
      Insets insets = c.getInsets();
      int height = yInset + insets.top;
      
      Component[] children = c.getComponents();
      Dimension compSize = null;
      for (int i = 0; i < children.length; i++) {
	  compSize = children[i].getPreferredSize();
	  children[i].setSize(compSize.width, compSize.height);
	  children[i].setLocation( xInset + insets.left, height);
	  height += compSize.height + yGap;
      }

  }

  public Dimension minimumLayoutSize(Container c) {
      Insets insets = c.getInsets();
      int height = yInset + insets.top;
      int width = 0 + insets.left + insets.right;
      
      Component[] children = c.getComponents();
      Dimension compSize = null;
      for (int i = 0; i < children.length; i++) {
	  compSize = children[i].getPreferredSize();
	  height += compSize.height + yGap;
	  width = Math.max(width, compSize.width + insets.left + insets.right + xInset*2);
      }
      height += insets.bottom;
      return new Dimension( width, height);
  }
  
  public Dimension preferredLayoutSize(Container c) {
      return minimumLayoutSize(c);
  }
   
  public void removeLayoutComponent(Component c) {}

}
