package com.example.mapirstylewithmapboxsdk.Mapir;

import android.content.Context;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.example.mapirstylewithmapboxsdk.Mapir.utils.MapLoadUtils;
import com.example.mapirstylewithmapboxsdk.Mapir.utils.NetworkUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil;

import okhttp3.OkHttpClient;

@Keep
public final class Mapir {

    private static String apiKey;
    private static Mapir INSTANCE;

    private Mapir(Context context, String key) {
        Mapbox.getInstance(context, null);
        apiKey = key;
        OkHttpClient client = new NetworkUtils(context).getOkHttpClient(apiKey);
        HttpRequestUtil.setOkHttpClient(client);
        new MapLoadUtils().sendUsageRequest(client);
    }

    @NonNull
    public static void init(@NonNull Context context, @NonNull String apiKey) {
        if (apiKey != null && !apiKey.isEmpty()) {
            if (INSTANCE == null)
                INSTANCE = new Mapir(context.getApplicationContext(), apiKey);
        } else
            throw new RuntimeException("No APIKEY Provided, Please visit https://corp.map.ir/registration/ to get new APIKEY");
    }

    public static String getApiKey() {
        return apiKey;
    }
}
