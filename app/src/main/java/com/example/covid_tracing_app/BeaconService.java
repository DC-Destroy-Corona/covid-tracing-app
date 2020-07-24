package com.example.covid_tracing_app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BeaconService extends Service implements BeaconConsumer {
    private static final int BEACON_COUNT_LIMIT = 50;

    public static Intent serviceIntent = null;

    private Thread mainThread;

    private static final String TAG = "BeaconService";

    private BeaconManager beaconManager;

    private List<Beacon> beaconList = new ArrayList<>();// 감지된 비콘들을 임시로 담을 리스트
    private List<String> beaconIdList = new ArrayList<>();// 현재 범위 내에 감지되고 있는 비콘의 ID를 담을 리스트
    private List<String> beaconPreIdList = new ArrayList<>();// 현재 범위 내에 감지되고 있는 비콘들이 변경 됐을 때 이전에 감지된 비콘의 ID을 담을 리스트
    private List<String> newBeaconIdList = new ArrayList<>();// 새롭게 감지된 비콘을 찾기위한 index 리스트 (BeaconList의 index와 매칭)
    private List<JSONObject> jsonObjectList = new ArrayList<>();// 새롭게 감지된 비콘 정보를 저장하는 리스트 (UUID, MAJOR, MINOR, HEAD(감지된 시간) 값을 저장)

    private int count = 0;
    private List<String> head = new ArrayList<>();
    private List<String> tail = new ArrayList<>();

    private boolean sw = true;

    //private String url = "http://203.250.32.29:80";
    //private String url = "http://1.251.103.64:8888";
    private String url = "http://180.189.121.112:63000";

    public BeaconService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);
        //handler.sendEmptyMessage(0);

        Log.d(TAG, "onCreate() called");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");

        serviceIntent = intent;
        showToast(getApplication(), "Start Service");
        /*
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        Date date = new Date();
                        //showToast(getApplication(), sdf.format(date));
                        sendNotification(sdf.format(date));
                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();
        */


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy() called");

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }

    }
    /*
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d(TAG, "onTaskRemoved() called");

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }
    */


    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle("Service test")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onBeaconServiceConnect() {
        Toast.makeText(getApplicationContext(),"Start Beacon scan service", Toast.LENGTH_LONG).show();

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) { //이 엔터를 타야만 비콘검색가능
                Log.i(TAG, "I just saw an beacon for the first time!");

            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+i);
                if(i==0){
                    beaconList.clear();
                }
                count = i;
            }
        });
        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.i(TAG, "Search beacons in region");

                beaconList.clear();

                if(beaconIdList.size()!=0){
                    beaconPreIdList.clear();
                    beaconPreIdList.addAll(beaconIdList);
                }else{

                }
                beaconIdList.clear();
                newBeaconIdList.clear();

                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");

                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                        String uuid = String.valueOf(beacon.getId1());
                        String major = String.valueOf(beacon.getId2());
                        String minor = String.valueOf(beacon.getId3());
                        beaconIdList.add(uuid);
                        //새로운 beacon 탐색
                        if(!beaconPreIdList.contains(uuid)){
                            //beaconList에서
                            newBeaconIdList.add(uuid);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                //LocalDateTime currentTime = LocalDateTime.now();
                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                String time = mFormat.format(date);

                                jsonObject.put("uuid", uuid);
                                jsonObject.put("major", major);
                                jsonObject.put("minor", minor);
                                jsonObject.put("head", time);
                                jsonObject.put("tail","");

                                if(jsonObjectList.size()>=BEACON_COUNT_LIMIT){
                                    //NetworkTask networkTask= new NetworkTask(url+"/send",jsonObjectList.get(0),"POST");
                                    //networkTask.execute();
                                    String userId = "3";
                                    JSONObject message = new JSONObject();
                                    message.put("userId",userId);
                                    message.put("beacon",jsonObjectList.get(0));
                                    AmqpTask amqpTask = new AmqpTask(message);
                                    amqpTask.execute();

                                    jsonObjectList.remove(0);
                                    Log.e(TAG,"beacon count limit over");
                                }
                                jsonObjectList.add(jsonObject);

                                Log.i(TAG,"find new beacon");
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            //기존 존재하는 beacon에 대한 처리
                        }
                    }

                }

                if(beaconPreIdList.size()!=0) {
                    List<String> tempId = new ArrayList<>();
                    tempId.addAll(beaconIdList);
                    List<String> tempPreId = new ArrayList<>();
                    tempPreId.addAll(beaconPreIdList);
                    //새로운 beacon이 있다면 제외

                    for (int i = 0; i < newBeaconIdList.size(); i++) {
                        String newBeaconId = newBeaconIdList.get(i);
                        tempId.remove(newBeaconId);
                    }
                    //이전과 비교하여 여전히 존재하는 beacon 제외
                    tempPreId.removeAll(tempId);
                    //사라진 beacon 탐색
                    for (int j = 0; j < tempPreId.size(); j++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            //LocalDateTime currentTime = LocalDateTime.now();

                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            String time = mFormat.format(date);

                            String head = null;
                            String uuid = tempPreId.get(j);
                            String major = "";
                            String minor = "";
                            int key=BEACON_COUNT_LIMIT;

                            //list에 저장된 json 객체중 uuid 가 일치하는 객체 찾기
                            for(int k = 0; k < jsonObjectList.size(); k++){
                                JSONObject temp = jsonObjectList.get(k);
                                String tuuid = temp.getString("uuid");
                                if(tuuid.equals(uuid)){
                                    //찾을경우 head 값을 따로 저장
                                    head = jsonObjectList.get(k).getString("head");
                                    major = jsonObjectList.get(k).getString("major");
                                    minor = jsonObjectList.get(k).getString("minor");
                                    key=k;
                                }
                            }

                            //일치하는 list가 존재할 경우
                            if(key<BEACON_COUNT_LIMIT){
                                jsonObject.put("uuid", uuid);
                                jsonObject.put("major", major);
                                jsonObject.put("minor", minor);
                                jsonObject.put("head", head);
                                jsonObject.put("tail", time);

                                //NetworkTask networkTask= new NetworkTask(url+"/send",jsonObject,"POST");
                                //networkTask.execute();

                                String userId = "3";
                                JSONObject message = new JSONObject();
                                message.put("userId",userId);
                                message.put("beacon",jsonObject);
                                AmqpTask amqpTask = new AmqpTask(message);
                                amqpTask.execute();

                                //전송 후 list에서 삭제
                                jsonObjectList.remove(key);
                            }else{//일치하는 list가 존재하지 않을 경우(BEACON_COUNT_LIMIT를 넘어 삭제된 경우)
                                Log.e(TAG,"lost beacon head error");
                            }

                            beaconPreIdList.clear();
                            Log.i(TAG,"exit region");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                else{
                    if(jsonObjectList.size()>0){
                        //현재 감지되는 비컨이 없는데 비컨 리스트에 값이 남아있을 경우
                    }
                }

            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        JSONObject values;
        String method;

        NetworkTask(String url, JSONObject values, String method){
            this.url = url;
            this.values = values;
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values, method);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            Log.d(TAG, result);
        }
    }
}
