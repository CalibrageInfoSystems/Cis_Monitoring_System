package com.cis.service;

import com.cis.views.EventsModel;
import com.cis.views.GetDetailScreen;
import com.cis.views.Getscreen;
import com.cis.views.SearchResponse;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {


    @GET
    Observable<List<Getscreen>> getlernings(@Url String url);

    @GET
    Observable<List<GetDetailScreen>> getDetailScreens(@Url String url);

    @POST(APIConstantURL.SEARCH_URL)
    Observable<List<SearchResponse>> searchResponsePage(@Body JsonObject data);

    @GET
    Observable<EventsModel> getEvents(@Url String url);
}
