/*
 * ExpectUtilsSSHTest.java
 * JUnit based test
 *
 * Created on March 16, 2007, 9:43 AM
 */

package expect4j.poc;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;

import javax.swing.*;
import java.util.Hashtable;

/**
 * @author karthik raghunathan
 */
public class SSHConnection {

	private static final int SSH_PORT = 22;
	private static String ENTER_CHARACTER = "\r";

	String [] commands = {"./stopall.sh","./deploy.sh","./startall.sh","./checklogs.sh"};
	Expect4j expect = null;
	ChannelShell channel = null;
	Session session = null;



    public SSHConnection(String host, String user, String privateKey) throws Exception {
        try {
            JSch jsch = new JSch();

            jsch.addIdentity(privateKey);
            System.out.println("identity added ");

            Session session = jsch.getSession(user, host, SSH_PORT);
            System.out.println("session created.");

            // disabling StrictHostKeyChecking may help to make connection but makes it insecure
            // see http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no
            //
            // java.util.Properties config = new java.util.Properties();
            // config.put("StrictHostKeyChecking", "no");
            // session.setConfig(config);

            session.connect();
            System.out.println("session connected.....");

            Channel channel = session.openChannel("exec");
//            channel.setInputStream(System.in);
//            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("shell channel connected....");
            ChannelExec c = ( ChannelExec)channel;
            c.setCommand("hostname; ls");
//            while(  c.getOutputStream().

            // ChannelSftp c = (ChannelSftp) channel;
            // String fileName = "test.txt";
            // c.put(fileName, "./in/");
            // c.exit();
            System.out.println("done");

        } catch (Exception e) {
            System.err.println(e);
        }
    }

	public SSHConnection(String host, String user) throws Exception {

		String password = "password";

		JSch jsch = new JSch();
		session = jsch.getSession( user, host, SSH_PORT);
		if (password == null) {
			JPasswordField pf = new JPasswordField();
			int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (okCxl == JOptionPane.OK_OPTION) {
				password = new String(pf.getPassword());
			}
		}
		session.setPassword(password);
		Hashtable<String,String> config = new Hashtable<String,String>();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect(60000);
		channel = (ChannelShell) session.openChannel("shell");
		((ChannelShell) channel).setPtyType("dumb");
		expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
		channel.connect();

	}

	public void disconnect() throws Exception {
		channel.disconnect();
		session.disconnect();
		expect.close();
	}

	public  void say( String s) throws Exception {
		//		System.out.println("[we say]:" + s);
		channel.getOutputStream().flush();
		expect.send(s);
		expect.send(ENTER_CHARACTER);
	}
	public  String  listen( )  throws Exception {
		return hear(".*");
	}

	public  String  hear( String s)  throws Exception {
		expect.expect( new Match[]{ new RegExpMatch( s, new Closure(){
			@Override
			public void run(ExpectState arg0) throws Exception {
				//				System.out.println( "[server says]:" + arg0.getMatch());
			}})
		});
		String s1 = expect.getLastState().getMatch();
		System.out.println( "[duplicate]" + s1 );
		return expect.getLastState().getMatch();
	}

	public static void main(String[] args) throws Exception {
		SSHConnection s = new SSHConnection("server", "user");
		while(true) {
			String command = JOptionPane.showInputDialog("Enter command to send","");

			if( command == null ) command = "exit";

			s.say( command);
			Thread.sleep(1000);

			s.listen();

			if( command.equals("exit"))
				break;
		}
		s.disconnect();
	}

}
