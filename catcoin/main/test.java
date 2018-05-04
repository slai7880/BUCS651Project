package main;


import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;



public class test {
	
	
	
    public static void main(String[] args) throws GeneralSecurityException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();
        CoinbaseWallet coinbase = new CoinbaseWallet();
        Pet p = coinbase.newpet();
        Pet p2 = coinbase.newpet();
        System.out.println("Creating and Mining Genesis block... ");
        ArrayList<Transaction> Xaction = new ArrayList<Transaction>();
        walletA.publicKey.toString();
        Xaction.add(coinbase.moneyTransantion(walletA.publicKey, 100f));
//        Xaction.add(coinbase.petTransaction(walletB.publicKey, p.getHash()));
//        Xaction.add(coinbase.petTransaction(walletC.publicKey, p2.getHash()));
//
        Block block0 = coinbase.newblock(Xaction);
        coinbase.addBlock(block0);
        walletA.longestchain(coinbase.blockchain);
//        walletB.longestchain(coinbase.blockchain);
//        walletC.longestchain(coinbase.blockchain);
//
        System.out.println("\nWalletA's balance is: " + walletA.getBalance(walletA.publicKey));
//        System.out.println("\nWalletA is pet's owner: " + walletA.ifowner(p.getHash())+", pet2's owner:"+ walletA.ifowner(p2.getHash()));
//        System.out.println("\nWalletB is pet's owner: " + walletB.ifowner(p.getHash())+", pet2's owner:"+ walletB.ifowner(p2.getHash()));
//        System.out.println("\nWalletC is pet's owner: " + walletC.ifowner(p.getHash())+", pet2's owner:"+ walletC.ifowner(p2.getHash()));
//        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(coinbase.blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);
//        blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(walletA.blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);
//        blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(walletB.blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);
//        blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(walletC.blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);*

        }
    }

