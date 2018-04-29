
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

class recieve implements Runnable
{
	static ArrayList<String> mylist = new ArrayList<String>();
	public void run()
	{
		while(true){
			byte[] recvBuf = new byte[100];
			DatagramPacket recvPacket 
			= new DatagramPacket(recvBuf , recvBuf.length);
			try {
				send.client.receive(recvPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
			//提取序列号
			InetAddress ipAddress = recvPacket.getAddress();
			int port = recvPacket.getPort();
			String ip = ipAddress.toString();
			ip=ip.substring(1, ip.length());
			if (recvStr.substring(0, 1).equals("1-")) {
				recvStr=recvStr.substring(2);
					if (!mylist.contains(recvStr)){
						mylist.add(ip);
				}
			}
			System.out.println(recvStr);
			
			send.close=false; send.a=false;
		}
	}
}