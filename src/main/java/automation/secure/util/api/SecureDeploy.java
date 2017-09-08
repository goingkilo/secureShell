package automation.secure.util.api;

/**
 * @author Karthik Raghunathan
 */
public interface SecureDeploy {
	
	//remote setup
	public void installOnRemote() throws Exception;
	
	//user interaction
	public void getDomainPassword() throws Exception;
	public void getLocalPassword() throws Exception;
	
	//config
	public void backupConfig()throws Exception;
	public void checkoutConfig()throws Exception;
	public void installConfig()throws Exception;
	
	// deploy 
	public void sudo() throws Exception;
	public void stop() throws Exception ;
	public void waitUntilStopped()throws Exception;
	public void deploy() throws Exception;
	public void verifyDeploy()throws Exception;
	public void start() throws Exception;
	public void waitUntilStarted()throws Exception;
	public void verifyLogs(String[] error)throws Exception;
	
	//audit
	public void generateAuditLog() throws Exception;
	public void generateDeployReport() throws Exception;
	public void checkInDeployReport() throws Exception;

	public void read();

}
