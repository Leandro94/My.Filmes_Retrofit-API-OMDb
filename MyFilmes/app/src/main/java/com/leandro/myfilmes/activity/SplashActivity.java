package com.leandro.myfilmes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leandro.myfilmes.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waitHandler.postDelayed(waitCallbackMain, 3000);
    }

    private final Handler waitHandler = new Handler();
    private Runnable waitCallbackMain = new Runnable() {
        @Override
        public void run() {
            if(isOnline()){
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Você não possui conexão com a internet!", Toast.LENGTH_SHORT).show();
                sair();

            }

        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    //----------------------------------------------------------------------------------------------
    private void sair(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.alert_con,null);

        Button btnOk = view.findViewById(R.id.btnOk);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.setCancelable(false);

        btnOk.setOnClickListener(new View.OnClickListener() {
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
}
