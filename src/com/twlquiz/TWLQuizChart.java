package com.twlquiz;

import android.webkit.WebView;

public class TWLQuizChart {
	private WebView webView;

	public TWLQuizChart  (WebView newWebView) {
		this.webView = newWebView;
	}

	public String getGraphTitle() {
		return "WOAHHAHAHAHH!!!!!";
	}  

	public void loadGraph() {
		webView.loadUrl("javascript:GotGraph([[[0,0],[5,5],[10,10],[15,15]]])");
	}
}