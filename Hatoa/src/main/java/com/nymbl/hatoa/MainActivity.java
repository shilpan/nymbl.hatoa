package com.nymbl.hatoa;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.nymbl.hatoa.zbarIntegration.CameraPreview;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class MainActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    GridView gridView;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        autoFocusHandler = new Handler();
//        mCamera = getCameraInstance();
//
//        /* Instance barcode scanner */
//        scanner = new ImageScanner();
//        scanner.setConfig(0, Config.X_DENSITY, 3);
//        scanner.setConfig(0, Config.Y_DENSITY, 3);
//
//        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
//        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
//        preview.addView(mPreview);
//
//        scanText = (TextView)findViewById(R.id.scanText);
//
//        scanButton = (Button)findViewById(R.id.ScanButton);
//
//        scanButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (barcodeScanned) {
//                    barcodeScanned = false;
//                    scanText.setText("Scanning...");
//                    mCamera.setPreviewCallback(previewCb);
//                    mCamera.startPreview();
//                    previewing = true;
//                    mCamera.autoFocus(autoFocusCB);
//                }
//            }
//        });
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    scanText.setText("barcode result " + sym.getData());
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    
}
