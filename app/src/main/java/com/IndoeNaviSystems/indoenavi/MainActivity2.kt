package com.indoenavisystems.indoenavi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.estimote.uwb.api.EstimoteUWBFactory
import com.estimote.uwb.api.ranging.EstimoteUWBRangingResult
import com.estimote.uwb.api.scanning.EstimoteDevice
import com.estimote.uwb.api.scanning.EstimoteUWBScanResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    val uwbManager = EstimoteUWBFactory.create()
    val devices = ArrayList<EstimoteDevice>()
    var job: Job? = null
    var job2: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
        //Pause jobs and start a new one to range
        val myView = com.indoenavisystems.indoenavi.DynamicView(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(myView)


        uwbManager.uwbDevices.onEach {
            when (it){
                is EstimoteUWBScanResult.Devices -> {
                    for (device in it.devices){
                        if (!checkIfDeviceExist(device)){
                            devices.add(device);
                            if(devices.size == 1){
                                onMyMainActivityListItemClicked(device, 1);
                            }
                            if(devices.size == 2){
                                onMyMainActivityListItemClicked(device,2);
                            }
                            Log.d("UWB-Adding estimote device", "New device found!" + it.toString());

                        }
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
                }
                is EstimoteUWBRangingResult.PeerDisconnected -> {
                    Log.d("UWB-Distance", "PeerDisconnected: ${it.device.address}");
                }
                else -> {}
            }
        }.launchIn(lifecycleScope)

        uwbManager.startDeviceScanning(this)
    }

    private fun checkIfDeviceExist(device : EstimoteDevice): Boolean {
        for (knownDevice in devices){
            if (knownDevice.deviceId == device.deviceId){
                return true;
            }
        }
        return false;
    }

    // Step 5 - call connect on selected UWB device to start ranging
    private fun onMyMainActivityListItemClicked(device: EstimoteDevice, jobint: Int) {
        Log.d("UWB-Starting connect suspend", device.toString());
        var ctx: Context = this;
        if(jobint == 1) {
            device.device?.let {
                job = lifecycleScope.launch {
                    uwbManager.connectSuspend(device.device!!, ctx)
                }
            }
        }
        if(jobint == 2) {
            device.device?.let {
                job2 = lifecycleScope.launch {
                    uwbManager.connectSuspend(device.device!!, ctx)
                }
            }
        }

    }
}