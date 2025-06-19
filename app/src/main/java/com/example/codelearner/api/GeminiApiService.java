package com.example.codelearner.api;

import com.example.codelearner.Models.ChatRequest;
import com.example.codelearner.Models.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/chat")
    Call<ChatResponse> askGemini(@Body ChatRequest request);
}
