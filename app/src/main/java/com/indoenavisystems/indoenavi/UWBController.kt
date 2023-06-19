package com.indoenavisystems.indoenavi

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.estimote.uwb.api.EstimoteUWBFactory
import com.estimote.uwb.api.ranging.EstimoteUWBRangingResult
import com.estimote.uwb.api.scanning.EstimoteDevice
import com.estimote.uwb.api.scanning.EstimoteUWBScanResult
import com.indoenavisystems.indoenavi.Interfaces.UWBCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UWBController {
    val ctx: Context
    val uwbManager = EstimoteUWBFactory.create()
    var devices = ArrayList<EstimoteDevice>()
    var job: Job? = null
    val lifecycleScope: CoroutineScope
    val uwbCallback: UWBCallback

    constructor(ctx: Context, lifecycle: Lifecycle, callback: UWBCallback) {
        this.ctx = ctx
        this.lifecycleScope = lifecycle.coroutineScope;
        this.uwbCallback = callback;

        var oneTimeBool = true;

        uwbManager.uwbDevices.onEach {
            when (it){
                is EstimoteUWBScanResult.Devices -> {

                    val device = it.devices.find {  device -> device.deviceId.startsWith("F97191")}
                    if(device != null && oneTimeBool){
                        oneTimeBool = false;
                        connectToUWBDevice(device)
                    }
                }
                else -> {
                }
            }
        }.launchIn(lifecycleScope)

        uwbManager.rangingResult.onEach {
            Log.d("UWB-RangingResult", it.toString());
            when(it){
                is EstimoteUWBRangingResult.Position -> {
                    Log.d("UWB-Distance", it.device.address.toString() + " : " + it.position.distance?.value.toString());
//                    uwbCallback.rangeCallback(it.position)
                }
                is EstimoteUWBRangingResult.PeerDisconnected -> {
                    Log.d("UWB-Distance", "PeerDisconnected: ${it.device.address}");
                }
                else -> {
                    Log.d("UWB-else","")
                }
            }
        }.launchIn(lifecycleScope)

        uwbManager.startDeviceScanning(ctx)
    }

    private fun connectToUWBDevice(device: EstimoteDevice) {
        job = lifecycleScope.launch {
            device.device?.let {Log.d("UWB-De", "skrrt"); uwbManager.connectSuspend(it, ctx) }
        }
    }

}