/**
 * A UDP server
 * Listens to port 54545 and sends the ip addresses and ports to the clients
 * @author Nathan Kong, Ardeshir Bastani, Yangcha Ho
 *
 */
package main;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server2 {
	static ArrayList<String> mylist = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception{
		
		//create a socket for udp on port 54545
		DatagramSocket serverSocket = new DatagramSocket(54545);
		CoinbaseWallet coinbase = new CoinbaseWallet();
	    Pet p = coinbase.newpet();
	    Pet p2 = coinbase.newpet();
		while(true){
			System.out.println("Waiting for Clients on Port 54545...");
			
			//create packet to receive data
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			serverSocket.receive(receivePacket);
			
			//get address and port from datagram
			InetAddress ipAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String ip = ipAddress.toString();
			ip=ip.substring(1, ip.length());
			
			System.out.println("RECEIVED: " + ip + ":" + port);
			String temp=ip + ":" + port;
			if (!mylist.contains(temp)){
				mylist.add(temp);
			}
			DatagramSocket ServerSocket = new DatagramSocket();
		      //System.out.println(clientSocket.isBound());
		      ServerSocket.setReuseAddress(true);
		      for (String i:mylist) {
		    	  byte[] sendData = ("1-"+i).getBytes();

		      // send Data to Client
		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
		      ServerSocket.send(sendPacket);
		      }
		      
		}
	}

}
