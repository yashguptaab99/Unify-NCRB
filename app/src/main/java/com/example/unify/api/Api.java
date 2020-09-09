package com.example.unify.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api
{
    @Multipart
    @POST("api/upload/")
    //Call<ResponseBody> uploadImages(@Part MultipartBody.Part part,@Part("somedata") RequestBody requestBody);
    Call<ResponseBody> uploadImages(@Header ("Authorization") String  authToken,@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);

    @FormUrlEncoded
    @POST("api/api-token-auth/")
    Call<ResponseBody> userLogin(@Field("username") String email, @Field("password") String password);
}
