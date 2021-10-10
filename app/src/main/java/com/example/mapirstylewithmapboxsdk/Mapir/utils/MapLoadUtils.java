package com.example.mapirstylewithmapboxsdk.Mapir.utils;


import com.example.mapirstylewithmapboxsdk.Mapir.Mapir;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapLoadUtils {
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final String API_KEY_NAME = "x-api-key=";
    private static final String VECTOR_PER_LOAD = "https://map.ir/vector/load?" + API_KEY_NAME;
    private static final String RASTER_PER_LOAD = "https://map.ir/shiveh/load?" + API_KEY_NAME;

    public void sendUsageRequest(OkHttpClient client) {
        final Request request = new Request.Builder()
                .url(VECTOR_PER_LOAD + Mapir.getApiKey())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
            }
        });
    }
}
