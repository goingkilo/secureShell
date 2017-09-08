package automation.secure.util.api;

import automation.secure.util.SSHConnection;

import javax.swing.*;

/**
 * @author Karthik Raghunathan
 */
public class SecureDeployImpl {

	public static void main(String[] args) throws Exception {
	}

	/** Instead of depending on the process to respond synchronously,
	 * we wait a bit and look for a status file for each of the script
	 * for the job status
	 * @throws Exception
	 */
	
	public void startAll() throws Exception {
		SSHConnection s = new SSHConnection("securehost", "user","password");


		s.say( "./deploy/startall");
		Thread.sleep(2000);
		String response = s.hear("start-complete");
		System.out.println( response );
		s.disconnect();
	}

	/**
	 * callShowRunning throws the number of services running. We use this to poll
	 * @throws Exception
	 */
	
	public void isRunning(String instance) throws Exception {
		SSHConnection s = new SSHConnection("securehost", "user","password");

		boolean rerun = true;
		while(true) {
			if( rerun) {
				s.say( "./deploy/running");
				rerun = false;
			}
			Thread.sleep(2000);
			String response = s.hear("count=[0-9]*");
			if( response == null ) {
				rerun = true;
				continue;
			}

			String count = response.split("=")[1];
			try {
				if( Integer.parseInt(count) == 0 ){ 
					break;
				}
			}
			catch(NumberFormatException nfe) {
				System.out.println( "Not a number :" + count);
			}
			System.out.println( response );
		}
		s.disconnect();
	}

	
	public  void stopAll() throws Exception {
		SSHConnection s = new SSHConnection("securehost", "user","password");

		s.say( "./deploy/stopall");
		Thread.sleep(2000);
		String response = s.hear("stop-complete");
		System.out.println( response );
		s.disconnect();
	}
	
	// ????
	public static void go(String host, String user, String password) throws Exception {
		SSHConnection s = new SSHConnection(host, user, password);

		while(true) {
			String command = JOptionPane.showInputDialog("Enter command to send","");

			if( command == null ) command = "exit";

			s.say( command);
			Thread.sleep(1000);
			String response = s.listen();
			if( response.indexOf("is-ok") > 0) {
				System.out.println("completed");
			}

			if( command.equals("exit"))
				break;
		}
		s.disconnect();
	}

}
