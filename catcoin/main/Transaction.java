package main;


import utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


/**
 * 
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class Transaction {

    public String transactionId; //Contains a hash of transaction*
    public PublicKey sender; //Senders address/public key.
    public PublicKey reciepient; //Recipients address/public key.
    public float value; //Contains the amount we wish to send to the recipient.
    public String pethash;//Contains the hash of pets we are about to buy/sell.
    public byte[] signature; //This is to prevent anybody else from spending funds in our wallet.


    private static int sequence = 0; //A rough count of how many transactions have been generated

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,String pethash) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.pethash=pethash;
    }




    public boolean processTransaction() {

        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
        return true;
    }

    public String getPethash() {
		return pethash;
	}

	public void setPethash(String pethash) {
		this.pethash = pethash;
	}

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
