package com.example.unify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class BestMatches extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_matches);

        //GET THE RESPONSE STRING FROM INTENT
        Intent intent = getIntent();
        String s = intent.getStringExtra("matches");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //OBTAIN ALL THE FIRs, REGs AND Img URLs FROM THE STRING
        String img_str1= null, img_str2=null, img_str3=null, fir_str1=null, fir_str2=null, fir_str3=null, reg_str1=null, reg_str2=null, reg_str3=null;
        String state_str1 =null , state_str2= null, state_str3 = null, district_str1=null,district_str2=null,district_str3=null,ps_str1=null,ps_str2=null, ps_str3=null;
        try {
            img_str1 = jsonObject.getString("img1");
            img_str2=jsonObject.getString("img2");
            img_str3=jsonObject.getString("img3");
            fir_str1=jsonObject.getString("fir1");
            fir_str2=jsonObject.getString("fir2");
            fir_str3=jsonObject.getString("fir3");
            reg_str1=jsonObject.getString("reg1");
            reg_str2=jsonObject.getString("reg2");
            reg_str3=jsonObject.getString("reg3");
            state_str1 = jsonObject.getString("state1");
            state_str2 = jsonObject.getString("state2");
            state_str3 = jsonObject.getString("state3");
            district_str1 = jsonObject.getString("district1");
            district_str2 = jsonObject.getString("district2");
            district_str3 = jsonObject.getString("district3");
            ps_str1 = jsonObject.getString("ps1");
            ps_str2 = jsonObject.getString("ps2");
            ps_str3 = jsonObject.getString("ps3");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //CONVERT TO TWO DECIMAL PLACES
        DecimalFormat df = new DecimalFormat("##.##");
        double a = Double.parseDouble(reg_str1);
        reg_str1=df.format(a);

        float b = Float.parseFloat(reg_str2);
        reg_str2=df.format(b);

        float c = Float.parseFloat(reg_str3);
        reg_str3=df.format(c);


        TextView textViewstate1 = findViewById(R.id.state1);
        TextView textViewstate2 = findViewById(R.id.state2);
        TextView textViewstate3 = findViewById(R.id.state3);

        textViewstate1.setText(state_str1);
        textViewstate2.setText(state_str2);
        textViewstate3.setText(state_str3);

        TextView textViewdistrict1 = findViewById(R.id.district1);
        TextView textViewdistrict2 = findViewById(R.id.district2);
        TextView textViewdistrict3 = findViewById(R.id.district3);

        textViewdistrict1.setText(district_str1);
        textViewdistrict2.setText(district_str2);
        textViewdistrict3.setText(district_str3);


        TextView textViewps1 = findViewById(R.id.ps1);
        TextView textViewps2 = findViewById(R.id.ps2);
        TextView textViewps3 = findViewById(R.id.ps3);

        textViewps1.setText(ps_str1);
        textViewps2.setText(ps_str1);
        textViewps3.setText(ps_str1);

        TextView textViewFir1 = findViewById(R.id.fir1);
        TextView textViewFir2 = findViewById(R.id.fir2);
        TextView textViewFir3 = findViewById(R.id.fir3);

        textViewFir1.setText(fir_str1);
        textViewFir2.setText(fir_str2);
        textViewFir3.setText(fir_str3);

        TextView textViewMatch1 = findViewById(R.id.Reg1);
        TextView textViewMatch2 = findViewById(R.id.Reg2);
        TextView textViewMatch3 = findViewById(R.id.Reg3);

        textViewMatch1.setText(reg_str1);
        textViewMatch2.setText(reg_str2);
        textViewMatch3.setText(reg_str3);

        ImageView responseImage1 = findViewById(R.id.responseImage1);
        ImageView responseImage2 = findViewById(R.id.responseImage2);
        ImageView responseImage3 = findViewById(R.id.responseImage3);

        Picasso.get().load(img_str1).into(responseImage1);
        Picasso.get().load(img_str2).into(responseImage2);
        Picasso.get().load(img_str3).into(responseImage3);
    }
}
