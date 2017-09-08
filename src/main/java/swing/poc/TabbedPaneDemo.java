
package swing.poc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TabbedPaneDemo extends JPanel {


	String largeText = "  " ; 


	JTabbedPane tabbedPane ;
	ImageIcon icon;

	public TabbedPaneDemo() {
		super(new GridLayout(1, 1));

		tabbedPane = new JTabbedPane();
		icon = createImageIcon("images/middle.gif");
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		//panel.setSize(400,100);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}
	
	protected JComponent makeTextPanel() {
		JPanel panel = new JPanel(false);
		JTextField jtx = new JTextField(40);
		JTextArea jta = new JTextArea();
		
		panel.setLayout(new GridLayout(2, 1));
		panel.add(jtx);
		panel.add(jta);
		return panel;
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = TabbedPaneDemo.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void addComponent(String title, String filler, String hoverText, int mnemonic ) {
		JComponent panel1 = makeTextPanel();
		tabbedPane.addTab(title, icon, panel1,hoverText);
		tabbedPane.setMnemonicAt(0, mnemonic);
	}

	private void finish() {
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("TabbedPaneDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,100);

		TabbedPaneDemo t = new TabbedPaneDemo();
		t.addComponent("server","","connected", KeyEvent.VK_1);
		t.finish();
		frame.add(t, BorderLayout.CENTER);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}