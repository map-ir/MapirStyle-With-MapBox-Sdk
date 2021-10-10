package com.example.mapirstylewithmapboxsdk.Mapir;

import androidx.annotation.Keep;

@Keep
public final class MapirStyle {
    
    private static final String MAPIR_BASE_URL = "https://map.ir";

    /**
     * Map.ir basic map style
     */
    public static final String VERNA = MAPIR_BASE_URL + "/vector/styles/main/main_mobile_style.json";

    /**
     * Map.ir dark map style
     */
    public static final String CARMANIA = MAPIR_BASE_URL + "/vector/styles/main/mapir-style-dark.json";

    /**
     * Map.ir basic map style with minimum POIs
     */
    public static final String ISATIS = MAPIR_BASE_URL + "/vector/styles/main/mapir-style-min-poi.json";

    /**
     * Map.ir basic map style with no POIs
     */
    public static final String LIGHT = MAPIR_BASE_URL + "/vector/styles/main/mapir-xyz-style-min-poi.json";

    /**
     * Map.ir basic raster map style (using png tiles instead of pbf with lower quality)
     * Not recommended
     */
    public static final String HYRCANIA = "{\n" +
            "   \"version\":8,\n" +
            "   \"sources\":{\n" +
            "      \"raster-tiles\":{\n" +
            "         \"type\":\"raster\",\n" +
            "         \"tiles\":[\n" + String.format("\"%s", MAPIR_BASE_URL) +
            "/shiveh/xyz/1.0.0/Shiveh:Shiveh@EPSG:3857@png/{z}/{x}/{y}.png?\"\n" +
            "         ],\n" +
            "         \"tileSize\":256\n" +
            "      }\n" +
            "   },\n" +
            "   \"layers\":[\n" +
            "      {\n" +
            "         \"id\":\"background\",\n" +
            "         \"type\":\"background\",\n" +
            "         \"paint\":{\n" +
            "            \"background-color\":{\n" +
            "               \"base\":1,\n" +
            "               \"stops\":[\n" +
            "                  [\n" +
            "                     5,\n" +
            "                     \"#F3F4F2\"\n" +
            "                  ]\n" +
            "               ]\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\":\"simple-tiles\",\n" +
            "         \"type\":\"raster\",\n" +
            "         \"source\":\"raster-tiles\",\n" +
            "         \"minzoom\":1,\n" +
            "         \"maxzoom\":22\n" +
            "      }\n" +
            "   ]\n" +
            "}";
}