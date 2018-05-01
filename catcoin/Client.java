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

import main.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.*;

import javax.swing.*;

import java.text.*;

public class Client extends Thread{
	
	private JFrame frame = new JFrame("cat");
	private Container c = frame.getContentPane();
	private JTextField command = new JTextField();
	static TextArea output=new TextArea("",0,0,1);
	static TextArea iplist=new TextArea("",0,0,1);
	static TextArea cur=new TextArea("",0,0,1);
	static Label show_id=new Label();
	private MainPanel mpanel;
	static ArrayList<String> peers = new ArrayList<String>();
	static ArrayList<String> pks = new ArrayList<String>();
	static ArrayList<String> Req = new ArrayList<String>();
	private JScrollPane jp=new JScrollPane(output);
	private JScrollPane jp1=new JScrollPane(iplist);
	private JScrollPane jp2=new JScrollPane(cur);
	
	static Wallet wallet;
	
	private JButton ok = new JButton("ok");
	private JButton cancel = new JButton("exit");
	private JButton clear = new JButton("clear");
	
//	private rule re;
	
	static int px;
	static int py;
	
	static boolean cli_a=false;
	
	static int getpeers() {
		if (iplist.getText().equals("")) return 0;
		else if (!iplist.getText().matches("\n")) return 1;
		else return iplist.getText().split("\n").length;
	}
	
	static void updatestatus() {
		
	}
	
	public Client() throws IOException, GeneralSecurityException{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		wallet = new Wallet();
		frame.setSize(680,500);
		frame.setResizable(false);
		c.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initFrame();
		frame.setVisible(true);
		send.soc(("find"+"+"+Wallet.savePublicKey(wallet.publicKey)), "192.168.1.102",54545);
		}
	
	private void initFrame() {
		
		//output frame
		
		mpanel = new MainPanel(wallet.getPets());
		mpanel.setBounds(30,200,270,210);
		c.add(mpanel);
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel l1 = new JLabel("command");
		JLabel l2 = new JLabel("peer list:");
		

		l2.setBounds(30,30,270,20);
		l1.setBounds(350,30, 70, 20);
		fieldPanel.add(l1);
		fieldPanel.add(l2);

		command.setBounds(410,30,200,20);
		show_id.setBounds(350, 10, 270, 20);
		output.setBounds(350,60,270,140);
		iplist.setBounds(30,60,270,100);
		cur.setBounds(350,220,270,150);
		
		jp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		fieldPanel.add(show_id);
		fieldPanel.add(iplist);
		fieldPanel.add(jp);
		fieldPanel.add(command);
		fieldPanel.add(output);
		fieldPanel.add(cur);
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
					com.text(command.getText());
					command.setText("");
				}
				mpanel.setPetType(wallet.getPets());
				//todo
				mpanel.repaint();
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
	public static void main(String[] args) throws IOException, GeneralSecurityException{
		new Client();
	}
	
	
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
