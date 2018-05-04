/**
 * A UDP server
 * Listens to port 54545 and sends the ip addresses and ports to the clients
 * @author Nathan Kong, Ardeshir Bastani, Yangcha Ho
 *
 */

import java.net.*;
import java.security.Security;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;
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
	
	
	
	public static void main(String[] args) throws Exception{
		
		//create a socket for udp on port 54545
		DatagramSocket serverSocket = new DatagramSocket(54545);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		CoinbaseWallet coinbase=new CoinbaseWallet();

		while(true){
			System.out.println("Waiting for Clients on Port 54545...");
			//create packet to receive data
			DatagramPacket recvPacket = new DatagramPacket(new byte[1024], 1024);
			serverSocket.receive(recvPacket);
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
//		    	  try
//		    	  {
//		    	  Thread.currentThread().sleep(1000);//毫秒
//		    	  }
//		    	  catch(Exception e){}
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
		    	  for (String i:mylist) {
		    		  port=Integer.parseInt(i.split(":")[1]);
		    		  ip=i.split(":")[0];
		    		  byte[] sendData = sendstring.getBytes();
		    		  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);

//		    		  String blockJson = new GsonBuilder().setPrettyPrinting().create().toJson(coinbase.blockchain);
//					  System.out.println(blockJson);

//					  sent whole blockchain
					  String wholechain = FormatConversion.toJSON(coinbase.getBlockchain());
					  sendData=("chain:"+wholechain).getBytes();
					  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
					  ServerSocket.send(sendPacket);

//		    		  System.out.println(block0);
//		    		  String sendblock=FormatConversion.toJSON(block0);//new block sent
//
//		    		  sendData=("append "+sendblock).getBytes();
//		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
//		    		  ServerSocket.send(sendPacket);
//
//		    		  sendblock=FormatConversion.toJSON(block1);//new block sent
//
//		    		  sendData=("append "+sendblock).getBytes();
//		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
//		    		  ServerSocket.send(sendPacket);


		    	  }
		      }
		      
		}
	}

}
