package com.fortuneteller.cup.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.fortuneteller.cup.Interface.ItemClickListener;
import com.fortuneteller.cup.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AnswerAlertFragment extends DialogFragment {
    private final static String TAG = AnswerAlertFragment.class.getSimpleName();

    private static final int REQUEST_STORAGE_PERMISSIONS_CODE = 124;
    private final static String ANSWER_MESSAGE_KEY = "answerKey";

    private Context mActivityContext;
    private Activity activity;
    private static Context sContext;
    // click listener to pass click events to parent fragment
    private static ItemClickListener sItemClickListen;

    // click listener to pass click events to parent fragment
    //private static ItemClickListener itemClickListen;

    private AnswerAlertFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AnswerAlertFragment newInstance(Context context, ItemClickListener itemClickListener, String answer) {

        // instantiate click listener to pass click events to parent fragment
        sContext = context;

        // instantiate click listener to pass click events to parent fragment
        sItemClickListen = itemClickListener;

        AnswerAlertFragment fragment = new AnswerAlertFragment();
        Bundle args = new Bundle();
        //args.putParcelableArrayList(PRIVET_CONTACTS_KEY, privateContacts);
        args.putString(ANSWER_MESSAGE_KEY, answer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityContext = context;
        if (context instanceof Activity){// check if fragmentContext is an activity
            activity =(Activity) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // created options to select from
        //CharSequence options[] = new CharSequence[]{getString(R.string.alert_dialog_edit), getString(R.string.alert_dialog_unreveal)};
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(sContext);
        //alertDialogBuilder.setTitle(getString(R.string.edit_unreveal_alert_dialog_title));
        //alertDialogBuilder.setMessage(R.string.speaker_dialog_message);
        // get answer from arguments
        if (getArguments() != null) {
            alertDialogBuilder.setMessage(getArguments().getString(ANSWER_MESSAGE_KEY));
        }
       /* alertDialogBuilder.setPositiveButton(R.string.confirm_dialog_positive_button,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                Log.i(TAG, "onClick on success: request Permissions");
                if(sItemClickListen != null){
                    sItemClickListen.onClick(null, 1, false);
                }
            }
        });*/

        alertDialogBuilder.setNegativeButton(R.string.confirm_dialog_dismiss_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    // send click listener to scan fragment to show all answers recycler
                    if(sItemClickListen != null){
                        sItemClickListen.onClick(null, 1, false);
                    }
                    dialog.dismiss();
                }
            }
        });
        return alertDialogBuilder.create();

    }


    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(null != getDialog()){
            getDialog().setTitle(R.string.user_request_dialog_title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }*/

    /*@Override
    public void onClick(View view, int position, boolean isLongClick) {
        Log.i(TAG, "onClick view= " + view.getTag()+ " position= " +position);

        if(itemClickListen != null && position != RecyclerView.NO_POSITION){
            itemClickListen.onClick(view, position, false);
        }
    }

    // needed only if i want the listener to be inside the adapter
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListen = itemClickListener;
        }*/

}
