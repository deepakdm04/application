package com.example.demo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demo.R;
import com.example.demo.Utils.Permission_Activity;
import com.example.demo.Utils.common;

public class Login extends AppCompatActivity {

    Button loginbtn;
    EditText etemail,etpassword;
    SharedPreferences Login_share;
    public static final String Login_de = "Login_details";
    SharedPreferences.Editor login_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_gradient_sign_in);
        Login_share=getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        login_edit=Login_share.edit();
        etemail=findViewById(R.id.email);
        etpassword=findViewById(R.id.password);



        loginbtn=findViewById(R.id.login7);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getemail=etemail.getText().toString();
                String getpassword=etpassword.getText().toString();


                if (getemail.equals(""))
                {
                    common.showtoast("Enter email",getApplicationContext());
                }
                else if(!common.isValidEmail(getemail))
                {
                    common.showtoast("Invalid email", getApplicationContext());
                }
                else if(getpassword.equals(""))
                {
                    common.showtoast("Enter password",getApplicationContext());
                }
                else
                {
                    Intent intent=new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                }
            }
        });


    }
}
