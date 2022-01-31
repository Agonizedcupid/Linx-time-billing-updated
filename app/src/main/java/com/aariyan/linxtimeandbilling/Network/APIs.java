package com.aariyan.linxtimeandbilling.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIs {

    @GET("getusers.php?")
    Call<ResponseBody> getUsers(@Query("id") String id);

    @GET("getcustomers.php?")
    Call<ResponseBody> getCustomers(@Query("id") String id);

}
