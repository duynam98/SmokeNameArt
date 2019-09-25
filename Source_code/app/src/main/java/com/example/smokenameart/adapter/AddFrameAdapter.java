package com.example.smokenameart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smokenameart.R;
import com.example.smokenameart.interfacee.GetPositionInterface;

import java.util.ArrayList;

public class AddFrameAdapter extends RecyclerView.Adapter<AddFrameAdapter.GetViewHolder> {
    private ArrayList<String> pathList;
    private Context context;
    private int id =0;
    private GetPositionInterface getPositionInterface;

    public AddFrameAdapter(ArrayList<String> pathList, Context context, GetPositionInterface getPositionInterface) {
        this.pathList = pathList;
        this.context = context;
        this.getPositionInterface = getPositionInterface;
    }
    public void setId(int id){
        this.id =id;
        notifyDataSetChanged();
    }
    public void changedList(ArrayList<String> pathList){
        this.pathList = pathList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_name, parent, false);
        return new GetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetViewHolder holder, final int position) {
        String path = pathList.get(position);
        Glide.with(context).load(path).placeholder(R.drawable.load).into(holder.imgItem);
        holder.imgItem.setBackgroundResource(R.color.colorBlack);
        if (id!=position){
            holder.lnItem.setBackgroundResource(R.color.colorBlack);
        }else{
            holder.lnItem.setBackgroundResource(R.color.colorWhite);
        }
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==position){
                    holder.lnItem.setBackgroundResource(R.color.colorWhite);
                }else{
                    holder.lnItem.setBackgroundResource(R.color.colorBlack);
                    id=position;
                }
                getPositionInterface.getPosition(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public class GetViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private LinearLayout lnItem;
        public GetViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem= itemView.findViewById(R.id.imgItem);
            lnItem = itemView.findViewById(R.id.lnItem);
        }
    }
}
