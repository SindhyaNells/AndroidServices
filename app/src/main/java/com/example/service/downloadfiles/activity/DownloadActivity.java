package com.example.service.downloadfiles.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.service.downloadfiles.service.PdfBoundService;
import com.example.service.downloadfiles.service.PdfIntentService;
import com.example.service.downloadfiles.R;
import com.example.service.downloadfiles.service.PdfService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sindhya on 3/25/17.
 */
public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{

    IntentFilter intentFilter;

    EditText editTextPdf1;
    EditText editTextPdf2;
    EditText editTextPdf3;
    EditText editTextPdf4;
    EditText editTextPdf5;

    Button btnDownloadFiles;
    ArrayList<String> urlList=new ArrayList<>();

    //ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download);
        editTextPdf1=(EditText)findViewById(R.id.edit_text_pdf1);
        editTextPdf2=(EditText)findViewById(R.id.edit_text_pdf2);
        editTextPdf3=(EditText)findViewById(R.id.edit_text_pdf3);
        editTextPdf4=(EditText)findViewById(R.id.edit_text_pdf4);
        editTextPdf5=(EditText)findViewById(R.id.edit_text_pdf5);

        btnDownloadFiles=(Button)findViewById(R.id.btn_download_files);
        btnDownloadFiles.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        editTextPdf1=(EditText)findViewById(R.id.edit_text_pdf1);
        editTextPdf1.setText("http://www.cisco.com/c/dam/en_us/about/annual-report/2016-annual-report-full.pdf");
        editTextPdf2=(EditText)findViewById(R.id.edit_text_pdf2);
        editTextPdf2.setText("http://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf");
        editTextPdf3=(EditText)findViewById(R.id.edit_text_pdf3);
        editTextPdf3.setText("http://www.cisco.com/web/strategy/docs/gov/everything-for-cities.pdf");
        editTextPdf4=(EditText)findViewById(R.id.edit_text_pdf4);
        editTextPdf4.setText("http://www.cisco.com/web/offer/gist_ty2_asset/Cisco_2014_ASR.pdf");
        editTextPdf5=(EditText)findViewById(R.id.edit_text_pdf5);
        editTextPdf5.setText("http://www.cisco.com/web/offer/emear/38586/images/Presentations/P3.pdf");

        int id=view.getId();
        if(id==R.id.btn_download_files){

            /*progressBar=new ProgressDialog(DownloadActivity.this);
            progressBar.setTitle("Downloading Files");
            progressBar.setMessage("Downloading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setMax(100);
            progressBar.setCancelable(true);
            progressBar.show();*/

            intentFilter = new IntentFilter();
            intentFilter.addAction("FILE_DOWNLOAD");
            intentFilter.addAction("DOWNLOAD_PROGRESS");

            registerReceiver(progressBroadcastReceiver, intentFilter);

            urlList=new ArrayList<>();
            urlList.add(editTextPdf1.getText().toString());
            urlList.add(editTextPdf2.getText().toString());
            urlList.add(editTextPdf3.getText().toString());
            urlList.add(editTextPdf4.getText().toString());
            urlList.add(editTextPdf5.getText().toString());

            //using started service
            Intent intent=new Intent(this, PdfService.class);
            intent.putStringArrayListExtra("url_list",urlList);
            startService(intent);


            //using intent service
            /*Intent intent=new Intent(this,PdfIntentService.class);
            intent.putExtra("url",urlList.get(0));
            startService(intent);*/
        }

    }


    private BroadcastReceiver progressBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("FILE_DOWNLOAD")){
                //int progress=Integer.parseInt(intent.getExtras().get("progress"));
                //Log.d("Broadcast progress",String.valueOf(intent.getExtras().get("progress")));

                //progressBar.dismiss();
                Toast.makeText(DownloadActivity.this, "Files Downloaded!", Toast.LENGTH_SHORT).show();

            }

            if(intent.getAction().equals("DOWNLOAD_PROGRESS")){
                int prog=getIntent().getIntExtra("progress",0);
                //progressBar.setProgress(prog);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(progressBroadcastReceiver);
    }
}
