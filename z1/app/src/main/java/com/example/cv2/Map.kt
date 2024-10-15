package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.CameraOptions
import com.mapbox.geojson.Point

class Map : Fragment(R.layout.fragment_map) {

    private lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapView)

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
            val cameraPosition = CameraOptions.Builder()
                .center(Point.fromLngLat(17.10674, 48.14816))
                .zoom(10.0)
                .build()

            mapView.getMapboxMap().setCamera(cameraPosition)
        }

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            view.findNavController().navigate(R.id.logout)
        }
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