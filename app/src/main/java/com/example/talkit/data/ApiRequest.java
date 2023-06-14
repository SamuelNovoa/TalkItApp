package com.example.talkit.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiRequest {
    private static ApiRequest instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private ApiRequest(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiRequest getInstance(Context context) {
        if (instance == null)
            instance = new ApiRequest(context);

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());

        return requestQueue;
    }
}
