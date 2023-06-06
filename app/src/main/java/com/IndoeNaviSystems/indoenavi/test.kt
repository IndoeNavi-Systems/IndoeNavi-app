package com.indoenavisystems.indoenavi

import android.content.Context
import com.estimote.uwb.api.EstimoteUWBFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.CoroutineScope

class test(
        val ctx:Context,
        val lifecycleScope: CoroutineScope
        ) {

    fun jens() {
        val uwbManager = EstimoteUWBFactory.create()

        // Step 2 - Optional - call init and pass your activity/fragment/class implementing ActivityResultCaller parameter.
        // Step 3 - subscribe to observing UWB devices and ranging results
        uwbManager.uwbDevices.onEach {
        //display list of available devices in some kind of selectable list
        }.launchIn(lifecycleScope)
        uwbManager.rangingResult.onEach {
        //display Beacon position
        }.launchIn(lifecycleScope)
        // Step 4 - start scanning for UWB devices. This triggers results in uwbDevices.
        uwbManager.startDeviceScanning(ctx);
    }
}