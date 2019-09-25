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

public class EditRecentAdapter extends RecyclerView.Adapter<EditRecentAdapter.GetViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;
    private GetPositionInterface getPositionInterface;
    private int id =-1;

    public EditRecentAdapter(ArrayList<String> arrayList, Context context, GetPositionInterface getPosiionInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.getPositionInterface = getPosiionInterface;
    }


    @NonNull
    @Override
    public GetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_image,parent,false);
        return new GetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetViewHolder holder, final int position) {
        String path = arrayList.get(position);
        Glide.with(context).load(path).placeholder(R.drawable.load).into(holder.imgShowImage);
        if (id!=position){
            holder.lnShowImage.setBackgroundResource(R.color.colorBlack);
        }else{
            holder.lnShowImage.setBackgroundResource(R.color.colorWhite);
        }
        holder.imgShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==position){
                    holder.lnShowImage.setBackgroundResource(R.color.colorWhite);
                }else{
                    holder.lnShowImage.setBackgroundResource(R.color.colorBlack);
                    id=position;
                }
                getPositionInterface.getPosition(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GetViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgShowImage;
        private LinearLayout lnShowImage;
        public GetViewHolder(@NonNull View itemView) {
            super(itemView);
            imgShowImage = itemView.findViewById(R.id.imgShowImage);
            lnShowImage = itemView.findViewById(R.id.lnShowImage);
        }
    }
}
