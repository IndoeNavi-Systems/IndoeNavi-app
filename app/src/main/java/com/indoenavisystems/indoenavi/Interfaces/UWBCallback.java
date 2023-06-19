package com.indoenavisystems.indoenavi.Interfaces;

import androidx.core.uwb.RangingPosition;

public interface UWBCallback {
    //Send range back to activity
    void rangeCallback(RangingPosition position);
}
