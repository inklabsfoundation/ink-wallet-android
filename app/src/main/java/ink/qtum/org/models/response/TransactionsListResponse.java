package ink.qtum.org.models.response;

/**
 * Created by SV on 28.12.2017.
 */



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsListResponse {

    @SerializedName("pagesTotal")
    @Expose
    private Integer pagesTotal;
    @SerializedName("items")
    @Expose
    private List<Tx> txs = null;

    public Integer getPagesTotal() {
        return pagesTotal;
    }

    public void setPagesTotal(Integer pagesTotal) {
        this.pagesTotal = pagesTotal;
    }

    public List<Tx> getTxs() {
        return txs;
    }

    public void setTxs(List<Tx> txs) {
        this.txs = txs;
    }

}
