
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import main.Block;
import main.Transaction;
import main.Wallet;

class com
{
	static boolean app=false;
	
	static boolean in=false;
	static int count;
	
	public static boolean isNum(String str) {
		  
        try {
            int num=Integer.valueOf(str);
            if (num>100||num<0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	public static void boardcast(String sendstring) throws IOException {
		String[] ipl=Client.iplist.getText().toString().split("\n");
		
        for (String i :ipl) {
        	System.out.println(i);
        	int port=Integer.parseInt(i.split(":")[2]);
        	String ip=i.split(":")[1];
        	send.soc(sendstring,ip,port);
        }
	}
	
	static void text(String s)
	{
		
		if (s.matches("^/request.*")) {
            
        	String[] ipl=Client.iplist.getText().toString().split("\n");
        	String[] req=s.split(" ");
        	int des=Integer.parseInt(req[3]);
            try {
                send.soc("request"+":"+req[1]+":"+req[2], ipl[des].split(":")[1], Integer.parseInt(ipl[des].split(":")[2]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (s.matches("^/accept.+")) {
        	//send block
        	String[] req=Client.Req.get(Integer.parseInt(s.split(" ")[1])).split(":");
        	/*int order=Client.peers.indexOf(req[0]+":"+req[1]);
        	ArrayList<Transaction> Xaction = new ArrayList<Transaction>();
        	try {
        		Xaction.add(Client.wallet.sendPet(Wallet.loadPublicKey(Client.pks.get(order)), Client.pk.get(Integer.parseInt(req[2]))));
				//Xaction.add(Client.wallet.sendPet(Wallet.loadPublicKey(Client.pks.get(order)), Client.wallet.petList.get(req[2])));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (GeneralSecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	
        	String sendmessage="agree "+Client.wallet.blockchain.size();
            try {
				boardcast(sendmessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        
        	try {
				send.soc(("transaction "+req[2]+" "+req[3]+" "+req[0]+":"+req[1]),"155.41.53.145",54545);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else if (s.matches("/show requests")) {
        	Client.output.append("current requests:\n");
        	if (Client.Req.size()==0) {
        		Client.output.append("None\n");
        	}
        	else {
        		for (String i:Client.Req)
        		{
        			Client.output.append(i+"\n");
        		}
        	}
        	
        } else if (s.matches("^/decline.*")) {
        	
        	String[] req=s.split(" ");
        	int des=Integer.parseInt(req[1]);
        	String[] ipd=Client.Req.get(des).split(":");
            try {
                send.soc("decline", ipd[0], Integer.parseInt(ipd[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		else Client.output.append("command error\n");
	}
}