package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashTargetUtil;
import info.ernestas.awesomecoin.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    private String hash;
    private final String previousHash;
    private List<Transaction> transactions = new ArrayList<>();
    private final long timeStamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = Instant.now().toEpochMilli();
        this.hash = calculateHash();
    }

    public void mineBlock(int difficulty) {
        String target = HashTargetUtil.getHashTarget(difficulty);
        String hash = this.hash;
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        this.hash = hash;
    }

    public String calculateHash() {
        return HashUtil.calculateSha256Hash(previousHash + timeStamp + nonce);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) {
            return false;
        }
        if(!previousHash.equals("0")) {
            if(!transaction.processTransaction()) {
                LOGGER.info("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        LOGGER.info("Transaction Successfully added to Block");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
