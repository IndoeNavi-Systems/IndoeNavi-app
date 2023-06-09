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

class MainActivity2 : AppCompatActivity() {

    val uwbManager = EstimoteUWBFactory.create()
    val devices = ArrayList<EstimoteDevice>();
    var isActive : Boolean = false;
    var index : Int = 0;

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
                            devices.add(devices[index]);
                            Log.d("UWB-A", "New device found!" + it.toString());

                        }
                    }
                }
                else -> {
                }
            }
        }.launchIn(lifecycleScope)
        uwbManager.rangingResult.onEach {
            Log.d("UWB-B", it.toString());
            when(it){
                is EstimoteUWBRangingResult.Position -> {

                    Log.d("UWB-D", it.device.address.toString() + " : " + it.position.distance?.value.toString());
                    onMyMainAcitivityDisconnectClick();
                    isActive = false;
                }
                else -> {}
            }
        }.launchIn(lifecycleScope)
        uwbManager.startDeviceScanning(this)

        val t1 = Thread {
            while(true){
                if (devices.size > 5){
                    if (!isActive){
                        isActive = true;
                        onMyMainActivityListItemClicked(devices[index]);
                        index++;
                        if (index > 5){
                            index = 0;
                        }
                    }
                }
            }
        }
        t1.start()

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
    private fun onMyMainActivityListItemClicked(device: EstimoteDevice) {
        Log.d("UWB-C", device.toString());
        uwbManager.connect(device.device!!, this)
    }
    // Step 6 - Optional - disconnect device ranging.
    private fun onMyMainAcitivityDisconnectClick() {
        uwbManager.disconnectDevice()
    }
}