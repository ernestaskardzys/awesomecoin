package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashTargetUtil;
import info.ernestas.awesomecoin.util.HashUtil;

import java.time.Instant;

public class Block {

    private String hash;
    private final String previousHash;
    private final String data;
    private final long timeStamp;
    private int nonce;

    public Block(String previousHash, String data) {
        this.previousHash = previousHash;
        this.data = data;
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
        return HashUtil.calculateSha256Hash(previousHash + timeStamp + nonce + data);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
