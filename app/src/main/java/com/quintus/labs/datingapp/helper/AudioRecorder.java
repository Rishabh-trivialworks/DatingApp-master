package com.quintus.labs.datingapp.helper;

import android.app.Activity;
import android.content.Context;


import com.quintus.labs.datingapp.Utils.LogUtils;
import com.skd.androidrecording.audio.AudioPlaybackManager;
import com.skd.androidrecording.audio.AudioRecordingHandler;
import com.skd.androidrecording.audio.AudioRecordingThread;
import com.skd.androidrecording.video.PlaybackHandler;
import com.skd.androidrecording.visualizer.VisualizerView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.quintus.labs.datingapp.xmpp.utils.AppConstants.APP_FOLDER_AUDIO;


/**
 * Created by MyU10 on 6/12/2017.
 */

public class AudioRecorder {

    private final Timer audioTimer;
    private AudioPlaybackManager playbackManager;
    private Context context;
    private Activity activity;
    private int timeLimit;
    public boolean isRecording;
    private String recordedFilePath = "";
    private String filePath;
    private VisualizerView visualizerView;
    private int audioTotalTime = -1;
    public AudioRecordingThread recordingThread;
    public Callbacks callbacks;
    private TimerTask timerTask;
    private boolean isDeleted;

    public interface Callbacks {
        void onAudioRecordingStart();

        void onNeedSetup();

        void onAudioRecordingStop();

        void audioPausePlay(boolean isPlaying);

        void onAudioRecordingCompleted(String path, int audioTotalTime);

        void recordingUpdates(int time);
    }

    public String getRecordedFilePath() {
        return recordedFilePath;
    }

    public int getAudioTotalTime() {
        return audioTotalTime;
    }

    public boolean haveAudio() {
        return !isDeleted || !recordedFilePath.isEmpty();
    }

    public boolean isSetupDone() {
        if (visualizerView == null) {
            return false;
        } else
            return true;
    }

    public AudioRecorder(Context context, Activity activity, int timeLimit, Callbacks callbacks) {
        this.context = context;
        this.activity = activity;
        this.timeLimit = timeLimit - 1;
        this.callbacks = callbacks;

        audioTimer = new Timer();
    }

    public void setup(final VisualizerView visualizerView, String name) {
        try {
            this.visualizerView = visualizerView;
            File file = new File(APP_FOLDER_AUDIO + name);
            if (file.exists()) {
                file.delete();
            } else {
                file.getParentFile().mkdirs();
            }

            filePath = file.getAbsolutePath();

            playbackManager = new AudioPlaybackManager(activity, visualizerView, new PlaybackHandler() {
                @Override
                public void onPreparePlayback() {
                }
            });

//        playbackManager.s
            LogUtils.debug("AudioRecorder setup done...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        File file = new File(getRecordedFilePath());
        if (file.exists() && file.canRead() && file.length() > 100) {
            return true;
        }

        return false;
    }

    public void startRecording() {

        if (isRecording) {
            return;
        }

        isRecording = true;
        isDeleted = false;
        resetRecording();
        audioTotalTime = -1;

        LogUtils.debug("AudioRecorder Starting recording...");

        Utility.vibrate(activity, 150);
        callbacks.onAudioRecordingStart();

        recordingThread = new AudioRecordingThread(filePath, new AudioRecordingHandler() { // Pass file name where to store the recorded audio
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes); // Update VisualizerView with new audio portion
                        }
                    }
                });
            }

            @Override
            public void onRecordSuccess() {
                if (filePath != null) {
                    recordedFilePath = filePath;
                    playbackManager.setupPlayback(recordedFilePath);

//                    if (playbackManager.getDuration() / 1000 <= timeLimit * 1000) {
//                        audioTotalTime = playbackManager.getDuration() / 1000;
//                        callbacks.onAudioRecordingCompleted(recordedFilePath, playbackManager.getDuration());
//                    } else {
//                    }
                    callbacks.onAudioRecordingCompleted(recordedFilePath, audioTotalTime * 1000);
                }
            }

            @Override
            public void onRecordingError() {
            }

            @Override
            public void onRecordSaveError() {
            }
        });

        recordingThread.start();
        LogUtils.debug("AudioRecorder Starting recordingThread...");

        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (isRecording) {
                    if (audioTotalTime > timeLimit) {
                        stopRecording();
                    } else {
                        audioTotalTime++;
                        callbacks.recordingUpdates(audioTotalTime);
                    }
                    LogUtils.debug("AudioRecorder audioTotalTime: " + audioTotalTime);
                }
            }
        };

        audioTimer.schedule(timerTask, 0, 1000);
    }

    public void deleteAudio() {
        try {
            isDeleted = true;
            playbackManager.pause();
            stopRecording();
            resetRecording();

            audioTotalTime = -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetRecording() {
        try {
            audioTotalTime = -1;
            if (!recordedFilePath.isEmpty()) {
                File file = new File(recordedFilePath);

                if (file.exists()) {
                    file.delete();
                }
            }
            recordedFilePath = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            isRecording = false;
//        audioTotalTime = -1;
            try {
                recordingThread.stopRecording();
                callbacks.onAudioRecordingStop();
                timerTask.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.debug("AudioRecorder Stopping recordingThread...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getCurrentPosition() {
        try {
            return playbackManager.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isPlaying() {
        try {
            return playbackManager.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void playPauseAudio() {
        try {
            if (playbackManager.isPlaying()) {
                playbackManager.pause();
                callbacks.audioPausePlay(playbackManager.isPlaying());
            } else {
                playbackManager.start();
                callbacks.audioPausePlay(playbackManager.isPlaying());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int position) {
        try {
            if (playbackManager != null) {
                playbackManager.seekTo(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            playbackManager.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pausePlayback() {
        try {
            playbackManager.pause();
            callbacks.audioPausePlay(playbackManager.isPlaying());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeLimit(int limit) {
        timeLimit = limit - 1;

    }

}
