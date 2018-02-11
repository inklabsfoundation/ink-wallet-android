package ink.qtum.org.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SV on 04.01.2018.
 */

public class ScriptPubKey {


    @SerializedName("addresses")
    @Expose
    private List<String> addresses = null;

    @SerializedName("asm")
    @Expose
    private String asm;


    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getAsm() {
        return asm;
    }
}