package com.example.smokenameart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smokenameart.R;
import com.example.smokenameart.interfacee.GetPositionInterface;

import java.util.ArrayList;

public class PickFontAdapter extends RecyclerView.Adapter<PickFontAdapter.GetViewHolder> {
    private ArrayList<Typeface> typefaceArrayList;
    private Context context;
    private GetPositionInterface getPositionInterface;
    private int id =-1;

    public PickFontAdapter(ArrayList<Typeface> typefaceArrayList, Context context, GetPositionInterface getPositionInterface) {
        this.typefaceArrayList = typefaceArrayList;
        this.context = context;
        this.getPositionInterface = getPositionInterface;
    }

    @NonNull
    @Override
    public GetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font,parent,false);

        return new GetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetViewHolder holder, final int position) {
        Typeface path = typefaceArrayList.get(position);
        holder.txtItemFont.setTypeface(path);
        if (id!=position){
            holder.viewFont.setBackgroundResource(R.color.colorWhite);
        }else{
            holder.viewFont.setBackgroundResource(R.color.colorRed);
        }
        holder.txtItemFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==position){
                    holder.viewFont.setBackgroundResource(R.color.colorRed);
                }else{
                    holder.viewFont.setBackgroundResource(R.color.colorWhite);
                    id=position;
                }
                getPositionInterface.getPosition(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typefaceArrayList.size();
    }

    public class GetViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemFont;
        private LinearLayout lnFont;
        private View viewFont;
        public GetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemFont = itemView.findViewById(R.id.txtitemfont);
            lnFont = itemView.findViewById(R.id.lnFont);
            viewFont = itemView.findViewById(R.id.viewFont);
        }
    }
}
