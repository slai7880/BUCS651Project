
import java.io.IOException;
import java.net.DatagramPacket;

class recieve implements Runnable
{
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
			System.out.println(recvStr);
			
			send.close=false; send.a=false;
		}
	}
}