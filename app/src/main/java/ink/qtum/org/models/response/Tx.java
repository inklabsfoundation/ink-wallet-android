package ink.qtum.org.models.response;

/**
 * Created by SV on 29.12.2017.
 */


import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tx {

    @SerializedName("txid")
    @Expose
    private String txid;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("locktime")
    @Expose
    private Integer locktime;
    @SerializedName("vin")
    @Expose
    private List<Vin> vin = null;
    @SerializedName("vout")
    @Expose
    private List<Vout> vout = null;
    @SerializedName("blockhash")
    @Expose
    private String blockhash;
    @SerializedName("blockheight")
    @Expose
    private Integer blockheight;
    @SerializedName("confirmations")
    @Expose
    private Integer confirmations;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("valueOut")
    @Expose
    private BigDecimal valueOut;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("valueIn")
    @Expose
    private BigDecimal valueIn;
    @SerializedName("fees")
    @Expose
    private BigDecimal fees;

    private BigDecimal changeInBalance;

    public BigDecimal getChangeInBalance() {
        return changeInBalance;
    }

    public void setChangeInBalance(BigDecimal changeInBalance) {
        this.changeInBalance = changeInBalance;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLocktime() {
        return locktime;
    }

    public void setLocktime(Integer locktime) {
        this.locktime = locktime;
    }

    public List<Vin> getVin() {
        return vin;
    }

    public void setVin(List<Vin> vin) {
        this.vin = vin;
    }

    public List<Vout> getVout() {
        return vout;
    }

    public void setVout(List<Vout> vout) {
        this.vout = vout;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public Integer getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(Integer blockheight) {
        this.blockheight = blockheight;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public BigDecimal getValueOut() {
        return valueOut;
    }

    public void setValueOut(BigDecimal valueOut) {
        this.valueOut = valueOut;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public BigDecimal getValueIn() {
        return valueIn;
    }

    public void setValueIn(BigDecimal valueIn) {
        this.valueIn = valueIn;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

}
