package com.android.assignment.helper;

import android.content.Context;

import com.android.assignment.MyApplication;
import com.android.assignment.model.FactsResponse;

public class PrefUtils {

    private static String FACTS = "FACTS";

    public static void setFactsResponse(Context context, FactsResponse factsResponse) {
        String toJson = MyApplication.getGson().toJson(factsResponse);
        Prefs.with(context).save(FACTS, toJson);
    }

    public static FactsResponse getFactsResponse(Context context) {
        FactsResponse factsResponse = new FactsResponse();
        String getFactsString = Prefs.with(context).getString(FACTS, "");
        try {
            factsResponse = MyApplication.getGson().fromJson(getFactsString, FactsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factsResponse;
    }
}
