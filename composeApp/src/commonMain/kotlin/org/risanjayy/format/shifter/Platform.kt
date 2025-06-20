package org.risanjayy.format.shifter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform