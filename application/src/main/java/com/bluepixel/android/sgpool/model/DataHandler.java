package com.bluepixel.android.sgpool.model;


import android.content.ContentProviderOperation;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import static com.bluepixel.android.sgpool.util.LogUtils.makeLogTag;

/**
 * Created by wintunlin on 11/8/14.
 */
public class DataHandler extends JSONHandler{

    private static final String TAG = makeLogTag(DataHandler.class);
    private ArrayList<Data> mBlocks = new ArrayList<Data>();

    public DataHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {

    }

    @Override
    public void process(JsonElement element) {
        for (Data block : new Gson().fromJson(element, Data[].class)) {
            mBlocks.add(block);
        }
    }
}
