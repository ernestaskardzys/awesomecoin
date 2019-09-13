package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainTest {

    private static final int DIFFICULTY = 2;

    private Chain chain;

    @BeforeEach
    void setUp() {
        chain = new Chain(DIFFICULTY);
    }

    @Test
    void addBlock() {
        int initialSize = chain.getBlockchain().size();

        chain.addBlock(new Block("0", "test"));

        assertEquals(initialSize + 1, chain.getBlockchain().size());
    }

    @Test
    void getBlocks() {
        chain.addBlock(new Block("0", "test"));

        assertEquals(1, chain.getBlockchain().size());
    }

    @Test
    void isValid() {
        Block firstBlock = new Block("0", "test1");
        firstBlock.mineBlock(DIFFICULTY);
        Block secondBlock = new Block(firstBlock.getHash(), "test2");
        secondBlock.mineBlock(DIFFICULTY);

        chain.addBlock(firstBlock);
        chain.addBlock(secondBlock);

        assertTrue(chain.isValid());
    }

    @Test
    void isNotValid_whenPreviousHashIsInvalid() {
        Block firstBlock = new Block("0", "test1");
        firstBlock.mineBlock(DIFFICULTY);
        Block secondBlock = new Block(HashUtil.calculateSha256Hash("not valid hash"), "test2");
        secondBlock.mineBlock(DIFFICULTY);

        chain.addBlock(firstBlock);
        chain.addBlock(secondBlock);

        assertFalse(chain.isValid());
    }

    @Test
    void isNotValid_whenBlockIsNotMined() {
        Block firstBlock = new Block("0", "test1");
        firstBlock.mineBlock(DIFFICULTY);
        Block secondBlock = new Block(firstBlock.getHash(), "test2");

        chain.addBlock(firstBlock);
        chain.addBlock(secondBlock);

        assertFalse(chain.isValid());
    }
}