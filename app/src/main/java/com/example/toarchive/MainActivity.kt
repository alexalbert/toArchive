package com.example.toarchive

import android.os.Bundle
import android.view.KeyEvent
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
            val url = intent.getStringExtra("android.intent.extra.TEXT")

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

                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            // Check if the page is loaded
//                                        if (view!!.isShown) {
//                                            return
//                                        }

                                            val url =
                                                "https://www.nytimes.com/live/2023/09/11/world/earthquake-morocco-marrakesh"

                                            // Populate a field in HTML
                                            val js = String.format(
                                                "document.getElementById('url').value = '%s'",
                                                url
                                            )
                                            evaluateJavascript(js, null)
                                            evaluateJavascript(
                                                "document.getElementById('url').focus()",
                                                null
                                            )

                                            val keyboardEvent = KeyEvent(
                                                KeyEvent.ACTION_DOWN,
                                                KeyEvent.KEYCODE_ENTER
                                            )
                                            // Dispatch the KeyboardEvent object to the WebView object
                                            dispatchKeyEvent(keyboardEvent)
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
