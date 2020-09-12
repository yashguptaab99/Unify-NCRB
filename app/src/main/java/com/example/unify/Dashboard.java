package com.example.unify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.unify.api.Api;
import com.example.unify.api.RetrofitClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dashboard extends AppCompatActivity {

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCamera;
    private ImageView imageView;

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int CAMERA_REQUEST=100;

    String pathToFile, currentPhotoPath;
    Uri photoURI;
    private static Bitmap bitmap;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        buttonChoose =  findViewById(R.id.buttonChoose);
        buttonUpload =  findViewById(R.id.buttonUpload);
        buttonCamera = findViewById(R.id.buttonCamera);
        imageView =  findViewById(R.id.imageView);

        buttonUpload.setVisibility(View.INVISIBLE);

        if(Build.VERSION.SDK_INT > 23){
            requestPermissions(new String[]  {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
    }

    //THIS FUNCTION SELECTS IMAGE FROM DEVICE
    public void selectImage(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
        System.out.println("\n*******In select image****");

    }

    //THIS FUNCTION CAPTURES IMAGE FROM DEVICE
    public void captureImage(View view) {
        Intent takePic = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("\n*****In Capture*****");
        if(takePic.resolveActivity(getPackageManager())!=null)
        {
            File photoFile = null;
            //OBTAIN IMAGE FILE
            photoFile = createPhotoFile();
            System.out.println("\nphotofile :"+photoFile);
            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                System.out.println("\npathToFile :"+pathToFile);
                photoURI = FileProvider.getUriForFile(this, "com.example.unify.fileprovider", photoFile);
                System.out.println("\nphotoURI :"+photoURI);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, CAMERA_REQUEST);
            }
            else {
                Toast.makeText(getApplicationContext(), "error while accessing the photo taken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createPhotoFile() {
        @SuppressLint("SimpleDateFormat") String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image=File.createTempFile(name,".jpg",storageDir);
            currentPhotoPath = image.getAbsolutePath();
            Toast.makeText(getApplicationContext(),"File has been stored as " + name.toString() ,Toast.LENGTH_SHORT).show();
            //pathToFile = image.getAbsolutePath();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /*public String getPath(Uri uri) {      not working
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("\n*******In onActivityResult****");
        System.out.println(resultCode);
        System.out.println(data);
        if(resultCode == RESULT_OK) {
            System.out.println("\n*******result_ok****");
            if (requestCode == CAMERA_REQUEST) {
                System.out.println("\n*******camera-request output****");
                flag=1;
                if(Build.VERSION.SDK_INT > 27) {
                    System.out.println("\n*******In api>27****");
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoURI);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        buttonUpload.setVisibility(View.VISIBLE);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("\n*******In api<=27****");
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        buttonUpload.setVisibility(View.VISIBLE);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == PICK_IMAGE_REQUEST  && data!=null ) {
                System.out.println("\n*******In picK-image output****");
                Uri path = data.getData();
                System.out.println(path);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (path != null)
                {
                    Cursor cursor = getContentResolver().query(path, filePathColumn, null, null, null);
                    if (cursor != null)
                    {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        pathToFile = cursor.getString(columnIndex);
                    }
                }

                        //pathToFile = getPath(path); not working
                        System.out.println("pathtofile:"+pathToFile);
                    if(Build.VERSION.SDK_INT > 27) {
                        System.out.println("\n*******In api>27****");
                        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), path);
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source);
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                            buttonUpload.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        System.out.println("\n*******In api<=27****");
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                            buttonUpload.setVisibility(View.VISIBLE);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    }


    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("No face or multiple faces detected!!");
        dialog.setTitle("Alert!");
        dialog.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    //THIS FUNCTION UPLOADS IMAGE TO THE SERVER
    public void uploadImage(View view) {
        File file=new File(pathToFile);
        Toast.makeText(getApplicationContext(),pathToFile.toString(),Toast.LENGTH_SHORT).show();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);  //PASS TYPE OF FILE AND THE FILE
        RequestBody someData = RequestBody.create((MediaType.parse("text/plain")),"Image Upload"); //IMAGE HEADER
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),requestBody); //DIVIDE IMAGE INTO PARTS USING MULTIPART

        //RETROFIT OBJECT CREATION AND CAL INITIALIZATION
        Retrofit retrofit = RetrofitClient.getRetrofit();
        Api uploadApis = retrofit.create(Api.class);
        //Call call = uploadApis.uploadImages(part,someData);
        Call call = uploadApis.uploadImages(MainActivity.token,part,someData);
        Toast.makeText(getApplicationContext(),"Uploading Image.....",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),pathToFile.toString(),Toast.LENGTH_SHORT).show();

        //ENQUEUE CALL FOR RESPONSE FROM SERVER
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response <ResponseBody> response) {
                System.out.println("\nResponse:"+response);
                //WHEN RESPONSE IS RECEIVED FROM SERVER
                try {
                    //CONVERT JSON RESPONSE TO STRING
                    //System.out.println("\nResponse:"+response.body().string());
                    String s = response.body().string();
                    if(s.contains("face_status")){
                        //Toast.makeText(getApplicationContext(), "No face or multiple faces detected !", Toast.LENGTH_SHORT).show();
                        alertDialog();
                    }
                    else {
                        System.out.println("Response String :: " + s);
                        Intent i = new Intent(Dashboard.this, BestMatches.class);
                        i.putExtra("matches", s);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //IF RESPONSE IS NOT RECIEVED FROM SERVER
                System.out.println("\nResponse:"+t);
                Toast.makeText(Dashboard.this, "Could not Connect to Server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}