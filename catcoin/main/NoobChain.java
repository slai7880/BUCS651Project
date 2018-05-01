package main;



import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;


/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class NoobChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
//    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public static int difficulty = 4;
//    public static float minimumTransaction = 0.1f;
//    public static HashMap<PublicKey,Wallet> walletList=new HashMap<PublicKey,Wallet>();
//    public static Wallet walletB;
    public static ArrayList<Transaction>genesisTransaction=new ArrayList<Transaction>();

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();
//        walletList.add(walletA);
//        walletList.add(walletB);
//        walletList.add(walletC);
        CoinbaseWallet coinbase = new CoinbaseWallet();
        Pet p = coinbase.newpet();
        Pet p2 = coinbase.newpet();
        
        //create genesis transaction, which sends 100 NoobCoin to walletA:
//        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
//        System.out.println("give walletA 100f");
//        genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
//        genesisTransaction.transactionId = "0"; //manually set the transaction id
//        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        
        
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
//        genesisTransaction = coinbase.petTransaction(walletB.publicKey, p.getHash());
//        Transaction g1=coinbase.moneyTransantion(walletA.publicKey, 100f, null)
        genesisTransaction.add(coinbase.moneyTransantion(walletA.publicKey, 100f));
        genesisTransaction.add(coinbase.petTransaction(walletB.publicKey, p.getHash()));
        genesisTransaction.add(coinbase.petTransaction(walletC.publicKey, p2.getHash()));
        for(Transaction t:genesisTransaction){
        	// UTXOs.put(t.outputs.get(0).id,t.outputs.get(0));
        	 genesis.addTransaction(t);
        }
//        UTXOs.put(g1.outputs.get(0).id,g1.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
//        genesis.addTransaction(g1);
//        Transaction g2= coinbase.petTransaction(walletB.publicKey, p.getHash(),null);
//        UTXOs.put(g2.outputs.get(0).id, g2.outputs.get(0));
//        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is pet's owner: " + walletA.ifowner(p.getHash())+", pet2's owner:"+ walletA.ifowner(p2.getHash()));
        System.out.println("\nWalletB is pet's owner: " + walletB.ifowner(p.getHash())+", pet2's owner:"+ walletB.ifowner(p2.getHash()));
        System.out.println("\nWalletC is pet's owner: " + walletC.ifowner(p.getHash())+", pet2's owner:"+ walletC.ifowner(p2.getHash()));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB.,20 to walletC..");
        System.out.println("\nWalletB and C is Attempting to send pet to WalletA...");
        block1.addTransaction(walletB.sendPet(walletA.publicKey, p.getHash()));
        block1.addTransaction(walletC.sendPet(walletA.publicKey, p2.getHash()));
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        block1.addTransaction(walletA.sendFunds(walletC.publicKey, 20f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
        System.out.println("WalletC's balance is: " + walletC.getBalance());
        System.out.println("\nWalletA is pet's owner: " + walletA.ifowner(p.getHash())+", pet2's owner:"+ walletA.ifowner(p2.getHash()));
        System.out.println("\nWalletB is pet's owner: " + walletB.ifowner(p.getHash())+", pet2's owner:"+ walletB.ifowner(p2.getHash()));
        System.out.println("\nWalletC is pet's owner: " + walletC.ifowner(p.getHash())+", pet2's owner:"+ walletC.ifowner(p2.getHash()));
        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        System.out.println("\nWalletB Attempting to send pet it doesn't have...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        block2.addTransaction(walletB.sendPet(walletA.publicKey, p2.getHash()));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
        System.out.println("WalletC's balance is: " + walletC.getBalance());
        System.out.println("\nWalletA is pet's owner: " + walletA.ifowner(p.getHash())+", pet2's owner:"+ walletA.ifowner(p2.getHash()));
        System.out.println("\nWalletB is pet's owner: " + walletB.ifowner(p.getHash())+", pet2's owner:"+ walletB.ifowner(p2.getHash()));
        System.out.println("\nWalletC is pet's owner: " + walletC.ifowner(p.getHash())+", pet2's owner:"+ walletC.ifowner(p2.getHash()));
        
        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        System.out.println("\nWalletC is Attempting to send funds (10) to WalletA...");
        System.out.println("\nWalletA is Attempting to send pet to WalletC...");
        System.out.println("\nWalletA is Attempting to send pet2 to WalletB...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20f));
        block3.addTransaction(walletC.sendFunds( walletA.publicKey, 10f));
        
        block3.addTransaction(walletA.sendPet( walletC.publicKey, p.getHash()));
        block3.addTransaction(walletA.sendPet( walletB.publicKey, p2.getHash()));
        addBlock(block3);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
        System.out.println("WalletC's balance is: " + walletC.getBalance());
        System.out.println("\nWalletA is pet's owner: " + walletA.ifowner(p.getHash())+", pet2's owner:"+ walletA.ifowner(p2.getHash()));
        System.out.println("\nWalletB is pet's owner: " + walletB.ifowner(p.getHash())+", pet2's owner:"+ walletB.ifowner(p2.getHash()));
        System.out.println("\nWalletC is pet's owner: " + walletC.ifowner(p.getHash())+", pet2's owner:"+ walletC.ifowner(p2.getHash()));
        
        isChainValid();
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
    }

    public static Boolean isChainValid() {
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
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
//              if( currentTransaction.reciepient != currentTransaction.reciepient) {
//              System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
//              return false;
//          }
//                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
//                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
//                    return false;
//                }

//                for(TransactionInput input: currentTransaction.inputs) {
//                    tempOutput = tempUTXOs.get(input.transactionOutputId);
//
//                    if(tempOutput == null) {
//                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
//                        return false;
//                    }
//
//                    if(input.UTXO.value != tempOutput.value) {
//                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
//                        return false;
//                    }
//
//                    tempUTXOs.remove(input.transactionOutputId);
//                }
//
//                for(TransactionOutput output: currentTransaction.outputs) {
//                    tempUTXOs.put(output.id, output);
//                }
//

//                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
//                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
//                    return false;
//                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
