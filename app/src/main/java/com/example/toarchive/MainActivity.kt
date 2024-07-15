package com.example.toarchive

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.toarchive.ui.theme.ToArchiveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action = intent.action
        val type = intent.type

        if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
            val destinationUrl = intent.getStringExtra("android.intent.extra.TEXT")

            setContent {
                ToArchiveTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true

                                    webViewClient = object : MyWebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            // Populate a field in HTML
                                            var js = String.format("document.getElementById('url').value = '%s';", destinationUrl)
                                            js += "document.forms['submiturl'].submit();"
                                            evaluateJavascript(js, null)
                                        }
                                    }
                                    loadUrl("https://www.archive.md")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

open class MyWebViewClient : WebViewClient() {
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        val message = "JS Error: ${description} at URL ${failingUrl} }"
        Log.e("WebView", message)
    }
}
