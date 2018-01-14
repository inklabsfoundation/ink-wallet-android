package ink.qtum.org.models.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsInkListResponse {

    @SerializedName("addresses")
    @Expose
    private List<String> addresses;
    @SerializedName("items")
    @Expose
    private List<TxInk> txs = null;

    public List<String> getPagesTotal() {
        return addresses;
    }

    public void setPagesTotal(List<String> pagesTotal) {
        this.addresses = pagesTotal;
    }

    public List<TxInk> getTxs() {
        return txs;
    }

    public void setTxs(List<TxInk> txs) {
        this.txs = txs;
    }

}
