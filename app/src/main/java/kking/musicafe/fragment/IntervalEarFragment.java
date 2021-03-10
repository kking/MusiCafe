package kking.musicafe.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kking.musicafe.R;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import kking.musicafe.IntervalMediaPlayer;
import kking.musicafe.model.Interval;
import kking.musicafe.model.IntervalEar;
import kking.musicafe.model.Setting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntervalEarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntervalEarFragment extends Fragment {
    // region variables
    private static final String TAG = "IntervalEarFragment";

    // Constants - Quality and Degree Button initialization
    private static final int[] QUALITY_BUTTON_IDS = { R.id.majorButton, R.id.perfectButton, R.id.minorButton,
            R.id.augmentedButton, R.id.diminishedButton };

    private static final int[] DEGREE_BUTTON_IDS = { R.id.unisonButton, R.id.secondButton, R.id.thirdButton,
            R.id.fourthButton, R.id.fifthButton, R.id.sixthButton, R.id.seventhButton, R.id.octaveButton };

    private static final Interval.Quality[] QUALITY_BUTTON_VALUES = { Interval.Quality.MAJOR, Interval.Quality.PERFECT,
            Interval.Quality.MINOR, Interval.Quality.AUGMENTED, Interval.Quality.DIMINISHED };

    private static final Interval.Degree[] DEGREE_BUTTON_VALUES = { Interval.Degree.UNISON, Interval.Degree.SECOND,
            Interval.Degree.THIRD, Interval.Degree.FOURTH, Interval.Degree.FIFTH, Interval.Degree.SIXTH,
            Interval.Degree.SEVENTH, Interval.Degree.OCTAVE };

    // Constants - Button color IDs
    private static final int SELECTED_BUTTON = R.color.colorButton;
    private static final int SELECTED_BUTTON_TEXT = R.color.colorButtonText;
    private static final int UNSELECTED_BUTTON = R.color.colorButtonGrayed;
    private static final int UNSELECTED_BUTTON_TEXT = R.color.colorButtonTextGrayed;
    private static final int CORRECT_BUTTON = R.color.colorButtonCorrect;
    private static final int INCORRECT_BUTTON = R.color.colorButtonIncorrect;

    // Models
    private IntervalEar intervalQuiz;
    private IntervalMediaPlayer player;

    // Views
    private TextView scoreTextView;
    private Button[] qualityButtons, degreeButtons;
    private Button nextButton;
    private ImageButton playImageButton;

    // Controller components
    private Button selectedQualityButton, selectedDegreeButton;

    private Interval.Quality selectedQuality, actualQuality;
    private Interval.Degree selectedDegree, actualDegree;

    private int totalQuestions = 0, correctQuestions = 0;

    // endregion variables

    /** Required public constructor. */
    public IntervalEarFragment() { /* empty */ }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment IntervalEarFragment.
     */
    public static IntervalEarFragment newInstance() {
        IntervalEarFragment fragment = new IntervalEarFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interval_ear, container, false);

        // Views
        scoreTextView = view.findViewById(R.id.scoreTextView);

        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInterval();
            }
        });

        playImageButton = view.findViewById(R.id.playImageButton);
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playInterval();
            }
        });

        // Quality Buttons and their listeners
        qualityButtons = new Button[QUALITY_BUTTON_IDS.length];
        for (int i = 0; i < qualityButtons.length; i++) {
            qualityButtons[i] = view.findViewById(QUALITY_BUTTON_IDS[i]);
            qualityButtons[i].setOnClickListener(new QualityClickListener(QUALITY_BUTTON_VALUES[i]));
        }

        // Degree Buttons and their listeners
        degreeButtons = new Button[DEGREE_BUTTON_IDS.length];
        for (int i = 0; i < degreeButtons.length; i++) {
            degreeButtons[i] = view.findViewById(DEGREE_BUTTON_IDS[i]);
            degreeButtons[i].setOnClickListener(new DegreeClickListener(DEGREE_BUTTON_VALUES[i]));
        }

        // Models
        intervalQuiz = new IntervalEar(Setting.CHROMATIC, Setting.BOTH_MOTIONS);
        player = new IntervalMediaPlayer(getActivity());

        // Generate and play first question
        resetInterval();

        return view;
    }

    /**
     * Utilizes Interval player wrapper class to play the current interval.
     * precondition: a call to {@link IntervalEarFragment#resetInterval()} has been made.
     *
     * @see IntervalMediaPlayer#playSequence(int[])
     */
    public void playInterval() {
        int[] interval = { intervalQuiz.getStartNoteID(), intervalQuiz.getEndNoteID() };
        player.playSequence(interval);
    }

    /**
     * Updates the score, disables the "Next" Button, resets the Quality and Degree Buttons, generates the next
     * IntervalEar question, and plays its interval.
     */
    public void resetInterval() {
        // update score TextView with incremented total
        scoreTextView.setText(getString(R.string.txt_score, correctQuestions, ++totalQuestions));

        setNextButton(false);
        resetButtonGroups();

        // generate interval and record its Quality and Degree
        intervalQuiz.generateInterval();
        actualQuality = intervalQuiz.getQuality();
        actualDegree = intervalQuiz.getDegree();

        playInterval();
    }

    /**
     * Records the user-selected answer for the current interval quality and highlights the selected Button.
     *
     * @param quality  the {@link Interval.Quality} associated with the selected Button
     * @param view  a constituent Button of the {@link IntervalEarFragment#qualityButtons} array
     * @see IntervalEarFragment#guessInterval()
     */
    private void selectQuality(Interval.Quality quality, View view) {
        selectedQualityButton = (Button) view;
        selectedQuality = quality;

        highlightButton(selectedQualityButton, qualityButtons);

        if (selectedDegree != Interval.Degree.NULL) {
            guessInterval(); // degree is selcted, process this guess
        }
    }

    /**
     * Records the user-selected answer for the current interval degree and highlights the selected Button.
     *
     * @param degree  the {@link Interval.Degree} associated with the selected Button
     * @param view  a constituent Button of the {@link IntervalEarFragment#degreeButtons} array
     * @see IntervalEarFragment#guessInterval()
     */
    private void selectDegree(Interval.Degree degree, View view) {
        selectedDegreeButton = (Button)view;
        selectedDegree = degree;

        highlightButton(selectedDegreeButton, degreeButtons);

        if (selectedQuality != Interval.Quality.NULL) {
            guessInterval(); // quality is selected, process this guess
        }
    }

    /**
     * Called  when the user makes both an Interval Quality and Interval Degree selection for the current
     * IntervalEar question.
     */
    private void guessInterval() {
        Log.i(TAG, "Actual interval=" + intervalQuiz.toString()
                + ", Submitted interval=" + selectedQuality.name()
                + " " + selectedDegree.name());

        boolean correct = intervalQuiz.checkUserInput(selectedQuality, selectedDegree);

        // disable all Buttons
        setButtonGroupsEnabled(false);

        // enable "Next" Button
        setNextButton(true);

        if (correct) {
            // update score TextView with incremented correct
            scoreTextView.setText(getString(R.string.txt_score, ++correctQuestions, totalQuestions));

            // color selected Buttons in green
            colorButton(selectedQualityButton, CORRECT_BUTTON, SELECTED_BUTTON_TEXT);
            colorButton(selectedDegreeButton, CORRECT_BUTTON, SELECTED_BUTTON_TEXT);
        }
        else {
            // color selected Buttons in red
            colorButton(selectedQualityButton, INCORRECT_BUTTON, SELECTED_BUTTON_TEXT);
            colorButton(selectedDegreeButton, INCORRECT_BUTTON, SELECTED_BUTTON_TEXT);

            // color correct Buttons in green, TODO: optimize
            for (int i = 0; i < QUALITY_BUTTON_VALUES.length; i++) {
                if (actualQuality == QUALITY_BUTTON_VALUES[i]) {
                    colorButton(qualityButtons[i], CORRECT_BUTTON, SELECTED_BUTTON_TEXT);
                    break;
                }
            }
            for (int i = 0; i < DEGREE_BUTTON_VALUES.length; i++) {
                if (actualDegree == DEGREE_BUTTON_VALUES[i]) {
                    colorButton(degreeButtons[i], CORRECT_BUTTON, SELECTED_BUTTON_TEXT);
                    break;
                }
            }
        }
    }

    // region Button color and enabled state methods
    /**
     * Occurs within the "Next" Button's onClick call upon {@link IntervalEarFragment#resetInterval()}.
     */
    private void resetButtonGroups() {
        // Change Quality and Degree Buttons back to their original colors
        colorButtonGroup(qualityButtons, SELECTED_BUTTON, SELECTED_BUTTON_TEXT);
        colorButtonGroup(degreeButtons, SELECTED_BUTTON, SELECTED_BUTTON_TEXT);

        // Enable all Buttons
        setButtonGroupsEnabled(true);

        // Reset user selection
        selectedQuality = Interval.Quality.NULL;
        selectedDegree = Interval.Degree.NULL;
    }

    /**
     * Changes the colors and enabled state of the "Next" Button according to the given value.
     *
     * If enabled, the Button will become clickable and its background and text colors will represent
     * this availability. This state will occur upon calling {@link IntervalEarFragment#guessInterval()}.
     *
     * Otherwise, the Button will be disabled and colored in such a way that reflects this state upon a call
     * to {@link IntervalEarFragment#resetInterval()}.
     *
     * @param enabled  The enabled state of the "Next" Button
     */
    private void setNextButton(boolean enabled) {
        int backgroundColor = (enabled) ? SELECTED_BUTTON : UNSELECTED_BUTTON;
        int textColor = (enabled) ? SELECTED_BUTTON_TEXT : UNSELECTED_BUTTON_TEXT;

        nextButton.setEnabled(enabled);
        colorButton(nextButton, backgroundColor, textColor);
    }

    /**
     * Changes the enabled states of {@link IntervalEarFragment#qualityButtons}
     * and {@link IntervalEarFragment#degreeButtons} to the given value.
     *
     * @param enabled  The enabled state of the Button arrays that represent interval qualities and interval degrees
     */
    private void setButtonGroupsEnabled(boolean enabled) {
        for (Button q : qualityButtons) {
            q.setEnabled(enabled);
        }
        for (Button d : degreeButtons) {
            d.setEnabled(enabled);
        }
    }

    /**
     * Emphasizes the selected Button of its grouping by color.
     *
     * @param selectedButton  A single Button from an array of Buttons that represent an interval quality or degree
     * @param buttonGroup  An array of Buttons
     */
    private void highlightButton(Button selectedButton, Button[] buttonGroup) {
        colorButtonGroup(buttonGroup, UNSELECTED_BUTTON, UNSELECTED_BUTTON_TEXT);
        colorButton(selectedButton, SELECTED_BUTTON, SELECTED_BUTTON_TEXT);
    }

    /**
     * Colors a single Button.
     *
     * @param button  A single Button from an array of Buttons that represent an interval quality degree
     * @param backgroundColor  The integer ID of the Button's new background color
     * @param textColor  The integer ID of the Button's new text color
     */
    private void colorButton(Button button, int backgroundColor, int textColor) {
        button.setBackgroundTintList(button.getResources().getColorStateList(backgroundColor));
        button.setTextColor(getResources().getColor(textColor));
    }

    /**
     * Colors an array of Buttons.
     *
     * @param buttonGroup  An array of Buttons that represent an interval quality or interval degree
     * @param backgroundColor  The integer ID of the Buttons' new background color
     * @param textColor  The integer ID of the Buttons' new text color
     */
    private void colorButtonGroup(Button[] buttonGroup, int backgroundColor, int textColor) {
        for (Button b : buttonGroup) {
            colorButton(b, backgroundColor, textColor);
        }
    }

    // endregion Button colors and enabled state methods
    // region custom OnClickListeners
    /**
     * Custom OnClickListener for degree buttons (unison, second, third, ..., seventh, octave). The overridden onClick
     * calls on selectDegree with the selected Button and the {@link Interval.Degree} that it represents.
     */
    private class DegreeClickListener implements View.OnClickListener {
        private Interval.Degree mDegree;

        private DegreeClickListener(Interval.Degree degree) {
            mDegree = degree;
        }

        @Override
        public void onClick(View v) {
            selectDegree(mDegree, v);
        }

    } // End of class DegreeClickListener

    /**
     * Custom OnClickListener for quality buttons (minor, major, perfect, augmented, diminished). The overridden onClick
     * calls on selectQuality with the selected Button and the {@link Interval.Quality} that it represents.
     */
    private class QualityClickListener implements View.OnClickListener {
        private Interval.Quality mQuality;

        private QualityClickListener(Interval.Quality quality) {
            mQuality = quality;
        }

        @Override
        public void onClick(View v) {
            selectQuality(mQuality, v);
        }

    } // End of class QualityClickListener

    // endregion custom OnClickListeners

} // End of class IntervalEarFragment
