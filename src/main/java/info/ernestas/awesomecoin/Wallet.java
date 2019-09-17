package info.ernestas.awesomecoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.
    private float minimumTransaction;

    public Wallet(float minimumTransaction, PublicKey publicKey, PrivateKey privateKey) {
        this.minimumTransaction = minimumTransaction;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : AwesomeChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.getRecipient().equals(publicKey)) {
                UTXOs.put(UTXO.getId(), UTXO);
                total += UTXO.getValue();
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) { //gather balance and check funds.
            LOGGER.info("Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total > value) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value, minimumTransaction, inputs);
        newTransaction.generateSignature(privateKey);

        inputs.forEach(input -> UTXOs.remove(input.getTransactionOutputId()));

        return newTransaction;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
