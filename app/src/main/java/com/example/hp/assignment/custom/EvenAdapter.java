package com.example.hp.assignment.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.assignment.R;
import com.example.hp.assignment.model.Even;

import java.util.List;

/**
 * Created by hp on 10/20/2017.
 */

public class EvenAdapter extends RecyclerView.Adapter<EvenAdapter.EvenViewHolder> {
    private List<Even> evens;
    private OnEvenClickListener onEvenClickListener;

    public EvenAdapter(List<Even> evens) {
        this.evens = evens;
    }

    @Override
    public EvenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(viewType, parent, false);
        return new EvenViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_evens;
    }

    @Override
    public void onBindViewHolder(final EvenViewHolder holder, int position) {
        Even even=evens.get(position);
        if (even.getmTitle().isEmpty()) {
            holder.tvTitle.setText(R.string.untitle);
        }else {
            holder.tvTitle.setText(even.getmTitle());
        }
        holder.tvContent.setText(even.getmContent());
        holder.tvTime.setText(even.getmAlarm());
        if (even.getmDate()=="") {
            holder.ivClock.setVisibility(View.INVISIBLE);
        }else {
            holder.ivClock.setVisibility(View.VISIBLE);
        }
        switch (even.getmBackground()) {
            case 1:
                holder.llItemEven.setBackgroundResource(R.drawable.shape_while);
                break;
            case 2:
                holder.llItemEven.setBackgroundResource(R.drawable.shape_cyan);
                break;
            case 3:
                holder.llItemEven.setBackgroundResource(R.drawable.shape_orange);
                break;
            case 4:
                holder.llItemEven.setBackgroundResource(R.drawable.shape_blue);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEvenClickListener.clicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null!=evens ? evens.size() : 0;
    }


    class EvenViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        ImageView ivClock;
        LinearLayout llItemEven;
        public EvenViewHolder(View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvContent=itemView.findViewById(R.id.tv_content);
            tvTime=itemView.findViewById(R.id.tv_alarm);
            llItemEven=itemView.findViewById(R.id.ll_item_evens);
            ivClock=itemView.findViewById(R.id.iv_clock);
        }
    }

    public void setOnEvenClickListener(OnEvenClickListener listener) {
        onEvenClickListener=listener;
    }
    public interface OnEvenClickListener {
        void clicked(int position);
    }
}
