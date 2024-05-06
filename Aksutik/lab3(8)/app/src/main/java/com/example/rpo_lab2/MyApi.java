package com.example.rpo_lab2;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

interface MyApi {
    @GET("posts")
    Call<ArrayList<Model>>callModel();
}
