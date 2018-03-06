package automation.secure.util.api;

import automation.secure.util.SSHConnection;


/**
 * @author Karthik Raghunathan
 */
public class SecureDeployDummyImpl implements SecureDeploy {

	SSHConnection s;

	public SecureDeployDummyImpl(SSHConnection conn) {
		this.s = conn;
	}

	public void backupConfig() throws Exception {

	}

	public void checkoutConfig() throws Exception {
	}

	public void installConfig() throws Exception {
	}

	public void sudo() throws Exception {

	}

	public void stop() throws Exception {
		s.say( "./deploy/stopall");
		Thread.sleep(2000);
		s.hear("stop-complete");
	}

	public void waitUntilStopped() throws Exception {
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
	}

	public void deploy() throws Exception {
	}

	public void verifyDeploy() throws Exception {
	}

	public void start() throws Exception {
		s.say( "./deploy/startall");
		Thread.sleep(2000);
		String response = s.hear("start-complete");
		System.out.println( response );
	}

	public void waitUntilStarted() throws Exception {
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
				if( Integer.parseInt(count) > 0 ){ 
					break;
				}
			}
			catch(NumberFormatException nfe) {
				System.out.println( "Not a number :" + count);
			}
			System.out.println( response );
		}
	}

	public void installOnRemote() throws Exception {
	}

	public void getDomainPassword() throws Exception {
	}

	public void getLocalPassword() throws Exception {
	}

	public void generateAuditLog() throws Exception {
	}

	public void generateDeployReport() throws Exception {
	}

	public void checkInDeployReport() throws Exception {
	}

	public void verifyLogs(String[] error) throws Exception {
		// TODO Auto-generated method stub

	}

	public void read() {
		String read = null;
		while(true) {
			try {
				read = s.hear(".*");
				System.out.println(read);
				if (read == null ) 
					break;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
