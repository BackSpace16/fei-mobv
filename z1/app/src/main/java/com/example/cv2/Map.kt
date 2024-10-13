package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.CameraOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.gestures.GesturesPlugin

class Map : Fragment(R.layout.fragment_map) {

    private lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nájdeme MapView
        mapView = view.findViewById(R.id.mapView)

        // Nastavenie štýlu mapy (môže byť napr. satelitný, tmavý, atď.)
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
            // Po načítaní štýlu mapy, nastavíme počiatočnú pozíciu
            val cameraPosition = CameraOptions.Builder()
                .center(Point.fromLngLat(17.10674, 48.14816))  // Súradnice Bratislavy
                .zoom(10.0)  // Úroveň priblíženia (12 je dobrá na mesto)
                .build()

            mapView.getMapboxMap().setCamera(cameraPosition)
        }

        /*mapView.gestures.enableAllGestures()

        mapView.gestures.apply {
            scrollEnabled = true
            rotateEnabled = true
            pitchEnabled = true
        }*/
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

}