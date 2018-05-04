
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

class com
{
	static boolean app=false;
	
	static boolean in=false;
	static int count;
	
	public static PublicKey decodepk (byte[] b) {
		PublicKey publicKey = null;
        try {
			publicKey = 
				    KeyFactory.getInstance("ECDSA").generatePublic(new X509EncodedKeySpec(b));
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return publicKey;
	}
	
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
        	int port=Integer.parseInt(i.split(":")[1]);
        	String ip=i.split(":")[0];
        	send.soc(sendstring,ip,port);
        }
	}
	
	static void text(String s)
	{
		
		if (s.matches("^/request.*")) {
            
        	String[] ipl=Client.iplist.getText().toString().split("\n");
        	String[] req=s.split(" ");
        	int des=Integer.parseInt(req[2]);
            try {
                send.soc("request"+":"+req[1], ipl[des].split(":")[0], Integer.parseInt(ipl[des].split(":")[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (s.matches("^/accept.+")) {
        	//send block
        	count=0;
        	app=true;
        	String sendmessage="";
            try {
				boardcast(sendmessage);
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
        	
        } else if (s.matches("/decline")) {
        	
        	String[] req=s.split(" ");
        	int des=Integer.parseInt(req[2]);
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