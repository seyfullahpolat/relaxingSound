package com.seyfullahpolat.rahatlaticisesler.services;

import com.seyfullahpolat.rahatlaticisesler.item.CategoryItem;
import com.seyfullahpolat.rahatlaticisesler.item.SoundItem;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by seyfullahpolat on 15/07/2018.
 */
public class RestClient {

    private static Sound sound;
    //private static String baseUrl = "localhost:80";
    private static String baseUrl = "http://185.86.6.182:8080";

    public static Sound getClient() {
        if (sound == null) {
            OkHttpClient okClient = new OkHttpClient();
            okClient.setReadTimeout(400, TimeUnit.SECONDS);
            okClient.setConnectTimeout(400, TimeUnit.SECONDS);
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response;
                }
            });
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sound = client.create(Sound.class);
        }
        return sound;
    }

    public interface Sound {
        @GET("/rahatlaticisesler/v1/api.php?request=kitaplık")
        Call<ArrayList<CategoryItem>> getCategory();

        @GET("/rahatlaticisesler/v1/api.php?request=sound")
        Call<ArrayList<SoundItem>> getCategoryDetail(@QueryMap Map<String, Object> queryMap);
    }
}