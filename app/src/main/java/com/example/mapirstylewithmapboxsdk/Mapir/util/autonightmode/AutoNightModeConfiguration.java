package com.example.mapirstylewithmapboxsdk.Mapir.util.autonightmode;

import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.CARMANIA;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.HYRCANIA;
import static com.example.mapirstylewithmapboxsdk.Mapir.MapirStyle.VERNA;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class AutoNightModeConfiguration {

    private final String darkStyle;
    private final String lightStyle;
    private final String rasterStyle;
    private LatLng location;

    private AutoNightModeConfiguration(Builder builder) {
        this.darkStyle = builder.darkStyle;
        this.lightStyle = builder.lightStyle;
        this.rasterStyle = builder.rasterStyle;
        this.location = builder.location;
    }

    public String getDarkStyle() {
        return this.darkStyle;
    }

    public String getLightStyle() {
        return this.lightStyle;
    }

    public String getRasterStyle() {
        return this.rasterStyle;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public static class Builder {

        private String darkStyle = CARMANIA;
        private String lightStyle = VERNA;
        private String rasterStyle = HYRCANIA;
        private LatLng location = new LatLng(35.6892, 51.3890);

        public Builder() {
        }

        public Builder setDefaultDarkStyle(@NonNull String darkStyle) {
            if (darkStyle != null) {
                this.darkStyle = darkStyle;
            } else {
                throw new RuntimeException("darkStyle should not be null");
            }
            return this;
        }

        public Builder setDefaultLightStyle(@NonNull String lightStyle) {
            if (lightStyle != null) {
                this.lightStyle = lightStyle;
            } else {
                throw new RuntimeException("lightStyle should not be null");
            }
            return this;
        }

        public Builder setDefaultRasterStyle(@NonNull String rasterStyle) {
            if (rasterStyle != null) {
                this.rasterStyle = rasterStyle;
            } else {
                throw new RuntimeException("rasterStyle should not be null");
            }
            return this;
        }

        public Builder setLocation(@NonNull LatLng location) {
            if (location != null) {
                this.location = location;
            } else {
                throw new RuntimeException("location should not be null");
            }
            return this;
        }

        public AutoNightModeConfiguration build() {
            return new AutoNightModeConfiguration(this);
        }
    }
}
