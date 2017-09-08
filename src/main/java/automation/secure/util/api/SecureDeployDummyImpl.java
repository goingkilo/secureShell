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

	@Override
	public void backupConfig() throws Exception {

	}

	@Override
	public void checkoutConfig() throws Exception {
	}

	@Override
	public void installConfig() throws Exception {
	}

	@Override
	public void sudo() throws Exception {

	}

	@Override
	public void stop() throws Exception {
		s.say( "./deploy/stopall");
		Thread.sleep(2000);
		s.hear("stop-complete");
	}

	@Override
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

	@Override
	public void deploy() throws Exception {
	}

	@Override
	public void verifyDeploy() throws Exception {
	}

	@Override
	public void start() throws Exception {
		s.say( "./deploy/startall");
		Thread.sleep(2000);
		String response = s.hear("start-complete");
		System.out.println( response );
	}

	@Override
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

	@Override
	public void installOnRemote() throws Exception {
	}

	@Override
	public void getDomainPassword() throws Exception {
	}

	@Override
	public void getLocalPassword() throws Exception {
	}

	@Override
	public void generateAuditLog() throws Exception {
	}

	@Override
	public void generateDeployReport() throws Exception {
	}

	@Override
	public void checkInDeployReport() throws Exception {
	}

	@Override
	public void verifyLogs(String[] error) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
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
