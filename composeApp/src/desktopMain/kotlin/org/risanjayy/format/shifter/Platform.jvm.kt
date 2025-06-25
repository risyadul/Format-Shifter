package org.risanjayy.format.shifter

import org.risanjayy.format.shifter.view.Platform

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}