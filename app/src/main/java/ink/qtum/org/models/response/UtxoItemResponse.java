
package ink.qtum.org.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UtxoItemResponse {

    @SerializedName("txid")
    @Expose
    private String txid;
    @SerializedName("vout")
    @Expose
    private Long vout;
    @SerializedName("satoshis")
    @Expose
    private Long satoshis;
    @SerializedName("isStake")
    @Expose
    private Boolean isStake;
    @SerializedName("height")
    @Expose
    private int height;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Long getVout() {
        return vout;
    }

    public void setVout(Long vout) {
        this.vout = vout;
    }

    public Long getSatoshis() {
        return satoshis;
    }

    public void setSatoshis(Long satoshis) {
        this.satoshis = satoshis;
    }

    public Boolean getIsStake() {
        return isStake;
    }

    public void setIsStake(Boolean isStake) {
        this.isStake = isStake;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}