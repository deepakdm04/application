package com.example.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.Utils.Tools;
import com.example.demo.model.category_model;


import java.util.ArrayList;
import java.util.List;

public class AdapterListDashboard extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<category_model> items = new ArrayList<>();
    private List<category_model> filtereditems=new ArrayList<>();
    private OnLoadMoreListener onLoadMoreListener;
    private String listtype;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, category_model obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListDashboard(Context context, List<category_model> items,String listtype) {
        this.items = items;
        this.filtereditems=items;
        this.listtype = listtype;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView counter;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if(listtype.equals("1"))
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_list, parent, false);
        }
        else
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem, parent, false);
        }
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        category_model obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.name.setText(obj.name);

            Tools.displayImageOriginal(ctx, view.image, obj.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    items = filtereditems;
                } else {
                    List<category_model> filteredList = new ArrayList<>();
                    for (category_model row : filtereditems) {
                        if (row.name.startsWith(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }
                    items = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (ArrayList<category_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}