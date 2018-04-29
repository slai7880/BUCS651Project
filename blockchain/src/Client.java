/**
 * Client side P2P chat
 * 
 * Creates UDP connection to server
 * Gets IP and Port
 * Creates TCP connection with peer
 * listens on port
 * @author Nathan Kong, Ardeshir Bastani, Yangcha Ho
 *
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import java.text.*;

public class Client extends Thread{
	
	private JFrame frame = new JFrame("cat");
	private Container c = frame.getContentPane();
	private JTextField command = new JTextField();
	static TextArea output=new TextArea("",0,0,1);
	static Label show_id=new Label();
	private JScrollPane jp=new JScrollPane(output);
	
	private JButton ok = new JButton("ok");
	private JButton cancel = new JButton("cancel");
	private JButton clear = new JButton("clear");
	
//	private rule re;
	
	static int px;
	static int py;
	
	static boolean cli_a=false;
	
	public Client(){
		
		frame.setSize(680,500);
		frame.setResizable(false);
		c.setLayout(new BorderLayout());
		initFrame();
		frame.setVisible(true);
		
		}
	
	private void initFrame() {
		
		//output frame
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel l1 = new JLabel("command");
		l1.setBounds(410,30, 70, 20);
		fieldPanel.add(l1);
		
		command.setBounds(480,30,200,20);
		show_id.setBounds(410, 10, 150, 20);
		
		output.setBounds(410,60,270,340);
		
		show_id.setText("user's id=");
		
		jp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		fieldPanel.add(show_id);
		fieldPanel.add(jp);
		fieldPanel.add(command);
		fieldPanel.add(output);
		output.setEditable(false);
		
		c.add(fieldPanel,"Center");

		
		cancel.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			if (e.getSource()==cancel){
				System.exit(0);
				}
	}	
		});
		ok.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getSource()==ok){
					output.append(">"+command.getText()+"\n");
					command.setText("");
				}
			}
		});
		clear.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getSource()==clear){
					output.setText("");
				}
			}	
		});
		
		
		//bottom
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		buttonPanel.add(clear);
		c.add(buttonPanel,"South");
	}
	public static void main(String[] args){
		new Client();
	}
	
/*	
	public static void main(String[] args) throws Exception {
      //String serverName = "teamone.onthewifi.com";
	  String serverName = "155.41.53.145";
	  int port = 54545;

      // prepare Socket and data to send
      DatagramSocket clientSocket = new DatagramSocket();
      //System.out.println(clientSocket.isBound());
      clientSocket.setReuseAddress(true);

     
      while (true) {
    	  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	  System.out.println("Enter your order: (chat/find)");
    	  String ord = reader.readLine();
    	  if (ord.equals("chat")) {
    	  System.out.println("Enter your destination: ");
    	  String[] ip; 
    	  String des = reader.readLine();
    	  ip=des.split(":");
    	  System.out.println("Enter your message: ");
    	  String word = reader.readLine();
    	  send.soc(word, ip[0], Integer.parseInt(ip[1]));
    	  }
    	  else if (ord.equals("find"))
    	  {
    		  send.soc("test", serverName, port);
    	  }
      }
      
      
	}//main
	
*/
	
	/**************************************************
	 * Creates a time stamp
	 */
	private static String getTime(){
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
	}
	
	/**************************************************
	 * Creates a random timer to wait
	 */
	private static void pause()throws Exception{
		//create random number between 1 and 15
		Random rand = new Random();
		int breath = rand.nextInt(5) + 1;
		Thread.sleep(breath*1000);
	}
}
