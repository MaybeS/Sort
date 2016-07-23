package com.maybestudio.sort.sort_gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by saltfactory on 6/8/15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * GCM�� ���� Instance ID�� ��ū�� �����Ͽ� �����´�.
     * @param intent
     */
    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {

        // GCM Instance ID�� ��ū�� �������� �۾��� ���۵Ǹ� LocalBoardcast�� GENERATING �׼��� �˷� ProgressBar�� �����ϵ��� �Ѵ�.
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(new Intent(QuickstartPreferences.REGISTRATION_GENERATING));

        // GCM�� ���� Instance ID�� �����´�.
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try {
            synchronized (TAG) {
                // GCM ���� ����ϰ� ȹ���� ���������� google-services.json�� ������� SenderID�� �ڵ����� �����´�.
                String default_senderId = getString(R.string.gcm_defaultSenderId);
                // GCM �⺻ scope�� "GCM"�̴�.
                String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                // Instance ID�� �ش��ϴ� ��ū�� �����Ͽ� �����´�.
                token = instanceID.getToken(default_senderId, scope, null);

                Log.i(TAG, "GCM Registration Token: " + token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // GCM Instance ID�� �ش��ϴ� ��ū�� ȹ���ϸ� LocalBoardcast�� COMPLETE �׼��� �˸���.
        // �̶� ��ū�� �Բ� �Ѱ��־ UI�� ��ū ������ Ȱ���� �� �ֵ��� �ߴ�.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}