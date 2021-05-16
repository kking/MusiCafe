package kking.musicafe.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import kking.musicafe.R;
import kking.musicafe.MCSoundPool;

/**
 * PianoFragment
 */
public class PianoFragment extends Fragment {
    private static final String TAG = "PianoFragment";

    /** IDs for each Button. */
    private static final int[] KEY_BUTTON_IDS = { R.id.cButton, R.id.cSharpButton, R.id.dButton, R.id.dSharpButton,
            R.id.eButton, R.id.fButton, R.id.fSharpButton, R.id.gButton, R.id.gSharpButton, R.id.aButton,
            R.id.aSharpButton, R.id.bButton };

    /** Maximum number of simultaneous streams by a SoundPool. */
    private static final int MAX_STREAMS = 2;

    private Button[] mKeyButtons;
    private MCSoundPool mPianoSoundPool;



    /** Callback method that defines the layout. */
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
        mPianoSoundPool = new MCSoundPool(Objects.requireNonNull(getActivity()).getApplicationContext(), MAX_STREAMS, KEY_BUTTON_IDS);
        return layout;
    }

    private void play(View view) {
        mPianoSoundPool.playPianoKey(view);
    }

    /** Relinquishes resources used by the PianoSoundPool object. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPianoSoundPool.destroy();
    }

} // End of class PianoFragment
