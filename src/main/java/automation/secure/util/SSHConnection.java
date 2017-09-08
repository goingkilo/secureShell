package automation.secure.util;

import automation.secure.util.api.ServerConfig;
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
 * @author Karthik Raghunathan
 */
public class SSHConnection {

	private static final int SSH_PORT = 22;
	private static String ENTER_CHARACTER = "\r";
	private String password;

	String host;
	Expect4j expect = null;
	ChannelShell channel = null;
	Session session = null;

	public SSHConnection(ServerConfig config) throws Exception {
		this( config.getHost(), config.getUser(), config.getPassword());
	}
	
	public SSHConnection(String host, String user, String password) throws Exception {

		this.host = host;
		this.password = password;
		
		JSch jsch = new JSch();
		session = jsch.getSession( user, host, SSH_PORT);
		session.setPassword(this.password);
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
		// System.out.println("[say]:" + s);
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
				//System.out.println( "[hear]:" + arg0.getMatch()+"</>");
			}})
		});
		String s1 = expect.getLastState().getMatch();
		// System.out.println( "[hear*]" + s1 +"</>");
		return s1;
	}
	
	public static void main(String[] args) throws Exception {
		SSHConnection s = new SSHConnection("server1", "user1", "password1");

		while(true) {
			String command = JOptionPane.showInputDialog("Enter command to send","");

			if( command == null ) 
				command = "exit";

			s.say( command);
			Thread.sleep(1000);
			s.hear("is-ok");

			if( command.equals("exit"))
				break;
		}
		s.disconnect();
	}

}
