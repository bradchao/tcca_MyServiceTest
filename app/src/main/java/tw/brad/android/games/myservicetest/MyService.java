package tw.brad.android.games.myservicetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer player;
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("brad", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        player = MediaPlayer.create(this,R.raw.try_everything);

        int len = player.getDuration();
        Intent it = new Intent("brad"); // action = "brad"
        it.putExtra("len", len);
        sendBroadcast(it);

        player.start();
        timer.schedule(new PlayTask(), 0, 200);

    }

    private class PlayTask extends TimerTask {
        @Override
        public void run() {
            if (player != null && player.isPlaying()){
                int now = player.getCurrentPosition();
                Intent it = new Intent("brad"); // action = "brad"
                it.putExtra("now", now);
                sendBroadcast(it);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isStart = intent.getBooleanExtra("isStart", false);
        boolean isPause = intent.getBooleanExtra("isPause", false);
        int progress = intent.getIntExtra("progress", -1);

        if (progress>=0) {
            player.seekTo(progress);
        }else if (isPause) {
            if (player != null && player.isPlaying()){
                player.pause();
            }else{
                player.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("brad", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (player != null ){
            player.stop();
        }
    }

}
