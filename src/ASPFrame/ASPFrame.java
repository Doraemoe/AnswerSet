package ASPFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;

import netp.canvas.NetpCanvas;
import AnswerSet.ASP;
import AnswerSet.Config;
import AnswerSet.ExampleFileFilter;
import data.SystemRoot;


public class ASPFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuBar menuBar;
    static public String m_version="";
    static JDesktopPane desktop;
    static final Integer DOCLAYER = new Integer(5);
    static final Integer TOOLLAYER = new Integer(6);
    static final Integer HELPLAYER = new Integer(7);

    static final String ABOUTMSG = "Answer Set Programming Test Platform \n \nSchool of ICT \nGriffith University\n\n";
    static String themeName="Steel";
    static public final int STYLE_VERTICAL=1, STYLE_DIAGONAL=2;
    static public int m_linkStyle = STYLE_DIAGONAL;
    static public boolean changed=false;
    static String filename=null;


    static public SystemRoot m_data;


//    static BerpHelpFrame hFrame;
    static ProjectFrame projectFrame;
    static OptionBox opt;

    static JToolBar toolbar;
    static NetpCanvas activeCanvas=null;

    
    static public void setActiveCanvas(NetpCanvas cvs){
      activeCanvas=cvs;
    }

    static public ProjectFrame getProjectFrame()
    {
        return projectFrame;
    }
//    static public BerpTreeEditCanvas getTreeEditCanvasBySysName(String sysName){
//    	return cFrame.getTreeEditCanvasBySysName(sysName);
//    }
    static public void constructTitle() {
        String tt="ASP testing platform";
/*
        if(filename==null)
            tt+="Untiled";
        else tt+=filename;
        if(changed) tt+="*";
        tt+="]";
*/
        ASP.getTopFrame().setTitle(tt);
    }
	static JDesktopPane getDesktop()
	{   return desktop;
	}
	static public String getThemeName() {
		return themeName;
	}
	static public void setThemeName(String nm)
	{	themeName=nm;
	}
	static public MetalTheme[] themes = {
			new DefaultMetalTheme(),
		};

	static public MetalTheme[] getThemes()
	{
		return themes;
	}
	static public SystemRoot getData(){
		return m_data;
	}
    public ASPFrame() {
        super("AST testing Platform");
        m_data=new SystemRoot();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        final int inset = 0;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds ( inset, inset, screenSize.width - inset*2, screenSize.height - inset*2 );
		this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					quit();
				}
			}
		);
		UIManager.addPropertyChangeListener(new UISwitchListener((JComponent)getRootPane()));

		buildContent();
		buildMenus();
        buildToolbar();
		initFrames();

    }

    protected JButton createToolbarButton(String img,String tip) {
        JButton b = new JButton(new ImageIcon(img)) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public float getAlignmentY() { return 0.5f; }
	    };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1,1,1,1));

	    b.setToolTipText(tip);
        return b;
    }

    private Component buildToolbar() {
    	toolbar = new JToolBar();
	getContentPane().add("North", toolbar);

	toolbar.add(Box.createHorizontalStrut(5));
        JButton btNew=createToolbarButton("images/new.gif","Create a new system.");
        toolbar.add(btNew);
	btNew.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	   newDocument();
	}});

        JButton btOpen=createToolbarButton("images/open.gif","Open an existing system.");
        toolbar.add(btOpen);
	btOpen.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	   openDocument();
       }});

        JButton btSave=createToolbarButton("images/save.gif","Save all systems.");
        toolbar.add(btSave);
	btSave.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	   saveDocument();
	}});
        toolbar.addSeparator();
        JButton btNewMethod=createToolbarButton("images/newmethod.gif","Insert a new behavior node");
        toolbar.add(btNewMethod);
        btNewMethod.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
        }});

        toolbar.add(Box.createHorizontalGlue());
        return toolbar;
    }

    private void buildMenus() {
        menuBar = new JMenuBar();
		menuBar.setOpaque(true);
		JMenu file = buildFileMenu();
		JMenu views = buildViewsMenu();
		JMenu help = buildHelpMenu();

		menuBar.add(file);
		menuBar.add(views);
		menuBar.add(help);
		setJMenuBar(menuBar);
    }

    protected JMenu buildFileMenu() {
    	
		JMenu file = new JMenu("Systems");
		JMenuItem newWin = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save all");
		JMenuItem export = new JMenuItem("Export ...");

		JMenuItem saveas = new JMenuItem("Save As ...");


		JMenuItem quit = new JMenuItem("Quit");

        export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                exportDocument();
            }});
		save.addActionListener(new ActionListener() {
							   public void actionPerformed(ActionEvent e) {
					   saveDocument();
					   }});
		saveas.addActionListener(new ActionListener() {
							   public void actionPerformed(ActionEvent e) {
					   saveAsDocument();
					   }});

		newWin.addActionListener(new ActionListener() {
							   public void actionPerformed(ActionEvent e) {
					   newDocument();
					   }});

		open.addActionListener(new ActionListener() {
							   public void actionPerformed(ActionEvent e) {
					   openDocument();
					   }});

		quit.addActionListener(new ActionListener() {
							   public void actionPerformed(ActionEvent e) {
					   quit();
					   }});
		file.add(newWin);
		file.add(open);
		file.add(save);
