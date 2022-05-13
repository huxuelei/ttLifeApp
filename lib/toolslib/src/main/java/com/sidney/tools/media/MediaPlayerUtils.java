package com.sidney.tools.media;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.sidney.tools.LogUtils;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/4/3 17:48
 * desc:播放媒体资源
 * version:1.0
 */
public class MediaPlayerUtils implements IPlayerAdapter {
    public static int PLAYSTATUS0 = 0;//正在播放
    public static int PLAYSTATUS1 = 1;//暂停播放
    public static int PLAYSTATUS2 = 2;//重置
    public static int PLAYSTATUS3 = 3;//播放完成
    public static int PLAYSTATUS4 = 4;//媒体流装载完成
    public static int PLAYSTATUS5 = 5;//媒体流加载中
    public static int PLAYSTATUSD1 = -1;//错误

    public int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000;
    private String TAG = MediaPlayerUtils.class.getSimpleName();
    private MediaPlayer mMediaPlayer;
    private ScheduledExecutorService mExecutor;//开启线程
    private IPlayback mPlaybackListener;
    private Runnable mSeekbarPositionUpdateTask;
    private String mMusiUrl;//音乐地址，可以是本地的音乐，可以是网络的音乐


    public void setmPlaybackListener(IPlayback mPlaybackListener) {
        this.mPlaybackListener = mPlaybackListener;
    }

    /**
     * 初始化MediaPlayer
     **/
    private void initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            //注册，播放完成后的监听
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopUpdatingCallbackWithPosition(true);
                    if (mPlaybackListener != null) {
                        mPlaybackListener.onStateChanged(PLAYSTATUS3);
                        mPlaybackListener.onPlaybackCompleted();
                    }
                }
            });

            //监听媒体流是否装载完成
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    medisaPreparedCompled();
                }
            });

            /**
             * 监听媒体错误信息
             **/
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (mPlaybackListener != null) {
                        mPlaybackListener.onStateChanged(PLAYSTATUSD1);
                    }
                    LogUtils.d("OnError - Error code: " + what + " Extra code: " + extra);
                    switch (what) {
                        case -1004:
                            LogUtils.d("MEDIA_ERROR_IO");
                            break;
                        case -1007:
                            LogUtils.d("MEDIA_ERROR_MALFORMED");
                            break;
                        case 200:
                            LogUtils.d("MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                            break;
                        case 100:
                            LogUtils.d("MEDIA_ERROR_SERVER_DIED");
                            break;
                        case -110:
                            LogUtils.d("MEDIA_ERROR_TIMED_OUT");
                            break;
                        case 1:
                            LogUtils.d("MEDIA_ERROR_UNKNOWN");
                            break;
                        case -1010:
                            LogUtils.d("MEDIA_ERROR_UNSUPPORTED");
                            break;
                    }
                    switch (extra) {
                        case 800:
                            LogUtils.d("MEDIA_INFO_BAD_INTERLEAVING");
                            break;
                        case 702:
                            LogUtils.d("MEDIA_INFO_BUFFERING_END");
                            break;
                        case 701:
                            LogUtils.d("MEDIA_INFO_METADATA_UPDATE");
                            break;
                        case 802:
                            LogUtils.d("MEDIA_INFO_METADATA_UPDATE");
                            break;
                        case 801:
                            LogUtils.d("MEDIA_INFO_NOT_SEEKABLE");
                            break;
                        case 1:
                            LogUtils.d("MEDIA_INFO_UNKNOWN");
                            break;
                        case 3:
                            LogUtils.d("MEDIA_INFO_VIDEO_RENDERING_START");
                            break;
                        case 700:
                            LogUtils.d("MEDIA_INFO_VIDEO_TRACK_LAGGING");
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 加载媒体资源
     **/
    @Override
    public void loadMedia(String musiUrl) {
        if (TextUtils.isEmpty(musiUrl)) {
            LogUtils.i("地址为空");
            return;
        }
        if (mPlaybackListener != null) {
            mPlaybackListener.onStateChanged(PLAYSTATUS5);
        }
        this.mMusiUrl = musiUrl;
        initializeMediaPlayer();
        try {
            mMediaPlayer.reset();//防止再次添加进来出现崩溃信息
            mMediaPlayer.setDataSource(musiUrl);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放媒体资源
     **/
    @Override
    public void release() {
        if (mMediaPlayer != null) {
            stopUpdatingCallbackWithPosition(false);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 判断是否正在播放
     **/
    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 播放开始
     **/
    @Override
    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            if (mPlaybackListener != null) {
                mPlaybackListener.onStateChanged(PLAYSTATUS0);
            }
            startUpdatingCallbackWithPosition();
        }
    }

    /**
     * 开启线程，获取当前播放的进度
     **/
    private void startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                }
            };
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void reset() {
        if (mMediaPlayer != null) {
            loadMedia(mMusiUrl);
            if (mPlaybackListener != null) {
                mPlaybackListener.onStateChanged(PLAYSTATUS2);
            }
            stopUpdatingCallbackWithPosition(true);
        }
    }

    /**
     * 暂停
     **/
    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (mPlaybackListener != null) {
                mPlaybackListener.onStateChanged(PLAYSTATUS1);
            }
        }
    }

    /**
     * 更新当前的进度
     **/
    private void updateProgressCallbackTask() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if (mPlaybackListener != null) {
                mPlaybackListener.onPositionChanged(currentPosition);
            }
        }
    }

    /**
     * 加载完成回调
     **/
    @Override
    public void medisaPreparedCompled() {
        int duration = mMediaPlayer.getDuration();//获取总时长
        if (mPlaybackListener != null) {
            mPlaybackListener.onDurationChanged(duration);
            mPlaybackListener.onPositionChanged(0);
            mPlaybackListener.onStateChanged(PLAYSTATUS4);
        }
    }

    /**
     * 滑动播放到某个位置
     **/
    @Override
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }


    /**
     * 播放完成回调监听
     **/
    private void stopUpdatingCallbackWithPosition(boolean resetUIPlaybackPosition) {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
            if (resetUIPlaybackPosition && mPlaybackListener != null) {
                mPlaybackListener.onPositionChanged(0);
            }
        }
    }

    /**
     * 播放回调
     **/
    public interface IPlayback {
        void onDurationChanged(int duration);//返回音乐的总时长

        void onPositionChanged(int position);//当播放的进度

        void onStateChanged(int state);//记录当前的状态

        void onPlaybackCompleted();//播放完成回调
    }


}

