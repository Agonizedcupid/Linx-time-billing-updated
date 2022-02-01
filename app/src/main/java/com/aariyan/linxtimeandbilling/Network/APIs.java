package com.aariyan.linxtimeandbilling.Network;

import com.aariyan.linxtimeandbilling.Model.PostingModel;
import com.aariyan.linxtimeandbilling.Model.TimingModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIs {

    @GET("getusers.php?")
    Call<ResponseBody> getUsers(@Query("id") String id);

    @GET("getcustomers.php?")
    Call<ResponseBody> getCustomers(@Query("id") String id);

    @POST("postjobsnew.php")
    Call<String> posting (@Body List<PostingModel> models);
}
