package com.example.mapirstylewithmapboxsdk.Mapir.maps;

import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.CARMANIA;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.HYRCANIA;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.ISATIS;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.LIGHT;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.VERNA;
import static com.example.mapirstylewithmapboxsdk.Mapir.util.autonightmode.SunriseSunset.getSunriseSunset;
import static com.example.mapirstylewithmapboxsdk.Mapir.utils.VectorUtils.compassIconPathList;
import static com.example.mapirstylewithmapboxsdk.Mapir.utils.VectorUtils.darkLogoPathList;
import static com.example.mapirstylewithmapboxsdk.Mapir.utils.VectorUtils.lightLogoPathList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapirstylewithmapboxsdk.Mapir.Mapir;
import com.example.mapirstylewithmapboxsdk.Mapir.util.autonightmode.AutoNightModeConfiguration;
import com.example.mapirstylewithmapboxsdk.Mapir.utils.MapLoadUtils;
import com.example.mapirstylewithmapboxsdk.Mapir.utils.NetworkUtils;
import com.example.mapirstylewithmapboxsdk.Mapir.utils.VectorDrawableCreator;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;


@Keep
public class MapView extends com.mapbox.mapboxsdk.maps.MapView {

    private MapboxMap map;
    private boolean isAutoNightModeEnabled = false;
    private AutoNightModeConfiguration styleConfiguration = new AutoNightModeConfiguration.Builder().build();
    BroadcastReceiver tickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_TIME_TICK) && isAutoNightModeEnabled) {
                changeMapStyle(getSunriseSunset(Calendar.getInstance(), styleConfiguration.getLocation().getLatitude(), styleConfiguration.getLocation().getLongitude()));
            }
        }
    };

    public MapView(@NonNull Context context) {
        super(context);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MapView(@NonNull Context context, @Nullable MapboxMapOptions options) {
        super(context, options);
    }

    private boolean isInitialOnResume = true;

    @SuppressLint("RtlHardcoded")
    @Override
    protected void initialize(@NonNull Context context, @NonNull MapboxMapOptions options) {
        super.initialize(context, options);

        Drawable lightLogo = VectorDrawableCreator.getVectorDrawable(
                context,
                42,
                100,
                110,
                42,
                lightLogoPathList
        );

        Drawable darkLogo = VectorDrawableCreator.getVectorDrawable(
                context,
                42,
                100,
                110,
                42,
                darkLogoPathList
        );

        Drawable compassIcon = VectorDrawableCreator.getVectorDrawable(
                context,
                40,
                40,
                40,
                40,
                compassIconPathList
        );

        TextView copyRight = new TextView(MapView.this.getContext());
        copyRight.setText("© Map © OpenStreetMap");
        copyRight.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        copyRight.setTextSize(12);
        copyRight.setPadding(0, 0, 12, 12);
        addView(copyRight);

        addOnDidFinishLoadingStyleListener(new OnDidFinishLoadingStyleListener() {
            @Override
            public void onDidFinishLoadingStyle() {
                if (map != null) {
                    map.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            try {
                                map.getUiSettings().setAttributionEnabled(false);

                                Field compassViewField;
                                ImageView compassImageView;

                                compassViewField = UiSettings.class.getDeclaredField("compassView");
                                compassViewField.setAccessible(true);

                                compassImageView = (ImageView) compassViewField.get(map.getUiSettings());

                                compassImageView.setImageDrawable(compassIcon);

                                Field logoViewField;
                                ImageView logoImageView;

                                logoViewField = UiSettings.class.getDeclaredField("logoView");
                                logoViewField.setAccessible(true);

                                logoImageView = (ImageView) logoViewField.get(map.getUiSettings());

                                // Change mapir logo base on style
                                if (logoImageView != null)
                                    if (style.getUri().equals(CARMANIA)) {
                                        logoImageView.setImageDrawable(darkLogo);
                                        logoImageView.setMaxWidth(150);
                                        logoImageView.setMaxHeight(50);
                                    } else if (style.getUri().equals(LIGHT) || style.getUri().equals(VERNA) || style.getUri().equals(ISATIS)) {
                                        logoImageView.setImageDrawable(lightLogo);
                                        logoImageView.setMaxWidth(150);
                                        logoImageView.setMaxHeight(50);
                                    } else {
                                        if (style.getJson().equals(HYRCANIA)) {
                                            logoImageView.setImageDrawable(lightLogo);
                                            logoImageView.setMaxWidth(150);
                                            logoImageView.setMaxHeight(50);
                                        }
                                    }
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Sets a callback object which will be triggered when the {@link MapboxMap} instance is ready to be used.
     *
     * @param callback The callback object that will be triggered when the map is ready to be used.
     */
    @Override
    public void getMapAsync(@NonNull final OnMapReadyCallback callback) {
        super.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setMinZoomPreference(1);
                map.setMaxZoomPreference(22);
                if (isAutoNightModeEnabled) {
                    changeMapStyle(getSunriseSunset(Calendar.getInstance(), styleConfiguration.getLocation().getLatitude(), styleConfiguration.getLocation().getLongitude()));
                    enableAutoNightMode(styleConfiguration);
                } else {
                    map.setStyle(new Style.Builder().fromUri(VERNA));
                }
                callback.onMapReady(map);
            }
        });
    }

    private void changeMapStyle(Calendar[] sunriseSunset) {
        final String styleUrl;
        Date currentTime = Calendar.getInstance().getTime();
        Date sunrise = sunriseSunset[0].getTime();
        Date sunset = sunriseSunset[1].getTime();
        if (currentTime.after(sunrise) && currentTime.before(sunset)) {
            styleUrl = styleConfiguration.getLightStyle();
        } else if (currentTime.after(sunset)) {
            styleUrl = styleConfiguration.getDarkStyle();
        } else {
            styleUrl = styleConfiguration.getLightStyle();
        }
        if (map != null) {
            map.setStyle(styleUrl);
        }
    }

    void enableAutoNightMode(AutoNightModeConfiguration configuration) {
        isAutoNightModeEnabled = true;
        this.styleConfiguration = configuration;
        try {
            if (getContext() != null)
                getContext().registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    void disableAutoNightMode() {
        isAutoNightModeEnabled = false;
        try {
            if (tickReceiver != null)
                getContext().unregisterReceiver(tickReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void enableAutoNightMode(boolean enable, AutoNightModeConfiguration configuration) {
        if (enable)
            enableAutoNightMode(configuration);
        else
            disableAutoNightMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInitialOnResume) {
            OkHttpClient client = new NetworkUtils(getContext()).getOkHttpClient(Mapir.getApiKey());
            new MapLoadUtils().sendUsageRequest(client);
        }
        isInitialOnResume = false;
        if (isAutoNightModeEnabled) {
            changeMapStyle(getSunriseSunset(Calendar.getInstance(), styleConfiguration.getLocation().getLatitude(), styleConfiguration.getLocation().getLongitude()));
            enableAutoNightMode(styleConfiguration);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (tickReceiver != null)
                if (getContext() != null)
                    getContext().unregisterReceiver(tickReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}