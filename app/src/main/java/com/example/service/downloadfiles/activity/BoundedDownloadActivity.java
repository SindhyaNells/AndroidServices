package com.example.service.downloadfiles.activity;

/**
 * Created by sindhya on 3/29/17.
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.service.downloadfiles.R;
import com.example.service.downloadfiles.service.PdfBoundService;

import java.util.ArrayList;


/**
 * Created by sindhya on 3/25/17.
 */
public class BoundedDownloadActivity extends AppCompatActivity implements View.OnClickListener{

    //bounded service
    PdfBoundService boundService;
    boolean bounded=false;
    ArrayList<String> urlList;

    EditText editTextPdf1;
    EditText editTextPdf2;
    EditText editTextPdf3;
    EditText editTextPdf4;
    EditText editTextPdf5;

    Button btnDownloadFiles;

    //ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download);
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

        btnDownloadFiles=(Button)findViewById(R.id.btn_download_files);
        btnDownloadFiles.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();


        //using bounded service
        Intent intent=new Intent(this, PdfBoundService.class);
        intent.putStringArrayListExtra("url_list",urlList);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);




    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btn_download_files){

            /*progressBar=new ProgressDialog(DownloadActivity.this);
            progressBar.setTitle("Downloading Files");
            progressBar.setMessage("Downloading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setMax(100);
            progressBar.setCancelable(true);
            progressBar.show();*/


            urlList=new ArrayList<>();
            urlList.add(editTextPdf1.getText().toString());
            urlList.add(editTextPdf2.getText().toString());
            urlList.add(editTextPdf3.getText().toString());
            urlList.add(editTextPdf4.getText().toString());
            urlList.add(editTextPdf5.getText().toString());


            if(bounded) {
                boundService.DownloadFiles(urlList);
            }

        }

    }

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PdfBoundService.LocalBinder localBinder=(PdfBoundService.LocalBinder)iBinder;
            boundService=localBinder.getService();
            bounded=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bounded=false;

        }
    };


    @Override
    protected void onStop() {
        super.onStop();

        if(bounded){
            unbindService(serviceConnection);
            bounded=false;
        }
    }
}
