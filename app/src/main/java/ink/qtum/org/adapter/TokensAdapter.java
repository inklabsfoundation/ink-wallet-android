package ink.qtum.org.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ink.qtum.org.inkqtum.R;

public class TokensAdapter extends RecyclerView.Adapter<TokensItemHolder> {

    public interface OnItemClickListener {
        void OnItemClick(int position, String name, String code);
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

    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

}

class TokensItemHolder extends RecyclerView.ViewHolder {




    TokensItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}


