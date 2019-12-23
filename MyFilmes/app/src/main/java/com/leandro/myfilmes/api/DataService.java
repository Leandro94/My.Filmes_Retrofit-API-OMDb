package com.leandro.myfilmes.api;

import com.leandro.myfilmes.model.Filme;
import com.leandro.myfilmes.model.ResultadoPesquisa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.leandro.myfilmes.util.Constantes.KEY;
import static com.leandro.myfilmes.util.Constantes.PARAMETRO_01_API_KEY;

public interface DataService {

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET(PARAMETRO_01_API_KEY+KEY)
    Call<ResultadoPesquisa> recuperarFilmesTitulo(@Query("s") String title);//Faz chamada web e o retorno será do tipo FILME, Query tem que ter mesmo nome do parametro da url

    @GET(PARAMETRO_01_API_KEY+KEY)
    Call<Filme> recuperarFilmesId(@Query("i") String id);

}
