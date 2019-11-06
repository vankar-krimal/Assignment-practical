package com.android.assignment.api;

import com.android.assignment.model.FactsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

// interface for retrofit
public interface ApiService {

    @GET(Apis.FACTS)
    Call<FactsResponse> getFacts();

}
