/*
 * ExpectUtilsSSHTest.java
 * JUnit based test
 *
 * Created on March 16, 2007, 9:43 AM
 */

package expect4j.poc;

import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.ExpectUtils;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author justin
 */
public class ExpectUtilsSSHTest {

	public ExpectUtilsSSHTest() {
	}

	/**
	 * Test of SSH method, of class expect4j.ExpectUtils.
	 * First response should be something like:
	 * Last login: Wed Mar 14 12:13:29 2007 from pool-71-126-249-188.bstnma.fios.verizon.net
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("SSH");

		String hostname = "server";
		String username = "user";
		String password = "password";


		Expect4j expect = ExpectUtils.SSH(hostname, username, password, 22);
		//expect.setDefaultTimeout(Expect4j.TIMEOUT_FOREVER);

		final DateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss yyy z");
		// Mar 15 17:42:02 2007
		expect.expect( new Match[] {
				new RegExpMatch("Last login: \\w{3} (.*) from", new Closure() {
					public void run(ExpectState state) throws Exception {
						String time = state.getMatch(1);
						Date date = format.parse( time + " UTC");
						state.addVar("timestamp", date );


					}
				})
		});


		expect.close();

		Date result = (Date) expect.getLastState().getVar("timestamp");
		System.out.println("Timestamp: " + result);

		Date expResult = new Date();
		System.out.println("Timestamp: " + expResult);

		expect.close();
	}

}
