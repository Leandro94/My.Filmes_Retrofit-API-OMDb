package com.leandro.myfilmes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leandro.myfilmes.R;
import com.leandro.myfilmes.adapter.MainRecyclerViewAdapter;
import com.leandro.myfilmes.api.DataService;
import com.leandro.myfilmes.model.Filme;
import com.leandro.myfilmes.model.ResultadoPesquisa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.leandro.myfilmes.util.Constantes.BASE_URL;


public class MainActivity extends AppCompatActivity {


    private Retrofit retrofit;
    private DataService service;
    private List<Filme> listaDeFilmes;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private RecyclerView recyclerViewMain;
    public static SQLiteDatabase banco;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = findViewById(R.id.textToolbar);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);

        titulo.setText("My.Filmes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        configurarRecycler();
        criarBanco();

    }
    //----------------------------------------------------------------------------------------------
    private void configurarRecycler(){
        listaDeFilmes = new ArrayList<>();
        recyclerViewMain = findViewById(R.id.recyclerMain);
        recyclerViewMain.setHasFixedSize(true);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        String[] array = new String[7];
        array[0]="cars";
        array[1]="batman";
        array[2]="x-men";
        array[3]="wolverine";
        array[4]="avengers";
        array[5]="supernatural";
        array[6]="star wars";
        String randomStr = array[new Random().nextInt(array.length)];

        pesquisarRetrofit(randomStr);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, listaDeFilmes);
        recyclerViewMain.setAdapter(mainRecyclerViewAdapter);
        mainRecyclerViewAdapter.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------------------------
    private void pesquisarRetrofit(String titulo) {
        listaDeFilmes.clear();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataService servicosRetrofit = retrofit.create(DataService.class);

        Call<ResultadoPesquisa> call = servicosRetrofit.recuperarFilmesTitulo(titulo);

        call.enqueue(new Callback<ResultadoPesquisa>() {
            @Override
            public void onResponse(Call<ResultadoPesquisa> call, retrofit2.Response<ResultadoPesquisa> response) {

                if (response.isSuccessful()) {
                    ResultadoPesquisa resultadoPesquisa = response.body();

                    List<Filme>listaFilmes = resultadoPesquisa.getSearch();

                    if (listaFilmes != null) {
                        for (int i = 0; i < listaFilmes.size(); i++) {
                            Filme filme;
                            filme = listaFilmes.get(i);
                            listaDeFilmes.add(filme);
                            mainRecyclerViewAdapter.notifyDataSetChanged();

                        }
                    } else
                        Log.i("XXX", "Retornou lista nula");
                }
            }

            @Override
            public void onFailure(Call<ResultadoPesquisa> call, Throwable t) {
                Log.i("XXX", "Erro: " + t.getMessage());
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        menu.add(1, 1, 1, criarMenuItem(
                getResources().getDrawable(R.drawable.ic_favoritos_roxo_24dp),
                "Favoritos",
                getResources().getColor(R.color.colorPrimary)));

        menu.add(1, 2, 2, criarMenuItem(
                getResources().getDrawable(R.drawable.ic_sair_roxo_24dp),
                "Sair",
                getResources().getColor(R.color.colorPrimary)));


        return super.onCreateOptionsMenu(menu);
    }
    //----------------------------------------------------------------------------------------------
    //Método para configurar itens de menu | Imagem + texto título + cor texto
    private CharSequence criarMenuItem(Drawable r, String title, int color) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);

        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        int i = title.indexOf(title)+4;
        sb.setSpan(colorSpan, i, i + title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()){
            case R.id.menuPesquisa:
                intent = new Intent(MainActivity.this, PesquisaActivity.class);
                startActivity(intent);
                //showInputDialog();
                break;
            case 1:
                intent = new Intent(MainActivity.this, FavoritosActivity.class);
                startActivity(intent);
                break;
            case 2:
                sair();

        }
        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------------------------------------------------
    private void sair(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.alert_sair,null);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button  btnSair = view.findViewById(R.id.btnOk);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alertDialog.cancel();
            }
        });

      btnSair.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              acoes_finalizar();
          }
      });

        alertDialog.show();
    }

    //--------------------------------------------------------------------------------------------------
    private void acoes_finalizar()
    {
        this.finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
       sair();
    }

    //----------------------------------------------------------------------------------------------
    private void criarBanco(){
        try {
            banco = openOrCreateDatabase("myfilmes",MODE_PRIVATE,null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS Filme(imdbID VARCHAR PRIMARY KEY)");
            //Log.i("XXX", "banco criado");

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Ocorreu um erro ao criar o banco de dados.", Toast.LENGTH_SHORT).show();
            //Log.i("XXX", "banco error: "+e.getMessage());

        }
    }


}