//		file.add(saveas);

		if(!Config.bIsEduVersion){
			file.addSeparator();
	        file.add(export);
		}
        file.addSeparator();
		file.add(quit);
		return file;
    }


    protected JMenu buildViewsMenu() {
		JMenu views = new JMenu("Views");

		JMenuItem cCon = new JMenuItem("Projects");

		JMenuItem cRIT = new JMenuItem("Requirements Integration Table");


		JMenuItem opt = new JMenuItem("Options ...");

		
		JMenuItem setRootDir=new JMenuItem("Project Directory...");

		setRootDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openSetRootFrame();
		}});
		

		JMenuItem setDlvDir=new JMenuItem("Dlv Location ...");

		setDlvDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openSetDLVFrame();
		}});
		
		JMenuItem setSmodelsDir=new JMenuItem("Smodels Location ...");

		setSmodelsDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openSetSmodelsFrame();
		}});

		JMenuItem setLParseDir=new JMenuItem("lparse Location ...");

		setLParseDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openSetLParseFrame();
		}});
		
		
		
		JMenuItem setClaspDir=new JMenuItem("Clasp Location ...");

		setClaspDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openSetClaspFrame();
		}});
		
		
		
		opt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openOptionFrame();
		}});

		cCon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFrame(projectFrame);
		}});

		cRIT.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					showRIT();
		}});


		views.add(cCon);
//        views.add(cRIT);
		views.addSeparator();

		if(!Config.bIsEduVersion) 
			views.add(opt);
		views.add(setRootDir);
		views.add(setDlvDir);
		views.add(setSmodelsDir);
		views.add(setClaspDir);
		views.add(setLParseDir);
		
		return views;
		
    }
    
    public void openSetRootFrame(){
    	ProjectRootDirBox box=new ProjectRootDirBox(this,ProjectRootDirBox.PROJECT_ROOT);
    	box.setVisible(true);
    }
    
    public void openSetDLVFrame(){
    	ProjectRootDirBox box=new ProjectRootDirBox(this,ProjectRootDirBox.DLV_LOC);
    	box.setVisible(true);
    	
    }

    public void openSetSmodelsFrame(){
    	ProjectRootDirBox box=new ProjectRootDirBox(this,ProjectRootDirBox.SMODELS_LOC);
    	box.setVisible(true);
    	
    }
    public void openSetLParseFrame(){
    	ProjectRootDirBox box=new ProjectRootDirBox(this,ProjectRootDirBox.LPARSE_LOC);
    	box.setVisible(true);
    	
    }
    
    public void openSetClaspFrame(){
    	ProjectRootDirBox box=new ProjectRootDirBox(this,ProjectRootDirBox.CLASP_LOC);
    	box.setVisible(true);
    	
    }
	 public void openOptionFrame()
	 {
		opt.setVisible(true);
	 }

    protected JMenu buildHelpMenu() {
		JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About BECIE...");
		JMenuItem openHelp = new JMenuItem("Help...");

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutBox();
			}
		}
		);

		openHelp.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
