package com.arapp.buildit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.arapp.buildit.rendering.external.CustomSurfaceView;
import com.arapp.buildit.rendering.external.Driver;
import com.arapp.buildit.rendering.external.GLRenderer;
import com.arapp.buildit.rendering.external.OccluderCube;
import com.arapp.buildit.rendering.external.StrokedCube;
import com.wikitude.NativeStartupConfiguration;
import com.wikitude.WikitudeSDK;
import com.wikitude.common.WikitudeError;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.rendering.RenderExtension;
import com.wikitude.rendering.ExternalRendering;
import com.wikitude.tracker.ObjectTarget;
import com.wikitude.tracker.ObjectTracker;
import com.wikitude.tracker.ObjectTrackerConfiguration;
import com.wikitude.tracker.ObjectTrackerListener;
import com.wikitude.tracker.TargetCollectionResource;

public class ObjectTrackingActivity extends Activity implements ObjectTrackerListener, ExternalRendering {



    private static final String TAG = "MultipleTargetsActivity";
    WikitudeSDK wikitudeSDK;
    NativeStartupConfiguration startupConfiguration;
    GLRenderer glRenderer;
    CustomSurfaceView view;
    Driver driver;
    Button stopExtendedTrackingButton;
    ObjectTracker objectTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        wikitudeSDK = new WikitudeSDK(this);
        startupConfiguration = new NativeStartupConfiguration();
        startupConfiguration.setLicenseKey(getString(R.string.LICENSE_KEY));
        startupConfiguration.setCameraPosition(CameraSettings.CameraPosition.BACK);
        startupConfiguration.setCameraResolution(CameraSettings.CameraResolution.AUTO);
        wikitudeSDK.onCreate(getApplicationContext(), this, startupConfiguration);

        final TargetCollectionResource targetCollectionResource = wikitudeSDK.getTrackerManager().createTargetCollectionResource("file:///android_asset/tracker.wto");
        ObjectTrackerConfiguration trackerConfiguration = new ObjectTrackerConfiguration();
        trackerConfiguration.setExtendedTargets(new String[]{"*"});
        ObjectTracker objectTracker = wikitudeSDK.getTrackerManager().createObjectTracker(targetCollectionResource, ObjectTrackingActivity.this, trackerConfiguration);

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
        wikitudeSDK.getCameraManager().setRenderingCorrectedFovChangedListener(glRenderer);
        view = new CustomSurfaceView(getApplicationContext(), glRenderer);
        driver = new Driver(view, 30);
        setContentView(R.layout.activity_extended_tracking);
        RelativeLayout root = findViewById(R.id.extended_tracking_layout);
        root.addView(view, 0);

        stopExtendedTrackingButton = findViewById(R.id.stop_extended_tracking_button);
        stopExtendedTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExtendedTrackingButton.setVisibility(View.GONE);
                objectTracker.stopExtendedTracking();
            }
        });
    }
    @Override
    public void onTargetsLoaded(ObjectTracker objectTracker) {
        Log.v(TAG, "Object tracker loaded");
    }

    @Override
    public void onErrorLoadingTargets(ObjectTracker objectTracker, WikitudeError error) {
        Log.v(TAG, "Unable to load object tracker. Reason: " + error.getMessage());
    }

    @Override
    public void onObjectRecognized(ObjectTracker objectTracker, ObjectTarget target) {
        Log.v(TAG, "Object recognized");
        StrokedCube strokedCube = new StrokedCube();
        OccluderCube occluderCube = new OccluderCube();
        glRenderer.setRenderablesForKey(target.getName(), strokedCube, occluderCube);
    }

    @Override
    public void onObjectTracked(ObjectTracker objectTracker, ObjectTarget target) {

        StrokedCube strokedCube = (StrokedCube)glRenderer.getRenderableForKey(target.getName());
        if (strokedCube != null) {
            strokedCube.projectionMatrix = target.getProjectionMatrix();
            strokedCube.viewMatrix = target.getViewMatrix();

            strokedCube.setXScale(target.getTargetScale().x);
            strokedCube.setYScale(target.getTargetScale().y);
            strokedCube.setZScale(target.getTargetScale().z);
        }

        OccluderCube occluderCube = (OccluderCube)glRenderer.getOccluderForKey(target.getName());
        if (occluderCube != null) {
            occluderCube.projectionMatrix = target.getProjectionMatrix();
            occluderCube.viewMatrix = target.getViewMatrix();

            occluderCube.setXScale(target.getTargetScale().x);
            occluderCube.setYScale(target.getTargetScale().y);
            occluderCube.setZScale(target.getTargetScale().z);
        }
    }

    @Override
    public void onObjectLost(ObjectTracker objectTracker, ObjectTarget target) {
        glRenderer.removeRenderablesForKey(target.getName());
    }

    @Override
    public void onExtendedTrackingQualityChanged(final ObjectTracker tracker, final ObjectTarget target, final int oldTrackingQuality, final int newTrackingQuality) {
        EditText trackingQualityIndicator = findViewById(R.id.tracking_quality_indicator);
        switch (newTrackingQuality) {
            case -1:
                trackingQualityIndicator.setBackgroundColor(Color.parseColor("#FF3420"));
                trackingQualityIndicator.setText(R.string.tracking_quality_indicator_bad);
                break;
            case 0:
                trackingQualityIndicator.setBackgroundColor(Color.parseColor("#FFD900"));
                trackingQualityIndicator.setText(R.string.tracking_quality_indicator_average);
                break;
            default:
                trackingQualityIndicator.setBackgroundColor(Color.parseColor("#6BFF00"));
                trackingQualityIndicator.setText(R.string.tracking_quality_indicator_good);
        }
        trackingQualityIndicator.setVisibility(View.VISIBLE);
    }
}