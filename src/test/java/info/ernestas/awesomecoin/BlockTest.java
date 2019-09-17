package info.ernestas.awesomecoin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockTest {

    @Mock
    private Transaction transaction;

    private Block block;

    @BeforeEach
    void setUp() {
        block = new Block("0");
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2 })
    void mineBlock(int difficulty) {
        block.mineBlock(difficulty);

        assertNotNull(block.getHash());
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "123", "hash" })
    void calculateHash(String input) {
        block = new Block(input);

        assertNotNull(block.calculateHash());
    }

    @Test
    void addTransaction() {
        int initialSize = block.getTransactions().size();

        assertTrue(block.addTransaction(transaction));
        assertEquals(initialSize + 1, block.getTransactions().size());
    }

    @Test
    void addTransaction_shouldNotAddTransaction_whenTransactionIsNull() {
        assertFalse(block.addTransaction(null));
    }

    @Test
    void addTransaction_shouldNotAddTransaction_whenTransactionIsNotProcessable() {
        block = new Block("123");
        when(transaction.processTransaction()).thenReturn(false);

        assertFalse(block.addTransaction(transaction));
    }

}