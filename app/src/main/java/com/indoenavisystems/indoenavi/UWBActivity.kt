package com.indoenavisystems.indoenavi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.estimote.uwb.api.EstimoteUWBFactory
import com.estimote.uwb.api.ranging.EstimoteUWBRangingResult
import com.estimote.uwb.api.scanning.EstimoteDevice
import com.estimote.uwb.api.scanning.EstimoteUWBScanResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UWBActivity : AppCompatActivity() {
    val uwbManager = EstimoteUWBFactory.create()
    var job: Job? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uwbactivity)
        //Pause jobs and start a new one to range
        var oneTimeBool = true;
        // Step 2 - Optional - call init and pass your activity/fragment/class implementing ActivityResultCaller parameter
        // Step 3 - subscribe to observing UWB devices and ranging results
        uwbManager.uwbDevices.onEach {
            when (it){
                is EstimoteUWBScanResult.Devices -> {

//                    val device = it.devices.find {  device -> device.deviceId.startsWith("F97191")}
                    if(it.devices.size > 0 && oneTimeBool){
                        oneTimeBool = false;
                        onMyMainActivityListItemClicked(it.devices[0])
                    }
                }
                else -> {
                }
            }
        }.launchIn(lifecycleScope)

        uwbManager.rangingResult.onEach {
            //display Beacon position
            when(it) {
                is EstimoteUWBRangingResult.Position -> {
                    Log.d("UWB-Distance", it.device.address.toString() + " : " + it.position.distance?.value.toString());

                }
                is EstimoteUWBRangingResult.PeerDisconnected -> {
                    Log.d("UWB-Distance", "PeerDisconnected: ${it.device.address}");
                }

                EstimoteUWBRangingResult.Error.IllegalArgumentException -> Log.d("UWB-e",EstimoteUWBRangingResult.Error.IllegalArgumentException.message)
                EstimoteUWBRangingResult.Error.IllegalStateException -> Log.d("UWB-e",EstimoteUWBRangingResult.Error.IllegalStateException.message)
                EstimoteUWBRangingResult.Error.SecurityException -> Log.d("UWB-e",EstimoteUWBRangingResult.Error.SecurityException.message)
                is EstimoteUWBRangingResult.Error.Unknown -> Log.d("UWB-u",it.message)
                EstimoteUWBRangingResult.Error.UwbSystemCallbackException -> Log.d("UWB2e",EstimoteUWBRangingResult.Error.UwbSystemCallbackException.message)
                EstimoteUWBRangingResult.Initiated -> Log.d("UWB-i","Started ranging")
            }

        }.launchIn(lifecycleScope)

        // Step 4 - start scanning for UWB devices. This triggers results in uwbDevices.
        uwbManager.startDeviceScanning(this);
    }

    // Step 5 - call connect on selected UWB device to start ranging
    private fun onMyMainActivityListItemClicked(device: EstimoteDevice) {
        var ctx: Context = this;
        job = lifecycleScope.launch {
            device.device?.let {Log.d("UWB-","skrr"); uwbManager.connectSuspend(it, ctx) }
        }
    }

    fun stopRanging() {
        job?.cancel()
    }

    override fun onStop() {
        super.onStop()
        //Stop ranging
        job?.cancel()
    }
}