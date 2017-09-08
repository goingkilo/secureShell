
package swing.poc;

import expect4j.poc.SSHConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TexCo {
    
    private final static String newline = "\n";

    private JTextField input;
    private JTextArea output;
    
    SSHConnection s = null;
    
    public TexCo() throws Exception {
    	s = new SSHConnection("server", "user");
    	input = new JTextField();
    	output = new JTextArea();
    }

    public void go() {
    	
    	JFrame frame = new JFrame("CBAS Secure Deploy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		
		input.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = input.getText();
				
				try {
					s.say(command);
					Thread.sleep(1000);
					String heard = s.listen();
					output.setText( heard );
				} 
				catch (Exception e1) {
					e1.printStackTrace();
					output.setText( "command " + command + " failed."  );
				}
			}});
		
		output.setCaretColor(Color.RED);
		output.getCaret().setBlinkRate(600);
		output.setText( " -- server -- " );

		panel.setLayout( new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(new JScrollPane(input));
		panel.add( new JScrollPane(output));

		frame.add( panel);

		frame.setSize( new Dimension(350,500));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {
    	new TexCo().go();
    }

}