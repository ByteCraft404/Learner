package com.example.codelearner.api;

import com.example.codelearner.Models.LearningPlan;
import com.example.codelearner.Models.LearningPlanRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface ApiService {

    @POST("plans/generate")
    Call<LearningPlan> generatePlan(@Body LearningPlanRequest request);

    @GET("plans/{userId}")
    Call<List<LearningPlan>> getUserPlans(@Path("userId") String userId);

    @GET("/ping")
    Call<ResponseBody> pingBackend();




    // Add more endpoints as needed...
}
