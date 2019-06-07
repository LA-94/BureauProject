package la.com.github.bureau.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import la.com.github.bureau.R;

public class DialogEditTextAdFragment extends DialogFragment {
    private static final String ARG_TITLE = "la.com.github.bureau.arg_title";
    public static final String EXTRA_TITLE = "la.com.github.bureau.title";
    public static final String EXTRA_DESCRIPTION = "la.com.github.bureau.description";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fr_dialog_edit_title_ad, null);
        final EditText editText = view.findViewById(R.id.fr_dialog_edit_title_ad);
        if (getArguments() != null) {
            String str = getArguments().getString(ARG_TITLE);
            editText.setText(str);
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choice_category)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = editText.getText().toString();
                        for (int i = 0; i < title.length(); i++) {
                            char ch = (char) editText.getText().toString().substring(i, i+1).charAt(0);
                            if(Character.isLetterOrDigit(ch)){
                                title = editText.getText().toString().substring(i, i+1).toUpperCase() +
                                        title.substring(i+1);
                                break;
                            }
                        }

                        sendResult(Activity.RESULT_OK, title);
                    }
                })
                .setView(view)
                .create();
    }

    private void sendResult(int resultCode, String string) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        if (getTargetRequestCode() == AddsAdFragment.REQUEST_CODE_TITLE) {
            intent.putExtra(EXTRA_TITLE, string);
        } else if (getTargetRequestCode() == AddsAdFragment.REQUEST_CODE_DESCRIPTION) {
            intent.putExtra(EXTRA_DESCRIPTION, string);
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static DialogEditTextAdFragment getInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        DialogEditTextAdFragment fragment = new DialogEditTextAdFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
