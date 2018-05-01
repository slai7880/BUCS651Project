

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.Block;
import main.FormatConversion;
import main.StringToBlock;
import main.Transaction;
import main.Wallet;

class recieve implements Runnable
{
	boolean valid() {
		return true;
	};
	
	void getbalance() {
		
	}
	
	public void run()
	{
		while(true){
			byte[] recvBuf = new byte[10000];
			DatagramPacket recvPacket 
			= new DatagramPacket(recvBuf , recvBuf.length);
			try {
				send.client.receive(recvPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
			InetAddress ipAddress = recvPacket.getAddress();
			int port = recvPacket.getPort();
			String ip = ipAddress.toString();
			ip=ip.substring(1, ip.length());
			String separator = "nozuonodie";
			System.out.println(recvStr);
			if (recvStr.matches("^1-.+")) {
				
				String[] iplist=recvStr.split("-");
				
				for (String i:iplist)
				{
					System.out.println(i);
					if (i.equals("1")){}
					
					else{
						String i1=i.split("=>")[0];
						if (!Client.peers.contains(i1)&&!i1.equals(send.myadd))
						{	
							Client.peers.add(i1);
							Client.pks.add(i.split("=>")[1]);
						}
					}
				}
				for (String b:Client.pks) {
					try {
						System.out.println(Wallet.loadPublicKey(b));
					} catch (GeneralSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Client.iplist.setText("");
				int count=0;
				for (String i:Client.peers)
				{
					Client.iplist.append("peer"+count+":"+i+"\n");
					count++;
				}
			} else if (recvStr.matches("^request:.*")) {
				String res=ip+":"+port+":"+recvStr.split(":")[1]+":"+recvStr.split(":")[2];
				if (!Client.Req.contains(res))
					Client.Req.add(res);
				res="Request from:"+ip+":"+port+": pet"+recvStr.split(":")[1]+" for:"+recvStr.split(":")[2]+"\n";
				Client.output.append(res);
            } else if (recvStr.equals("^accept.*")) {
                // yay
            } else if (recvStr.equals("decline")) {
                // wtf?
            	Client.output.append("your request has been declined");
            } else if (recvStr.equals("approve")) {
            	com.count++;
            	if (com.count*2>Client.getpeers()&&com.app) {
            		//Client.wallet.addBlock(new Block);
            		com.app=false;
            	}
            	
            } else if (recvStr.matches("^append.*")) {
            	System.out.println(recvStr);
            	if (valid()) {
            		String[] tmp = recvStr.split(" ");
            		try {
						Client.wallet.addBlock(StringToBlock.abc(recvStr.substring(7)));
					} catch (GeneralSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	
            	System.out.println("hi1"+Client.wallet.getBalance(Client.wallet.publicKey));
            	try {
					System.out.println(StringToBlock.abc(recvStr.substring(7)).transactions.reciepient);
				} catch (GeneralSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	try {
					Client.BalanceList();
				} catch (GeneralSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
           // } else if (recvStr.matches(regex))
            } else if (recvStr.matches("^agree.*")) {
                System.out.println(recvStr);
            	if (valid()) {
            		String[] tmp = recvStr.split(" ");
            	    if (Client.wallet.blockchain.size() > Integer.parseInt(tmp[1])) {
                        try {
                            String chainStr = "";
                            for (int i = 0; i < Client.wallet.blockchain.size(); i++) {
                                chainStr += separator + Client.wallet.blockchain.get(i).BlockToString();
                            }
                            send.soc("chain: " + chainStr, ip, port);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (Client.wallet.blockchain.size() < Integer.parseInt(tmp[1])) {
                        try {
                            send.soc("show me your chain", ip, port);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            	}
            
            } else if (recvStr.matches("^chain:.*")) {
                String[] splitted = recvStr.split(separator);
                ArrayList<Block> newChain = new ArrayList<Block>();
                for (int i = 1; i < splitted.length; i++) {
                    try {
                        newChain.add(StringToBlock.abc(splitted[i]));
                    } catch (Exception e) {
                    
                    }
                }
                Client.wallet.blockchain = newChain;
            } else if (recvStr.matches("^p.*")) {
            	System.out.println("miao");
            	String[] p=recvStr.split(" ");
            	Client.pk.add(p[1]);
            	Client.wallet.savePet(p[1], p[0]);
            	for (String i:Client.wallet.petList.keySet()) {
            		System.out.println(i);
            		System.out.println(Client.wallet.petList.get(i));
            	}
            	
            } else if (recvStr.matches("^money.*"))
            {
            	Client.cur.setText("");
            	String s="";
            	String[] a0=recvStr.substring(6).split("-");
            	for (String i:a0) {
            		System.out.println(i);
            		String[] a1=i.split("=");
            		Client.cur.append(a1[0]+"'s currency:"+a1[1]+"\n"+a1[0]+"'s pet:"+a1[2]+"\n");
            		if (a1[0].equals(send.myadd)) s=a1[2];
            	}
            	
            	Client.mpanel.set(s);
            	Client.mpanel.repaint();
            }
			
			
			//System.out.println(recvStr);
			
			
			}
	}
}