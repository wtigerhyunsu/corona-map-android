package com.example.p502;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText logintext,pwdtext;
    TextView datetext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logintext = findViewById(R.id.logintext);
        pwdtext = findViewById(R.id.pwdtext);
        datetext = findViewById(R.id.datetext);
        progressDialog = new ProgressDialog(MainActivity.this);
        datetext.setText(getToDay());
    }

    // 로그인 아이디 입력 받아서 버튼 입력시 httptask 로 전송 //
    public void login(View v){
        String id = logintext.getText().toString();
        String pwd = pwdtext.getText().toString();
        HttpTask task = new HttpTask(id,pwd);
        task.execute();
        Log.d("---","loginbutton");

    }

    // 오늘 날짜 받아서 메인화면에 날짜 뿌려주기 //
    public static String getToDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // 애플리케이션 껏다가 다시 켜도 아이디, 비밀번호가 남아있도록 설정 //
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("---","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("---","onResume");
        restoreState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("---","onPause");
        saveState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("---","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("---","onDestroy");
        clearState();
    }

    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if((pref != null) && (pref.contains("id")) && (pref.contains("pwd"))){
            String id = pref.getString("id","");
            String pwd = pref.getString("pwd","");
            logintext.setText(id);
            pwdtext.setText(pwd);
        }
    }

    protected void saveState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",logintext.getText().toString());
        editor.putString("pwd",pwdtext.getText().toString());
        editor.commit();
    }

    protected void clearState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }

    // 받아온 로그인 정보로 유효성 검사 //
    class HttpTask extends AsyncTask<Void, Void, String> {

        String url;

        public HttpTask(String id, String pwd){
            url = "http://52.78.108.32:8080/webview/login.jsp?";
            url += "id=" + id + "&pwd=" + pwd;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("HTTP Connecting");
            progressDialog.setMessage("Plz wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids)
        {
            Log.d("---","Background Processing");
            return HttpHandler.getString(url);
        }


        @Override
        protected void onPostExecute(String s) {
           Log.d("---",s.trim());

           if(s.trim().equals("1")){
               Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
               startActivity(intent);
           }else{
               Toast.makeText(MainActivity.this, "Login Denied", Toast.LENGTH_SHORT).show();
           }

           progressDialog.dismiss();

        }
    }
}
