package info.ernestas.awesomecoin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @Mock
    private PublicKey publicKey;

    @Mock
    private TransactionOutput output;

    @Mock
    private TransactionOutput output2;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(publicKey, publicKey, 10, 0.01f, new ArrayList<>());
    }

    @Test
    void processTransaction() {
    }

    @Test
    void getInputsValue_WithNoValue_whenThereIsNoTransactionOutput() {
        when(output.getValue()).thenReturn(10f);

        TransactionInput input = mock(TransactionInput.class);
        when(input.getUTXO()).thenReturn(output);
        TransactionInput input2 = mock(TransactionInput.class);

        transaction = new Transaction(publicKey, publicKey, 10, 0.01f, Arrays.asList(input, input2));

        assertEquals(10, transaction.getInputsValue());
    }

    @Test
    void getInputsValue() {

    }

    @Test
    void getOutputsValue() {
        when(output.getValue()).thenReturn(10f);
        when(output2.getValue()).thenReturn(2f);
        transaction.setOutputs(Arrays.asList(output, output2));

        assertEquals(12, transaction.getOutputsValue());
    }

    @Test
    void generateSignature() {
    }

    @Test
    void verifySignature() {
    }

    @Test
    void getTransactionId() {
    }

    @Test
    void setTransactionId() {
    }

    @Test
    void getSender() {
    }

    @Test
    void getRecipient() {
    }

    @Test
    void getValue() {
    }

    @Test
    void getSignature() {
    }

    @Test
    void getInputs() {
    }

    @Test
    void getOutputs() {
    }

    @Test
    void getSequence() {
    }

}