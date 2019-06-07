package la.com.github.bureau.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import la.com.github.bureau.R;

public class DialogRemoveByPublish extends DialogFragment {


    public static final String EXTRA_REASON = "la.com.github.bureau.reason";
    public static final int ANOTHER_REASON_CODE = 1;
    public static final int RETURNED_REASON_CODE = 0;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fr_dialog_remove_by_published, null);
        final RadioGroup radioGroup = view.findViewById(R.id.reason_remove_ad_radio_group);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.reason_remove_ad)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int idRadioButton = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = view.findViewById(idRadioButton);
                        int index = radioGroup.indexOfChild(radioButton);
                        sendResult(Activity.RESULT_OK, index);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(view)
                .create();
    }

    private void sendResult(int resultCode, int idCategory) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REASON, idCategory);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