//			   openFrame(hFrame);
			   }
			}
		);
		help.add(openHelp);
		help.add(about);
		return help;
    }

    private void buildContent() {
        desktop = new JDesktopPane();
        getContentPane().add(desktop);
    }

    public void quit() {
        if(changed) {
            int res=JOptionPane.showConfirmDialog(this,
                "Save Changes?");
            if(res==JOptionPane.YES_OPTION) {
                saveDocument();
                System.exit(0);
            }
            if(res==JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    public void openDocument() {
//		BerpComponentFrame.getRootNode().tryOpenSystem();

    
    }


	static public void openFrame(JInternalFrame f)
	{
		try {
			f.setVisible(true);
			f.setSelected(true);
		} catch (java.beans.PropertyVetoException e2) {}
	}

    public void showAboutBox() {
    	if(Config.bIsEduVersion)
    		JOptionPane.showMessageDialog(this, ABOUTMSG+"\nEducational Version.\n");
    	else
    		JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nFree Memory: "+Runtime.getRuntime().freeMemory());
//        JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nFree Memory: "+Runtime.getRuntime().freeMemory());
    }
	static public void addFrame(Component f)
	{
        try {
		    desktop.add(f,DOCLAYER);
        }
        catch (Exception e) {
            System.out.println("Exception in add "+f.toString());
            e.printStackTrace();
            addFrame(f);
        }
	}
	void initFrames()
	{
		projectFrame = new ProjectFrame();
		desktop.add(projectFrame, DOCLAYER);
        openFrame(projectFrame);

//		hFrame = new BerpHelpFrame();
//		desktop.add(hFrame, HELPLAYER);


		opt = new OptionBox(this);
	}
	public void newDocument()
	{
//		BerpComponentFrame.getRootNode().tryCreateNewSystem();
	}
	public void saveAsDocument()
	{
		if(findFileName())
			saveDocument();
	}
    public void saveDocument()
	{
//		BerpComponentFrame.getRootNode().trySaveAllSystem();


	}
	private boolean findFileName()
	{
        JFileChooser chooser = new JFileChooser("doc/");
		ExampleFileFilter filter = new ExampleFileFilter("gse","Genetic Software Engineering Design Files");
		chooser.setFileFilter(filter);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setApproveButtonText("Save");
		chooser.setDialogTitle("Select the file name to save");
		int result = chooser.showOpenDialog(this);

		if(result == JFileChooser.APPROVE_OPTION) {
			File choseFile=chooser.getSelectedFile();
			String fname=choseFile.getPath();
			if(fname==null) {return false;}
			if((fname.length()<=4)||(!".gse".equals(fname.substring(fname.length()-4,fname.length()))))
				fname=fname+".gse";
			File f=new File(fname);
			if(f.exists()){
				int ret =JOptionPane.
					showConfirmDialog(this,
                    fname + " exists, overwrite it ?",
                    "warning",JOptionPane.YES_NO_OPTION);
				if(ret!=JOptionPane.YES_OPTION)
					return false;
			}

			filename=fname;
			return true;
		}
		return false;
	}
/*	
	private void openFile(String fn)
	{
		StringBuffer bf=new StringBuffer();
		char ch[] = new char[1024];
		int n;
		try{
			FileReader in = new FileReader(fn);
			while((n=in.read(ch))!=-1) {
				bf.append(ch,0,n);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(parseDocument(bf.toString())){
			filename=fn;
		}
	}
*/
    public void close() {
        projectFrame.close();
//        dFrame.close();
//        fdFrame.close();
//        m_data.clear();
        filename=null;
        changed=false;
    }
/*
	public static void saveRuntimeSystem(String systemName, BuildTree bt) {
    	String filename=BerpConfig.getConfig("docRoot")+"/"+systemName+".rbe";

		FileOutputStream f;
		try {
			f = new FileOutputStream(filename);

			PrintStream pr=new PrintStream(f);

			pr.print(bt.toString());
			pr.flush();
			pr.close();
			f.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/

}
