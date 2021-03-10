package kking.musicafe.model;

import android.util.Log;
import kking.musicafe.R;

/**
 * Interval : A model class to represent a musical interval. Contains information relating to an interval's size in
 * semitones, its quality {@link Quality} and degree {@link Degree} through one octave, and IDs to two audio files to
 * play the interval.
 */
public class Interval {
    private static final String TAG = "Interval";

    /** The total number of intervals. */
    protected static final int TOTAL_SEMITONES = 13;

    // copied from PianoFragment, TODO: maintain this data in one place
    /** Audio IDs; wav files for each note within one octave of the C chromatic scale. */
    private static final int[] NOTES_IDS = { R.raw.c4, R.raw.c_sharp_d_flat, R.raw.d, R.raw.d_sharp_e_flat,
            R.raw.e, R.raw.f, R.raw.f_sharp_g_flat, R.raw.g, R.raw.g_sharp_a_flat, R.raw.a, R.raw.a_sharp_b_flat,
            R.raw.b, R.raw.c5 };

    /** The diatonic (major and perfect) intervals. */
    private static final int[] DIATONIC_SEMITONES = { 0, 2, 4, 5, 7, 9, 11, 12 };

    /** The tritone (augmented fourth/diminished fifth). */
    private static final int TRITONE_SEMITONES = 6;

    /** The size of this interval in semitones. */
    private int mSemitones;

    /** For playing the audio files for this interval. Corresponds to an index in {@link Interval#NOTES_IDS}. */
    private int mStartNoteIndex, mEndNoteIndex;

    private boolean mIsTritone;

    private Degree mDegree;
    private Quality mQuality;

    public enum Degree {
        NULL, UNISON, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, OCTAVE
    }

    public enum Quality {
        NULL, MAJOR, MINOR, PERFECT, AUGMENTED, DIMINISHED
    }

    // the 'tritone' is represented as a diminished fifth
    private static final Interval.Degree[] DEGREES = { Degree.UNISON, Degree.SECOND, Degree.SECOND, Degree.THIRD,
            Degree.THIRD, Degree.FOURTH, Degree.FIFTH, Degree.FIFTH, Degree.SIXTH, Degree.SIXTH, Degree.SEVENTH,
            Degree.SEVENTH, Degree.OCTAVE};

    private static final Interval.Quality[] QUALITIES = { Quality.PERFECT, Quality.MINOR, Quality.MAJOR,
            Quality.MINOR, Quality.MAJOR, Quality.PERFECT, Quality.DIMINISHED, Quality.PERFECT, Quality.MINOR,
            Quality.MAJOR, Quality.MINOR, Quality.MAJOR, Quality.PERFECT };

    protected Interval(int semitones, int startNoteIndex, int endNoteIndex) {
        mSemitones = semitones;
        mStartNoteIndex = startNoteIndex;
        mEndNoteIndex = endNoteIndex;
        setInterval();
    }

    protected Interval(int semitones) {
        mSemitones = semitones;
        setInterval();
    }

    protected Interval() {
        mSemitones = TOTAL_SEMITONES;
        setInterval();
    }

    public static Interval[] getAllIntervals() {
        Interval[] intervals = new Interval[TOTAL_SEMITONES];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(i);
        }
        return intervals;
    }

    public static Interval[] getDiatonicIntervals() {
        Interval[] intervals = new Interval[DIATONIC_SEMITONES.length];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(DIATONIC_SEMITONES[i]);
        }
        return intervals;
    }

    /**
     * @see Interval#QUALITIES
     * @see Interval#DEGREES
     */
    private void setInterval() {
        mIsTritone = (mSemitones == TRITONE_SEMITONES);
        if (mSemitones >= 0 && mSemitones <= TOTAL_SEMITONES) {
            mQuality = QUALITIES[mSemitones];
            mDegree = DEGREES[mSemitones];
        }
        else {
            Log.e(TAG, "setInterval: invalid Interval entry.");
            mQuality = Quality.NULL;
            mDegree = Degree.NULL;
        }
    }

    public void setSemitones(int semitones) {
        mSemitones = semitones;
        setInterval();
    }

    public void setNoteIndices(int startNoteIndex, int endNoteIndex) {
        mStartNoteIndex = NOTES_IDS[startNoteIndex];
        mEndNoteIndex = NOTES_IDS[endNoteIndex];
    }

    public void setStartNoteIndex(int startNoteIndex) {
        mStartNoteIndex = startNoteIndex;
    }

    public void setEndNoteIndex(int endNoteIndex) {
        mEndNoteIndex = endNoteIndex;
    }

    public Quality getQuality() {
        return mQuality;
    }

    public Degree getDegree() {
        return mDegree;
    }

    public int getSemitones() {
        return mSemitones;
    }

    public boolean getIsTritone() {
        return mIsTritone;
    }

    public int getStartNoteIndex() {
        return mStartNoteIndex;
    }

    public int getEndNoteIndex() {
        return mEndNoteIndex;
    }

    public int getStartNoteID() {
        return NOTES_IDS[mStartNoteIndex];
    }

    public int getEndNoteID() {
        return NOTES_IDS[mEndNoteIndex];
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Interval)) {
            return false;
        }
        else {
            Interval otherInterval = (Interval) other;
            return (mSemitones == otherInterval.mSemitones)
                    && (mStartNoteIndex == otherInterval.mStartNoteIndex)
                    && (mEndNoteIndex == otherInterval.mEndNoteIndex)
                    && (mIsTritone == otherInterval.mIsTritone)
                    && (mQuality == otherInterval.mQuality)
                    && (mDegree == otherInterval.mDegree);
        }
    }

    public boolean equals(Quality quality, Degree degree) {
        return (mQuality == quality) && (mDegree == degree)
                || (mIsTritone && (quality == Quality.AUGMENTED && degree == Degree.FOURTH));
    }

    /**
     * @return a String in the following
     * format: "Interval [MINOR SECOND, 1 semitones, tritone=false, start note index=1, end note index=2]"
     */
    @Override
    public String toString() {
        return "Interval [" + mQuality.name()
                + " " + mDegree.name()
                + ", " + mSemitones
                + " semitones, tritone=" + mIsTritone
                + ", start index=" + mStartNoteIndex
                + ", end note index=" + mEndNoteIndex + "]";
    }

} // End of class Interval
