package com.vinay.wizdem.ganesha;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.security.PrivateKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.playImageButton)
    ImageButton playImageButton;
    @BindView(R.id.pauseImageButton)
    ImageButton pauseImageButton;

    boolean mServiceBound = false;


    private MediaPlayer mediaPlayerOne, mediaPlayerTwo;
    private static int count = 0;
    private static boolean bipas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mediaPlayerOne = MediaPlayer.create(this, R.raw.part_one);
        mediaPlayerTwo = MediaPlayer.create(this, R.raw.part_two);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
      /*  if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerTwo.stop();
        mediaPlayerOne.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* Intent intent = new Intent(this, MPlayerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);*/
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playImageButton: {
                pauseImageButton.setVisibility(View.VISIBLE);
                playImageButton.setVisibility(View.GONE);
              //  mPlayerService.play();
                //  if(!mediaPlayerOne.isPlaying()&&!mediaPlayerTwo.isPlaying()){
                   if(!bipas){
                        mediaPlayerTwo.start();
                    }else {
                       mediaPlayerOne.start();
                       mediaPlayerOne.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                           @Override
                           public void onCompletion(MediaPlayer mediaPlayerOne) {
                               bipas=false;
                               mediaPlayerTwo.start();

                           }

                       });
                   }

                    mediaPlayerTwo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            count++;
                            if(count<54){
                                mediaPlayerTwo.start();
                            }else {
                                bipas=true;
                                count=0;
                                playImageButton.setVisibility(View.VISIBLE);
                                pauseImageButton.setVisibility(View.GONE);
                            }
                        }
                    });
                //   }

                //  mediaPlayerTwo.reset();
                break;
            }
            case R.id.pauseImageButton: {
                playImageButton.setVisibility(View.VISIBLE);
                pauseImageButton.setVisibility(View.GONE);
           //     mPlayerService.pause();
                if (bipas) {
                    mediaPlayerOne.pause();
                } else if (mediaPlayerTwo.isPlaying()) {
                    mediaPlayerTwo.pause();
                }

                break;
            }
            default: {
                break;
            }
        }

    }

}
