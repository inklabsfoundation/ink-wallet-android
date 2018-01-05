package ink.qtum.org.models;

import org.bitcoinj.core.Coin;

import java.math.BigDecimal;

/**
 * Created by SV on 03.01.2018.
 */

public class TransactionHistory {
    private String coinId;
    private String fromAddress;
    private String toAddress;
    private long timestamp;
    private BigDecimal fees;
    private BigDecimal value;
    private long valueSat;
    private String txHash;
    private String description;
    private int blockHeight;
    private boolean isInTx;

    public String getCoinId() {
        return coinId;
    }

    public boolean isInTx() {
        return isInTx;
    }

    public void setIsInTx(boolean inTx) {
        isInTx = inTx;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public long getValueSat() {
        return valueSat;
    }

    public void setValueSat(long valueSat) {
        this.valueSat = valueSat;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    @Override
    public String toString() {
        return "from - " + fromAddress + "\n"
                + "to - " + toAddress + "\n"
                + "value - " + Coin.parseCoin(value.toString());
    }
}
