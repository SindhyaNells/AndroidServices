package com.example.service.downloadfiles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.service.downloadfiles.service.PdfIntentService;
import com.example.service.downloadfiles.R;
import com.example.service.downloadfiles.service.PdfService;

import java.util.ArrayList;
import java.util.List;

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

        ArrayList<String> urlList=new ArrayList<>();
        urlList.add("http://www.cisco.com/c/dam/en_us/about/annual-report/2016-annual-report-full.pdf");
        urlList.add("http://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf");
        urlList.add("http://www.cisco.com/web/strategy/docs/gov/everything-for-cities.pdf");
        urlList.add("http://www.cisco.com/web/offer/gist_ty2_asset/Cisco_2014_ASR.pdf");
        urlList.add("http://www.cisco.com/web/offer/emear/38586/images/Presentations/P3.pdf");

        int id=view.getId();
        if(id==R.id.btn_download_files){

            //using service
            Intent intent=new Intent(this, PdfService.class);
            intent.putStringArrayListExtra("url_list",urlList);
            startService(intent);


            //using intent service
            /*Intent intent=new Intent(this,PdfIntentService.class);
            intent.putExtra("url",urlList.get(0));
            startService(intent);*/
        }

    }
}
