package com.leandro.myfilmes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.leandro.myfilmes.R;
import com.leandro.myfilmes.adapter.FilmeRecyclerViewAdapter;
import com.leandro.myfilmes.api.DataService;
import com.leandro.myfilmes.model.Filme;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.leandro.myfilmes.util.Constantes.BASE_URL;

public class FavoritosActivity extends AppCompatActivity {

    private FilmeRecyclerViewAdapter filmeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private List<Filme> listaDeFilmes;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);

        titulo = findViewById(R.id.textToolbar);
        titulo.setText("Favoritos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        configurarObjetos();

        }

    @Override
    protected void onRestart() {
        super.onRestart();
        listaDeFilmes.clear();
        filmeRecyclerViewAdapter.notifyDataSetChanged();
        pesquisarRetrofit();
       // Log.i("XXX", "ON RESTART");
    }

    //--------------------------------------------------------------------------------------------
    private void configurarObjetos(){
        listaDeFilmes = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filmeRecyclerViewAdapter = new FilmeRecyclerViewAdapter(this, listaDeFilmes);
        recyclerView.setAdapter(filmeRecyclerViewAdapter);
        filmeRecyclerViewAdapter.notifyDataSetChanged();
        pesquisarRetrofit();
    }
    //---------------------------------------------------------------------------------------------
    private void pesquisarRetrofit() {
        Cursor cursor = MainActivity.banco.rawQuery("SELECT*FROM Filme",null);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataService servicosRetrofit = retrofit.create(DataService.class);
        if((cursor != null) && (cursor.getCount() > 0)){
            while (cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex("imdbID"));
                Call<Filme> call = servicosRetrofit.recuperarFilmesId(id);

                call.enqueue(new Callback<Filme>() {
                    @Override
                    public void onResponse(Call<Filme> call, retrofit2.Response<Filme> response) {

                        if (response.isSuccessful()) {
                            Filme filme = response.body();
                            filme.setType("Tipo: " + filme.getType());
                            filme.setYear("Ano: " + filme.getYear());
                            listaDeFilmes.add(filme);
                            filmeRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            Log.i("XXX", "Objeto filme nulo");
                        }
                    }

                    @Override
                    public void onFailure(Call<Filme> call, Throwable t) {
                        Log.i("XXX", "Erro: " + t.getMessage());
                    }

                });

            }
            cursor.close();

        }
        else{
            Toast.makeText(getApplicationContext(), "Você não possui nada salvo na lista de favoritos.", Toast.LENGTH_SHORT).show();

        }
    }
}
