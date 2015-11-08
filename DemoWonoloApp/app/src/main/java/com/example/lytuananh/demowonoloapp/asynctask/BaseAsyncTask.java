package com.example.lytuananh.demowonoloapp.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.lytuananh.demowonoloapp.R;
import com.example.lytuananh.demowonoloapp.callback.RequestCallback;
import com.example.lytuananh.demowonoloapp.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lytuananh on 11/8/15.
 */
public class BaseAsyncTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String[] keys;
    private String[] values;
    private RequestCallback mListener;
    private Boolean isShowLoading = true;
    private ProgressDialog pda;

    public BaseAsyncTask(Context context, String[] keys, String[] values,Boolean isLoading,RequestCallback listener) {
        mContext = context;
        this.keys = keys;
        this.values = values;
        mListener = listener;
        this.isShowLoading = isLoading;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowLoading){
            pda = new ProgressDialog(mContext, R.style.processDialog);
            pda.setCancelable(false);
            pda.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String content = Utilities.getUrlContents(params[0]);


        JSONObject obj = null;
        String data = "";
        try {
            obj = new JSONObject(content);
            if (obj.has(Utilities.INS_META)) {
                int code = obj.getJSONObject(Utilities.INS_META).getInt(Utilities.INS_CODE);
                if(code == Utilities.INS_SUCCESS_META_CODE) {
                    data =  obj.getString(Utilities.INS_DATA);
                }else{
                    //TODO
                }

            } else {
                //TODO
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pda != null && isShowLoading){
            pda.dismiss();
        }

        mListener.success(s);
    }
}

