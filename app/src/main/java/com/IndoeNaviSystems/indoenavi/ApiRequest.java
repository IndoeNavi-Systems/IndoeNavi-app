package com.IndoeNaviSystems.indoenavi;

import android.content.Context;
import android.util.Log;

import com.IndoeNaviSystems.indoenavi.Interfaces.VolleyCallBack;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiRequest {

    private Context ctx;

    public ApiRequest(Context context) {
        ctx = context;
    }


    public void jsonRequest(String url,int method, final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //If we did not get a bad response then proceed response.
                        Log.d("jsonResponse", response.toString());
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("jsonResponse-failed", error.toString());
                        callBack.onFail(error);
                    }
                }) {
        };
        NetworkRequests.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public void stringRequest(String url, int method, final VolleyCallBack callBack) {
        StringRequest request = new StringRequest(method, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("stringResponse", response);
                if(callBack != null) {
                    callBack.onSuccess(response);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("stringResponse-failed", error.toString());
                if(callBack != null){
                    callBack.onFail(error);
                }
            }
        });
        NetworkRequests.getInstance(ctx).addToRequestQueue(request);
    }

}
