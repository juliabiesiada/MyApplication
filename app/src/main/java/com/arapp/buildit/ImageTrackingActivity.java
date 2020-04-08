package com.arapp.buildit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.arapp.buildit.rendering.external.CustomSurfaceView;
import com.arapp.buildit.rendering.external.Driver;
import com.arapp.buildit.rendering.external.GLRenderer;
import com.arapp.buildit.rendering.external.StrokedRectangle;
import com.wikitude.NativeStartupConfiguration;
import com.wikitude.WikitudeSDK;
import com.wikitude.common.WikitudeError;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.permission.PermissionManager;
import com.wikitude.common.rendering.RenderExtension;
import com.wikitude.rendering.ExternalRendering;
import com.wikitude.tracker.ImageTarget;
import com.wikitude.tracker.ImageTracker;
import com.wikitude.tracker.ImageTrackerListener;
import com.wikitude.tracker.TargetCollectionResource;



public class ImageTrackingActivity extends Activity implements ImageTrackerListener, ExternalRendering {

    private static final String TAG = "IMAGE TRACKING ACTIVITY";
    WikitudeSDK wikitudeSDK;
    NativeStartupConfiguration startupConfiguration;
    GLRenderer glRenderer;
    CustomSurfaceView view;
    Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wikitudeSDK = new WikitudeSDK(this);
        startupConfiguration = new NativeStartupConfiguration();
        startupConfiguration.setLicenseKey(getString(R.string.LICENSE_KEY));
        startupConfiguration.setCameraPosition(CameraSettings.CameraPosition.BACK);
        startupConfiguration.setCameraResolution(CameraSettings.CameraResolution.AUTO);
        wikitudeSDK.onCreate(getApplicationContext(), this, startupConfiguration);

        final TargetCollectionResource targetCollectionResource = wikitudeSDK.getTrackerManager().createTargetCollectionResource("file:///android_asset/card.png");
        ImageTracker imageTracker = wikitudeSDK.getTrackerManager().createImageTracker(targetCollectionResource, this, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wikitudeSDK.onResume();
        view.onResume();
        driver.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
        driver.stop();
        wikitudeSDK.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wikitudeSDK.onDestroy();
    }

    @Override
    public void onRenderExtensionCreated(RenderExtension renderExtension) {
        glRenderer = new GLRenderer(renderExtension);
        view = new CustomSurfaceView(getApplicationContext(), glRenderer);
        driver = new Driver(view, 30);
        setContentView(view);
    }

    @Override
    public void onTargetsLoaded(ImageTracker tracker) {
        Log.v(TAG, "Image tracker loaded");
    }

    @Override
    public void onErrorLoadingTargets(ImageTracker imageTracker, WikitudeError error) {
        Log.v(TAG, "Unable to load image tracker. Reason: " + error.getMessage());
    }

    @Override
    public void onImageRecognized(ImageTracker tracker, ImageTarget target) {
        Log.v(TAG, "Image recognized");
        StrokedRectangle strokedRectangle = new StrokedRectangle(StrokedRectangle.Type.STANDARD);
        glRenderer.setRenderablesForKey(target.getName() + target.getUniqueId(), strokedRectangle, null);
    }

    @Override
    public void onImageTracked(ImageTracker tracker, ImageTarget target) {
        StrokedRectangle strokedRectangle = (StrokedRectangle)glRenderer.getRenderableForKey(target.getName() + target.getUniqueId());

        if (strokedRectangle != null) {
            strokedRectangle.projectionMatrix = target.getProjectionMatrix();
            strokedRectangle.viewMatrix = target.getViewMatrix();

            strokedRectangle.setXScale(target.getTargetScale().x);
            strokedRectangle.setYScale(target.getTargetScale().y);
        }
    }

    @Override
    public void onImageLost(ImageTracker tracker, ImageTarget target) {
        Log.v(TAG, "Lost target " + target.getName());
        glRenderer.removeRenderablesForKey(target.getName() + target.getUniqueId());
    }

    @Override
    public void onExtendedTrackingQualityChanged(ImageTracker tracker, ImageTarget target, int i, int i1) {

    }
}