package com.example.mapirstylewithmapboxsdk.Mapir.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public final class NetworkUtils {

    public static String userAgent;

    public NetworkUtils(Context context) {
        userAgent = userAgentString(context);
    }

    public OkHttpClient getOkHttpClient(final String apiKey) {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .header("MapIr-SDK", userAgent)
                        .header("x-api-key", apiKey)
                        .build());
            }
        })
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, @NonNull Response response) {
                        Log.e("MAPIR", "Mapir APIKEY Not provided or expired, Please visit https://corp.map.ir/registration/ to get new APIKEY or extend yours");
                        return null;
                    }
                })
                .build();
    }

    public String userAgentString(@NonNull Context context) {
        return String.format("Android/%s(%s)(%s)-MapSdk/%s-%s(%s)/%s-(%s)",
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE,
                Build.CPU_ABI,
                "0.0.0",
                context.getPackageName(),
                Charset.forName("US-ASCII").newEncoder().canEncode(getApplicationName(context)) ?
                        getApplicationName(context) :
                        getApplicationName(context).getBytes(),
                Build.BRAND,
                Build.MODEL);
    }

    public String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }
}
