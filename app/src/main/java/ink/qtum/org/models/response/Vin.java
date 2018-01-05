package ink.qtum.org.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Vin {


    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("valueSat")
    @Expose
    private long valueSat;
    @SerializedName("value")
    @Expose
    private BigDecimal value;

    private boolean isOwnAddress = false;

    public boolean isOwnAddress() {
        return isOwnAddress;
    }

    public void setOwnAddress(boolean ownAddress) {
        isOwnAddress = ownAddress;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getValueSat() {
        return valueSat;
    }

    public void setValueSat(Integer valueSat) {
        this.valueSat = valueSat;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}