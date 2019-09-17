package info.ernestas.awesomecoin;

import info.ernestas.awesomecoin.util.HashUtil;
import info.ernestas.awesomecoin.util.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {

    private final String id;
    private final PublicKey recipient;
    private final float value;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.id = HashUtil.calculateSha256Hash(StringUtil.getStringFromKey(recipient) + value + parentTransactionId);
    }

    public String getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public PublicKey getRecipient() {
        return recipient;
    }
}
