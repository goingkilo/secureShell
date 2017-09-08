/*
 * ExpectUtilsSSHTest.java
 * JUnit based test
 *
 * Created on March 16, 2007, 9:43 AM
 */

package expect4j.poc;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 *
 * @author karthik raghunathan
 */
public class SSHConnectionPoC {

	private static final int SSH_PORT = 22;
	private static String ENTER_CHARACTER = "\r";

	String [] commands = {"./stopall.sh","./deploy.sh","./startall.sh","./checklogs.sh"};
	public SSHConnectionPoC() {
	}

	public static void main(String[] args) throws Exception {
//		new MyExpectSSHTest().new OutputDisplay();
		System.out.println("SSH");

		String hostname = "server";
		//hostname = "fpr-cbapp-01.payflex.com";
		String username = "user";
		String password = "";

		JSch jsch = new JSch();
		Session session = jsch.getSession(username, hostname, SSH_PORT);
		if (password != null) {
			JPasswordField pf = new JPasswordField();
			int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (okCxl == JOptionPane.OK_OPTION) {
				String password1 = new String(pf.getPassword());
				if( password != null ) {
					//System.out.println( password1);
					session.setPassword(password1);
				}
			}
		}
		Hashtable<String,String> config = new Hashtable<String,String>();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect(60000);
		ChannelShell channel = (ChannelShell) session.openChannel("shell");
		((ChannelShell) channel).setPtyType("dumb");
		Expect4j expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
		channel.connect();

		while(true) {
			String command = JOptionPane.showInputDialog("Enter command to send","");

			if( command == null ) command = "exit";

			say(expect, command);
			Thread.sleep(1000);

			hear( expect, ".*");
			if( command.equals("exit"))
				break;
		}

		channel.disconnect();
		session.disconnect();
		expect.close();

	}

	public static void say( Expect4j expect, String s) throws Exception {
		System.out.println("[we say]:" + s);
		expect.send(s);
		expect.send(ENTER_CHARACTER);
	}

	public static void hear(  Expect4j expect, String s)  throws Exception {
		expect.expect( new Match[]{ new RegExpMatch( s, new Closure(){
			@Override
			public void run(ExpectState arg0) throws Exception {
				System.out.println( "[server says]:" + arg0.getMatch());
			}})
		});
	}
	
	public class OutputDisplay {
		JFrame frame;
		JPanel panel;
		JTextPane text;
		
		public OutputDisplay() {
			frame = new JFrame();
			panel = new JPanel();

			text = new JTextPane();

			text.setCaretColor(Color.RED);
			text.getCaret().setBlinkRate(600);

			text.setText( "" );

			panel.setLayout( new BoxLayout(panel,BoxLayout.Y_AXIS));
			panel.add( new JScrollPane(text));

			frame.add( panel);


			frame.setSize( new Dimension(350,500));
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

		}
		
		
	}

}
