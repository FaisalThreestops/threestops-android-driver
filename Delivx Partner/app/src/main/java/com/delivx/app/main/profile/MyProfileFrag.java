package com.delivx.app.main.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.app.main.MainActivity;
import com.delivx.app.main.profile.editProfile.EditProfileActivity;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.delivx.utility.DialogHelper;
import com.delivx.utility.TextUtil;
import com.driver.Threestops.R;
import com.delivx.pojo.ProfileData;
import com.delivx.utility.CircleImageView;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;
import eu.janmuller.android.simplecropimage.CropImage;


public class MyProfileFrag extends DaggerFragment implements View.OnClickListener,
        ProfileContract.ViewOperations {

    private String TAG = MyProfileFrag.class.getSimpleName();
    private View rootView;
    private static final int CROP_IMAGE = 13;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tvLogout) TextView tv_logout;
    @BindView(R.id.tv_phone) TextView tv_phone;
    @BindView(R.id.tv_email) TextView tv_email;
    @BindView(R.id.tv_email_value) TextView tv_email_value;
    @BindView(R.id.tv_pass) TextView tv_pass;
    @BindView(R.id.tv_vechtype) TextView tv_vechtype;
    @BindView(R.id.tv_vech_number) TextView tv_vech_number;
    @BindView(R.id.iv_prof_img) CircleImageView iv_prof_img;
    @BindView(R.id.iv_profpic_prog) ImageView iv_profpic_prog;
    @BindView(R.id.iv_name_edit) ImageView iv_name_edit;
    @BindView(R.id.iv_phone_edit) ImageView iv_phone_edit;
    @BindView(R.id.iv_password_edit) ImageView iv_password_edit;
    @BindView(R.id.tv_plan) TextView tv_plan;
    @BindView(R.id.tv_prof_pass) TextView tv_prof_pass;
    @BindView(R.id.tv_plan_type) TextView tv_plan_type;
    @BindView(R.id.tv_prof_name) TextView tv_prof_name;
    @BindView(R.id.tv_prof_phone) TextView tv_prof_phone;
    @BindView(R.id.tv_prof_vechtype) TextView tv_prof_vechtype;
    @BindView(R.id.tv_prof_vech_number) TextView tv_prof_vech_number;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @Inject   ProfileContract.PresenterOpetaions presenter;
    @Inject   FontUtils fontUtils;
    @Inject   DialogHelper dialogHelper;
    @BindDrawable(R.drawable.drop_down) Drawable drop_down;

    @BindView(R.id.tv_selected_language) TextView tv_selected_language;
    @BindView(R.id.tv_language) TextView tv_language;
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Inject
    public MyProfileFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this,rootView);
        initializeViews();
        presenter.attachView(this);
        presenter.getProfileDetails();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (VariableConstant.IS_PROFILE_EDITED) {
            VariableConstant.IS_PROFILE_EDITED = false;
            presenter.getProfileDetails();
        }
    }

    /**
     * <h1>initializeViews</h1>
     * <p>Initialize widgets </p>
     */
    private void initializeViews() {
        Typeface clanaproNarrNews = fontUtils.titaliumRegular();
        tv_name.setTypeface(clanaproNarrNews);
        tv_logout.setTypeface(clanaproNarrNews);
        tv_phone.setTypeface(clanaproNarrNews);
        tv_email.setTypeface(clanaproNarrNews);
        tv_email_value.setTypeface(clanaproNarrNews);
        tv_pass.setTypeface(clanaproNarrNews);
        tv_vechtype.setTypeface(clanaproNarrNews);
        tv_vech_number.setTypeface(clanaproNarrNews);
        tv_prof_name.setTypeface(clanaproNarrNews);
        tv_plan.setTypeface(clanaproNarrNews);
        tv_plan_type.setTypeface(clanaproNarrNews);
        tv_prof_phone.setTypeface(clanaproNarrNews);
        tv_prof_pass.setTypeface(clanaproNarrNews);
        tv_prof_vechtype.setTypeface(clanaproNarrNews);
        tv_prof_vech_number.setTypeface(clanaproNarrNews);
        tv_selected_language.setTypeface(clanaproNarrNews);
        tv_language.setTypeface(clanaproNarrNews);

    }

    @OnClick({R.id.iv_name_edit,R.id.iv_phone_edit,
            R.id.iv_password_edit,R.id.iv_prof_img,R.id.tvLogout ,R.id.tv_selected_language})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //event for edit name
            case R.id.iv_name_edit:
                presenter.editName();
                break;
            //event for phone number edit
            case R.id.iv_phone_edit:
                presenter.editPhone();
                break;
            //event for password edit
            case R.id.iv_password_edit:
                presenter.editPassword();
                break;
            //event for profile image
            case R.id.iv_prof_img:
                presenter.profileEdit();
                break;
                ////event for logout the account
            case R.id.tvLogout:
                presenter.logout();
                break;
                //event for select the language
            case R.id.tv_selected_language:
                presenter.getLanguages();
                break;
        }
    }
    @Override
    public void setProfileDetails(ProfileData profileData) {

        tv_prof_name.setText(profileData.getName());
        tv_email_value.setText(profileData.getEmail());
        tv_prof_phone.setText(profileData.getPhone());
        if(!TextUtil.isEmpty(profileData.getAccountType())&&profileData.getAccountType().equals("3")){
            tv_plan_type.setText(profileData.getStoreName());
            tv_plan.setText(getActivity().getResources().getString(R.string.store_name));
        }else {
            tv_plan_type.setText(profileData.getPlanName());
            tv_plan.setText(getActivity().getResources().getString(R.string.plan));
        }

        tv_prof_vechtype.setText(profileData.getVehicleTypeName());
        tv_prof_vech_number.setText(profileData.getVehiclePlatNo());
        String url = profileData.getProfilePic();

        if (!TextUtil.isEmpty(url)) {
            if (url.contains(" ")) {
                url = url.replace(" ", "%20");
            }
            iv_profpic_prog.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(url)
                    .resize(200, 200)
                    .into(iv_prof_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Utility.printLog(TAG+"  onSuccess  ");
                            iv_profpic_prog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Utility.printLog(TAG+"  onError  ");
                            iv_profpic_prog.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startEditActivity(String data) {
        Intent intentName = new Intent(getActivity(), EditProfileActivity.class);
        intentName.putExtra("data", data);
        startActivity(intentName);
        getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult( requestCode,  resultCode,  data);
    }

    @Override
    public void startCropImage() {
        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        getActivity().startActivityForResult(intent, MyProfileFrag.CROP_IMAGE);
    }

    @Override
    public void setProfileImage(Bitmap circle_bMap) {
        iv_prof_img.setImageBitmap(circle_bMap);
    }

    @Override
    public void networkError(String message) {
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void setLanguageDialog(ArrayList<LanguagesList> languagesListModel, int indexSelected) {
        languagesLists = languagesListModel;
        DialogHelper.languageSelectDialog(getActivity(), languagesLists, indexSelected);

    }

    @Override
    public void setLanguage(String languagesListModel, boolean indexSelected) {
        tv_selected_language.setText(languagesListModel);
        tv_selected_language.setCompoundDrawablesWithIntrinsicBounds(null,null ,drop_down,null);
        if(indexSelected)
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }
}
