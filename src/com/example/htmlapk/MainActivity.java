package com.example.htmlapk;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	static WebView webView;
	Stack<WebView> webViewStack = new Stack<WebView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ʵ����WebView��
		if(webView == null)
		{
			webView = newWebView();
			webView.loadUrl("http://game.hopmet.com");
			//����Web��ͼ          
			setContentView(webView);
		}
		else
		{
			ViewGroup parent = (ViewGroup)webView.getParent();
			parent.removeView(webView);
			setContentView(webView);
		}
	}      
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private WebView newWebView()
	{
		WebView webview = new WebView(this);          
		//������Ҫ��ʾ����ҳ           
		WebSettings settings = webview.getSettings(); 

        // ����javascript���� 

        settings.setJavaScriptEnabled( true );   
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // ���ÿ���ʹ��localStorage 

        settings.setDomStorageEnabled( true ); 
        settings.setJavaScriptCanOpenWindowsAutomatically(true); 
        settings.setSupportMultipleWindows(true);
        settings.setUseWideViewPort(true);//���ô����ԣ��������������
        settings.setLoadWithOverviewMode(true);
          
        webview.setWebViewClient(new WebViewClient(){

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				//super.onReceivedSslError(view, handler, error);
				handler.proceed();
			}

        });
        webview.setWebChromeClient(new WebChromeClient(){

			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog,
					boolean isUserGesture, Message resultMsg) {
                WebView childView = newWebView();
                childView.setWebChromeClient(this ); 
                webViewStack.push(webView);
                webView = childView;
                MainActivity.this.setContentView(webView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj; 
                transport.setWebView(childView); 
                resultMsg.sendToTarget(); 
                return true; 
			}

			@Override
			public void onCloseWindow(WebView window) {
				// TODO Auto-generated method stub
				if(webViewStack.empty())
				{
					super.onCloseWindow(window);
				}
				else
				{
					WebView tmp = webView;
					webView = webViewStack.lastElement();
					MainActivity.this.setContentView(webView);
					tmp.destroy();
					webView.reload();
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				// TODO Auto-generated method stub
				return super.onJsPrompt(view, url, message, defaultValue, result);
			}

			@Override
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsBeforeUnload(view, url, message, result);
			}
        });
        return webview;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(webView.canGoBack())
		{
			webView.goBack(); //goBack()��ʾ����WebView����һҳ��
		}
		else
		{
			finish();
		}
		//super.onBackPressed();
	}

}
