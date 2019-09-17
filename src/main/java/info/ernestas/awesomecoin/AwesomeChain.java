package info.ernestas.awesomecoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;

public class AwesomeChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwesomeChain.class);

    private static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    private static int difficulty = 3;
    private static final float MINIMUM_TRANSACTION = 0.1f;
    private static Wallet walletA;
    private static Wallet walletB;
    private static Transaction genesisTransaction;

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        KeyPair walletAKeys = generateKeyPair();
        walletA = new Wallet(MINIMUM_TRANSACTION, walletAKeys.getPublic(), walletAKeys.getPrivate());
        KeyPair walletBKeys = generateKeyPair();
        walletB = new Wallet(MINIMUM_TRANSACTION,  walletBKeys.getPublic(), walletBKeys.getPrivate());
        KeyPair coinbaseKeys = generateKeyPair();
        Wallet coinbase = new Wallet(MINIMUM_TRANSACTION, coinbaseKeys.getPublic(), coinbaseKeys.getPrivate());

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, MINIMUM_TRANSACTION, null);
        genesisTransaction.generateSignature(coinbase.getPrivateKey());     //manually sign the genesis transaction
        genesisTransaction.setTransactionId("0"); //manually set the transaction id
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.

        LOGGER.info("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.getHash());
        LOGGER.info("WalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        addBlock(block1);
        LOGGER.info("WalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

        Block block2 = new Block(block1.getHash());
        LOGGER.info("WalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
        addBlock(block2);
        LOGGER.info("WalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

        Block block3 = new Block(block2.getHash());
        LOGGER.info("WalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.getPublicKey(), 20));
        LOGGER.info("WalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
