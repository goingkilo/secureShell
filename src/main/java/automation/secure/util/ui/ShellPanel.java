
package automation.secure.util.ui;

import automation.secure.util.SSHConnection;
import automation.secure.util.api.SecureDeploy;
import automation.secure.util.api.SecureDeployDummyImpl;
import automation.secure.util.api.ServerConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TODO: one tab to do builds
//TODO: buttons should run an sync task and throw up a progress bar
//TODO: sign off on interface api ,and implement
//TODO: certify Jsch as safe
//TODO: integrate TFS co and build
//TODO: script editor, script running and progress
//TODO: integrate persistence for Components,use hibernate
//TODO: use spring to inject api implementations
//TODO: break up api into deploy,build, ci items
//TODO: porting to maven 
//TODO: Junits, to make it a 'real' project
//TODO: menus,toolbars, methods to add multiple servers
//TODO: github secure buffer for copy

/*DONE: server list at startup, with flag domain y/n, ask for passwords
 *  server list , marked ad-y/n, ask for 2 passwords only. then populate list
 */


/**
 * @author Karthik Raghunathan
 */
public class ShellPanel extends JPanel {

	private final static String newline = "\n";

	private JTextField input;
	private JTextArea output;

	SecureDeploy api;
	SSHConnection s ;

	ActionListener sudoActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.sudo();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener stopActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.stop();
				output.setText("stop server completed");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener verifyStopActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				api.waitUntilStopped();
				output.setText("stop server completed.Instance count 0");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener deployActionListener = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.deploy();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener verifyDeployActionListener = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.verifyDeploy();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener startActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.start();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	ActionListener verifyStartActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.waitUntilStarted();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	ActionListener pullActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				api.read();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public ShellPanel(String host, String user, String password) throws Exception {

		try {
			s = new SSHConnection(host, user ,password);
		}
		catch(Exception e) {
			System.out.println( "ShellPanel failed to connect:" + e.getMessage());
		}

		api = new SecureDeployDummyImpl(s);

		input = new JTextField();
		output = new JTextArea();

		input.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String command = input.getText();

				try {
					s.say(command);
					Thread.sleep(1000);
					String heard = s.listen();
					output.setText( heard );
				} 
				catch (Exception e1) {
					e1.printStackTrace();
					output.setText( "command " + command + " failed."  );
				}
			}});

		output.setCaretColor(Color.RED);
		output.getCaret().setBlinkRate(600);
		output.setText( " -- server -- " );

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		JPanel top = new JPanel();
		top.setLayout( new BoxLayout(top, BoxLayout.Y_AXIS));
		JPanel topTop = new JPanel();
		topTop.setLayout( new BoxLayout(topTop, BoxLayout.X_AXIS));

		JButton sudo = new JButton("sudo");
		sudo.addActionListener( sudoActionListener);

		JButton stop = new JButton("stop");
		stop.addActionListener( stopActionListener);

		JButton verifyStop = new JButton("verifyStop");
		verifyStop.addActionListener( verifyStopActionListener);

		JButton deploy = new JButton("deploy");
		deploy.addActionListener( deployActionListener);

		JButton verifyDeploy = new JButton("verifyDeploy");
		verifyDeploy.addActionListener( verifyDeployActionListener);

		JButton start = new JButton("start");
		start.addActionListener( startActionListener);

		JButton verifyStart = new JButton("verifyStart");
		verifyStart.addActionListener( verifyStartActionListener);
		
		JButton readMore = new JButton("Pull");
		verifyStart.addActionListener( pullActionListener);

		topTop.add( sudo);
		topTop.add( stop);
		topTop.add( verifyStop);
		topTop.add( deploy);
		topTop.add( verifyDeploy);
		topTop.add( start);
		topTop.add( verifyStart);
		topTop.add( readMore );
		
		JPanel topBottom = new JPanel();
		topBottom.setLayout( new BoxLayout(topBottom, BoxLayout.X_AXIS));
		topBottom.setMaximumSize( new Dimension(600,30));
		topBottom.add(new JScrollPane(input));

		top.add(topTop);
		top.add(topBottom);
		add(top);

		output.setPreferredSize( new Dimension(600,400));
		add( new JScrollPane(output));
		
	}

	public void go() {
		JFrame frame = new JFrame("Secure Deploy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add( this);

		frame.setSize( new Dimension(350,500));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new ShellPanel("taxonomyv2-app1-uat.snc1", "kraghunathan", ServerConfig.askPassword(">>")).go();
	}

}
