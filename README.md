# SmartphoneApp-SnapAndEat

# AlertDialog
``` 
AlertDialog alertDialog = new AlertDialog.Builder(holder.mView.getContext()).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Are you sure you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDBHelper dbHelper = new SQLiteDBHelper(holder.mView.getContext());
                                dbHelper.deleteOne(stuffToDelete);
                                mStuffs.remove(position);  // remove the item from list
                                notifyItemRemoved(position); // notify the adapter about the removed item
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                // remove your item from data base

            }
        });
```        
# Webview     
WebView via XML:
```
<WebView
    android:id="@+id/webview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
/>

WebView myWebView = (WebView) findViewById(R.id.webview);
myWebView.loadUrl("http://www.example.com");
```

WebView via JAVA:
```
WebView myWebView = new WebView(activityContext);
setContentView(myWebView);
myWebView.loadUrl("http://www.example.com");
```
