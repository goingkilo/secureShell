package automation.secure.util.api;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class ServerConfig {
	
	private String host;
	private String user;
	private String password;

	private boolean isActiveDirectoryLogin = false;
	
	public ServerConfig(String host, String user, String password) {
		this.setHost(host);
		this.setUser(user);
		this.setPassword(password);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		if ( password != null) {
		return password;
		}
		else {
			
			return askPassword("Please enter Password for " + host);
		}
	}

	public static String askPassword(String prompt) {
		String password = null;
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, prompt,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (okCxl == JOptionPane.OK_OPTION) {
			password = new String(pf.getPassword());
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActiveDirectoryLogin() {
		return isActiveDirectoryLogin;
	}

	public void setActiveDirectoryLogin(boolean isActiveDirectoryLogin) {
		this.isActiveDirectoryLogin = isActiveDirectoryLogin;
	}


}
