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
 * LwaPlugin (Amazon Login)
 */
public class LwaPlugin implements FlutterPlugin, ActivityAware {

    private RequestContext requestContext;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        final MethodChannel channel = new MethodChannel(
                binding.getBinaryMessenger(),
                "com.github.ayvazj/flutter_lwa"
        );

        // We'll initialize requestContext later when the activity is attached
        channel.setMethodCallHandler(newMethodHandler(null, binding.getApplicationContext()));
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        // no-op
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        if (binding.getActivity() instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) binding.getActivity();
            requestContext = RequestContext.create(activity);
        } else {
            throw new IllegalStateException("Amazon Login SDK requires a FragmentActivity.");
        }
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // no-op
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        if (requestContext != null) {
            requestContext.onResume();
        }
    }

    @Override
    public void onDetachedFromActivity() {
        // no-op
    }

    private static LwaMethodHandler newMethodHandler(final RequestContext requestContext,
                                                     final @NonNull Context context) {
        return new LwaMethodHandler(requestContext, context);
    }
}
