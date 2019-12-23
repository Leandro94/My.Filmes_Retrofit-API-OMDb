package com.leandro.myfilmes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.leandro.myfilmes.R;
import com.leandro.myfilmes.adapter.FilmeRecyclerViewAdapter;
import com.leandro.myfilmes.api.DataService;
import com.leandro.myfilmes.model.Filme;
import com.leandro.myfilmes.model.ResultadoPesquisa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.leandro.myfilmes.util.Constantes.BASE_URL;

public class PesquisaActivity extends AppCompatActivity {
    private ImageView imgPesquisar;
    private EditText edtPesquisa;


    private FilmeRecyclerViewAdapter filmeRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Filme> listaDeFilmes;
    private List<Filme> listaFavoritos;
    private Retrofit retrofit;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        carregarListaFavoritos();
        titulo = findViewById(R.id.textToolbar);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        //toolbar.setTitle("Pesquisa");
        titulo.setText("Pesquisa");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgPesquisar = findViewById(R.id.imgPesquisa);
        edtPesquisa = findViewById(R.id.txtPesquisa);

        configurarObjetos();
    }
    //--------------------------------------------------------------------------------------------------

    public void pesquisar(View v){
        if(!TextUtils.isEmpty(edtPesquisa.getText().toString())){
            pesquisarRetrofit(edtPesquisa.getText().toString());
        }
        else{
            Toast.makeText(getApplicationContext(), "Informe um título para pesquisar!", Toast.LENGTH_SHORT).show();
        }
    }
    //-----------------------------------------------------------------------------------------------------
    private void configurarObjetos(){
        listaDeFilmes = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filmeRecyclerViewAdapter = new FilmeRecyclerViewAdapter(this, listaDeFilmes);
        recyclerView.setAdapter(filmeRecyclerViewAdapter);
        filmeRecyclerViewAdapter.notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------------------
    private void carregarListaFavoritos(){
        listaFavoritos = new ArrayList<Filme>();
        Cursor cursor = MainActivity.banco.rawQuery("SELECT*FROM Filme",null);
        while (cursor.moveToNext()){
            Filme f = new Filme();
            f.setImdbID(cursor.getString(cursor.getColumnIndex("imdbID")));
            listaFavoritos.add(f);
        }
    }
    //---------------------------------------------------------------------------------------------
    private void pesquisarRetrofit(String searchTerm) {

        listaDeFilmes.clear();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataService servicosRetrofit = retrofit.create(DataService.class);

        Call<ResultadoPesquisa> call = servicosRetrofit.recuperarFilmesTitulo(searchTerm);

        call.enqueue(new Callback<ResultadoPesquisa>() {
            @Override
            public void onResponse(Call<ResultadoPesquisa> call, retrofit2.Response<ResultadoPesquisa> response) {

                if (response.isSuccessful()) {
                    ResultadoPesquisa resultadoPesquisa = response.body();

                    List<Filme>listaFilmes = resultadoPesquisa.getSearch();

                    if (listaFilmes != null) {
                        for (int i = 0; i < listaFilmes.size(); i++) {

                            Filme filme;
                            listaFilmes.get(i).setType("Tipo: "+listaFilmes.get(i).getType());
                            listaFilmes.get(i).setYear("Ano: "+listaFilmes.get(i).getYear());
                            filme = listaFilmes.get(i);
                            listaDeFilmes.add(filme);
                            filmeRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Nenhum resultado encontrado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultadoPesquisa> call, Throwable t) {
                Log.i("XXX", "Erro: " + t.getMessage());
            }

        });
    }
}
