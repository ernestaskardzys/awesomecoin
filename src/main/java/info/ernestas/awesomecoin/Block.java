package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashTargetUtil;
import info.ernestas.awesomecoin.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {

    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    private String hash;
    private final String previousHash;
    private final List<Transaction> transactions = new ArrayList<>();
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

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (!Objects.equals(previousHash, "0") && !transaction.processTransaction()) {
            LOGGER.info("Transaction failed to process. Discarded.");
            return false;
        }
        transactions.add(transaction);
        LOGGER.info("Transaction Successfully added to Block");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
