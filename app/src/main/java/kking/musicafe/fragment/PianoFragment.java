package kking.musicafe.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kking.musicafe.R;
import kking.musicafe.PianoSoundPool;

/**
 * PianoFragment
 */
public class PianoFragment extends Fragment {
    /** Audio IDs; wav files for each note within one octave (C4-C5). */
    private static final int[] NOTES_IDS = { R.raw.c4, R.raw.c_sharp_d_flat, R.raw.d, R.raw.d_sharp_e_flat,
            R.raw.e, R.raw.f, R.raw.f_sharp_g_flat, R.raw.g, R.raw.g_sharp_a_flat, R.raw.a, R.raw.a_sharp_b_flat,
            R.raw.b, R.raw.c5 };

    /** IDs for each Button. */
    private static final int[] KEY_BUTTON_IDS = { R.id.cButton, R.id.cSharpButton, R.id.dButton, R.id.dSharpButton,
            R.id.eButton, R.id.fButton, R.id.fSharpButton, R.id.gButton, R.id.gSharpButton, R.id.aButton,
            R.id.aSharpButton, R.id.bButton };

    /** Maximum number of simultaneous streams by a SoundPool. */
    private static final int MAX_STREAMS = 2;

    private Button[] mKeyButtons;
    private PianoSoundPool mPianoSoundPool;

    /**
     * Callback method that defines the layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_piano, container, false);

        // wire up the Buttons and their OnClickListeners
        mKeyButtons = new Button[KEY_BUTTON_IDS.length];
        for (int i = 0; i < mKeyButtons.length; i++) {
            mKeyButtons[i] = layout.findViewById(KEY_BUTTON_IDS[i]);
            mKeyButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(v);
                }
            });
        }

        // create SoundPool wrapper instance - plays an audio file associated with a given Button
        mPianoSoundPool
                = new PianoSoundPool(getActivity().getApplicationContext(), MAX_STREAMS, NOTES_IDS, KEY_BUTTON_IDS);

        return layout;
    }

    private void play(View view) {
        mPianoSoundPool.play(view);
    }

    /**
     * Relinquishes resources used by the PianoSoundPool object.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPianoSoundPool.destroy();
    }

} // End of class PianoFragment
