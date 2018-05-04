package main;

import com.google.gson.GsonBuilder;
import utils.StringUtil;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public Transaction transactions ; //our data will be a simple message.
    public long timeStamp; //as number of milliseconds since 1/1/1970.
    public int nonce;

    //Block Constructor.
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        ArrayList<Transaction> tmp = new ArrayList<Transaction>();
        tmp.add(transactions);
        merkleRoot = StringUtil.getMerkleRoot(tmp);
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((previousHash != "0")) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        this.transactions= transaction;
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
//    public String BlockToString() throws GeneralSecurityException {
//        String sender = StringToBlock.savePublicKey(transactions.sender);
//        String reciepient = StringToBlock.savePublicKey(transactions.reciepient);
//
//        String signature;
//        try {
//             signature = new String(transactions.signature, "ISO-8859-1");
//        }catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        String res = "";
//        res += hash+"fuckfuckfuck"+previousHash+"fuckfuckfuck"+merkleRoot+"fuckfuckfuck"
//                +sender+"fuckfuckfuck"+reciepient+"fuckfuckfuck"+Float.toString(transactions.value)+"fuckfuckfuck"+transactions.pethash
//                +"fuckfuckfuck"+signature+"fuckfuckfuck"+Long.toString(timeStamp)+"fuckfuckfuck"+Integer.toString(nonce) ;
//        return  res;
//    }
//


}
