package com.example.smokenameart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smokenameart.R;
import com.example.smokenameart.interfacee.GetPositionInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PickColorAdapter extends RecyclerView.Adapter<PickColorAdapter.GetViewHolder> {
    private ArrayList<Integer> integerArrayList;
    private Context context;
    private GetPositionInterface getPositionInterface;
    private int id =-1;

    public PickColorAdapter(ArrayList<Integer> integerArrayList, Context context, GetPositionInterface getPositionInterface) {
        this.integerArrayList = integerArrayList;
        this.context = context;
        this.getPositionInterface = getPositionInterface;
    }
    public void changeId(){
        id=-1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_color,parent,false);

        return new GetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetViewHolder holder, final int position) {
        int path = integerArrayList.get(position);
        holder.imgItem.setImageResource(path);
        if (id!=position){
            holder.imgBord.setImageResource(R.drawable.img_white);
        }else{
            holder.imgBord.setImageResource(R.drawable.img_black);
        }
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==position){
                    holder.imgBord.setImageResource(R.drawable.img_black);
                }else{
                    holder.imgBord.setImageResource(R.drawable.img_white);
                    id=position;
                }
                getPositionInterface.getPosition(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return integerArrayList.size();
    }

    public class GetViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnItem;
        private ImageView imgItem;
        private CircleImageView imgBord;
        public GetViewHolder(@NonNull View itemView) {
            super(itemView);
            lnItem = itemView.findViewById(R.id.lnItemPickColor);
            imgItem = itemView.findViewById(R.id.imgitemPickColor);
            imgBord = itemView.findViewById(R.id.imgBord);
        }
    }
}
