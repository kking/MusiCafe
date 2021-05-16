package kking.musicafe;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * IntervalMediaPlayer :
 * TODO: playing an overlapping sequence of files
 *
 * @author kking
 */
public class IntervalMediaPlayer {

    private MediaPlayer mPlayer;
    private Context mContext;

    public IntervalMediaPlayer(Context context) {
        mContext = context;
    }

    public void play(int soundID) {
        interruptCurrentStream();
        mPlayer = MediaPlayer.create(mContext, soundID);
        mPlayer.start();
    }

    public void playSequence(final int[] soundIDs) {
        interruptCurrentStream();
        mPlayer = MediaPlayer.create(mContext, soundIDs[0]);
        mPlayer.setOnCompletionListener(new IntervalCompletionListener(soundIDs));
        mPlayer.start();
    }

    public void playUnison(int[] soundIDs) {

    }

    /**
     * Stops the current MediaPlayer stream if the user selects the "Play" Button as its playing.
     */
    private void interruptCurrentStream() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            stop();
        }
    }

    /**
     * Stops the MediaPlayer stream.
     */
    private void stop() {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * IntervalCompletionListener :
     */
    private class IntervalCompletionListener implements MediaPlayer.OnCompletionListener {
        private int mIndex;
        private int[] mSoundIDs;

        private IntervalCompletionListener(int[] soundIDs) {
            mIndex = 0;
            mSoundIDs = soundIDs;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mIndex++;
            mp.reset();

            if (mIndex < mSoundIDs.length) {
                mp = MediaPlayer.create(mContext, mSoundIDs[mIndex]);
                mp.setOnCompletionListener(this);
                mp.start();
            }
            else {
                mp.release();
            }
        }

    } // End of class IntervalCompletionListener

} // End of class IntervalMediaPlayer
