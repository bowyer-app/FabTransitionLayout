package com.bowyer.fabtransitionlayout.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerHolder> {

  List<String> mList;

  public RecyclerViewAdapter(List<String> list, Context context) {
    this.mList = list;
  }

  @Override
  public RecyclerViewAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false);
    RecyclerHolder viewHolder = new RecyclerHolder(v);
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerViewAdapter.RecyclerHolder holder, int position) {
    TextView tv = (TextView) holder.itemView;
    tv.setText(mList.get(position));
  }

  @Override public int getItemCount() {
    return mList.size();
  }

  public static class RecyclerHolder extends RecyclerView.ViewHolder {

    public RecyclerHolder(View itemView) {
      super(itemView);
    }
  }
}
