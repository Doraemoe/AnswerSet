package ASPFrame;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ASPNode.DocNode;
import AnswerSet.ImageManager;
public class CellRenderer extends DefaultTreeCellRenderer
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
					  boolean selected, boolean expanded,
					  boolean leaf, int row,
						  boolean hasFocus) {
	super.getTreeCellRendererComponent(tree, value,
					  selected, expanded,
					  leaf, row,hasFocus);
	DocNode nd;
	nd=(DocNode) value;
	setIcon(ImageManager.getImageIcon(nd.getNodeImage()));
	setToolTipText(nd.getToolTips());
	return this;
    }

}
