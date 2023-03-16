package com.IndoeNaviSystems.indoenavi;

import android.content.Context;
import android.util.Log;

import com.IndoeNaviSystems.indoenavi.Interfaces.VolleyCallBack;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiRequest {

    private Context ctx;

    public ApiRequest(Context context) {
        ctx = context;
    }


    public void request(String url, final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //If we did not get a bad response then proceed response.
                        Log.d("response123", response.toString());
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response123-failed", error.toString());
                        callBack.onFail(error);
                    }
                }) {
        };
        NetworkRequests.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
}
