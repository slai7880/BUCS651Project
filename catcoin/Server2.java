/**
 * A UDP server
 * Listens to port 54545 and sends the ip addresses and ports to the clients
 * @author Nathan Kong, Ardeshir Bastani, Yangcha Ho
 *
 */

import java.net.*;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;

import main.Block;
import main.CoinbaseWallet;
import main.FormatConversion;
import main.Pet;
import main.StringToBlock;
import main.Transaction;
import main.Wallet;

import java.io.*;

public class Server2 {
	static ArrayList<String> mylist = new ArrayList<String>();
	static ArrayList<String> pklist = new ArrayList<String>();
	static ArrayList<Pet> plist=new ArrayList<Pet>();
	static int pcount=0;
	static ArrayList<Float> money=new ArrayList<Float>();
	static ArrayList<String> pet=new ArrayList<String>();
	
	public static String dropNSort(String str, char target) {
        
		String result = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != target) {
                result += str.charAt(i);
            }
        }
        char[] array = new char[result.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = result.charAt(i);
        }
        Arrays.sort(array);
        result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        if (result.equals("")) return "0";
        return result;
    }
	public static String addNSort(String str, char extra) {
		if (str.equals("0")) return ""+extra;
        String result = str + extra;
        char[] array = new char[result.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = result.charAt(i);
        }
        Arrays.sort(array);
        result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        
        return result;
    }
	
	
	public static void main(String[] args) throws Exception{
		
		//create a socket for udp on port 54545
		DatagramSocket serverSocket = new DatagramSocket(54545);

		while(true){
			System.out.println("Waiting for Clients on Port 54545...");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			//create packet to receive data
			DatagramPacket recvPacket = new DatagramPacket(new byte[1024], 1024);
			serverSocket.receive(recvPacket);
			CoinbaseWallet coinbase=new CoinbaseWallet();
			ArrayList<Transaction> Xaction = new ArrayList<Transaction>();
			
			//get address and port from datagram
			InetAddress ipAddress = recvPacket.getAddress();
			int port = recvPacket.getPort();
			String ip = ipAddress.toString();
			ip=ip.substring(1, ip.length());
			
			System.out.println("RECEIVED: " + ip + ":" + port);
			String temp=ip + ":" + port;
			
			DatagramSocket ServerSocket = new DatagramSocket();
		      //System.out.println(clientSocket.isBound());
		      ServerSocket.setReuseAddress(true);
		      String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
		      System.out.println(recvStr);
		      if (recvStr.matches("^find.+")){
		    	  try   
		    	  {   
		    	  Thread.currentThread().sleep(1000);//毫秒   
		    	  }   
		    	  catch(Exception e){}
		    	  if (!mylist.contains(temp)){
						mylist.add(temp);
						pklist.add(recvStr.substring(5));
					}
		    	   
		    	  String sendstring="1";
		    	  for (int i=0;i<mylist.size();i++) {
		    		  sendstring+="-"+mylist.get(i)+"=>"+pklist.get(i);
		    	  }
		      // send Data to Client
		    	  {   
			    	  Thread.currentThread().sleep(1000);//毫秒   
			    	  }   
		    	  Xaction.add(coinbase.moneyTransantion(Wallet.loadPublicKey(recvStr.substring(5)), 100f));
		    	  Block block0 = coinbase.newblock(Xaction);
		          Pet p=new Pet();
		          plist.add(p);
		    	  pcount++;
		    	  Xaction.add(coinbase.petTransaction(Wallet.loadPublicKey(recvStr.substring(5)), p.getHash()));
		    	  Block block1 = coinbase.newblock(Xaction);
		    	  
		    	  money.add(100f);
		    	  pet.add(String.valueOf(pcount));
		    	  
		    	  String moneys="";
		    	  for (int i=0;i<money.size();i++){
		    		  moneys+="-"+mylist.get(i)+"="+money.get(i)+"="+pet.get(i);
		    	  }
		    	  
		    	  for (String i:mylist) {
		    		  port=Integer.parseInt(i.split(":")[1]);
		    		  ip=i.split(":")[0];
		    		  byte[] sendData = sendstring.getBytes();
		    		  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    		  
		    		  System.out.println(block0);
		    		  String sendblock=block0.BlockToString();//new block sent
		    		  
		    		  sendData=("append "+sendblock).getBytes();
		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    		  
		    		  sendData=("p "+pcount+" "+p.getHash()).getBytes();
		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    		  
		    		  sendData=("money"+moneys).getBytes();
		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    		  
		    		  /*sendblock=block1.BlockToString();//new block sent
		    		  
		    		  sendData=("append "+sendblock).getBytes();
		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);*/
		    	  }
		      }
		      if(recvStr.matches("^transaction.+")) {
		    	  String[] res=recvStr.split(" ");
		    	  int to=mylist.indexOf(res[3]);
		    	  int from=mylist.indexOf(temp);
		    	  
		    	  String s=addNSort(pet.get(to),res[1].charAt(0));
		    			  
		    	  
		    	  pet.set(to, s);
		    	  s=dropNSort(pet.get(from),res[1].charAt(0));
		    	  pet.set(from, s);
		    	  money.set(from, money.get(from)+Float.parseFloat(res[2]));
		    	  money.set(to, money.get(to)-Float.parseFloat(res[2]));
		    	  String moneys="";
		    	  
		    	  for (int i=0;i<money.size();i++){
		    		  moneys+="-"+mylist.get(i)+"="+money.get(i)+"="+pet.get(i);
		    	  }
		    	  for (String i:mylist) {
		    		  port=Integer.parseInt(i.split(":")[1]);
		    		  ip=i.split(":")[0];
		    	  byte[] sendData = ("money"+moneys).getBytes();
	    		  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
	    		 
		    	 
	    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
	    		  ServerSocket.send(sendPacket);
		    	  }
		      }
		      
		}
	}

}
