package com.example.mapirstylewithmapboxsdk.Mapir.maps;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mapirstylewithmapboxsdk.Mapir.util.autonightmode.AutoNightModeConfiguration;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.utils.MapFragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment wrapper around a mapView view.
 * <p>
 * A Map component in an app. This fragment is the simplest way to place a mapView in an application.
 * It's a wrapper around a view of a mapView to automatically handle the necessary life cycle needs.
 * Being a fragment, this component can be added to an activity's layout or can dynamically be added
 * using a FragmentManager.
 * </p>
 * <p>
 * To get a reference to the MapView, use {@link #getMapAsync(OnMapReadyCallback)}}
 * </p>
 *
 * @see #getMapAsync(OnMapReadyCallback)
 */
public final class MapFragment extends Fragment implements OnMapReadyCallback {

    private final List<OnMapReadyCallback> mapReadyCallbackList = new ArrayList<>();
    private OnMapViewReadyCallback mapViewReadyCallback;
    private MapboxMap map;
    private MapView mapView;

    /**
     * Creates a default MapFragment instance
     *
     * @return MapFragment instantiated
     */
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    /**
     * Creates a MapFragment instance
     *
     * @param mapboxMapOptions The configuration options to be used.
     * @return MapFragment instantiated.
     */
    @NonNull
    public static MapFragment newInstance(@Nullable MapboxMapOptions mapboxMapOptions) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(mapboxMapOptions));
        return mapFragment;
    }

    /**
     * Called when this fragment is inflated, parses XML tag attributes.
     *
     * @param context            The context inflating this fragment.
     * @param attrs              The XML tag attributes.
     * @param savedInstanceState The saved instance state for the mapView fragment.
     */
    @Override
    public void onInflate(@NonNull Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        setArguments(MapFragmentUtils.createFragmentArgs(MapboxMapOptions.createFromAttributes(context, attrs)));
    }

    /**
     * Called when the context attaches to this fragment.
     *
     * @param context the context attaching
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapViewReadyCallback) {
            mapViewReadyCallback = (OnMapViewReadyCallback) context;
        }
    }

    /**
     * Creates the fragment view hierarchy.
     *
     * @param inflater           Inflater used to inflate content.
     * @param container          The parent layout for the mapView fragment.
     * @param savedInstanceState The saved instance state for the mapView fragment.
     * @return The view created
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context context = inflater.getContext();
        mapView = new MapView(context, MapFragmentUtils.resolveArgs(context, getArguments()));
        return mapView;
    }

    /**
     * Called when the fragment view hierarchy is created.
     *
     * @param view               The content view of the fragment
     * @param savedInstanceState The saved instance state of the fragment
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // notify listeners about mapview creation
        if (mapViewReadyCallback != null) {
            mapViewReadyCallback.onMapViewReady(mapView);
        }
    }

    /**
     * Called when the style of the mapView has successfully loaded.
     *
     * @param map The public api controller of the mapView
     */
    @Override
    public void onMapReady(@NonNull MapboxMap map) {
        this.map = map;
        for (OnMapReadyCallback onMapReadyCallback : mapReadyCallbackList) {
            onMapReadyCallback.onMapReady(map);
        }
    }

    /**
     * Called when the fragment is visible for the users.
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Called when the fragment is pausing.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Called when the fragment state needs to be saved.
     *
     * @param outState The saved state
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onSaveInstanceState(outState);
        }
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null && !mapView.isDestroyed()) {
            mapView.onLowMemory();
        }
    }

    /**
     * Called when the fragment is view hiearchy is being destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    /**
     * Called when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapReadyCallbackList.clear();
    }

    /**
     * Sets a callback object which will be triggered when the MapboxMap instance is ready to be used.
     *
     * @param onMapReadyCallback The callback to be invoked.
     */
    public void getMapAsync(@NonNull final OnMapReadyCallback onMapReadyCallback) {
        if (map == null) {
            mapReadyCallbackList.add(onMapReadyCallback);
        } else {
            onMapReadyCallback.onMapReady(map);
        }
    }

    public void enableAutoNightMode(boolean enable, AutoNightModeConfiguration configuration) {
        if (enable)
            mapView.enableAutoNightMode(configuration);
        else
            mapView.disableAutoNightMode();
    }

    /**
     * Callback to be invoked when the mapView fragment has inflated its MapView.
     * <p>
     * To use this interface the context hosting the fragment must implement this interface.
     * That instance will be set as part of Fragment#onAttach(Context context).
     * </p>
     */
    public interface OnMapViewReadyCallback {

        /**
         * Called when the mapView has been created.
         *
         * @param mapView The created mapview
         */
        void onMapViewReady(MapView mapView);
    }
}
