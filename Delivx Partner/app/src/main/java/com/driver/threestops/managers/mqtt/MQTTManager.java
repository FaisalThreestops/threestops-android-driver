package com.driver.threestops.managers.mqtt;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.driver.threestops.RxObservers.RXMqttMessageObserver;
import com.driver.threestops.RxObservers.RxNetworkObserver;
import com.driver.threestops.app.bookingRequest.BookingPopUp;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.Threestops.BuildConfig;
import com.driver.Threestops.R;
import com.driver.threestops.managers.booking.BookingManager;
import com.driver.threestops.mqttChat.ChatDataObervable;
import com.driver.threestops.mqttChat.ChattingActivity;
import com.driver.threestops.networking.NetworkStateHolder;
import com.driver.threestops.pojo.BookingAssigned;
import com.driver.threestops.utility.AcknowledgeHelper;
import com.driver.threestops.utility.AcknowledgementCallback;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import static com.driver.threestops.utility.VariableConstant.FORGROUND_LOCK;
import static com.driver.threestops.utility.VariableConstant.IS_POP_UP_OPEN;


/**
 * <h1>MQTTManager</h1>
 * <p>Mqtt Connection, Subscribe and manage the Mqtt messages</p>
 */
public class MQTTManager
{
    private static final String TAG = "MQTTManager";
    private IMqttActionListener mMQTTListener;
    private static MqttAndroidClient mqttAndroidClient;
    private static  MqttConnectOptions mqttConnectOptions;
    private Context mContext;

    private AcknowledgeHelper acknowledgeHelper;
    private PreferenceHelperDataSource helperDataSource;
    private BookingManager bookingManager;

