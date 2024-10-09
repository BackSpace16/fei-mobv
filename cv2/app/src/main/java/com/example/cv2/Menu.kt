package com.example.cv2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout

class Menu(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_menu, this, true)
        // kod na spracovania kliknuti na ikony
        // ...
    }

    // pridanie metod, ktore dokazu menit stav widgetu
    // .. napriklad zvyraznenie, aktivnej ikony
}
