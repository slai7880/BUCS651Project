package main;


import main.Transaction;
import utils.StringUtil;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class CoinbaseWallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public Integer petcount;
  //  public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    public ArrayList<Block> blockchain = new ArrayList<Block>();
    public int difficulty;

    {
        difficulty = 4;
    }

    public CoinbaseWallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Pet newpet() {
        Pet p = new Pet();
        String pethash = StringUtil.applySha256(
                petcount +
                        Integer.toString(p.getHp()));
        p.setHash(pethash);
        return  p;
    }

    public Transaction moneyTransantion(PublicKey reciepient, float value){
        Transaction genesisTransaction = new Transaction(publicKey, reciepient, value,"");
        genesisTransaction.generateSignature(privateKey);	 //manually sign the genesis transaction
        return genesisTransaction;
    }

    public Transaction petTransaction( PublicKey reciepient, String pethash) {
        Transaction genesisTransaction = new Transaction(publicKey, reciepient, 0,pethash);
        genesisTransaction.generateSignature(privateKey);	 //manually sign the genesis transaction
        return genesisTransaction;
    }


    public Transaction sendFunds(PublicKey _recipient,float value ) {
        Transaction newTransaction = new Transaction(publicKey, _recipient , value,"");
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }
    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
//            TransactionOutput tempOutput;

                Transaction currentTransaction = currentBlock.transactions;

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction() is Invalid");
                    return false;
                }



        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public Block newblock(ArrayList<Transaction> transactions){
        if (blockchain.size()==0) {
            Block genesis = new Block("0");
            for(Transaction t:transactions)
                genesis.addTransaction(t);
            return genesis;
        }
        else{
            Block bk  = new Block( blockchain.get(blockchain.size()-1).hash);
            for(Transaction t:transactions)
            bk.addTransaction(t);
            return bk;
            }
    }
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public void replacechain(ArrayList<Block> chain){
        blockchain = chain;
    }
    public void longestchain(ArrayList<Block> chain){
        Integer newlength = chain.size();
        if (newlength> blockchain.size()){
            replacechain(chain);
        }
    }
}
