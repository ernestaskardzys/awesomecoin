package info.ernestas.awesomecoin;

public class TransactionInput {

    private final String transactionOutputId;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
}
