package com.example.codelearner.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

import com.example.codelearner.Models.LearningPath;
import com.example.codelearner.Models.Feedback;
import com.example.codelearner.Models.LearningPlan;
import com.example.codelearner.Models.LearningPlanRequest;
import com.example.codelearner.Models.Student;

public interface ApiService {

    @POST("learning-path/generate")
    Call<LearningPath> generateLearningPath(@Body Map<String, Object> body);

    @GET("learning-path/{studentId}")
    Call<LearningPath> getLearningPath(@Path("studentId") String studentId);

    @PUT("learning-path/{studentId}/complete-topic")
    Call<LearningPath> completeTopic(@Path("studentId") String studentId, @Body Map<String, String> body);

    @POST("feedback/{studentId}/submit")
    Call<Feedback> submitFeedback(@Path("studentId") String studentId, @Body Feedback feedback);

    @GET("resources/search")
    Call<List<String>> getResources(@Query("topic") String topic);

    @POST("plans/generate")
    Call<LearningPlan> generatePlan(@Body LearningPlanRequest request);

    @GET("/ping")
    Call<ResponseBody> pingBackend();

    @GET("students/{studentId}")
    Call<Student> getStudentById(@Path("studentId") String studentId);
}