    @Inject
    public MQTTManager(Context context, AcknowledgeHelper acknowledgeHelper,
                       PreferenceHelperDataSource dDataSource, final NetworkStateHolder holder,
                       final RxNetworkObserver rxNetworkObserver, BookingManager bookingManager)
    {
        mContext = context;
        this.acknowledgeHelper=acknowledgeHelper;
        this.helperDataSource=dDataSource;
        this.bookingManager=bookingManager;

        mMQTTListener = new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {

                if(mqttAndroidClient!=null && mqttAndroidClient.isConnected()) {
                    subscribeToTopic(helperDataSource.getDriverChannel());
                    subscribeToTopic(helperDataSource.getDriverChannel_msg());

                    if(helperDataSource.getServiceZoneList().getServiceZones().size()>0)
                    for(int i=0;i<helperDataSource.getServiceZoneList().getServiceZones().size();i++){
                        String topic = "onlineDrivers/".concat(helperDataSource.getCityId().concat("/").concat(helperDataSource.getServiceZoneList().getServiceZones().get(i)));
                        Utility.printLog("subscribed Mqtt Zone topic :  "+topic);
                        subscribeToTopic(topic);
                    }

                    holder.setConnected(true);
                    Utility.printLog(TAG + " TEST MQTT" +" connected " + mqttAndroidClient.getClientId());
                    rxNetworkObserver.publishData(holder);
                    Utility.printLog(TAG + " onSuccessPhone: myqtt client " + asyncActionToken.isComplete());
                }

            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                exception.printStackTrace();
                Utility.printLog(TAG+ "onFailure: myqtt client "+asyncActionToken.isComplete()
                        +" "+exception.getMessage());
            }
        };
    }

    /**
     * <h2>subscribeToTopic</h2>
     * This method is used to subscribe to the mqtt topic
     */
    public void subscribeToTopic(String mqttTopic)
    {
        try
        {
            if (mqttAndroidClient != null)
                mqttAndroidClient.subscribe(mqttTopic, 1);
        }
        catch (MqttException e)
        {
            Utility.printLog(TAG+" MqttException "+e);
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            Utility.printLog(TAG+" MqttException "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>unSubscribeToTopic</h2>
     * This method is used to unSubscribe to topic already subscribed
     * @param topic Topic name from which to  unSubscribe
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public void unSubscribeToTopic(String topic)
    {
        try
        {
            if (mqttAndroidClient != null)
                mqttAndroidClient.unsubscribe(topic);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>isMQTTConnected</h2>
     * This method is used to check whether MQTT is connected
     * @return boolean value whether MQTT is connected
     */
    public boolean isMQTTConnected()
    {
        try {
            return mqttAndroidClient != null && mqttAndroidClient.isConnected();
        }catch (Exception e){
            return true;
        }

    }

    /**
     * <h2>createMQttConnection</h2>
     * This method is used to create the connection with MQTT
     * @param clientId customer ID to connect MQTT
     */
    @SuppressWarnings("unchecked")
    public void createMQttConnection(String clientId)
    {
        Utility.printLog(TAG+" createMQtftConnection: "+clientId);
        String serverUri = "tcp://" + BuildConfig.MQTT_HOST + ":" + BuildConfig.MQTT_PORT;
//        String serverUri = "ssl://" + BuildConfig.MQTT_HOST + ":" + BuildConfig.MQTT_PORT;
        mqttAndroidClient = new MqttAndroidClient(mContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Utility.printLog(TAG+" TEST MQTT not connect "+cause);
            }
            @Override
            public void messageArrived(String topic, final MqttMessage message) throws Exception
            {
                final JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                Utility.printLog("mqtt message : " + jsonObject.toString());
                Utility.printLog(TAG+" messageArrived: "+new String(message.getPayload()));


                if(jsonObject.has("data")){

                    if(ChattingActivity.isOpen){
                        ChatDataObervable.getInstance().emitData(jsonObject);
                    }else {
                        String bid = jsonObject.getJSONObject("data").getString("bid");
                        String content = jsonObject.getJSONObject("data").getString("content");
                        String custID = jsonObject.getJSONObject("data").getString("fromID");
                        String custName = jsonObject.getJSONObject("data").getString("name");
                        sendNotification(bid,content,custID,custName);
                    }

                }else if(jsonObject.has("bookingData")) {

                    switch (jsonObject.getJSONObject("bookingData").getString("action")){
                        //Handle the booking request cancel
                        case "41":
                            Intent orderUpdateIntent = new Intent(VariableConstant.BOOKING_DISPATCH_CANCEL);
                            mContext.sendBroadcast(orderUpdateIntent);
                            break;
                        //Handle the Assign Booking from dispatcher
                        case "29":
                            if(isApplicationSentToBackground() || FORGROUND_LOCK) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(intent);
                            }
                            else {
                                BookingAssigned bookingAssigned = new BookingAssigned();
                                bookingAssigned.setAssigned(true);
                                bookingManager.assignedBooking(bookingAssigned);
                            }
                            break;

                        //Handle New Booking
                        case "11":
                            try{
                                if(helperDataSource.getDriverChannel().equals(jsonObject.getJSONObject("bookingData").getString("chn"))){
                                    acknowledgeHelper.bookingAckApi(jsonObject.getJSONObject("bookingData").getString("orderId"),mContext, new AcknowledgementCallback() {
                                        @Override
                                        public void callback(String bid) {
                                            if(!IS_POP_UP_OPEN){
                                                try {
                                                    Intent intent = new Intent(mContext, BookingPopUp.class);
                                                    intent.putExtra("booking_Data", jsonObject.getString("bookingData"));
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    mContext.startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }


                                    });
                                }

                            }catch (Exception e){
                                Utility.printLog(TAG+" "+e.getMessage());
                            }
                            break;

                        //handle the chat messages
                        case "16":
                            RXMqttMessageObserver.getInstance().emit(jsonObject);
                            break;
                        //Handle Booking cancel
                        case "3":
                            if(IS_POP_UP_OPEN) {
                                IS_POP_UP_OPEN = false;
                                if(BookingPopUp.mediaPlayer!=null && BookingPopUp.mediaPlayer.isPlaying())
                                    BookingPopUp.mediaPlayer.stop();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(intent);
                            }

                            break;

                    }

                }
                else {
                    int action=jsonObject.has("a")?jsonObject.getInt("a"):jsonObject.getInt("action");
                    switch (action){

                        //Handle the Assign Booking from dispatcher
                        case 29:
                            if(isApplicationSentToBackground() || FORGROUND_LOCK) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(intent);
                            }
                            else {
                                BookingAssigned bookingAssigned = new BookingAssigned();
                                bookingAssigned.setAssigned(true);
                                bookingManager.assignedBooking(bookingAssigned);
                            }
                            break;

                        //Handle New Booking
                        case 11:
                            try{
                                if(helperDataSource.getDriverChannel().equals(jsonObject.getString("chn"))){
                                    acknowledgeHelper.bookingAckApi(jsonObject.getString("orderId"),mContext, new AcknowledgementCallback() {
                                        @Override
                                        public void callback(String bid) {
                                            if(!IS_POP_UP_OPEN){
                                                Intent intent = new Intent(mContext, BookingPopUp.class);
                                                intent.putExtra("booking_Data", jsonObject.toString());
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                mContext.startActivity(intent);
                                            }

                                        }


                                    });
                                }

                            }catch (Exception e){
                                Utility.printLog(TAG+" "+e.getMessage());
                            }

                            break;

                        //handle the chat messages
                        case 10:
                        case 12:
                        case 16:
                        case 14:
                            RXMqttMessageObserver.getInstance().emit(jsonObject);
                            break;

                        //Handle Booking cancel
                        case 3:
                            if(IS_POP_UP_OPEN) {
                                IS_POP_UP_OPEN = false;
                                if(BookingPopUp.mediaPlayer!=null && BookingPopUp.mediaPlayer.isPlaying())
                                    BookingPopUp.mediaPlayer.stop();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(intent);
                            }
                            break;
                    }

                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Utility.printLog(TAG+" deliveryComplete: "+token);
            }
        });
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(BuildConfig.MQTT_USERNAME);
        mqttConnectOptions.setPassword(BuildConfig.MQTT_PASSWORD.toCharArray());
       /* mqttConnectOptions.setUserName(BuildConfig.MQTT_USERNAME);
        mqttConnectOptions.setPassword(BuildConfig.MQTT_PASSWORD.toCharArray());
        SocketFactory.SocketFactoryOptions socketFactoryOptions = new SocketFactory.SocketFactoryOptions();
        try {
            //socketFactoryOptions.withCaInputStream(mContext.getResources().openRawResource(R.raw.ca));
            socketFactoryOptions.withClientP12Password("3Embed");
            socketFactoryOptions.withClientP12InputStream(mContext.getResources().openRawResource(R.raw.client));
            mqttConnectOptions.setSocketFactory(new SocketFactory(socketFactoryOptions));
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }*/
        connectMQTTClient(mContext);
    }


    /**
     * <h1>publish</h1>
     * @param jsonObject : body to publish
     */
    public void publish(JSONObject jsonObject){Utility.printLog("mqtt message status : " );
        try {

            if(helperDataSource.getServiceZoneList().getServiceZones().size()>0)
                for(int i=0;i<helperDataSource.getServiceZoneList().getServiceZones().size();i++){
                    String topic = "onlineDrivers/".concat(helperDataSource.getCityId().concat("/").concat(helperDataSource.getServiceZoneList().getServiceZones().get(i)));
                    Utility.printLog("published Mqtt Zone topic :  "+topic);
                    mqttAndroidClient.publish(topic,jsonObject.toString().getBytes(),2,false);

                }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * <h2>connectMQTTClient</h2>
     * This method is used to connect to MQTT client
     */
    private void connectMQTTClient(Context mContext)
    {
        try
        {
            Utility.printLog(TAG+" connectMQTTClient: ");
            mqttAndroidClient.connect(mqttConnectOptions, mContext, mMQTTListener);
        }
        catch (Exception e)
        {
            Utility.printLog(TAG+" MqttException: "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>disconnect</h2>
     * This method is used To disconnect the MQtt client
     */
    public void disconnect()
    {
        try
        {
            if (mqttAndroidClient != null)
            {
                unSubscribeToTopic(helperDataSource.getDriverChannel());
                unSubscribeToTopic(helperDataSource.getDriverChannel_msg());
                unSubscribeToTopic("message/"+helperDataSource.getDriverID());
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>sendNotification</h1>
     * <p>set the notification for chat, and show</p>
     * @param bid booking ID
     * @param message message for chat
     * @param custID customer ID
     * @param custName Customer name
     */
    private void sendNotification(String bid, String message, String custID, String custName){
        Intent intent=new Intent(mContext,ChattingActivity.class);
        intent.putExtra("BID",bid);
        intent.putExtra("CUST_ID",custID);
        intent.putExtra("CUST_NAME",custName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(mContext.getString(R.string.app_name));
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.cancel(0);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = mContext.getString(R.string.app_name_withoutSpace);;// The id of the channel.
        CharSequence name = mContext.getString(R.string.app_name);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setStyle(bigText);

        notificationManager.notify(0 , notificationBuilder.build());
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

    }

    /**
     * <h1>isApplicationSentToBackground</h1>
     * <p>check whether the app is background or not</p>
     * @return :boolean value true: app is background  ,false: not in background
     */
    private boolean isApplicationSentToBackground()
    {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName()))
            {
                VariableConstant.APPISBACKGROUND = true;
                return true;
            }
        }
        VariableConstant.APPISBACKGROUND = false;
        return false;
    }
}
