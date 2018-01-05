package ink.qtum.org.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class Vout {

    @SerializedName("value")
    @Expose
    private BigDecimal value;
    @SerializedName("scriptPubKey")
    @Expose
    private ScriptPubKey scriptPubKey;

    private boolean isOwnAddress = false;

    public boolean isOwnAddress() {
        return isOwnAddress;
    }

    public void setOwnAddress(boolean ownAddress) {
        isOwnAddress = ownAddress;
    }


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ScriptPubKey getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(ScriptPubKey scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public String getAddress() {
        if (scriptPubKey != null && scriptPubKey.getAddresses() != null) {
            return scriptPubKey.getAddresses().get(0);
        } else {
            return "";
        }
    }
}