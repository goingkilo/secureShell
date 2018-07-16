package expect4j.poc;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHKeyPocCMine {
    public static void main(String[] arg){

        try{
            JSch jsch=new JSch();

            jsch.addIdentity( "/Users/kraghunathan/.ssh/id_rsa", "frog7jumper");
            jsch.setKnownHosts( "/Users/kraghunathan/.ssh/known_hosts");
            Session session=jsch.getSession( "id", "host", 22);

            session.connect();
            ChannelExec channel= (ChannelExec) session.openChannel("exec");

            // channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.setCommand("hostname; ls; jar tvf pt.jar");

            channel.connect();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

}
