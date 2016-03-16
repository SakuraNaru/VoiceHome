package com.jlu.voicehome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("Date", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor =sharedPreferences.edit();
        username=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(username.getText().toString(), password.getText().toString())) {
                    editor.putBoolean("LoginState", true);
                    editor.commit();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }

    private boolean check(String user,String password)
    {

        if (user.equals(sharedPreferences.getString("User","admin"))&&password.equals(sharedPreferences.getString("Password","root")))
            return true;
        else
            return false;
    }
}
