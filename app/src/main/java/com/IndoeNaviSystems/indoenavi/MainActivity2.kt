package com.indoenavisystems.indoenavi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
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

class MainActivity2 : AppCompatActivity() {

    val uwbManager = EstimoteUWBFactory.create()
    val uwbManager2 = EstimoteUWBFactory.create()
    var job: Job? = null;
    var job2: Job? = null;
    var moduleSwitcher: Job? = null
    var foundDevices: List<EstimoteDevice>? = null
    var deviceSwitch = false;
    var dummy = 0;
    private var tapCount = 0
    private var tapCounterStartMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)

        val myView = DynamicView(this)
        setContentView(myView)

        var jens = 0;
        // Step 2 - Optional - call init and pass your activity/fragment/class implementing ActivityResultCaller parameter
        // Step 3 - subscribe to observing UWB devices and ranging results
        uwbManager.uwbDevices.onEach {
            //display list of available devices in some kind of selectable list

            if(it is EstimoteUWBScanResult.Devices && jens < 1){
                val h = it as EstimoteUWBScanResult.Devices
                if(h.devices.size > 2){
                    Log.d("UWB2",h.devices[0].toString())
                    jens++;


                    foundDevices = h.devices;
                    onMyMainActivityListItemClicked(h.devices[0])

//                    moduleSwitcher = lifecycleScope.launch {
//                        while(true){
//                            dummy = 0;
//                            onMyMainActivityListItemClicked(h.devices[0])
//                            while(!deviceSwitch){
//                                delay(10);
//                            }
//                            deviceSwitch = false;
//                            stop()
//                            dummy = 1
//                            onMyMainActivityListItemClicked(h.devices[1])
//                            while(!deviceSwitch){
//                                delay(10);
//                            }
//                            deviceSwitch = false;
//                            stop()
//
//                        }
//                    }

//                    startRanging();
                }
            }

        }.launchIn(lifecycleScope)

        uwbManager.rangingResult.onEach {
            //display Beacon position
            when(it) {
                is EstimoteUWBRangingResult.Position -> {
                    Log.d("UWB22", "Device id: ${it.device.address}   Position: ${it.position.distance?.value.toString()}   Azimuth: ${it.position.azimuth?.value}   x:${DynamicView.GetPosition().x} y:${DynamicView.GetPosition().y}")
                    if (dummy == 0){
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance1(it1) };
                    }
                    else{
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance2(it1) }
                    }

                    deviceSwitch = true;
                }
                EstimoteUWBRangingResult.Error.IllegalArgumentException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.IllegalArgumentException.message)
                EstimoteUWBRangingResult.Error.IllegalStateException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.IllegalStateException.message)
                EstimoteUWBRangingResult.Error.SecurityException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.SecurityException.message)
                is EstimoteUWBRangingResult.Error.Unknown -> Log.d("UWB2",it.message)
                EstimoteUWBRangingResult.Error.UwbSystemCallbackException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.UwbSystemCallbackException.message)
                EstimoteUWBRangingResult.Initiated -> Log.d("UWB2","Started ranging")
                is EstimoteUWBRangingResult.PeerDisconnected -> Log.d("UWB2","Peer Disconnected: ${it.device.address.toString()}")
            }

        }.launchIn(lifecycleScope)

        uwbManager2.rangingResult.onEach {
            //display Beacon position
            when(it) {
                is EstimoteUWBRangingResult.Position -> {
                    Log.d("UWB22", "Device id: ${it.device.address}   Position: ${it.position.distance?.value.toString()}   Azimuth: ${it.position.azimuth?.value}   x:${DynamicView.GetPosition().x} y:${DynamicView.GetPosition().y}")
                    if (dummy == 0){
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance1(it1) };
                    }
                    else{
                        it.position.distance?.value?.let { it1 -> DynamicView.SetDistance2(it1) }
                    }

                    deviceSwitch = true;
                }
                EstimoteUWBRangingResult.Error.IllegalArgumentException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.IllegalArgumentException.message)
                EstimoteUWBRangingResult.Error.IllegalStateException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.IllegalStateException.message)
                EstimoteUWBRangingResult.Error.SecurityException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.SecurityException.message)
                is EstimoteUWBRangingResult.Error.Unknown -> Log.d("UWB2",it.message)
                EstimoteUWBRangingResult.Error.UwbSystemCallbackException -> Log.d("UWB2",EstimoteUWBRangingResult.Error.UwbSystemCallbackException.message)
                EstimoteUWBRangingResult.Initiated -> Log.d("UWB2","Started ranging")
                is EstimoteUWBRangingResult.PeerDisconnected -> Log.d("UWB2","Peer Disconnected: ${it.device.address.toString()}")
            }

        }.launchIn(lifecycleScope)
        // Step 4 - start scanning for UWB devices. This triggers results in uwbDevices.
        uwbManager.startDeviceScanning(this);

    }

    // Step 5 - call connect on selected UWB device to start ranging
    private fun onMyMainActivityListItemClicked(device: EstimoteDevice) {
        var ctx: Context = this;

        job = lifecycleScope.launch {
            device.device?.let { uwbManager.connectSuspend(it, ctx) }
        }
    }

    fun stop() {
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val eventaction = event!!.action
        if (eventaction == MotionEvent.ACTION_UP) {

            //get system current milliseconds
            val time = System.currentTimeMillis()


            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (tapCounterStartMillis == 0L || time - tapCounterStartMillis > 3000) {
                tapCounterStartMillis = time
                tapCount = 1
            } else { //  time-tapCounterStartMillis < 3000
                tapCount++
            }
            if (tapCount == 5) {
                var ctx: Context = this;
                job2 = lifecycleScope.launch {
                    foundDevices?.get(2)?.device?.let {Log.d("UWB2","Doing it ${it.address}"); uwbManager2.connectSuspend(it, ctx) }
                }
            }
            return true
        }
        return false
    }
}