package by.dimanolog.farfor.dialogs;

/**
 * Created by Dimanolog on 20.01.2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import by.dimanolog.farfor.R;

public class ProgressDialogFragment extends DialogFragment {
    public static final String FRAGMENT_TAG = "progress_dialog";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle(getString(R.string.progress_dialog_title));
        dialog.setMessage(getString(R.string.progress_dialog_message));
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}
