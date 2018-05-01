/**
 * A UDP server
 * Listens to port 54545 and sends the ip addresses and ports to the clients
 * @author Nathan Kong, Ardeshir Bastani, Yangcha Ho
 *
 */

import java.net.*;
import java.security.Security;
import java.util.ArrayList;

import main.Block;
import main.CoinbaseWallet;
import main.FormatConversion;
import main.Transaction;
import main.Wallet;

import java.io.*;

public class Server2 {
	static ArrayList<String> mylist = new ArrayList<String>();
	static ArrayList<String> pklist = new ArrayList<String>();
	
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
		    	  if (!mylist.contains(temp)){
						mylist.add(temp);
						pklist.add(recvStr.substring(5));
					}
		    	  String sendstring="1";
		    	  for (int i=0;i<mylist.size();i++) {
		    		  sendstring+="-"+mylist.get(i)+"=>"+pklist.get(i);
		    	  }
		      // send Data to Client
		    	  Xaction.add(coinbase.moneyTransantion(Wallet.loadPublicKey(recvStr.substring(5)), 100f));
		    	  Block block0 = coinbase.newblock(Xaction);
		          
		    	  for (String i:mylist) {
		    		  port=Integer.parseInt(i.split(":")[1]);
		    		  ip=i.split(":")[0];
		    		  
		    		  byte[] sendData = sendstring.getBytes();
		    		  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    		  
		    		  
		    		  String sendblock="append "+FormatConversion.toJSON(block0);//new block sent
		    		  sendData=sendblock.getBytes();
		    		  sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		    		  ServerSocket.send(sendPacket);
		    	  }
		      }
		      
		}
	}

}
