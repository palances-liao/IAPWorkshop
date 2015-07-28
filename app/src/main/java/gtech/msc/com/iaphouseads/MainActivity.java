package gtech.msc.com.iaphouseads;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.*;
import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.purchase.InAppPurchaseResult;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import android.util.Log;

public class MainActivity extends Activity implements PlayStorePurchaseListener {
    private InterstitialAd mInterstitial;
    private Button mBtnShowInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.test_ad_unit_id));
        mInterstitial.setPlayStorePurchaseParams(this, null);

        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        mBtnShowInterstitial = (Button)findViewById(R.id.btn_show_interstitial);
        mBtnShowInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitial.isLoaded()){
                    mInterstitial.show();
                }
            }
        });

        requestNewInterstitial();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestNewInterstitial(){
        AdRequest request = new AdRequest.Builder().build();
        mInterstitial.loadAd(request);
    }

    @Override
    public boolean isValidPurchase(String sku){
        Toast toast= Toast.makeText(getApplicationContext(), "SKU:" + sku, Toast.LENGTH_LONG);
        toast.show();

        return true;
    }

    @Override
    public void onInAppPurchaseFinished(InAppPurchaseResult result){
        Toast toast = Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_LONG);
        toast.show();

        if(result.getResultCode() != Activity.RESULT_OK){
            Log.d("IAP-Ad", "Purchase was failed with error code : " + result.getResultCode());
            return;
        }

        if(!result.isVerified()){
            Log.d("IAP-Ad", "Fail to verify the purchase");
            return;
        }

        Log.d("Iap-Ad", "The item has been purchased!");
        result.finishPurchase();
    }
}
