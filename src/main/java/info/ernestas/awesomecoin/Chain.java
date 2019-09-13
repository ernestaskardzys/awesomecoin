package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashTargetUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Chain {

    private final List<Block> blockchain = new ArrayList<>();

    private final String hashTarget;

    private final int difficulty;

    public Chain(int difficulty) {
        this.difficulty = difficulty;
        this.hashTarget = HashTargetUtil.getHashTarget(difficulty);
    }

    public void addBlock(Block block) {
        blockchain.add(block);
    }

    public List<Block> getBlockchain() {
        return Collections.unmodifiableList(blockchain);
    }

    public boolean isValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!Objects.equals(currentBlock.getHash(), currentBlock.calculateHash()) ||
                    !Objects.equals(previousBlock.getHash(), currentBlock.getPreviousHash()) ||
                    !Objects.equals(currentBlock.getHash().substring(0, difficulty), hashTarget)) {
                return false;
            }
        }
        return true;
    }

}
