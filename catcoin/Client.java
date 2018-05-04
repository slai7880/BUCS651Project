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
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.*;
import java.security.PublicKey;
import javax.swing.*;

import java.text.*;

public class Client extends Thread{
	
	private JFrame frame = new JFrame("BlockchainPet");
	private Container c = frame.getContentPane();
	private JTextField command = new JTextField();
	static TextArea output=new TextArea("",0,0,1);
	static TextArea iplist=new TextArea("",0,0,1);
	static TextArea cur=new TextArea("",0,0,1);
	static JLabel show_id=new JLabel();
	private MainPanel mpanel;
	static ArrayList<String> peers = new ArrayList<String>();
	static ArrayList<String> pks = new ArrayList<String>();
	static ArrayList<String> Req = new ArrayList<String>();
	private JScrollPane jp=new JScrollPane(output);
	private JScrollPane jp1=new JScrollPane(iplist);
	private JScrollPane jp2=new JScrollPane(cur);
	
	
	static Wallet wallet;
	
	private JButton ok = new JButton("Ok");
	private JButton cancel = new JButton("Exit");
	private JButton clear = new JButton("Clear");
	
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
		send.soc(("find"+"+"+Wallet.savePublicKey(wallet.publicKey)), "155.41.31.66",54545);
		}
	
	private void initFrame() throws GeneralSecurityException {
		
		//output frame
		JPanel fieldPanel = (JPanel) frame.getContentPane();
		Color c=new Color(230,230,250);
		frame.setBackground(c);
		fieldPanel.setOpaque(false);
		
		fieldPanel.setLayout(null);
		mpanel = new MainPanel(wallet.getPets());
		mpanel.setBounds(30,200,270,210);
		fieldPanel.add(mpanel);
	
		JLabel l1 = new JLabel("Command");
		JLabel l2 = new JLabel("Peer list:");
		JLabel l3 = new JLabel("Status:");

		l2.setBounds(30,30,270,20);
		l1.setBounds(350,170, 70, 20);
		l3.setBounds(350, 30, 70, 20);
		ok.setBounds(560, 170, 60, 25);
		fieldPanel.add(l1);
		fieldPanel.add(l2);
		fieldPanel.add(l3);

		command.setBounds(410,170,150,25);
		show_id.setBounds(30, 10, 270, 20);
		output.setBounds(350,200,270,210);
		iplist.setBounds(30,60,270,100);
		cur.setBounds(350,60,270,100);
		jp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		fieldPanel.add(ok);
		fieldPanel.add(show_id);
		fieldPanel.add(iplist);
		fieldPanel.add(jp);
		fieldPanel.add(command);
		fieldPanel.add(output);
		fieldPanel.add(cur);
		output.setEditable(false);
		

		
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
					if(command.getText().equals("")){
						JOptionPane.showMessageDialog(null, "please input command", "error", JOptionPane.ERROR_MESSAGE);
					} else {
						output.append(">" + command.getText() + "\n");
						com.text(command.getText());
						command.setText("");
					}
				}
				try {
					mpanel.setPetType(wallet.getPets());
				} catch (GeneralSecurityException e1) {
					e1.printStackTrace();
				}
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
		cancel.setBounds(220, 420, 80, 30);
		clear.setBounds(320, 420, 80, 30);
		fieldPanel.add(cancel);
		fieldPanel.add(clear);
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
	public static void BalanceList() throws GeneralSecurityException {
		Integer i = 0;
		for (String pkstring:pks){
			PublicKey pk = StringToBlock.loadPublicKey(pkstring);
			System.out.println("PublicKey of :"+i+","+" Balance"+Client.wallet.getBalance(pk));
			i++;
		}
	}
}
