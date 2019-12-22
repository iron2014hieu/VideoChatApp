package com.example.androidvideochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, Publisher.PublisherListener{
    private static String API_KEY = "46481802";
    private static String SESSION_ID = "1_MX40NjQ4MTgwMn5-MTU3Njk4MDM3OTk2Mn5ma3R6cU1mZStIYUdrR0pvM1RsNkp5MHF-fg";
    //lấy Generate Token
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjQ4MTgwMiZzaWc9MGM0ZmY0YzI0MDg3YTJmOTZmODRkZjk4NjYzZDc3NzE5ZjY4YzBiNjpzZXNzaW9uX2lkPTFfTVg0ME5qUTRNVGd3TW41LU1UVTNOams0TXpVek56VTNOWDVFWXpWR1JIcElVazlET0hka2NqbHRUMjlxTUVKT0swWi1mZyZjcmVhdGVfdGltZT0xNTc2OTgzNTUwJm5vbmNlPTAuNjc3OTUwMTQ4MTg5NjgxOSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTc3MDA1MTQ5JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    // nhớ lấy của opentok nha
    private Session session;

    private FrameLayout PublisherContainer;
    private FrameLayout SubcriberContainer;

    private Publisher publisher;
    private Subscriber subscriber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requesPermissions();

        PublisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        SubcriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(RC_SETTINGS_SCREEN_PERM)
    private void requesPermissions() {
        String[] perm ={Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perm))
        {
            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }else
        {
            EasyPermissions.requestPermissions(this, "Ứng dụng này cần truy cập vào máy ảnh và míc của bạn.", RC_SETTINGS_SCREEN_PERM);
        }
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null)
        {
            subscriber = new Subscriber.Builder(this,stream).build();
            session.subscribe(subscriber);

            SubcriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (subscriber!=null)
        {
            subscriber = null;
            SubcriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    //pulisher
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
