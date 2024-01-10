package com.interview.interviewtask.retrofit;

import com.interview.interviewtask.ui.home.response.ListResposne;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("pdf/list/pdf")
    Call<List<ListResposne>> getList();
}
