package AnswerSet;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageManager
{
	public static ImageIcon icons[];
	public static final int TREENODE=0,LEAFNODE=1,ROOTMETHOD=2;
	public static final int PROJECTROOT=3,SYSTEMROOT=4,RUNTREENODE=5;
	public static final int MAXIMAGENUM=100;
	
	static public void init()
	{
		icons=new ImageIcon[MAXIMAGENUM];
		icons[TREENODE]=new ImageIcon("images/treenode.gif");
		icons[RUNTREENODE]=new ImageIcon("images/runtreenode.gif");
		icons[LEAFNODE]=new ImageIcon("images/leaf.gif");
		icons[ROOTMETHOD]=new ImageIcon("images/root.gif");
		icons[PROJECTROOT]=new ImageIcon("images/projectroot.gif");
		icons[SYSTEMROOT]=new ImageIcon("images/systemroot.gif");
	}
	static public ImageIcon getImageIcon(int i)
	{
		return icons[i];
	}
    static public Image getImage(int i) 
    {
        return icons[i].getImage();
    }
}