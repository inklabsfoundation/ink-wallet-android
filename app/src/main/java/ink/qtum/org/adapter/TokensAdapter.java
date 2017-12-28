package ink.qtum.org.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ink.qtum.org.inkqtum.R;

public class TokensAdapter extends RecyclerView.Adapter<TokensItemHolder> {

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    OnItemClickListener listener;

    private List<Integer> cryptoList;
    private View view;

    public TokensAdapter(List<Integer> transList) {
        this.cryptoList = transList;
    }

    public TokensAdapter() {
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
    public TokensItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token, parent, false);
        return new TokensItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TokensItemHolder holder, final int position) {
        holder.vRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

}

class TokensItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_root)
    LinearLayout vRoot;


    TokensItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}


