package com.igalata.wavesidebar

import android.view.animation.Interpolator

/**
 * Created by irinagalata on 1/29/18.
 */
class SpringInterpolator : Interpolator {

    var factor = 0.4f // the bigger value the less amplitude

    override fun getInterpolation(x: Float): Float {
        return (Math.pow(2.0, -10.0 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }

}