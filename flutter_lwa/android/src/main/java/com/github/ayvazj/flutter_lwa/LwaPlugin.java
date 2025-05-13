package com.github.ayvazj.flutter_lwa;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.amazon.identity.auth.device.api.workflow.RequestContext;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;

/**
 * LwaPlugin
 */
public class LwaPlugin implements FlutterPlugin, ActivityAware {

    private RequestContext requestContext;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        final MethodChannel channel = new MethodChannel(
                binding.getBinaryMessenger(),
                "com.github.ayvazj/flutter_lwa"
        );

        Context context = binding.getApplicationContext();
        requestContext = RequestContext.create(context);
        channel.setMethodCallHandler(newMethodHandler(requestContext, context));
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        // Cleanup if needed
    }

    private static LwaMethodHandler newMethodHandler(final RequestContext requestContext,
                                                     final @NonNull Context context) {
        return new LwaMethodHandler(requestContext, context);
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        Context activityContext = binding.getActivity();
        if (activityContext instanceof FragmentActivity) {
            requestContext = RequestContext.create(activityContext);
        } else {
            throw new IllegalStateException("Activity must extend FragmentActivity");
        }
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // Optional cleanup
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        if (requestContext != null) {
            requestContext.onResume();
        }
    }

    @Override
    public void onDetachedFromActivity() {
        // Optional cleanup
    }
}
