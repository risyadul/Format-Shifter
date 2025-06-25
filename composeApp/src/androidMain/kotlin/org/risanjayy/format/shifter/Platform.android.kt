package org.risanjayy.format.shifter

import android.os.Build
import org.risanjayy.format.shifter.view.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}