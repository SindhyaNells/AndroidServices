package com.example.service.downloadfiles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.service.downloadfiles.service.PdfIntentService;
import com.example.service.downloadfiles.R;

/**
 * Created by sindhya on 3/25/17.
 */
public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextPdf1;
    EditText editTextPdf2;
    EditText editTextPdf3;
    EditText editTextPdf4;
    EditText editTextPdf5;

    Button btnDownloadFiles;

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

        int id=view.getId();
        if(id==R.id.btn_download_files){

            Intent intent=new Intent(this,PdfIntentService.class);
            intent.putExtra("url","http://www.cisco.com/c/dam/en_us/about/annual-report/2016-annual-report-full.pdf");
            startService(intent);
        }

    }
}
