package ink.qtum.org.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.utils.TextUtils;

import static ink.qtum.org.models.Constants.BALANCE_SHOW_PATTERN;

public class TxHistoryAdapter extends RecyclerView.Adapter<TxHistoryItemHolder> {

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    OnItemClickListener listener;

    private List<TransactionHistory> originalTxList;
    private List<TransactionHistory> filteredTxList;
    private View view;

    public TxHistoryAdapter(List<TransactionHistory> transList) {
        this.originalTxList = transList;
        filteredTxList = new ArrayList<>(originalTxList);
    }

    public void filterReceived(){
        filteredTxList.clear();
        for (TransactionHistory tx : originalTxList) {
            if (tx.isInTx()){
                filteredTxList.add(tx);
            }
        }
        notifyDataSetChanged();
    }

    public void filterSent(){
        filteredTxList.clear();
        for (TransactionHistory tx : originalTxList) {
            if (!tx.isInTx()){
                filteredTxList.add(tx);
            }
        }
        notifyDataSetChanged();
    }

    public void clearFilter(){
        filteredTxList.clear();
        filteredTxList.addAll(originalTxList);
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<TransactionHistory> transList) {
        this.originalTxList = transList;
        notifyDataSetChanged();
    }

    @Override
    public TxHistoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tx_history, parent, false);
        return new TxHistoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TxHistoryItemHolder holder, final int position) {
        TransactionHistory item = filteredTxList.get(position);
        char txSign;
        if (isTransactionIncome(position)) {
            holder.ivStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_incoming_transaction));
            holder.tvBalance.setTextColor(view.getResources().getColor(R.color.blueTextColor));
            txSign = '+';
        } else {
            holder.ivStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_outgoing_transaction));
            holder.tvBalance.setTextColor(view.getResources().getColor(R.color.turquoiseColor));
            txSign = '-';
        }
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(BALANCE_SHOW_PATTERN, symbols);
        holder.tvBalance.setText(String.format("%s%s %s", txSign, decimalFormat.format(item.getValue()),
                item.getCoinId()));
        holder.tvHash.setText(TextUtils.txHashToShort(item.getTxHash()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        holder.tvDate.setText(dateFormat.format(new Date(item.getTimestamp())));
        holder.vRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemClick(position);
                }
            }
        });
    }

    private boolean isTransactionIncome(int position) {
        return filteredTxList.get(position).isInTx();
    }

    @Override
    public int getItemCount() {
        return filteredTxList.size();
    }

}

class TxHistoryItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_root)
    LinearLayout vRoot;
    @BindView(R.id.iv_tx_status)
    ImageView ivStatus;
    @BindView(R.id.tv_tx_hash)
    TextView tvHash;
    @BindView(R.id.tv_tx_date)
    TextView tvDate;
    @BindView(R.id.tv_tx_balance)
    TextView tvBalance;


    TxHistoryItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}



