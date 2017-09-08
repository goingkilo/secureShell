package jsch.poc;

import automation.secure.util.SSHConnection;

public class SSHTest {
	
	public static void main(String[] args) throws Exception {
		SSHConnection s = new SSHConnection("server", "user", "password");
	}

}
