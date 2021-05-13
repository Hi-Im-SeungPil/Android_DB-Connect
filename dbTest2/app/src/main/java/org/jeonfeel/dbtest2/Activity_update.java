package org.jeonfeel.dbtest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_update extends AppCompatActivity {

    EditText et_modifyName,et_modifyTel,et_modifyEmail;
    Button btn_modify;
    String currentName,currentTel,currentEmail;
    final String TAG = "Activity_update.class : ";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        et_modifyName = findViewById(R.id.et_modifyName);
        et_modifyTel = findViewById(R.id.et_modifyTel);
        et_modifyEmail = findViewById(R.id.et_modifyEmail);
        btn_modify = findViewById(R.id.btn_modify);

        Intent intent = getIntent();
        currentName = intent.getStringExtra("name");
        currentTel = intent.getStringExtra("tel");
        currentEmail = intent.getStringExtra("eMail");

        et_modifyName.setText(currentName);
        et_modifyTel.setText(currentTel);
        et_modifyEmail.setText(currentEmail);

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModify();
            }
        });
    }
    private void dataModify() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String name = et_modifyName.getText().toString();
                    String tel = et_modifyTel.getText().toString();
                    String eMail = et_modifyEmail.getText().toString();

                    URL setURL = new URL("http://10.0.2.2/dataModify.php/");
                    Log.d(TAG,"성공");

                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(name).append("/").append(tel).append("/").append(eMail).append("/").append(currentName).append("/");
                    OutputStreamWriter
                            outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    outStream.write(buffer.toString());
                    outStream.flush();

                    Log.d(TAG,"성공");

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    final BufferedReader redear = new BufferedReader(tmp);
                    while(redear.readLine() != null)
                    {
                        System.out.println(redear.readLine());
                    }
                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();
    }
}
