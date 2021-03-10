package kking.musicafe.model;

import java.security.SecureRandom;

/**
 * Intervals : Model class for interval ear training.
 */
public class IntervalEar {
    private static final SecureRandom RNG = new SecureRandom();

    private Setting mScale, mMotion;

    private Interval[] mAllIntervals;

    private Interval mCurrentInterval;

    public IntervalEar(Setting scale, Setting motion) {
        mScale = scale;
        mMotion = motion;

        mAllIntervals = (mScale == Setting.DIATONIC) ? Interval.getDiatonicIntervals() : Interval.getAllIntervals();
    }

    public IntervalEar() {
        this(Setting.CHROMATIC, Setting.BOTH_MOTIONS);
    }

    public void generateInterval() {
        int index = RNG.nextInt(mAllIntervals.length); // between 0 (inclusive) and the specified value (exclusive)
        mCurrentInterval = mAllIntervals[index];

        int leftNote = RNG.nextInt(Interval.TOTAL_SEMITONES - mCurrentInterval.getSemitones());
        int rightNote = (leftNote + mCurrentInterval.getSemitones());

        if (mMotion == Setting.ASCENDING || mMotion == Setting.DESCENDING) {
            mCurrentInterval.setStartNoteIndex((mMotion == Setting.ASCENDING) ? leftNote : rightNote);
            mCurrentInterval.setEndNoteIndex((mMotion == Setting.ASCENDING) ? rightNote : leftNote);
        }
        else {
            boolean ascending = RNG.nextBoolean();
            mCurrentInterval.setStartNoteIndex((ascending) ? leftNote : rightNote);
            mCurrentInterval.setEndNoteIndex((ascending) ? rightNote : leftNote);
        }
    }

    public boolean checkUserInput(Interval.Quality quality, Interval.Degree degree) {
        return mCurrentInterval.equals(quality, degree);
    }

    public int getSemitones() {
        return mCurrentInterval.getSemitones();
    }

    public Interval.Quality getQuality() {
        return mCurrentInterval.getQuality();
    }

    public Interval.Degree getDegree() {
        return mCurrentInterval.getDegree();
    }

    public int getStartNoteID() {
        return mCurrentInterval.getStartNoteID();
    }

    public int getEndNoteID() {
        return mCurrentInterval.getEndNoteID();
    }

    @Override
    public String toString() {
        return "IntervalEar [current interval=" + mCurrentInterval.getQuality().name()
                + " " + mCurrentInterval.getDegree().name() + "]";
    }

} // End of class IntervalEar
