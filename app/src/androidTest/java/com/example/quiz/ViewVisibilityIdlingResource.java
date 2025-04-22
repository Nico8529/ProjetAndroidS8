package com.example.quiz;

import android.view.View;

import androidx.test.espresso.IdlingResource;

public class ViewVisibilityIdlingResource implements IdlingResource {
    private final View mView;
    private ResourceCallback mCallback;

    public ViewVisibilityIdlingResource(View view) {
        mView = view;
    }

    @Override
    public String getName() {
        return ViewVisibilityIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        // Retourne vrai lorsque la vue est visible
        boolean isIdle = mView.getVisibility() == View.VISIBLE;
        if (isIdle && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}
