
import java.io.*;
import java.net.*;


class send{
	static boolean a=true;
	static int count;
	static boolean close=false;

	static DatagramSocket client;
    public static void soc(String sendStr,String ip,int port) throws IOException{
        if (a) {
        	client = new DatagramSocket();
        	a=false;
        	recieve cl=new recieve();
        	Thread c1=new Thread(cl);
        	c1.start();
        	count=0;
        }
        if (close) {client.close();   return;}

		count++;
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        InetAddress addr = InetAddress.getByName(ip);
        DatagramPacket sendPacket 
            = new DatagramPacket(sendBuf ,sendBuf.length , addr , port);
        
        client.send(sendPacket);

 //       }
        	}
}