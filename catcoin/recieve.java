

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.gson.Gson;

import main.Block;
import main.FormatConversion;
import main.Transaction;
import main.Wallet;

class recieve implements Runnable
{
	boolean valid() {
		return true;
	};
	
	void updateblock() {
		
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
							Client.peers.add(i1);
							Client.pks.add(i.split("=>")[1]);
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
				String res=ip+":"+port+":"+recvStr.split(":")[1];
				if (!Client.Req.contains(res))
					Client.Req.add(res);
				Client.output.append(res);
            } else if (recvStr.equals("^accept.*")) {
                // yay
            } else if (recvStr.equals("^decline.*")) {
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
            		String[] tmp = recvStr.split("append ");
            	     String blockjson  = tmp[tmp.length-1];
            	     Gson gson = new Gson();
            	     Block block  = gson.fromJson(blockjson, Block.class);
            	     Client.wallet.addBlock(block);
            		
            		System.out.println("miao?");
            		//Client.wallet.addBlock(FormatConversion.fromJSON(recvStr.substring(7)));
            	}
            	System.out.println("hi"+Client.wallet.getBalance());
            } else if (recvStr.matches("longer")) {
            	//Jsontochain()
            	//addnewchain()
            }
			
			
			//System.out.println(recvStr);
			
			
			}
	}
}