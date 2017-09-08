
package automation.secure.util.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import automation.secure.util.api.ServerConfig;

/**
 * @author Karthik Raghunathan
 */

//dec 3 : button to pull some more
public class MultiSession extends JPanel {

	String largeText = "  " ; 

	JTabbedPane tabbedPane ;
	ImageIcon icon;

	public MultiSession() {
		super(new GridLayout(1, 1));

		tabbedPane = new JTabbedPane();
		//		icon = createImageIcon("images/middle.gif");
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}


	protected JComponent makeTextPanel(String host, String user, String password) {
		try {
			return new ShellPanel(host,user,password);
		} 
		catch (Exception e) {
			System.out.println( " makeTextPanel failed");
			e.printStackTrace();
			return null;
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = MultiSession.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void addComponent(String title, String filler, String hoverText, int mnemonic, String host, String user, String password ) {
		JComponent panel1 = makeTextPanel(host,user,password);
		tabbedPane.addTab(title, icon, panel1, hoverText);
		tabbedPane.setMnemonicAt(0, mnemonic);
	}

	private void finish() {
		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame(" Secure Schell (TM)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900,850);


		ServerConfig server1 =  new ServerConfig("server1","userid3",null);
		server1.setActiveDirectoryLogin(false);
		ServerConfig server2 =	new ServerConfig("server1","userid3",null);
		server2 .setActiveDirectoryLogin(true);
		ServerConfig server3 =	new ServerConfig("server1","userid3",null);
		server3.setActiveDirectoryLogin(true);

		ServerConfig[] configs = new ServerConfig[]{ server1, server2, server3 };

		MultiSession t = new MultiSession();
		
//		String localPassword = ServerConfig.askPassword("Please enter your local password");
//		String domainPassword = ServerConfig.askPassword("Please enter your Domain password");

		for( ServerConfig config : configs ) {
			String password =  config.isActiveDirectoryLogin() ? "pass@" : "pass" ;
//			password = config.isActiveDirectoryLogin() ? domainPassword : localPassword ;
			t.addComponent(  config.getHost(),"","connected", KeyEvent.VK_1, config.getHost(), config.getUser(), password);
		}
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