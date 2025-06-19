package com.example.codelearner.api;

import com.example.codelearner.Models.LoginRequest;
import com.example.codelearner.Models.LoginResponse;
import com.example.codelearner.Models.Student;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/api/students/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @POST("/api/students/register")
    Call<Student> registerUser(@Body Student student);

}
