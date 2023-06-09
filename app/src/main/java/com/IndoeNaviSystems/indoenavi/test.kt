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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Test : AppCompatActivity() {

    val uwbManager = EstimoteUWBFactory.create()
    var job: Job? = null;
    var moduleSwitcher: Job? = null
    var foundDevices: List<EstimoteDevice>? = null
    var deviceSwitch = false;
    var dummy = 0;
    private var tapCount = 0
    private var tapCounterStartMillis: Long = 0
    var time2 = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
        //Pause jobs and start a new one to range
        val myView = com.indoenavisystems.indoenavi.DynamicView(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(myView)
        var jens = 0;
        // Step 2 - Optional - call init and pass your activity/fragment/class implementing ActivityResultCaller parameter
        // Step 3 - subscribe to observing UWB devices and ranging results
        uwbManager.uwbDevices.onEach {
            //display list of available devices in some kind of selectable list
            if(it is EstimoteUWBScanResult.Devices && jens < 1){
                if(it.devices.size > 2){
                    val devices = it.devices
                    if (devices.size > 2) {
                        moduleSwitcher = lifecycleScope.launch{
                            while (true){
                                for (device in devices) {
                                    //Make sure no ranging is ongoing
                                    stopRanging()
                                    delay(10)
                                    onMyMainActivityListItemClicked(device)
                                    while (!deviceSwitch) {
                                        delay(10)
                                    }
                                    deviceSwitch = false
                                    stopRanging()
                                }
                            }
                        }
                    }
                    jens++
                }
            }

            when(it){
                is EstimoteUWBScanResult.Devices -> foundDevices = it.devices;
                is EstimoteUWBScanResult.Error -> Log.w("UWB2 scan error", "${it.errorCode}")
                EstimoteUWBScanResult.ScanNotStarted -> Log.w("UWB2 scan not started", "scan not started")
            }

        }.launchIn(lifecycleScope)

        uwbManager.rangingResult.onEach {
            //display Beacon position
            when(it) {
                is EstimoteUWBRangingResult.Position -> {
                    deviceSwitch = true;
                    Log.d("UWB22", "Device id: ${it.device.address}   Position: ${it.position.distance?.value.toString()}   Azimuth: ${it.position.azimuth?.value}   time:${System.currentTimeMillis() - time2}   x:${DynamicView.GetPosition().x} y:${DynamicView.GetPosition().y}")
                    if (dummy == 0){
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance1(it1) };
                    }
                    else{
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance2(it1) }
                    }

                }
                EstimoteUWBRangingResult.Error.IllegalArgumentException -> Log.d("UWBe",EstimoteUWBRangingResult.Error.IllegalArgumentException.message)
                EstimoteUWBRangingResult.Error.IllegalStateException -> Log.d("UWB2e",EstimoteUWBRangingResult.Error.IllegalStateException.message)
                EstimoteUWBRangingResult.Error.SecurityException -> Log.d("UWB2e",EstimoteUWBRangingResult.Error.SecurityException.message)
                is EstimoteUWBRangingResult.Error.Unknown -> Log.d("UWB2u",it.message)
                EstimoteUWBRangingResult.Error.UwbSystemCallbackException -> Log.d("UWB2e",EstimoteUWBRangingResult.Error.UwbSystemCallbackException.message)
                EstimoteUWBRangingResult.Initiated -> Log.d("UWB2i","Started ranging")
                is EstimoteUWBRangingResult.PeerDisconnected -> Log.d("UWB2d","Peer Disconnected: ${it.device.address.toString()}")
            }

        }.launchIn(lifecycleScope)

        // Step 4 - start scanning for UWB devices. This triggers results in uwbDevices.
        uwbManager.startDeviceScanning(this);
    }

    // Step 5 - call connect on selected UWB device to start ranging
    private fun onMyMainActivityListItemClicked(device: EstimoteDevice) {
        var ctx: Context = this;
        time2 = System.currentTimeMillis()
        job = lifecycleScope.launch {
            device.device?.let { uwbManager.connectSuspend(it, ctx) }
        }
    }

    fun stopRanging() {
        job?.cancel()
    }

    fun startRanging(){
        moduleSwitcher = lifecycleScope.launch {

        }
    }

    override fun onStop() {
        super.onStop()
        //Stop ranging
        job?.cancel()
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        val eventaction = event!!.action
//        if (eventaction == MotionEvent.ACTION_UP) {
//
//            //get system current milliseconds
//            val time = System.currentTimeMillis()
//
//
//            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
//            if (tapCounterStartMillis == 0L || time - tapCounterStartMillis > 3000) {
//                tapCounterStartMillis = time
//                tapCount = 1
//            } else { //  time-tapCounterStartMillis < 3000
//                tapCount++
//            }
//            if (tapCount == 5) {
//                var ctx: Context = this;
//
//                job2 = lifecycleScope.launch {
//                    foundDevices?.get(1)?.device?.let {Log.d("UWB2","Doing it ${it.address}"); uwbManager2.connectSuspend(it, ctx) }
//                }
//            }
//            return true
//        }
//        return false
//    }
}