package org.jeonfeel.dbtest2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText et_name,et_phoneNum,et_email,et_searchName;
    Button btn_submit,btn_search,btn_del,btn_modify;
    TextView tv_name,tv_phoneNum,tv_eMail;
    LinearLayout linear_btn;
    String searchName,searchTel,searchEmail;
    Handler handler;
    final String TAG = "MainActivity.class : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findId();
        handler = new Handler();
        linear_btn.setVisibility(View.GONE);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataInsert();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSearch();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataDelete();
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Activity_update.class);
                intent.putExtra("name",searchName);
                intent.putExtra("tel",searchTel);
                intent.putExtra("eMail",searchEmail);
                startActivity(intent);
            }
        });
    }

    private void findId(){

        et_name = findViewById(R.id.et_name);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_email = findViewById(R.id.et_email);
        et_searchName = findViewById(R.id.et_searchName);
        btn_submit = findViewById(R.id.btn_submit);
        btn_search = findViewById(R.id.btn_search);
        btn_del = findViewById(R.id.btn_del);
        btn_modify = findViewById(R.id.btn_modify);
        tv_name = findViewById(R.id.tv_name);
        tv_phoneNum = findViewById(R.id.tv_phoneNum);
        tv_eMail = findViewById(R.id.tv_eMail);
        linear_btn = findViewById(R.id.linear_btn);
    }

    private void dataInsert() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String name = et_name.getText().toString();
                    String tel = et_phoneNum.getText().toString();
                    String email = et_email.getText().toString();

                    URL setURL = new URL("http://10.0.2.2/insert.php/");
                    Log.d(TAG,"성공");

                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(name).append("/").append(tel).append("/").append(email).append("/");
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

    private void dataSearch() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String key = et_searchName.getText().toString();
                    URL url = new URL("http://10.0.2.2/search.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(key);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outputStreamWriter.write(buffer.toString());
                    outputStreamWriter.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                    }
                    String resultData = builder.toString();
                    final String[] sResult = resultData.split("/");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(sResult.length > 1) {
                                tv_name.setText("이름 : " + sResult[0]);
                                tv_phoneNum.setText("전번 : " + sResult[1]);
                                tv_eMail.setText("이멜 : " + sResult[2]);

                                linear_btn.setVisibility(View.VISIBLE);
                                searchName = sResult[0];
                                searchTel = sResult[1];
                                searchEmail = sResult[2];
                            }else{
                                tv_name.setText("없는 데이터");
                                linear_btn.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (Exception e){
                    Log.e("","Error",e);
                }
            }
        }.start();
    }

    private void dataDelete(){
        new Thread() {
            @Override
            public void run() {
                try {
                    String name = searchName;
                    URL url = new URL("Http://10.0.2.2/dataDelete.php/");
                    HttpURLConnection http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(name);
                    Log.d(TAG, String.valueOf(buffer));
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(http.getOutputStream(),"UTF-8");
                    outputStreamWriter.write(buffer.toString());
                    outputStreamWriter.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine())!=null) {
                        builder.append(str);
                        Log.d(TAG,str);
                    }
                } catch (Exception e){
                    Log.e("","Error",e);
                }
            }
        }.start();
    }


}