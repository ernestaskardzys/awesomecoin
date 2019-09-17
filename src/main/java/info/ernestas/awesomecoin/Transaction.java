package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashUtil;
import info.ernestas.awesomecoin.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    private String transactionId; // this is also the hash of the transaction.
    private final PublicKey sender; // senders address/public key.
    private final PublicKey recipient; // Recipients address/public key.
    private final float value;
    private byte[] signature;

    private final float minimumTransaction;
    private final List<TransactionInput> inputs;
    private List<TransactionOutput> outputs = new ArrayList<>();

    private int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, float minimumTransaction, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.minimumTransaction = minimumTransaction;
        this.inputs = inputs;
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            LOGGER.info("#Transaction Signature failed to verify");
            return false;
        }

        for (TransactionInput i : inputs) {
            i.setUTXO(AwesomeChain.UTXOs.get(i.getTransactionOutputId()));
        }

        if (getInputsValue() < minimumTransaction) {
            LOGGER.info("Transaction Inputs to small:  {}", getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(recipient, value, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(sender, leftOver, transactionId)); //send the left over 'change' back to sender

        outputs.forEach(o -> AwesomeChain.UTXOs.put(o.getId(), o));
        inputs.stream().filter(i -> i.getUTXO() != null).forEach(i -> AwesomeChain.UTXOs.remove(i.getUTXO().getId()));

        return true;
    }

    public float getInputsValue() {
        return inputs.stream().filter(i -> i.getUTXO() != null).map(i -> i.getUTXO().getValue()).reduce(0f, Float::sum);
    }

    public float getOutputsValue() {
        return outputs.stream().map(o -> o.getValue()).reduce(0f, Float::sum);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;

        try {
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = data.getBytes();
            dsa.update(strByte);
            signature = dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(sender);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    private String calculateHash() {
        sequence++;
        return HashUtil.calculateSha256Hash(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        value +
                        sequence
        );
    }
}
