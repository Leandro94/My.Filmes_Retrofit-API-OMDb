package com.leandro.myfilmes.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leandro.myfilmes.R;
import com.leandro.myfilmes.activity.FilmeDetalhes;
import com.leandro.myfilmes.model.Filme;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.CustomViewHolder> {

    private Context context;
    private List<Filme> filmeList;


    public MainRecyclerViewAdapter(Context context, List<Filme> filmes) {
        this.context = context;
        filmeList = filmes;
    }

    @NonNull
    @Override
    public MainRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_main, parent, false);

        return new MainRecyclerViewAdapter.CustomViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.CustomViewHolder holder, int position) {
        Filme filme = filmeList.get(position);
        String posterLink = filme.getPoster();

        Picasso.with(context)
                .load(posterLink)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgFilme);
    }

    @Override
    public int getItemCount() {
        return filmeList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgFilme;

        @Override
        public void onClick(View v) {
        }

        public CustomViewHolder(@NonNull final View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            imgFilme = itemView.findViewById(R.id.imgCapaFilme);;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Filme filme = filmeList.get(getAdapterPosition());
                        Intent intent = new Intent(context, FilmeDetalhes.class);
                        intent.putExtra("filme", (Serializable) filme);
                        ctx.startActivity(intent);

                        //Log.d("XXX", "ID DO CLIQUE: "+filme.getImdbID());

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });


        }
    }
}
