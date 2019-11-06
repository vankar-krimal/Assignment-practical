package com.android.assignment.viewmodel;

import android.content.Context;

import com.android.assignment.api.client.FactClient;
import com.android.assignment.model.Fact;

import java.util.ArrayList;

public class FactsViewModel {

    private Context context;

    private FactsCallBack callBack;

    // constructor
    public FactsViewModel(Context context, FactsCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    // method for api call using client
    public void getFacts() {
        FactClient client = new FactClient(context);
        client.getFacts(new FactClient.FactsClientApiCallBack() {
            @Override
            public void onError(String error) {
                callBack.onError(error);
            }

            @Override
            public void onGetFacts(ArrayList<Fact> facts) {
                callBack.onGetFacts(facts);
            }

            @Override
            public void onGetTitle(String title) {
                callBack.onGetTitle(title);
            }
        });
    }

    // callback (interface) for view model
    public interface FactsCallBack {

        void onError(String error);

        void onGetFacts(ArrayList<Fact> facts);

        void onGetTitle(String title);

    }
}
