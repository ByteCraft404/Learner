package com.example.codelearner.utils;

import android.app.Activity;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Utility class to handle window insets consistently across all activities
 */
public class WindowInsetsHelper {
    
    /**
     * Sets up edge-to-edge display and proper window insets handling
     * @param activity The activity to configure
     * @param rootViewId The ID of the root view that should handle insets
     */
    public static void setupEdgeToEdge(Activity activity, int rootViewId) {
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        
        View rootView = activity.findViewById(rootViewId);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                // Get the status bar and navigation bar insets
                int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
                int navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                
                // Apply padding to avoid system UI overlap
                v.setPadding(
                    v.getPaddingLeft(),
                    statusBarHeight,
                    v.getPaddingRight(),
                    navigationBarHeight
                );
                
                return insets;
            });
        }
    }
    
    /**
     * Sets up edge-to-edge display and proper window insets handling for activities with navigation drawer
     * @param activity The activity to configure
     * @param rootViewId The ID of the root view (typically DrawerLayout)
     * @param mainContentId The ID of the main content view
     * @param navViewId The ID of the navigation view
     */
    public static void setupEdgeToEdgeWithDrawer(Activity activity, int rootViewId, int mainContentId, int navViewId) {
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        
        View rootView = activity.findViewById(rootViewId);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                // Get the status bar and navigation bar insets
                int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
                int navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                
                // Apply padding to main content
                View mainContent = activity.findViewById(mainContentId);
                if (mainContent != null) {
                    mainContent.setPadding(
                        mainContent.getPaddingLeft(),
                        statusBarHeight,
                        mainContent.getPaddingRight(),
                        navigationBarHeight
                    );
                }
                
                // Apply padding to navigation view
                View navView = activity.findViewById(navViewId);
                if (navView != null) {
                    ViewCompat.setOnApplyWindowInsetsListener(navView, (view, windowInsets) -> {
                        view.setPadding(
                            view.getPaddingLeft(),
                            statusBarHeight,
                            view.getPaddingRight(),
                            navigationBarHeight
                        );
                        return windowInsets;
                    });
                }
                
                return insets;
            });
        }
    }
}