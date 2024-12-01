package com.example.cv2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MapViewModel(private val dataRepository: DataRepository) : ViewModel() {
    val lat = MutableLiveData<Double>()
    val lon = MutableLiveData<Double>()
    val radius = MutableLiveData<Double>()

    init {
        radius.value = 100.0
    }

    private val _updateResult = MutableLiveData<String>()
    val updateResult: LiveData<String> get() = _updateResult

    fun updateGeofence() {
        val latValue = lat.value ?: return
        val lonValue = lon.value ?: return
        val radiusValue = radius.value ?: return

        viewModelScope.launch {
            Log.d("MapViewModel", "ViewModel poloha je $latValue $lonValue $radiusValue")
            val result = dataRepository.apiUpdateGeofence(latValue, lonValue, radiusValue)
            _updateResult.postValue(result)
        }
    }

    fun setLatLon(newLat: Double, newLon: Double) {
        Log.d("MapViewModel", "setLatLon called with newLat: $newLat, newLon: $newLon")
        if ((lat.value != newLat || lon.value != newLon) && radius.value != null) {
            lat.postValue(newLat)
            lon.postValue(newLon)
            Log.d("MapViewModel", "spustam updateGeofence")
            updateGeofence()
        }
    }
}