package kz.zhetysu.immerlex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
    private static final String HOST = "appassets.androidplatform.net";
    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        web = new WebView(this);
        web.setBackgroundColor(0xFFFFFFFF);
        setContentView(web);
        WindowInsetsControllerCompat ctl = WindowCompat.getInsetsController(getWindow(), web);
        ctl.setAppearanceLightStatusBars(true);
        ctl.setAppearanceLightNavigationBars(true);
        ViewCompat.setOnApplyWindowInsetsListener(web, (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(b.left, b.top, b.right, b.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .setDomain(HOST)
                .addPathHandler("/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);

        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                return loader.shouldInterceptRequest(req.getUrl());
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                if (HOST.equals(u.getHost())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) { /* no browser */ }
                return true;
            }
        });

        if (state != null) web.restoreState(state);
        else web.loadUrl("https://" + HOST + "/www/index.html");
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    public void onBackPressed() {
        String js = "(function(){var s=document.querySelector('.scr.on');if(!s||s.id==='home')return '0';"
                + "var b=s.querySelector('.top button');if(b){b.click();return '1'}"
                + "if(typeof show==='function'){show('home');return '1'}return '0'})()";
        web.evaluateJavascript(js, r -> { if (r == null || !r.contains("1")) finish(); });
    }
}
