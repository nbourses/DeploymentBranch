package com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

/**
 * Created by mehuldvgny on 15/10/15.
 */
    public class GeoFence extends java.lang.Object{

    PolygonOptions polygonOptions_Powai,polygonOptions_Bandra,polygonOptions_Andheri,polygonOptions_LowerParel;
    Polygon polygon1,polygon2,polygon3,polygon4;
    ArrayList<Polygon> polygonList = new ArrayList<>();

    /*PolyUtil pl;

    public GeoFence() {
        pl = new PolyUtil();
    }*/

    public void drawPloygon(GoogleMap map){
        //POWAI latlng details
        polygonOptions_Powai=  new PolygonOptions().add(
                new LatLng(19.118677, 72.886642),
                new LatLng(19.132245, 72.896083),
                new LatLng(19.134629, 72.900071),
                new LatLng(19.138440, 72.912516),
                new LatLng(19.147198, 72.918267),
                new LatLng(19.147279, 72.918267),
                new LatLng(19.149241, 72.921892),
                new LatLng(19.148888, 72.923967),
                new LatLng(19.145502, 72.922907),
                new LatLng(19.144412, 72.919588),
                new LatLng(19.134750, 72.918933),
                //new LatLng(19.128805, 72.),
                new LatLng(19.125607, 72.921877),
                new LatLng(19.124309, 72.921432),
                new LatLng(19.124181, 72.922142),
                new LatLng(19.123192, 72.921709),
                new LatLng(19.115385, 72.915647),
                new LatLng(19.109359, 72.912857),
                new LatLng(19.101137, 72.906421),
                new LatLng(19.101974, 72.904348),
                new LatLng(19.100738, 72.903299),
                new LatLng(19.101418, 72.902107),
                new LatLng(19.100921, 72.900505),
                new LatLng(19.103791, 72.896658),
                new LatLng(19.106400, 72.893622),
                new LatLng(19.105948, 72.887958),
                new LatLng(19.112103, 72.888606),
                new LatLng(19.113773, 72.888741),
                new LatLng(19.110455, 72.890474),
                new LatLng(19.114730, 72.891986),
                new LatLng(19.115947, 72.892909),
                new LatLng(19.118059, 72.892737),
                new LatLng(19.117986, 72.890091),
                new LatLng(19.120109, 72.890682)
                //new LatLng(19.123938, 72.890334),
                //new LatLng(19.124304, 72.888996),
                //new LatLng(19.125306, 72.889313),
                //new LatLng(19.124516, 72.892807),
                // new LatLng(19.124974, 72.893088)
        ).strokeColor(Color.RED);
        //fillColor(Color.FF99660)

        polygon1 = map.addPolygon(polygonOptions_Powai);
        polygonList.add(polygon1);

        //Bandra Lat-Lng
        polygonOptions_Bandra=  new PolygonOptions().add(
                new LatLng(19.066020, 72.823055),
                new LatLng(19.066402, 72.82367),
                new LatLng(19.065972, 72.824331),
                new LatLng(19.065512, 72.828247),
                new LatLng(19.064437, 72.828030),
                new LatLng(19.063609, 72.826796),
                new LatLng(19.062958, 72.826528),
                new LatLng(19.062789, 72.827701),
                new LatLng(19.061330, 72.827395),
                new LatLng(19.061637, 72.829564),
                new LatLng(19.068416, 72.830073),
                new LatLng(19.067834, 72.833454),
                new LatLng(19.062167, 72.834525),
                new LatLng(19.062055, 72.835289),
                new LatLng(19.062019, 72.835761),
                new LatLng(19.061772, 72.835845),
                new LatLng(19.061724, 72.835933),
                new LatLng(19.061641, 72.836447),
                new LatLng(19.063800, 72.836498),
                new LatLng(19.064097, 72.837822),
                new LatLng(19.064134, 72.839123),
                new LatLng(19.051060, 72.842013),
                new LatLng(19.050346, 72.840433),
                new LatLng(19.049756, 72.839782),
                new LatLng(19.049020, 72.839489),
                new LatLng(19.048525, 72.838391),
                new LatLng(19.049840, 72.838379),
                new LatLng(19.049919, 72.834121),
                new LatLng(19.049449, 72.834095),
                new LatLng(19.049437, 72.833776),
                new LatLng(19.049328, 72.833291),
                new LatLng(19.046313, 72.830267),
                new LatLng(19.046048, 72.829885),
                new LatLng(19.045216, 72.829783),
                new LatLng(19.042393, 72.823875),
                new LatLng(19.041405, 72.823110),
                new LatLng(19.040982, 72.822587),
                new LatLng(19.040584, 72.822497),
                new LatLng(19.043093, 72.821591),
                new LatLng(19.042960, 72.821170),
                new LatLng(19.042466, 72.820622),
                new LatLng(19.041634, 72.818223),
                new LatLng(19.045734, 72.818861),
                new LatLng(19.050884, 72.821170),
                new LatLng(19.057782, 72.820979),
                new LatLng(19.063721, 72.822346),
                new LatLng(19.064975, 72.823137)
        ).strokeColor(Color.RED);
        //fillColor(Color.FF99660)

        polygon2 = map.addPolygon(polygonOptions_Bandra);
        polygonList.add(polygon2);
        //Andheri west lat-lng
        polygonOptions_Andheri=  new PolygonOptions().add(
                new LatLng(19.146933, 72.806763),
                new LatLng(19.148320, 72.805700),
                new LatLng(19.165793, 72.826603),
                new LatLng(19.154499, 72.829819),
                new LatLng(19.151403, 72.835889),
                new LatLng(19.146186, 72.836617),
                new LatLng(19.145722, 72.831243),
                new LatLng(19.139194, 72.831887),
                new LatLng(19.139269, 72.833298),
                new LatLng(19.140301, 72.833086),
                new LatLng(19.140330, 72.835786),
                new LatLng(19.140874, 72.838973),
                new LatLng(19.137950, 72.840763),
                new LatLng(19.135169, 72.841734),
                new LatLng(19.134424, 72.842311),
                new LatLng(19.132245, 72.839580),
                new LatLng(19.129752, 72.837657),
                new LatLng(19.129921, 72.840387),
                new LatLng(19.128378, 72.840259),
                new LatLng(19.127558, 72.847736),
                new LatLng(19.121675, 72.847022),
                new LatLng(19.121942, 72.846306),
                new LatLng(19.116252, 72.843932),
                new LatLng(19.115770, 72.846153),
                new LatLng(19.112732, 72.845668),
                new LatLng(19.112925, 72.843779),
                new LatLng(19.113721, 72.842019),
                new LatLng(19.111502, 72.841304),
                new LatLng(19.111719, 72.838344),
                new LatLng(19.112563, 72.837298),
                new LatLng(19.114179, 72.836558),
                new LatLng(19.115987, 72.835001),
                new LatLng(19.116517, 72.836711),
                new LatLng(19.119772, 72.836201),
                new LatLng(19.121171, 72.834950),
                new LatLng(19.121147, 72.829923),
                new LatLng(19.116108, 72.829847),
                new LatLng(19.116059, 72.828086),
                new LatLng(19.118277, 72.819538),
                new LatLng(19.131619, 72.812147),
                new LatLng(19.141245, 72.803043)
                //new LatLng(19.146901, 72.806874),
                //new LatLng(19.160122, 72.818303),
                //new LatLng(19.154602, 72.829939)
        ).strokeColor(Color.RED);
        //fillColor(Color.FF99660)

        polygon3 = map.addPolygon(polygonOptions_Andheri);
        polygonList.add(polygon3);
        //Lower-Parel lat-lng
        polygonOptions_LowerParel=  new PolygonOptions().add(
                new LatLng(19.009968, 72.823866),
                new LatLng(19.008629, 72.830775),
                new LatLng(19.004815, 72.831075),
                new LatLng(19.006615, 72.835101),
                new LatLng(19.005131, 72.835426),
                new LatLng(18.987712, 72.832936),
                new LatLng(18.986381, 72.831709),
                new LatLng(18.989529, 72.831599),
                new LatLng(18.991968, 72.824346),
                new LatLng(18.992714, 72.824710),
                new LatLng(18.993431, 72.823860),
                new LatLng(18.994177, 72.822889),
                new LatLng(18.998424, 72.821827),
                new LatLng(19.004478, 72.822586),
                new LatLng(19.006228, 72.823405),
                new LatLng(19.006257, 72.823587)
        ).strokeColor(Color.RED);

        polygon4 = map.addPolygon(polygonOptions_LowerParel);
        polygonList.add(polygon4);

        Log.i("No of Polygons", polygonList.size() + "hello");
    }


    boolean contains;

    public void onMapClick(LatLng point,Context context) {
        for (Polygon p : polygonList) {
            contains = PolyUtil.containsLocation(point, p.getPoints(), false);
            if (contains)
                break;
        }

        if(contains == true){
            Toast.makeText(context, "You are in serviceable region", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "You are not in serviceable region", Toast.LENGTH_SHORT).show();}

}
