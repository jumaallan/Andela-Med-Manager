package com.androidstudy.andelamedmanager.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidstudy.andelamedmanager.R;
import com.androidstudy.andelamedmanager.data.model.User;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileDialog extends DialogFragment {

    @BindView(R.id.text_view_name)
    TextView nameText;
    @BindView(R.id.text_adv)
    TextView advText;
    @BindView(R.id.image_view_user)
    ImageView userImage;

    private User user;
    private static MaterialDialog.SingleButtonCallback callback;

    public static ProfileDialog newInstance(MaterialDialog.SingleButtonCallback buttonCallback) {
        callback = buttonCallback;
        return new ProfileDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Box<User> userBox = ((AndelaTrackChallenge) getActivity().getApplicationContext()).getBoxStore().boxFor(User.class);
//        user = userBox.query().build().findFirst();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_profile, null, false);
        ButterKnife.bind(this, view);

        if (user != null) {
            nameText.setText("");
            Glide.with(this)
                    .load("")
                    .into(userImage);
        }

        return new MaterialDialog.Builder(getActivity())
                .customView(view, false)
                .positiveText("Sign out")
                .onPositive(callback)
                .build();
    }
}