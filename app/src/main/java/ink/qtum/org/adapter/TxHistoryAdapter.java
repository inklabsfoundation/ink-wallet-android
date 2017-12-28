package ink.qtum.org.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ink.qtum.org.inkqtum.R;

public class TxHistoryAdapter extends RecyclerView.Adapter<TxHistoryItemHolder> {

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    OnItemClickListener listener;

    private List<Integer> cryptoList;
    private View view;

    public TxHistoryAdapter(List<Integer> transList) {
        this.cryptoList = transList;
    }

    public TxHistoryAdapter() {
        this.cryptoList = new ArrayList<>();
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Integer> transList) {
        this.cryptoList = transList;
        notifyDataSetChanged();
    }

    @Override
    public TxHistoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tx_history, parent, false);
        return new TxHistoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TxHistoryItemHolder holder, final int position) {

        if (isTransactionIncome(position)){
            holder.ivStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_incoming_transaction));
            holder.tvBalance.setTextColor(view.getResources().getColor(R.color.blueTextColor));
        } else {
            holder.ivStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_outgoing_transaction));
            holder.tvBalance.setTextColor(view.getResources().getColor(R.color.turquoiseColor));
        }
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
        return position % 2 == 0;
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
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



