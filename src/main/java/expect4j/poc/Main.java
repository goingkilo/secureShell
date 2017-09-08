package expect4j.poc;

import expect4j.Expect4j;
import expect4j.ExpectUtils;

public class Main {

	public static void main(String[] args) throws Exception {
		String host = "server";
		Expect4j expect = ExpectUtils.SSH(host, "user", "password", 22);

	}

}
