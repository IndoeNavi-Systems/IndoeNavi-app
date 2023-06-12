package com.indoenavisystems.indoenavi.Interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallBack <T> {
    void onSuccess(T successMessage);
    void onFail(VolleyError error);
}