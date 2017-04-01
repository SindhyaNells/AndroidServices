package com.example.service.downloadfiles.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.service.downloadfiles.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnDownload;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDownload=(Button)findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        btnClose=(Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.btn_download){
            Intent download_intent=new Intent(MainActivity.this,BoundedDownloadActivity.class);
            startActivity(download_intent);
        }else if(id==R.id.btn_close){
            MainActivity.this.finish();
        }

    }
}
