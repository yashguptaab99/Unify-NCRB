package com.example.unify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unify.api.Api;
import com.example.unify.api.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername =  findViewById(R.id.editTextUsername);
        editTextPassword =  findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
    }
    public static String token;
    //THIS FUNCTION IS CALLED ON CLICK OF LOGIN BUTTON
    public void userLogin(View view) {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //RETROFIT OBJECT CREATION AND CAL INITIALIZATION
        Retrofit retrofit = RetrofitClient.getRetrofit();
        Api uploadApis = retrofit.create(Api.class);
        Call call = uploadApis.userLogin(username, password);

        //ENQUEUE CALL FOR RESPONSE FROM SERVER
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //WHEN THE RESPONSE IS RECEIVED FROM SERVER
                try {
                    //CONVERT THE JSON RESPONSE TO STRING
                    String s = response.body().string();
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("token");
                    Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();

                    //if(result.equals("Yes")) {
                    if (result != null && !result.isEmpty() && !result.equals("null")) {
                        //IF THE AUTHENTICATION IS SUCCESSFUL
                        token = "Token" + "  " + result;
                        Intent i = new Intent(MainActivity.this, Dashboard.class);
                        Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_SHORT).show();

                        Toast.makeText(getApplicationContext(),token.toString(),Toast.LENGTH_SHORT).show();

                        startActivity(i);
                    }
                    else {
                        //IF AUTHENTICATION FAILS
                        //Intent i = new Intent(MainActivity.this, Dashboard.class);
                        //startActivity(i);
                        Toast.makeText(getApplicationContext(),"Invalid Credentials!",Toast.LENGTH_SHORT).show();
                        //editTextUsername.getText().clear();
                        editTextPassword.getText().clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //IF THERE IS NO RESPONSE FROM SERVER
                Toast.makeText(MainActivity.this, "Could not Connect to Server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}