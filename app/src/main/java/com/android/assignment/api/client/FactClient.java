package com.android.assignment.api.client;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.assignment.MyApplication;
import com.android.assignment.R;
import com.android.assignment.api.ApiService;
import com.android.assignment.api.Apis;
import com.android.assignment.api.ErrorResponse;
import com.android.assignment.helper.ConnectionUtils;
import com.android.assignment.helper.PrefUtils;
import com.android.assignment.model.Fact;
import com.android.assignment.model.FactsResponse;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FactClient {

    private Context context;
    private ProgressDialog dialog;

    // constructor
    public FactClient(Context context) {
        this.context = context;
    }

    // client method for api call
    public void getFacts(final FactsClientApiCallBack callBack) {
        if (!ConnectionUtils.isConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet));

            // if no internet but data exists in preference
            if (PrefUtils.getFactsResponse(context) != null) {
                callBack.onGetFacts(PrefUtils.getFactsResponse(context).getRows());
                callBack.onGetTitle(PrefUtils.getFactsResponse(context).getTitle());
            }
            return;
        }
        showDialog();
        ApiService apiService = MyApplication.getRetrofit().create(ApiService.class);
        apiService.getFacts().enqueue(new Callback<FactsResponse>() {
            @Override
            public void onResponse(Call<FactsResponse> call, Response<FactsResponse> response) {
                hideDialog();
                if (response.code() == Apis.OK_RESPONSE) {

                    if (response.body() != null) {

                        // save data to preference for offline mode
                        PrefUtils.setFactsResponse(context, response.body());

                        callBack.onGetFacts(response.body().getRows());
                        callBack.onGetTitle(response.body().getTitle());
                    } else {
                        callBack.onError(context.getString(R.string.cannot_parse));
                    }

                } else {
                    try {
                        ErrorResponse loginError = MyApplication.getGson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        callBack.onError(loginError.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callBack.onError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<FactsResponse> call, Throwable t) {
                hideDialog();
                callBack.onError(t.getMessage());
            }
        });
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Loading..");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // callback (interface) for api
    public interface FactsClientApiCallBack {

        void onError(String error);

        void onGetFacts(ArrayList<Fact> facts);

        void onGetTitle(String title);
    }
}
