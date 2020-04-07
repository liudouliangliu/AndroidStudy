package com.yunshitu.activitystudy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : liudouliang
 * @date : 2020/3/9 14:37
 * @ des   :
 */
public class MessegeAdapter extends RecyclerView.Adapter<MessegeAdapter.MyHolder> {

    private List<String> msgs = new ArrayList<>();

    public void setData(List<String> data){
        if (data==null){
            data = new ArrayList<>();
        }
        msgs = data;
    }

    public void addMsg(String msg){
        msgs.add(msg);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessegeAdapter.MyHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder( @NonNull MessegeAdapter.MyHolder holder, int position ) {
        String msg = msgs.get(position);
        holder.msg.setText(msg);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        private TextView msg;

        public MyHolder( @NonNull View itemView ) {
            super(itemView);
            msg = itemView.findViewById(R.id.tv_msg);
        }
    }
}
