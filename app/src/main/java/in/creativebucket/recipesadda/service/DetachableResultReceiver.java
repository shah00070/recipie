package in.creativebucket.recipesadda.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import in.creativebucket.recipesadda.utils.LogUtils;

/**
 * Created by Chandan Kumar on 13/09/15.
 */

public class DetachableResultReceiver extends ResultReceiver {
    private static final String TAG = LogUtils.makeLogTag(DetachableResultReceiver.class);

    public static interface Receiver {

        public abstract void onReceiveResult(int i, Bundle bundle);
    }


    private Receiver mReceiver;

    public DetachableResultReceiver(Handler handler) {
        super(handler);
    }

    public void clearReceiver() {
        mReceiver = null;
    }

    protected void onReceiveResult(int i, Bundle bundle) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(i, bundle);
            return;
        } else {
            LogUtils.LOGD(TAG, (new StringBuilder()).append("Dropping result on floor for code ").append(i).append(": ").append(bundle.toString()).toString());
            return;
        }
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

}
