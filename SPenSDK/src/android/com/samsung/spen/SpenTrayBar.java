/*
 * Copyright (c) 2015 Samsung Electronics, Co. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samsung.spen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import com.samsung.android.sdk.pen.SpenSettingPenInfo;
import com.samsung.android.sdk.pen.SpenSettingSelectionInfo;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.document.SpenObjectBase;
import com.samsung.android.sdk.pen.document.SpenObjectContainer;
import com.samsung.android.sdk.pen.document.SpenObjectImage;
import com.samsung.android.sdk.pen.document.SpenObjectStroke;
import com.samsung.android.sdk.pen.document.SpenObjectTextBox;
import com.samsung.android.sdk.pen.document.SpenPageDoc;
import com.samsung.android.sdk.pen.document.SpenPageDoc.HistoryListener;
import com.samsung.android.sdk.pen.document.SpenPageDoc.HistoryUpdateInfo;
import com.samsung.android.sdk.pen.engine.SpenColorPickerListener;
import com.samsung.android.sdk.pen.engine.SpenContextMenu;
import com.samsung.android.sdk.pen.engine.SpenContextMenuItemInfo;
import com.samsung.android.sdk.pen.engine.SpenControlListener;
import com.samsung.android.sdk.pen.engine.SpenLongPressListener;
import com.samsung.android.sdk.pen.engine.SpenPageEffectListener;
import com.samsung.android.sdk.pen.engine.SpenSelectionChangeListener;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.engine.SpenTextMeasure;
import com.samsung.android.sdk.pen.recognition.SpenCreationFailureException;
import com.samsung.android.sdk.pen.recognition.SpenRecognitionBase.ResultListener;
import com.samsung.android.sdk.pen.recognition.SpenRecognitionInfo;
import com.samsung.android.sdk.pen.recognition.SpenShapeRecognition;
import com.samsung.android.sdk.pen.recognition.SpenShapeRecognitionManager;
import com.samsung.android.sdk.pen.recognition.SpenTextRecognition;
import com.samsung.android.sdk.pen.recognition.SpenTextRecognitionManager;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;
import com.samsung.android.sdk.pen.settingui.SpenSettingSelectionLayout;
import com.samsung.spen.SpenException.SpenExceptionType;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
/**
 * Class responsible for creating the top traybar in both Inline and Popup surface.
 *
 */
public class SpenTrayBar {
    private static final String TAG = "SpenTrayBar";
    protected ImageButton mButtonPen, mButtonEraser, mButtonDone, mButtonUndo,
            mButtonRedo, mButtonEdit, mButtonBg, mButtonAddPage,
            mButtonDeletePage, mButtonPageNavigateRight,
            mButtonPageNavigateLeft, mButtonSelect, mpenAndFinger,
            mButtonCancel;
    protected RelativeLayout mRelativeLayout;
    private int mOriginalPosition;
    protected ImageView mImageView;
    protected SpenContextParams mContextParams;
    protected String id;
    private ImageButton mButtonColor1, mButtonColor2, mButtonColor3,
            mButtonColor4, mButtonColor5, mButtonColor6, mButtonColor7;
    private TextView mCurrentPageNumberTextView;
    private SpenSettingPenLayout mSpenSettingPenLayout;
    private SpenTextRecognition mTextRecognition = null;
    private SpenTextRecognitionManager mSpenTextRecognitionManager = null;
    private SpenShapeRecognitionManager mSpenShapeRecognitionManager = null;
    private SpenShapeRecognition mShapeRecognition = null;
    private boolean mIsProcessingRecognition = false;
    private boolean isSpenAndFinger = false;
    private ArrayList<SpenObjectBase> mClipboard;
    private SpenContextMenu mPasteContextMenu;
    private boolean isDone = false;
    private SpenSettingSelectionLayout mSelectionSettingView;
    private SpenSettingSelectionInfo mSpenSettingSelectionInfo;
    private Resources activityRes;
    private SpenPlugin mSpenCustomDrawPlugin;
    private SpenSurface mSpenSurface;
    private SpenSurfaceView mSpenSurfaceView;
    private SpenPageDoc mSpenPageDoc;
    private SpenNoteDoc mSpenNoteDoc;
    private SpenSettingPenInfo mPenInfo;
    private SpenTrayBarOptions mOptions;
    private LinearLayout mBasicColorView;
    private static ArrayList<SpenObjectBase> mInputList = null;
    private static ArrayList<SpenObjectBase> mSelectedList = null;
    private int mStrokeTimestamp;
    private int mStrokeColor;
    private float offsetX, offsetY;
    private Button mButtonClearAll;

    
    /**
     * Set Id
     * 
     * @params id String
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Get Id
     * 
     * @return id String
     */
    String getId() {
        return this.id;
    }
    /**
     * Constructor for SpenTrayBar
     * 
     * @param contextParams
     *             SpenContextParams
     * @param spenSurface
     *             SpenSurface
     * @param options
     *             SpenTrayBarOptions
     */
    
    SpenTrayBar(SpenContextParams contextParams, SpenSurface spenSurface,
            SpenTrayBarOptions options) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "SpenTrayBar Constructor");
        }
        mContextParams = contextParams;
        mSpenCustomDrawPlugin = contextParams.getSpenCustomDrawPlugin();
        activityRes = contextParams.getSpenCustomDrawPlugin().cordova
                .getActivity().getResources();
        mRelativeLayout = spenSurface.getRelativeLayout();
        mSpenSurface = spenSurface;
        mSpenSurfaceView = mSpenSurface.getSpenSurfaceView();
        mSpenPageDoc = mSpenSurface.getSpenPageDoc();
        mSpenNoteDoc = mSpenSurface.getSpenNoteDoc();
        mPenInfo = mSpenSurface.getSpenInfo();

        mOptions = options;
        if (mOptions.getReturnType() == Utils.RETURN_TYPE_TEXT
                || (mOptions.getsPenFlags() & Utils.FLAG_TEXT_RECOGNITION) == Utils.FLAG_TEXT_RECOGNITION) {
            setTextRecognition();
        }
        if ((mOptions.getsPenFlags() & Utils.FLAG_SHAPE_RECOGNITION) == Utils.FLAG_SHAPE_RECOGNITION)
            setShapeRecognition();

    }

    /**
     * closing all the controls of the traybar before remove the surface
     */
    public void removeTrayBar() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside removeTrayBar");
        }

        if (mSpenSettingPenLayout != null) {
            mSpenSettingPenLayout.close();
        }

        if (mSpenTextRecognitionManager != null) {
			if (mTextRecognition !=null){
				mSpenTextRecognitionManager.destroyRecognition(mTextRecognition);
			}
            mSpenTextRecognitionManager.close();
            mSpenTextRecognitionManager = null;
            mTextRecognition = null;
        }
        if (mSpenShapeRecognitionManager != null) {
            mSpenShapeRecognitionManager.close();
            mSpenShapeRecognitionManager = null;
            mTextRecognition = null;
        }

        if (mSelectionSettingView != null) {
            mSelectionSettingView.close();
        }
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.close();
            mSpenSurfaceView.closeControl();
            mSpenSurfaceView = null;
        }
        if (mPasteContextMenu != null) {
            mPasteContextMenu.close();
        }
        if (mSpenNoteDoc != null) {
            try {
                mSpenNoteDoc.close();
            } catch (IOException e) {
                Log.d(TAG,
                        "exception while removing surface: " + e.getMessage());
                e.printStackTrace();
            }
        }
        mSpenNoteDoc = null;
        onScrollListener = null;
    }

    /**
     * add the buttons to the layout view
     */
    private void addButtonsToSurfaceView() {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside addButtonsToSurfaceView");
        }
        if (((mOptions.getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN)
                || mOptions.getSurfaceType() == Utils.SURFACE_POPUP
                || ((mOptions.getsPenFlags() & Utils.FLAG_PEN_SETTINGS) == Utils.FLAG_PEN_SETTINGS)) {
            mButtonPen = (ImageButton) getView("penBtn", "id", View.VISIBLE);
        }

        if (((mOptions.getsPenFlags() & Utils.FLAG_ERASER) == Utils.FLAG_ERASER)
                || mOptions.getSurfaceType() == Utils.SURFACE_POPUP) {
            mButtonEraser = (ImageButton) getView("EraserBtn", "id",
                    View.VISIBLE);
        }

        if (((mOptions.getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO)
                || mOptions.getSurfaceType() == Utils.SURFACE_POPUP) {
            mButtonUndo = (ImageButton) getView("undoBtn", "id", View.VISIBLE);
            mButtonUndo.setEnabled(false);
            mButtonRedo = (ImageButton) getView("redoBtn", "id", View.VISIBLE);
            mButtonRedo.setEnabled(false);
        }

        mButtonDone = (ImageButton) getView("saveBtn", "id", View.VISIBLE);
        mButtonCancel = (ImageButton) getView("cancelBtn", "id", View.VISIBLE);
        if (mOptions.getIsFeatureEnabled()) {

            mpenAndFinger = (ImageButton) getView("penAndFingerMode", "id",
                    View.VISIBLE);
            int resid = activityRes.getIdentifier("ic_pen_only", "drawable",
                    activity.getPackageName());
            mpenAndFinger.setImageResource(resid);
            isSpenAndFinger = false;
            mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                    SpenSurfaceView.ACTION_NONE);
        } else {
            mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                    SpenSurfaceView.ACTION_STROKE);
        }

        if (((mOptions.getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN)
                || mOptions.getSurfaceType() == Utils.SURFACE_POPUP) {
            mBasicColorView = (LinearLayout) getView("color_select_layout",
                    "id", View.GONE);
            mButtonColor1 = (ImageButton) getView("pen1_btn", "id",
                    View.VISIBLE);
            mButtonColor2 = (ImageButton) getView("pen2_btn", "id",
                    View.VISIBLE);
            mButtonColor3 = (ImageButton) getView("pen3_btn", "id",
                    View.VISIBLE);
            mButtonColor4 = (ImageButton) getView("pen4_btn", "id",
                    View.VISIBLE);
            mButtonColor5 = (ImageButton) getView("pen5_btn", "id",
                    View.VISIBLE);
            mButtonColor6 = (ImageButton) getView("pen6_btn", "id",
                    View.VISIBLE);
            mButtonColor7 = (ImageButton) getView("pen7_btn", "id",
                    View.VISIBLE);
        }

        addOptionalButtonsToSurfaceView();
    }
    /**
     * add the otional buttons to the layout view
     */
    private void addOptionalButtonsToSurfaceView() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside addOptionalButtonsToSurfaceView");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        if ((mOptions.getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN) {
            mButtonPen = (ImageButton) getView("penBtn", "id", View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_ERASER) == Utils.FLAG_ERASER) {
            mButtonEraser = (ImageButton) getView("EraserBtn", "id",
                    View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
            mButtonUndo = (ImageButton) getView("undoBtn", "id", View.VISIBLE);
            mButtonRedo = (ImageButton) getView("redoBtn", "id", View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
            mButtonUndo = (ImageButton) getView("undoBtn", "id", View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_BACKGROUND) == Utils.FLAG_BACKGROUND) {
            mButtonBg = (ImageButton) getView("changeBgBtn", "id", View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_SELECTION) == Utils.FLAG_SELECTION) {
            mButtonSelect = (ImageButton) getView("selectionBtn", "id",
                    View.VISIBLE);
        }
        if ((mOptions.getsPenFlags() & Utils.FLAG_ADD_PAGE) == Utils.FLAG_ADD_PAGE) {

            mButtonDeletePage = (ImageButton) getView("deletePage", "id",
                    View.VISIBLE);
            mButtonPageNavigateLeft = (ImageButton) getView("navigateLeft",
                    "id", View.VISIBLE);
            mCurrentPageNumberTextView = (TextView) getView("pageNumber", "id",
                    View.VISIBLE);
            mButtonPageNavigateRight = (ImageButton) getView("navigateRight",
                    "id", View.VISIBLE);
            mButtonAddPage = (ImageButton) getView("addPage", "id",
                    View.VISIBLE);

        }
        mButtonClearAll = new Button(activity);
        mButtonClearAll.setBackgroundResource(activityRes.getIdentifier(
                "quickmemo_bubble", "drawable", activity.getPackageName()));
        mButtonClearAll.setVisibility(View.GONE);
        mButtonClearAll.setText("Erase All");
        mButtonClearAll.setOnClickListener(mActionButtonsOnClickListener);
        mButtonClearAll.setOnTouchListener(mOnTouchListener);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                (int) (((float) 90) * activity.getResources()
                        .getDisplayMetrics().density),
                (int) (((float) 45) * activity.getResources()
                        .getDisplayMetrics().density));
        lp.setMargins((int) (((float) 15) * activity.getResources()
                .getDisplayMetrics().density), (int) (((float) 40) * activity
                .getResources().getDisplayMetrics().density), 0, 0);
        lp.height = (int) (((float) 45) * activity.getResources()
                .getDisplayMetrics().density);
        lp.width = (int) (((float) 90) * activity.getResources()
                .getDisplayMetrics().density);
        mRelativeLayout.addView(mButtonClearAll, lp);
        initializeListenersForTrayButtons();
    }
    /**
     * Get View 
     * 
     * @return view 
     */
    private View getView(String id, String idType, int visible) {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        int backResId = activityRes.getIdentifier(id, idType,
                activity.getPackageName());
        View view = mRelativeLayout.findViewById(backResId);
        if (view != null) {
            view.setVisibility(visible);
        }
        return view;
    }

    /**
     * initialize the tray bar buttons
     */
    void initTrayBarButtons() {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        Context context = activity.getApplicationContext();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside initTrayBarButtons");
        }
        if (mImageView == null) {
            mImageView = new ImageView(context);
            mImageView.setId(View.generateViewId());
        }

        if (mButtonEdit == null) {
            mButtonEdit = new ImageButton(context);
            mButtonEdit.setId(View.generateViewId());
            int backResId = activityRes.getIdentifier("tool_ic_gesture",
                    "drawable", activity.getPackageName());
            mButtonEdit.setBackgroundColor(mOptions.getColor());
            mButtonEdit.setScaleType(ScaleType.FIT_CENTER);
            mButtonEdit.setAdjustViewBounds(true);
            mButtonEdit.setPadding(0, 0, 0, 0);
            mButtonEdit.setImageResource(backResId);
        }
    }

    /**
     * call gallery for input image
     * 
     * @params nRequestCode
     *                  int
     */
    private void callGalleryForInputImage(int nRequestCode) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside callGalleryForInputImage, request code is :"
                    + nRequestCode);
        }
        CordovaInterface cordova = mContextParams.getSpenCustomDrawPlugin().cordova;

        try {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            mSpenCustomDrawPlugin.setIdAndSurfaceType(mOptions.getId(),
                    mOptions.getSurfaceType());
            cordova.startActivityForResult(
                    (CordovaPlugin) mSpenCustomDrawPlugin, galleryIntent,
                    nRequestCode);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "unable to set the background: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_SET_BACKGROUND_IMAGE,
                    mContextParams.getCallbackContext());
        }
    }

    /**
     * create Setting Layout
     */
    public void createSettingsLayout() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Creating settings Layouts");
        }
        mSpenSettingPenLayout = getSpenSettingPenLayout();
        setSpenSettingPenLayout();

        mSelectionSettingView = getSpenSelectionSettingLayout();
        setSpenSelectionSettingLayout();
    }


    /**
     * Get Spen Setting Selection Layout
     * 
     * @params mSelectionSettingView SpenSettingSelectionLayout
     */
    public SpenSettingSelectionLayout getSpenSelectionSettingLayout() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside getSpenSelectionSettingLayout");
        }
        if (mSelectionSettingView == null) {
            mSelectionSettingView = new SpenSettingSelectionLayout(
                    mContextParams.getSpenCustomDrawPlugin().cordova
                            .getActivity().getApplicationContext(), "",
                    mRelativeLayout);
            mSelectionSettingView.setInfo(getSpenSettingSelectionInfo());
        }
        return mSelectionSettingView;
    }
    /**
     * Set Spen Setting Selection Layout
     * 
     */
    public void setSpenSelectionSettingLayout() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside setSpenSelectionSettingLayout");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.addView(mSelectionSettingView);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(
                RelativeLayout.BELOW,
                activityRes.getIdentifier("spen_traybar_layout", "id",
                        activity.getPackageName()));

        mSelectionSettingView.setInfo(getSpenSettingSelectionInfo());
        mRelativeLayout.addView(linearLayout, p);
        mSelectionSettingView.setCanvasView(mSpenSurfaceView);
        mSpenSurfaceView.setSelectionChangeListener(mSelectionListener);
        mSpenSurfaceView.setSelectionSettingInfo(getSpenSettingSelectionInfo());
    }

    /**
     * Set Spen Setting Selection Info
     * 
     * @params info SpenSettingSelectionInfo
     */
    public void setSpenSettingSelectionInfo(SpenSettingSelectionInfo info) {
        mSpenSettingSelectionInfo = info;
    }

    private final SpenSelectionChangeListener mSelectionListener = new SpenSelectionChangeListener() {

        @Override
        public void onChanged(SpenSettingSelectionInfo info) {
            setSpenSettingSelectionInfo(info);
            mSelectionSettingView.setVisibility(SpenSurfaceView.GONE);
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "SpenSelectionChangeListener, info:" + info.type);
            }
        }
    };


    /**
     * Get Spen Setting Selection Info
     * 
     * @return mSpenSettingSelectionInfo SpenSettingSelectionInfo
     */

    public SpenSettingSelectionInfo getSpenSettingSelectionInfo() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside getSpenSettingSelectionInfo");
        }
        if (mSpenSettingSelectionInfo == null) {
            mSpenSettingSelectionInfo = new SpenSettingSelectionInfo();
            mSpenSettingSelectionInfo.type = SpenSettingSelectionInfo.TYPE_LASSO;
        }
        return mSpenSettingSelectionInfo;
    }

    private final SpenControlListener mControlListener = new SpenControlListener() {
        @Override
        public void onRotationChanged(float arg0, SpenObjectBase arg1) {
        }

        @Override
        public void onRectChanged(RectF arg0, SpenObjectBase arg1) {
        }

        @Override
        public void onObjectChanged(ArrayList<SpenObjectBase> arg0) {
        }

        @Override
        public boolean onMenuSelected(ArrayList<SpenObjectBase> selectedList,
                int itemId) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG,
                        "Inside mControlListener onMenuSelected, itemId is :"
                                + itemId);
            }
            switch (itemId) {

                case Utils.CONTEXT_MENU_DELETE_ID :
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "Item Id is Delete case");
                    }
                    for (SpenObjectBase obj : selectedList) {
                        mSpenPageDoc.removeObject(obj);
                    }
                    mSpenSurfaceView.closeControl();
                    mSpenSurfaceView.update();
                    break;

                case Utils.CONTEXT_MENU_TEXT_ID :
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "Item Id is for text recognition");
                    }
                    if (selectedList.size() > 0 && !mIsProcessingRecognition) {
                        boolean result = processTextReconition(selectedList);
                        return result;
                    }
                    break;

                case Utils.CONTEXT_MENU_SHAPE_ID :
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "item Id is for shape recognition");
                    }
                    if (selectedList.size() > 0 && !mIsProcessingRecognition) {
                        boolean result = processShapeRecognition(selectedList);
                        return result;
                    }
                    break;

                case Utils.CONTEXT_MENU_CUT_ID :
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "Item Id is Cut");
                    }
                    mClipboard = new ArrayList<SpenObjectBase>();
                    for (int i = 0; i < mSpenPageDoc.getSelectedObjectCount(); i++) {
                        SpenObjectBase obj = mSpenPageDoc.getSelectedObject()
                                .get(i);

                        switch (obj.getType()) {
                            case SpenObjectBase.TYPE_STROKE :
                                SpenObjectBase stroke = new SpenObjectStroke();
                                stroke.copy(obj);
                                mClipboard.add(stroke);
                                break;

                            case SpenObjectBase.TYPE_TEXT_BOX :
                                SpenObjectTextBox textBox = new SpenObjectTextBox();
                                textBox.copy(obj);
                                mClipboard.add(textBox);
                                break;

                            case SpenObjectBase.TYPE_IMAGE :
                                SpenObjectBase image = new SpenObjectImage();
                                image.copy(obj);
                                mClipboard.add(image);
                                break;

                            case SpenObjectBase.TYPE_CONTAINER :
                                SpenObjectBase container = new SpenObjectContainer();
                                container.copy(obj);
                                mClipboard.add(container);
                                break;

                            default :
                                break;
                        }
                    }
                    if (mClipboard.size() != 0) {
                        mSpenPageDoc.removeSelectedObject();
                        mSpenSurfaceView.closeControl();
                        mSpenSurfaceView.update();
                    }
                    break;

                case Utils.CONTEXT_MENU_COPY_ID :
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "Item Id is Copy");
                    }
                    mClipboard = new ArrayList<SpenObjectBase>();
                    for (int i = 0; i < mSpenPageDoc.getSelectedObjectCount(); i++) {
                        SpenObjectBase tmp = mSpenPageDoc.getSelectedObject()
                                .get(i);
                        switch (tmp.getType()) {
                            case SpenObjectBase.TYPE_STROKE :
                                SpenObjectBase stroke = new SpenObjectStroke();
                                stroke.copy(tmp);
                                mClipboard.add(stroke);
                                break;
                            case SpenObjectBase.TYPE_TEXT_BOX :
                                SpenObjectTextBox textBox = new SpenObjectTextBox();
                                textBox.copy(tmp);
                                mClipboard.add(textBox);
                                break;
                            case SpenObjectBase.TYPE_IMAGE :
                                SpenObjectBase image = new SpenObjectImage();
                                image.copy(tmp);
                                mClipboard.add(image);
                                break;
                            case SpenObjectBase.TYPE_CONTAINER :
                                SpenObjectBase container = new SpenObjectContainer();
                                container.copy(tmp);
                                mClipboard.add(container);
                                break;
                            default :
                                break;
                        }
                    }
                    if (mClipboard.size() != 0) {
                        mSpenSurfaceView.closeControl();
                    }
                    break;

                default :
                    break;
            }
            return true;
        }
        private boolean processShapeRecognition(
                ArrayList<SpenObjectBase> selectedList) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG,
                        "Inside processShapeRecognition, slected list size is :"
                                + selectedList.size());
            }
            if (selectedList.size() > 0 && !mIsProcessingRecognition) {

                ArrayList<SpenObjectBase> inputList = new ArrayList<SpenObjectBase>();
                for (int i = 0; i < selectedList.size(); i++) {
                    if (selectedList.get(i).getType() == SpenObjectBase.TYPE_STROKE) {
                        inputList.add(selectedList.get(i));
                    }
                }

                if (inputList.size() <= 0) {
                    return false;
                }
                mIsProcessingRecognition = true;
                try {
                    mShapeRecognition.request(inputList);
                } catch (IllegalStateException e) {
                    Log.d(TAG,
                            "SpenShapeRecognition is not loaded: "
                                    + e.getMessage());
                    e.printStackTrace();
                    SpenException.sendPluginResult(
                            SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                            mContextParams.getCallbackContext());
                    return false;
                } catch (Exception e) {
                    Log.d(TAG,
                            "SpenShapeRecognition is not loaded: "
                                    + e.getMessage());
                    e.printStackTrace();
                    SpenException.sendPluginResult(
                            SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                            mContextParams.getCallbackContext());
                    return false;
                }
                return true;
            }
            return true;
        }

        private boolean processTextReconition(ArrayList<SpenObjectBase> list) {
            // List the selected strokes and send the list as a request.
            mSelectedList = list;
            mInputList = new ArrayList<SpenObjectBase>();
            mStrokeTimestamp = -1;
            for (SpenObjectBase obj : mSelectedList) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "run: type = " + obj.getType());
                }
                parse(mInputList, obj);
            }

            if (mInputList.size() <= 0) {
                return false;
            }
            mIsProcessingRecognition = true;
            try {
                mTextRecognition.request(mInputList);
            } catch (IllegalStateException e) {
                Log.d(TAG,
                        "SpenTextRecognition is not loaded: " + e.getMessage());
                e.printStackTrace();
                SpenException.sendPluginResult(
                        SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                        mContextParams.getCallbackContext());
                return false;
            } catch (Exception e) {
                Log.d(TAG,
                        "SpenTextRecognition engine not loaded: "
                                + e.getMessage());
                e.printStackTrace();
                SpenException.sendPluginResult(
                        SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                        mContextParams.getCallbackContext());
                return false;
            }
            return true;
        }

        private void parse(List<SpenObjectBase> inputList, SpenObjectBase obj) {
            if (obj.getType() == SpenObjectBase.TYPE_STROKE) {
                SpenObjectStroke stroke = (SpenObjectStroke) obj;
                int[] timestamp = stroke.getTimeStamps();

                if (mStrokeTimestamp == -1 || timestamp[0] < mStrokeTimestamp) {
                    mStrokeColor = stroke.getColor();
                    mStrokeTimestamp = timestamp[0];
                }

                if (!stroke.getPenName().equals("Eraser")) {
                    inputList.add(obj);
                }
            } else if (obj.getType() == SpenObjectBase.TYPE_CONTAINER) {

                ArrayList<SpenObjectBase> list = ((SpenObjectContainer) obj)
                        .getObjectList();

                if (list != null) {
                    for (SpenObjectBase objInContainer : list) {
                        if (objInContainer.getType() == SpenObjectBase.TYPE_STROKE) {
                            if (!((SpenObjectStroke) objInContainer)
                                    .getPenName().equals("eraser")) {
                                inputList.add(objInContainer);
                            }
                        } else if (objInContainer.getType() == SpenObjectBase.TYPE_CONTAINER) {
                            parse(inputList, objInContainer);
                        }
                    }
                }
            }
        }

        @Override
        public boolean onCreated(ArrayList<SpenObjectBase> objectList,
                ArrayList<Rect> relativeRectList,
                ArrayList<SpenContextMenuItemInfo> menu,
                ArrayList<Integer> styleList, int pressType, PointF point) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Inside mControlListener onCreated, presstype is :"
                        + pressType);
            }
            if (mOptions.getSurfaceType() == Utils.SURFACE_INLINE) {
                startScrollDetection();
            }
            menu.add(new SpenContextMenuItemInfo(Utils.CONTEXT_MENU_DELETE_ID,
                    "Delete", true));

            if ((mOptions.getsPenFlags() & Utils.FLAG_TEXT_RECOGNITION) == Utils.FLAG_TEXT_RECOGNITION)
                menu.add(new SpenContextMenuItemInfo(
                        Utils.CONTEXT_MENU_TEXT_ID, "To Text", true));

            if ((mOptions.getsPenFlags() & Utils.FLAG_SHAPE_RECOGNITION) == Utils.FLAG_SHAPE_RECOGNITION)
                menu.add(new SpenContextMenuItemInfo(
                        Utils.CONTEXT_MENU_SHAPE_ID, "To Shape", true));
            if ((mOptions.getsPenFlags() & Utils.FLAG_SELECTION) == Utils.FLAG_SELECTION) {
                menu.add(new SpenContextMenuItemInfo(Utils.CONTEXT_MENU_CUT_ID,
                        "Cut", true));
                menu.add(new SpenContextMenuItemInfo(
                        Utils.CONTEXT_MENU_COPY_ID, "Copy", true));
            }

            return true;
        }

        @Override
        public boolean onClosed(ArrayList<SpenObjectBase> arg0) {
            return false;
        }
    };

    private OnClickListener mActionButtonsOnClickListener = new OnClickListener() {

        HistoryUpdateInfo[] userData;
        @Override
        public void onClick(View v) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Inside mActionButtonsOnClickListener onClick");
            }
            Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                    .getActivity();
            closeSettings();
            mSpenSurfaceView.closeControl();
            if (mPasteContextMenu != null) {
                mPasteContextMenu.close();
            }
            if (v == mButtonPen) {
                boolean isAdvancedPen = (mOptions.getsPenFlags() & Utils.FLAG_PEN_SETTINGS) == Utils.FLAG_PEN_SETTINGS;
                if (isAdvancedPen) {
                    if (mSpenSurfaceView.getToolTypeAction(Utils.mToolTypeSpen) == SpenSurfaceView.ACTION_STROKE) {
                        if (mSpenSettingPenLayout.isShown()) {
                            mSpenSettingPenLayout.setVisibility(View.GONE);
                        } else {
                            mSpenSettingPenLayout
                                    .setViewMode(SpenSettingPenLayout.VIEW_MODE_NORMAL);
                            mSpenSettingPenLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                                SpenSurfaceView.ACTION_STROKE);
                        if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                            mSpenSurfaceView.setToolTypeAction(
                                    Utils.mToolTypeFinger,
                                    SpenSurfaceView.ACTION_STROKE);
                        }
                    }
                } else {
                    if (mSpenSurfaceView.getToolTypeAction(Utils.mToolTypeSpen) == SpenSurfaceView.ACTION_STROKE) {
                        if (mBasicColorView.isShown()) {
                            mBasicColorView.setVisibility(View.GONE);
                        } else {
                            mBasicColorView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                                SpenSurfaceView.ACTION_STROKE);
                        if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                            mSpenSurfaceView.setToolTypeAction(
                                    Utils.mToolTypeFinger,
                                    SpenSurfaceView.ACTION_STROKE);
                        }
                    }
                }
            } else if (v == mButtonColor1) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_BLACK);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor2) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_GREEN);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor3) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_BLUE);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor4) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_PURPLE);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor5) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_PINK);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor6) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_BROWN);
                mBasicColorView.setVisibility(View.GONE);
            } else if (v == mButtonColor7) {
                mSpenSurface.setColorInfo(Utils.PEN_COLOR_ORANGE);
                mBasicColorView.setVisibility(View.GONE);
            }
            if (v == mButtonEraser) {
                if (mSpenSurfaceView.getToolTypeAction(Utils.mToolTypeSpen) == SpenSurfaceView.ACTION_STROKE_REMOVER) {
                    if (mButtonClearAll.isShown()) {
                        mButtonClearAll.setVisibility(View.GONE);
                    } else {
                        mButtonClearAll.setVisibility(View.VISIBLE);
                    }
                } else {
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                            SpenSurfaceView.ACTION_STROKE_REMOVER);
                    if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                        mSpenSurfaceView.setToolTypeAction(
                                Utils.mToolTypeFinger,
                                SpenSurfaceView.ACTION_STROKE_REMOVER);
                    }
                }
            }
            if (v == mButtonClearAll) {
                mSpenPageDoc.removeAllObject();
                mSpenSurfaceView.update();
                mButtonClearAll.setVisibility(View.GONE);
            }
            if (v == mButtonUndo) {
                userData = mSpenPageDoc.undo();
                mSpenSurfaceView.updateUndo(userData);
            }
            if (v == mButtonRedo) {
                userData = mSpenPageDoc.redo();
                mSpenSurfaceView.updateRedo(userData);
            }

            if (v == mButtonDone) {
                mSpenSurfaceView.closeControl();
                writeSurfaceDataToClient();
                clearDataAndDismissDialog();
            }

            if (v == mButtonCancel) {
                showDiscardDialog();
            }
            if (v == mpenAndFinger) {
                if (isSpenAndFinger) {
                    int resid = activityRes.getIdentifier("ic_pen_only",
                            "drawable", activity.getPackageName());
                    mpenAndFinger.setImageResource(resid);
                    isSpenAndFinger = false;
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                            SpenSurfaceView.ACTION_NONE);
                } else {
                    int resid = activityRes.getIdentifier("ic_pen_finger",
                            "drawable", activity.getPackageName());
                    mpenAndFinger.setImageResource(resid);
                    isSpenAndFinger = true;
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                            mSpenSurfaceView
                                    .getToolTypeAction(Utils.mToolTypeSpen));
                }
            }

            if (v == mButtonEdit) {

                mRelativeLayout.removeView(mButtonEdit);
                showSurfaceAndTrayBar();
                mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                        SpenSurfaceView.ACTION_STROKE);
                if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                            SpenSurfaceView.ACTION_STROKE);
                }
                mRelativeLayout.setBackgroundColor(mOptions.getColor());
            }

            if (v == mButtonBg) {
                closeSettings();
                callGalleryForInputImage(Utils.REQUEST_CODE_SELECT_IMAGE_BACKGROUND);
            }

            if (v == mButtonAddPage) {
                mButtonAddPage.setEnabled(false);
                mButtonDeletePage.setEnabled(false);
                mSpenSurfaceView.closeControl();
                closeSettings();

                mSpenPageDoc = mSpenNoteDoc
                        .insertPage(((SPenPopupSurface) mSpenSurface)
                                .getCurrentPageindex() + 1);
                ((SPenPopupSurface) mSpenSurface)
                        .setCurrentPageindex(mSpenNoteDoc
                                .getPageIndexById(mSpenPageDoc.getId()));
                mSpenPageDoc.setBackgroundColor(mOptions.getColor());
                mSpenPageDoc.clearHistory();
                if (mButtonUndo != null) {
                    mButtonUndo.setEnabled(false);
                }
                if (mButtonRedo != null) {
                    mButtonRedo.setEnabled(false);
                }
                mSpenPageDoc.setHistoryListener(mHistoryListener);
                mSpenSurfaceView.setPageDoc(mSpenPageDoc,
                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_RIGHT,
                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_TYPE_SHADOW, 0);
                mCurrentPageNumberTextView.setText((mSpenNoteDoc
                        .getPageIndexById(mSpenPageDoc.getId()) + 1)
                        + "/"
                        + mSpenNoteDoc.getPageCount());
            }

            if (v == mButtonDeletePage) {
                mSpenSurfaceView.closeControl();
                closeSettings();

                if (mSpenNoteDoc.getPageCount() > 1) {
                    mButtonAddPage.setEnabled(false);
                    mButtonDeletePage.setEnabled(false);
                    int currentPageindex = ((SPenPopupSurface) mSpenSurface)
                            .getCurrentPageindex();
                    mSpenNoteDoc.removePage(currentPageindex);
                    if (currentPageindex == mSpenNoteDoc.getPageCount()) {
                        mSpenPageDoc = mSpenNoteDoc
                                .getPage(currentPageindex - 1);
                        mSpenSurfaceView
                                .setPageDoc(
                                        mSpenPageDoc,
                                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_LEFT,
                                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_TYPE_SHADOW,
                                        0);
                    } else {
                        mSpenPageDoc = mSpenNoteDoc.getPage(currentPageindex);
                        mSpenSurfaceView
                                .setPageDoc(
                                        mSpenPageDoc,
                                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_RIGHT,
                                        SpenSurfaceView.PAGE_TRANSITION_EFFECT_TYPE_SHADOW,
                                        0);
                    }
                    ((SPenPopupSurface) mSpenSurface)
                            .setCurrentPageindex(mSpenNoteDoc
                                    .getPageIndexById(mSpenPageDoc.getId()));

                    mCurrentPageNumberTextView.setText((mSpenNoteDoc
                            .getPageIndexById(mSpenPageDoc.getId()) + 1)
                            + "/"
                            + mSpenNoteDoc.getPageCount());
                    mSpenPageDoc.clearHistory();
                    if (mButtonUndo != null) {
                        mButtonUndo.setEnabled(false);
                    }
                    if (mButtonRedo != null) {
                        mButtonRedo.setEnabled(false);
                    }
                    mSpenPageDoc.setHistoryListener(mHistoryListener);
                }
            }

            if (v == mButtonPageNavigateLeft) {
                if (((SPenPopupSurface) mSpenSurface).getCurrentPageindex() > 0) {
                    mSpenSurfaceView.closeControl();
                    closeSettings();

                    mSpenPageDoc = mSpenNoteDoc
                            .getPage(((SPenPopupSurface) mSpenSurface)
                                    .getCurrentPageindex() - 1);
                    ((SPenPopupSurface) mSpenSurface)
                            .setCurrentPageindex(mSpenNoteDoc
                                    .getPageIndexById(mSpenPageDoc.getId()));
                    mSpenSurfaceView.setPageDoc(mSpenPageDoc,
                            SpenSurfaceView.PAGE_TRANSITION_EFFECT_LEFT,
                            SpenSurfaceView.PAGE_TRANSITION_EFFECT_TYPE_SHADOW,
                            0);
                    mCurrentPageNumberTextView.setText((mSpenNoteDoc
                            .getPageIndexById(mSpenPageDoc.getId()) + 1)
                            + "/"
                            + mSpenNoteDoc.getPageCount());
                    mSpenPageDoc.clearHistory();
                    if (mButtonUndo != null) {
                        mButtonUndo.setEnabled(false);
                    }
                    if (mButtonRedo != null) {
                        mButtonRedo.setEnabled(false);
                    }
                    mSpenPageDoc.setHistoryListener(mHistoryListener);
                }
            }

            if (v == mButtonPageNavigateRight) {
                if (((SPenPopupSurface) mSpenSurface).getCurrentPageindex() < (mSpenNoteDoc
                        .getPageCount() - 1)) {
                    mSpenSurfaceView.closeControl();
                    closeSettings();

                    mSpenPageDoc = mSpenNoteDoc
                            .getPage(((SPenPopupSurface) mSpenSurface)
                                    .getCurrentPageindex() + 1);
                    ((SPenPopupSurface) mSpenSurface)
                            .setCurrentPageindex(mSpenNoteDoc
                                    .getPageIndexById(mSpenPageDoc.getId()));
                    mSpenSurfaceView.setPageDoc(mSpenPageDoc,
                            SpenSurfaceView.PAGE_TRANSITION_EFFECT_RIGHT,
                            SpenSurfaceView.PAGE_TRANSITION_EFFECT_TYPE_SHADOW,
                            0);
                    mCurrentPageNumberTextView.setText((mSpenNoteDoc
                            .getPageIndexById(mSpenPageDoc.getId()) + 1)
                            + "/"
                            + mSpenNoteDoc.getPageCount());
                    mSpenPageDoc.clearHistory();
                    if (mButtonUndo != null) {
                        mButtonUndo.setEnabled(false);
                    }
                    if (mButtonRedo != null) {
                        mButtonRedo.setEnabled(false);
                    }
                    mSpenPageDoc.setHistoryListener(mHistoryListener);
                }
            }

            if (v == mButtonSelect) {
                mSpenSurfaceView.closeControl();
                if (mSpenSurfaceView.getToolTypeAction(Utils.mToolTypeSpen) == SpenSurfaceView.ACTION_SELECTION) {
                    // If SelectionSettingView is open, close it.
                    if (mSelectionSettingView.isShown()) {
                        mSelectionSettingView.setVisibility(View.GONE);
                        // If SelectionSettingView is not open, open it.
                    } else {
                        mSelectionSettingView.setVisibility(View.VISIBLE);
                    }
                } else {
                    // If Spen is not in selection mode, change it to selection
                    // mode.
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                            SpenSurfaceView.ACTION_SELECTION);
                    if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                        mSpenSurfaceView.setToolTypeAction(
                                Utils.mToolTypeFinger,
                                SpenSurfaceView.ACTION_SELECTION);
                    }
                }
            }
        }

        private void writeSurfaceDataToClient() {
            mClipboard = null;
            Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                    .getActivity();
            Context context = activity.getApplicationContext();
            if (mOptions.getReturnType() == (Utils.RETURN_TYPE_IMAGE_URI)
                    || mOptions.getReturnType() == (Utils.RETURN_TYPE_IMAGE_DATA)) {
                if (mOptions.getReturnType() == (Utils.RETURN_TYPE_IMAGE_URI)) {
                    String fileName = mOptions.getId().toString() + ".png";
                    Bitmap imgBitmap = mSpenSurfaceView
                            .captureCurrentView(true);
                    if (imgBitmap != null) {
                        FileOutputStream out = null;
                        try {
                            out = activity.openFileOutput(fileName,
                                    Context.MODE_PRIVATE);
                            imgBitmap.compress(CompressFormat.PNG, 100, out);
                            File mFile = new File(context.getFilesDir(),
                                    fileName);
                            Uri uri = Uri.fromFile(mFile);
                            PluginResult progressResult = new PluginResult(
                                    PluginResult.Status.OK, uri.toString());
                            progressResult.setKeepCallback(true);
                            mContextParams.getCallbackContext()
                                    .sendPluginResult(progressResult);
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "exception while compressing Image : "
                                    + e.getMessage());
                            SpenException.sendPluginResult(
                                    SpenExceptionType.FAILED_SAVING_IMAGE,
                                    mContextParams.getCallbackContext());
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                Log.e(TAG,
                                        "exception while closing outputstream : "
                                                + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        if (imgBitmap != null && !imgBitmap.isRecycled()) {
                            imgBitmap.recycle();
                            imgBitmap = null;
                        }
                    }
                } else {
                    Bitmap imgBitmap = mSpenSurfaceView
                            .captureCurrentView(true);
                    ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
                    try {
                        if (imgBitmap != null
                                && imgBitmap.compress(CompressFormat.JPEG, 40,
                                        jpeg_data)) {
                            byte[] code = jpeg_data.toByteArray();
                            String js_out = Base64.encodeToString(code,
                                    Base64.NO_WRAP);
                            PluginResult progressResult = new PluginResult(
                                    PluginResult.Status.OK, js_out);
                            progressResult.setKeepCallback(true);
                            mContextParams.getCallbackContext()
                                    .sendPluginResult(progressResult);

                            js_out = null;
                            code = null;
                        }
                    } catch (Exception e) {
                        Log.d(TAG,
                                "Error in Bitmap compress : " + e.getMessage());
                        e.printStackTrace();
                    }

                    if (imgBitmap != null && !imgBitmap.isRecycled()) {
                        imgBitmap.recycle();
                        imgBitmap = null;
                    }
                }

                if (mOptions.getSurfaceType() == Utils.SURFACE_INLINE) {

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            mOptions.getSurfacePosition().getWidth(), mOptions
                                    .getSurfacePosition().getHeight());
                    mRelativeLayout.setBackground(null);
                    if ((mOptions.getsPenFlags() & Utils.FLAG_EDIT) == Utils.FLAG_EDIT) {
                        params = new RelativeLayout.LayoutParams(
                                (int) (((float) 25) * activity.getResources()
                                        .getDisplayMetrics().density),
                                (int) (((float) 25) * activity.getResources()
                                        .getDisplayMetrics().density));
                        params.leftMargin = 5;
                        params.topMargin = 5;
                        mRelativeLayout.addView(mButtonEdit, params);
                    }
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                            SpenSurfaceView.ACTION_NONE);
                    if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                        mSpenSurfaceView.setToolTypeAction(
                                Utils.mToolTypeFinger,
                                SpenSurfaceView.ACTION_NONE);
                    }
                    hideSurfaceAndTrayBar();
                }
            } else {

                ArrayList<SpenObjectBase> selectedList = mSpenPageDoc
                        .getObjectList();
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Done button pressed, selectedList :"
                            + selectedList);
                }
                if (selectedList.size() > 0) {
                    boolean textInTextBox = false;
                    ArrayList<SpenObjectBase> inputList = new ArrayList<SpenObjectBase>();
                    for (int i = 0; i < selectedList.size(); i++) {
                        if (selectedList.get(i).getType() == SpenObjectBase.TYPE_STROKE) {
                            inputList.add(selectedList.get(i));
                        } else if (selectedList.get(i).getType() == SpenObjectBase.TYPE_TEXT_BOX) {
                            textInTextBox = true;
                        }
                    }

                    if (inputList.size() > 0 || textInTextBox) {
                        try {
                            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                                Log.d(TAG,
                                        "Giving input to text recognition, input:"
                                                + inputList);
                            }
                            mTextRecognition.request(inputList);
                        } catch (IllegalStateException e) {
                            Log.d(TAG, "SpenShapeRecognition is not loaded: "
                                    + e.getMessage());
                            e.printStackTrace();
                            SpenException.sendPluginResult(
                                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                                    mContextParams.getCallbackContext());
                        } catch (Exception e) {
                            Log.d(TAG,
                                    "error while recognizing the text: "
                                            + e.getMessage());
                            SpenException.sendPluginResult(
                                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                                    mContextParams.getCallbackContext());
                            e.printStackTrace();
                        }
                    }
                }
                isDone = true;
            }
        }

        private void clearDataAndDismissDialog() {
            if (mButtonUndo != null) {
                mButtonUndo.setEnabled(false);
            }
            if (mButtonRedo != null) {
                mButtonRedo.setEnabled(false);
            }
            mSpenPageDoc.clearHistory();
            if (mOptions.getSurfaceType() == Utils.SURFACE_POPUP) {
                ((SPenPopupSurface) mSpenSurface).dismissDialog();
            }
        }

        private void showDiscardDialog() {
            Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                    .getActivity();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            int resId = activityRes.getIdentifier("discard_change", "string",
                    activity.getPackageName());
            alertDialogBuilder.setTitle(activityRes.getString(resId))
                    .setPositiveButton(activityRes.getString(resId),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    userData = mSpenPageDoc.undoAll();
                                    mSpenSurfaceView.updateUndo(userData);
                                    writeSurfaceDataToClient();
                                    mSpenSurfaceView.closeControl();
                                    clearDataAndDismissDialog();
                                }
                            });

            resId = activityRes.getIdentifier("discard_change_msg", "string",
                    activity.getPackageName());
            alertDialogBuilder.setMessage(activityRes.getString(resId));
            resId = activityRes.getIdentifier("discard_cancel_btn", "string",
                    activity.getPackageName());
            alertDialogBuilder.setNegativeButton(activityRes.getString(resId),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            // setting the alert dialog for popup view
            // to handle orientation issues.
            if (mOptions.getSurfaceType() == Utils.SURFACE_POPUP) {
                mSpenSurface.setAlertDialog(alertDialog);
            }
        }
    };

    private OnScrollChangedListener onScrollListener = new OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            int newLocation;
            int[] location = new int[2];
            mRelativeLayout.getLocationOnScreen(location);
            DisplayMetrics metrics = mContextParams.getSpenCustomDrawPlugin().cordova
                    .getActivity().getApplicationContext().getResources()
                    .getDisplayMetrics();
            newLocation = (int) ((float) location[1] * metrics.density);
            if ((Math.abs(newLocation - mOriginalPosition) > mOptions
                    .getSurfacePosition().getHeight() / 2)) {
                if (mSpenSurfaceView != null) {
                    mSpenSurfaceView.closeControl();
                }
                if (mPasteContextMenu != null && mPasteContextMenu.isShowing()) {
                    mPasteContextMenu.close();
                }
                stopScrollDetection();
            }
        }
    };

    /**
     * start detecting the scroll of the web view for inline surface
     */
    private void startScrollDetection() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "inside startScrollDetection, scroll started");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        FrameLayout frameLayout = (FrameLayout) activity
                .findViewById(android.R.id.content);
        if (frameLayout != null) {
            ViewGroup viewGroup = (ViewGroup) ((ViewGroup) frameLayout
                    .getChildAt(0)).getChildAt(0);
            if (viewGroup != null) {
                viewGroup.getViewTreeObserver().addOnScrollChangedListener(
                        onScrollListener);
                int[] location = new int[2];
                mRelativeLayout.getLocationOnScreen(location);
                DisplayMetrics metrics = activity.getApplicationContext()
                        .getResources().getDisplayMetrics();
                mOriginalPosition = (int) ((float) location[1] * metrics.density);
            }
        }
    }

    /**
     * stops detection of the web view scroll for inilne surface
     */
    private void stopScrollDetection() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG,
                    "inside stopScrollDetection, scroll detection is stopped");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        FrameLayout frameLayout = (FrameLayout) activity
                .findViewById(android.R.id.content);
        if (frameLayout != null) {
            ViewGroup viewGroup = (ViewGroup) ((ViewGroup) frameLayout
                    .getChildAt(0)).getChildAt(0);
            if (viewGroup != null) {
                viewGroup.getViewTreeObserver().removeOnScrollChangedListener(
                        onScrollListener);
            }
        }
    }

    /**
     *  add traybar buttons and show surface and traybar.
     */
    private void showSurfaceAndTrayBar() {

        addTrayBarButtons();
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * To add tray button after edit option is selected in inline.
     */
    private void addTrayBarButtons() {

        if (mButtonDone != null) {
            mButtonDone.setVisibility(View.VISIBLE);
        }
        if (mButtonCancel != null) {
            mButtonCancel.setVisibility(View.VISIBLE);
        }
        if (mpenAndFinger != null) {
            mpenAndFinger.setVisibility(View.VISIBLE);
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_BACKGROUND) == Utils.FLAG_BACKGROUND) {
            if (mButtonBg != null) {
                mButtonBg.setVisibility(View.VISIBLE);
            }
        }
        if ((mOptions.getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
            if (mButtonUndo != null) {
                mButtonUndo.setVisibility(View.VISIBLE);
            }

            if (mButtonRedo != null) {
                mButtonRedo.setVisibility(View.VISIBLE);
            }
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_SELECTION) == Utils.FLAG_SELECTION) {
            if (mButtonSelect != null) {
                mButtonSelect.setVisibility(View.VISIBLE);
            }
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN
                || (mOptions.getsPenFlags() & Utils.FLAG_PEN_SETTINGS) == Utils.FLAG_PEN_SETTINGS) {
            if (mButtonPen != null) {
                mButtonPen.setVisibility(View.VISIBLE);
            }
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_ERASER) == Utils.FLAG_ERASER) {
            if (mButtonEraser != null) {
                mButtonEraser.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * To remove button from inline surface after save.
     */
    private void removeTrayBarButtons() {
        if (mButtonDone != null) {
            mButtonDone.setVisibility(View.GONE);
        }
        if (mButtonCancel != null) {
            mButtonCancel.setVisibility(View.GONE);
        }
        if (mButtonSelect != null) {
            mButtonSelect.setVisibility(View.GONE);
        }

        if (mpenAndFinger != null) {
            mpenAndFinger.setVisibility(View.GONE);
        }

        if (mButtonEraser != null) {
            mButtonEraser.setVisibility(View.GONE);
        }

        if (mButtonPen != null) {
            mButtonPen.setVisibility(View.GONE);
        }

        if (mButtonBg != null) {
            mButtonBg.setVisibility(View.GONE);
        }

        if (mButtonUndo != null) {
            mButtonUndo.setVisibility(View.GONE);
        }

        if (mButtonRedo != null) {
            mButtonRedo.setVisibility(View.GONE);
        }
    }

    private final HistoryListener mHistoryListener = new HistoryListener() {
        @Override
        public void onCommit(SpenPageDoc page) {
        }

        @Override
        public void onUndoable(SpenPageDoc page, boolean undoable) {
            // Enable or disable the button according to the availability of
            // undo.
            if (mButtonUndo != null) {
                mButtonUndo.setEnabled(undoable);
            }
        }

        @Override
        public void onRedoable(SpenPageDoc page, boolean redoable) {
            // Enable or disable the button according to the availability of
            // redo.
            if (mButtonRedo != null) {
                mButtonRedo.setEnabled(redoable);
            }
        }
    };

    private SpenColorPickerListener mColorPickerListener = new SpenColorPickerListener() {
        @Override
        public void onChanged(int color, int x, int y) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Inside mColorPickerListener onChanged, color is :"
                        + color + ", x is : " + x + ",  y is : " + y);
            }

            // Set the color from the Color Picker to the setting view.
            if (mSpenSettingPenLayout != null) {
                SpenSettingPenInfo penInfo = mSpenSettingPenLayout.getInfo();
                penInfo.color = color;
                mSpenSettingPenLayout.setInfo(penInfo);
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "color change end");
                }
            }
        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Drawable mDrawable = new ColorDrawable(
                        Color.argb(80, 0, 0, 256));
                v.setBackground(mDrawable);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v != mButtonClearAll && v != mButtonEdit) {
                    v.setBackground(null);
                } else {
                    if (v == mButtonClearAll) {
                        v.setBackgroundResource(activityRes.getIdentifier(
                                "quickmemo_bubble", "drawable", mContextParams
                                        .getSpenCustomDrawPlugin().cordova
                                        .getActivity().getPackageName()));
                    } else {
                        v.setBackgroundColor(mOptions.getColor());
                    }
                }
            }
            return false;
        }
    };

    private OnTouchListener mSpenSurfaceOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            if (mSpenSettingPenLayout.isShown()) {
                mSpenSettingPenLayout.setVisibility(View.GONE);
            }
            if (mSelectionSettingView.isShown()) {
                mSelectionSettingView.setVisibility(SpenSurfaceView.GONE);
            }
            if (mButtonClearAll.isShown()) {
                mButtonClearAll.setVisibility(View.GONE);
            }
            if (mBasicColorView != null && mBasicColorView.isShown()) {
                mBasicColorView.setVisibility(View.GONE);
            }
            if ((event.getAction() == MotionEvent.ACTION_DOWN)
                    && mPasteContextMenu != null
                    && mPasteContextMenu.isShowing()) {
                mPasteContextMenu.hide();
            }

            return false;
        }
    };
    /**
     * Close settings
     */
    private void closeSettings() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside closeSettings, closing settings layouts");
        }

        if (mSpenSettingPenLayout != null) {
            mSpenSettingPenLayout.setVisibility(SpenSurfaceView.GONE);
        }

        if (mSelectionSettingView != null) {
            mSelectionSettingView.setVisibility(SpenSurfaceView.GONE);
        }

        if (mButtonClearAll != null) {
            mButtonClearAll.setVisibility(View.GONE);
        }
        if (mBasicColorView != null) {
            mBasicColorView.setVisibility(View.GONE);
        }
    }
    /**
     * Hise Surafe and TrayBar
     */
    private void hideSurfaceAndTrayBar() {
        removeTrayBarButtons();
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.setVisibility(View.GONE);
        }
    }

    /**
     * To Intialize all listeners for TrayBar Buttons  
     */
    void initializeListenersForTrayButtons() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside initializeListenersForTrayButtons, "
                    + "initializing listeners for traybar buttons");
        }
        if (mButtonPen != null) {
            mButtonPen.setOnClickListener(mActionButtonsOnClickListener);
            mButtonPen.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonEraser != null) {
            mButtonEraser.setOnClickListener(mActionButtonsOnClickListener);
            mButtonEraser.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonUndo != null) {
            mButtonUndo.setOnClickListener(mActionButtonsOnClickListener);
            mButtonUndo.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonRedo != null) {
            mButtonRedo.setOnClickListener(mActionButtonsOnClickListener);
            mButtonRedo.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonBg != null
                && (mOptions.getsPenFlags() & Utils.FLAG_BACKGROUND) == Utils.FLAG_BACKGROUND) {
            mButtonBg.setOnClickListener(mActionButtonsOnClickListener);
            mButtonBg.setOnTouchListener(mOnTouchListener);
        }

        mButtonDone.setOnClickListener(mActionButtonsOnClickListener);
        mButtonDone.setOnTouchListener(mOnTouchListener);
        mButtonCancel.setOnClickListener(mActionButtonsOnClickListener);
        mButtonCancel.setOnTouchListener(mOnTouchListener);

        if (mpenAndFinger != null) {
            mpenAndFinger.setOnClickListener(mActionButtonsOnClickListener);
            mpenAndFinger.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonSelect != null
                && (mOptions.getsPenFlags() & Utils.FLAG_SELECTION) == Utils.FLAG_SELECTION) {
            mButtonSelect.setOnClickListener(mActionButtonsOnClickListener);
            mButtonSelect.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonAddPage != null
                && (mOptions.getsPenFlags() & Utils.FLAG_ADD_PAGE) == Utils.FLAG_ADD_PAGE) {
            mButtonAddPage.setOnClickListener(mActionButtonsOnClickListener);
            mButtonAddPage.setOnTouchListener(mOnTouchListener);

            mButtonPageNavigateLeft
                    .setOnClickListener(mActionButtonsOnClickListener);
            mButtonPageNavigateLeft.setOnTouchListener(mOnTouchListener);

            mButtonPageNavigateRight
                    .setOnClickListener(mActionButtonsOnClickListener);
            mButtonPageNavigateRight.setOnTouchListener(mOnTouchListener);

            mButtonDeletePage.setOnClickListener(mActionButtonsOnClickListener);
            mButtonDeletePage.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonEdit != null
                && (mOptions.getsPenFlags() & Utils.FLAG_EDIT) == Utils.FLAG_EDIT) {
            mButtonEdit.setOnClickListener(mActionButtonsOnClickListener);
            mButtonEdit.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor1 != null) {
            mButtonColor1.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor1.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor2 != null) {
            mButtonColor2.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor2.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor3 != null) {
            mButtonColor3.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor3.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor4 != null) {
            mButtonColor4.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor4.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor5 != null) {
            mButtonColor5.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor5.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor6 != null) {
            mButtonColor6.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor6.setOnTouchListener(mOnTouchListener);
        }

        if (mButtonColor7 != null) {
            mButtonColor7.setOnClickListener(mActionButtonsOnClickListener);
            mButtonColor7.setOnTouchListener(mOnTouchListener);
        }
    }

    /**
     * Get Spen Setting Layout
     * 
     * @params mSpenSettingPenLayout SpenSettingPenLayout
     */
    public SpenSettingPenLayout getSpenSettingPenLayout() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside getSpenSettingPenLayout");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        Context context = activity.getApplicationContext();
        if (mSpenSettingPenLayout == null) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "SpenSettingPenLayout was null, creating new object");
            }

            if (android.os.Build.VERSION.SDK_INT > 19) {
                mSpenSettingPenLayout = new SpenSettingPenLayout(activity, "",
                        mRelativeLayout);
            } else {
                mSpenSettingPenLayout = new SpenSettingPenLayout(context, "",
                        mRelativeLayout);
            }

            if (mSpenSettingPenLayout == null) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Cannot create new PenSettingView.");
                }
            }
        }

        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "getSpenSettingPenLayout(), mPenSettingView:"
                    + mSpenSettingPenLayout.getInfo().name);
        }

        return mSpenSettingPenLayout;
    }

    /**
     * Set Spen Setting Layout
     */
    private void setSpenSettingPenLayout() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside setSpenSettingPenLayout");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.addView(mSpenSettingPenLayout);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(
                RelativeLayout.BELOW,
                activityRes.getIdentifier("spen_traybar_layout", "id",
                        activity.getPackageName()));
        mRelativeLayout.addView(linearLayout, p);
        mSpenSettingPenLayout.setCanvasView(mSpenSurfaceView);
        mSpenSettingPenLayout.setVisibility(View.GONE);
        mSpenSettingPenLayout.setInfo(mPenInfo);
    }

    /**
     * Set Surface Listeners
    */
    void setSurfaceListeners() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside setSurfaceListeners, setting surface listeners");
        }
        // set surface listeners
        mSpenSurfaceView.setColorPickerListener(mColorPickerListener);
        mSpenSurfaceView.setOnTouchListener(mSpenSurfaceOnTouchListener);

        // undo-redo history listener for page doc
        mSpenPageDoc.setHistoryListener(mHistoryListener);
        mSpenSurfaceView.setControlListener(mControlListener);
        mSpenSurfaceView.setPageEffectListener(new SpenPageEffectListener() {

            @Override
            public void onFinish() {
                mButtonAddPage.setEnabled(true);
                mButtonDeletePage.setEnabled(true);
            }
        });

        createPasteConextMenu();
        mSpenSurfaceView.setLongPressListener(new SpenLongPressListener() {

            @Override
            public void onLongPressed(MotionEvent event) {
                mSpenSurfaceView.closeControl();
                RectF rectfRelative = new RectF(event.getX(), event.getY(),
                        event.getX() + 1, event.getY() + 1);
                RectF rectfAbsolute = convertRelative(rectfRelative);
                offsetX = rectfAbsolute.left;
                offsetY = rectfAbsolute.top;

                ArrayList<SpenObjectBase> objectList = mSpenPageDoc
                        .findObjectAtPosition(SpenPageDoc.FIND_TYPE_ALL,
                                offsetX, offsetY);
                if (mClipboard != null && mClipboard.size() != 0
                        && objectList.size() == 0) {
                    mPasteContextMenu.setRect(new Rect(
                            (int) rectfRelative.left, (int) rectfRelative.top,
                            (int) rectfRelative.right,
                            (int) rectfRelative.bottom));
                    mPasteContextMenu.show();
                    if (mOptions.getSurfaceType() == Utils.SURFACE_INLINE) {
                        startScrollDetection();
                    }
                }
                mSpenSurfaceView.cancelStroke();

            }
        });

    }
    /**
     * Create Paste Context Menu
    */
    private void createPasteConextMenu() {
        Context context = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity().getApplicationContext();
        ArrayList<SpenContextMenuItemInfo> pasteMenu = new ArrayList<SpenContextMenuItemInfo>();
        pasteMenu.add(new SpenContextMenuItemInfo(Utils.CONTEXT_MENU_PASTE_ID,
                "Paste", true));
        mPasteContextMenu = new SpenContextMenu(context, mRelativeLayout,
                pasteMenu, new SpenContextMenu.ContextMenuListener() {
                    @Override
                    public void onSelected(int itemId) {
                        try {
                            mSpenSurfaceView.closeControl();
                            pasteObjects();

                            mPasteContextMenu.close();
                            mSpenSurfaceView.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Set Shape Recognition
    */
    private void setShapeRecognition() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG,
                    "Inside setShapeRecognition, initializing shape Recognition");
        }
        Context context = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity().getApplicationContext();
        mSpenShapeRecognitionManager = new SpenShapeRecognitionManager(context);

        List<SpenRecognitionInfo> shapeRecognitionList = mSpenShapeRecognitionManager
                .getInfoList(SpenObjectBase.TYPE_STROKE,
                        SpenObjectBase.TYPE_CONTAINER);

        try {
            if (shapeRecognitionList.size() > 0) {
                for (SpenRecognitionInfo info : shapeRecognitionList) {
                    if (info.name.equalsIgnoreCase("SpenShape")) {
                        mShapeRecognition = mSpenShapeRecognitionManager
                                .createRecognition(info);
                        break;
                    }
                }
            }

            mShapeRecognition.setResultListener(new ResultListener() {
                @Override
                public void onResult(List<SpenObjectBase> input,
                        List<SpenObjectBase> output) {

                    // Remove the selected objects and append the recognized
                    // objects to pageDoc.
                    for (SpenObjectBase obj : input) {
                        mSpenPageDoc.removeObject(obj);
                    }

                    if (output == null) {
                        mIsProcessingRecognition = false;
                        mSpenSurfaceView.closeControl();
                        mSpenSurfaceView.update();
                        return;
                    }

                    handleResult(output);
                    mIsProcessingRecognition = false;
                    mSpenSurfaceView.closeControl();
                    mSpenSurfaceView.update();
                }
            });
        } catch (ClassNotFoundException e) {
            Log.d(TAG,
                    "SpenShapeRecognitionManager class not found: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        } catch (InstantiationException e) {
            Log.d(TAG,
                    "Failed to access the SpenShapeRecognitionManager constructor: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        } catch (IllegalAccessException e) {
            Log.d(TAG,
                    "Failed to access the SpenShapeRecognitionManager field or method: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        } catch (SpenCreationFailureException e) {
            Log.d(TAG, "exception while creating spen: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        } catch (IllegalStateException e) {
            Log.d(TAG, "SpenShapeRecognition is not loaded: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        } catch (Exception e) {
            Log.d(TAG,
                    "SpenShapeRecognitionManager engine not loaded: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_SHAPE,
                    mContextParams.getCallbackContext());
        }
    }

    /**
     * Set Text Recognition
    */
    private void setTextRecognition() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG,
                    "Inside setTextRecognition, intializing text recognition");
        }
        Context context = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity().getApplicationContext();
        mSpenTextRecognitionManager = new SpenTextRecognitionManager(context);

        List<SpenRecognitionInfo> textRecognitionList = mSpenTextRecognitionManager
                .getInfoList(SpenObjectBase.TYPE_STROKE,
                        SpenObjectBase.TYPE_CONTAINER);

        try {
            if (textRecognitionList.size() > 0) {
                for (SpenRecognitionInfo info : textRecognitionList) {
                    if (info.name.equalsIgnoreCase("SpenText")) {
						if (mTextRecognition==null){
                        mTextRecognition = mSpenTextRecognitionManager
                                .createRecognition(info);
						}
                        break;
                    }
                }
            }

            List<String> languageList = mTextRecognition.getSupportedLanguage();
            if (textRecognitionList.size() > 0) {
                for (String language : languageList) {
                    if (language.equalsIgnoreCase("eng")) {
                        mTextRecognition.setLanguage(language);
                        break;
                    }
                }
            }

            mTextRecognition.setResultListener(new ResultListener() {
                @Override
                public void onResult(List<SpenObjectBase> input,
                        List<SpenObjectBase> output) {

                    mIsProcessingRecognition = false;

                    if (isDone) {
                        if (mSpenNoteDoc != null) {
                            ArrayList<SpenObjectBase> selectedList = mSpenPageDoc
                                    .getObjectList();
                            int j = 0;
                            for (int i = 0; i < selectedList.size(); i++) {
                                if (selectedList.get(i).getType() == SpenObjectBase.TYPE_TEXT_BOX) {
                                    if (output == null) {
                                        output = new ArrayList<SpenObjectBase>();
                                    }
                                    output.add(j, selectedList.get(i));
                                    j++;
                                }
                            }
                        }

                        if (output == null) {
                            return;
                        }

                        StringBuilder js_out = new StringBuilder();

                        for (SpenObjectBase obj : output) {
                            if (obj instanceof SpenObjectTextBox) {
                                SpenObjectTextBox mSpenObjectTextBox = (SpenObjectTextBox) obj;
                                js_out.append(" "
                                        + mSpenObjectTextBox.getText());
                            }
                        }
                        String js_out2 = js_out.toString();
                        if (js_out2.contains("\n")) {
                            js_out2 = js_out2.replace("\n", " ");
                        }
                        js_out2 = js_out2.trim();
                        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                            Log.d(TAG, "getting text form output, text:"
                                    + js_out);
                        }

                        PluginResult progressResult = new PluginResult(
                                PluginResult.Status.OK, js_out2);
                        progressResult.setKeepCallback(true);
                        mContextParams.getCallbackContext().sendPluginResult(
                                progressResult);
                        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                            Log.d(TAG,
                                    "Returned Text for return type recognitzed text is: "
                                            + js_out2);
                        }
                        isDone = false;
                    } else {

                        if (output != null) {
                            handleResult((SpenObjectTextBox) output.get(0));
                        }
                        mSpenSurfaceView.closeControl();
                        mSpenSurfaceView.update();
                    }
                }
            });
        } catch (ClassNotFoundException e) {
            Log.d(TAG,
                    "SpenTextRecognitionManager class not found: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        } catch (InstantiationException e) {
            Log.d(TAG,
                    "Failed to access the SpenTextRecognitionManager constructor: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        } catch (IllegalAccessException e) {
            Log.d(TAG,
                    "Failed to access the SpenTextRecognitionManager field or method: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        } catch (SpenCreationFailureException e) {
            Log.d(TAG, "exception while creating spen: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        } catch (IllegalStateException e) {
            Log.d(TAG, "SpenTextRecognition is not loaded: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        } catch (Exception e) {
            Log.d(TAG,
                    "SpenTextRecognitionManager engine not loaded: "
                            + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_RECOGNIZE_TEXT,
                    mContextParams.getCallbackContext());
        }
    }

    /**
     * Handle Text Recognition Result
     * 
     * @params text SpenObjectTextBox
     */
    private void handleResult(SpenObjectTextBox text) {

        makeText(text, mInputList);
        text.parseHyperText();
        mSpenPageDoc.appendObject(text);
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "handleResult: text recognition = " + text.getText());
        }

        clear();
        if (mSpenPageDoc.getObjectList().contains(text)) {
            mSpenPageDoc.selectObject(text);
        }
        mSpenSurfaceView.update();
    }

    /**
     * Make Text
     * 
     * @params text SpenObjectTextBox
     * @params mObjectList ArrayList<SpenObjectBase>
     */
    private void makeText(SpenObjectTextBox text,
            ArrayList<SpenObjectBase> mObjectList) {
        RectF r = new RectF();
        int minWidth, minHeight = 0;
        float cx, cy;

        if (text.hasExtraDataInt("cx of the cell")) {
            cx = text.getExtraDataInt("cx of the cell");
            cy = text.getExtraDataInt("cy of the cell");

        } else {
            for (SpenObjectBase obj : mObjectList) {
                r.union(obj.getRect());
            }

            cx = (r.left + r.right) / 2;
            cy = (r.top + r.bottom) / 2;
        }

        setTextInfo(text);

        RectF bound = getTextBounds(text);

        float halfWidth = bound.width() / 2;
        float halfHeight = bound.height() / 2;

        r.set(cx - halfWidth, cy - halfHeight, cx + halfWidth,
                cy + bound.height());

        text.setTextColor(mStrokeColor);
        text.setRect(r, true);

        SpenTextMeasure measure = new SpenTextMeasure();
        measure.setObjectText(text);
        minWidth = minHeight = measure.getMinHeight();
        r.bottom = r.top + minHeight;

        if (r.width() < minWidth) {
            r.right = r.left + minWidth;
        }

        text.setRect(r, true);
        text.setExtraDataInt("Type", 17);
    }
    
    /**
     * Set Text Info
     * 
     * @params text SpenObjectTextBox
     */
    private void setTextInfo(SpenObjectTextBox text) {

        text.setFontSize(50);
        text.setFont("Plain");
        text.setRect(new RectF(0, 0, 100, 100), true);
        text.setTextColor(mStrokeColor);
    }
    
    /**
     * Get Text Bounds
     * 
     * @params textBox SpenObjectTextBox
     * @return bound RectF
     */
    private RectF getTextBounds(SpenObjectTextBox textBox) {
        // This margin is needed so that the recognized text will be
        // displayed properly in line.
        final int MARGIN = 40;
        RectF bound = new RectF();
        SpenTextMeasure text = new SpenTextMeasure();
        String str = textBox.getText();
        int length = str.length();

        float width = 0;
        float maxWidth = 0;
        float height = 0;

        text.setObjectText(textBox);

        int i;
        for (i = 0; i < length; i++) {
            if (str.charAt(i) == '\n') {
                if (width > maxWidth) {
                    maxWidth = width;
                }
                width = 0;
            }

            if (text.getTextRect(i) != null) {
                width += text.getTextRect(i).width();
            }
        }

        if (width < 66) {
            width = 66;
        }

        if (width > maxWidth) {
            maxWidth = width;
        }

        textBox.setRect(new RectF(0, 0, maxWidth, 100), true);
        text.setObjectText(textBox);

        height = text.getHeight();

        bound.set(0, 0, maxWidth + MARGIN, height);
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "width = " + maxWidth + ", height = " + height);
        }
        return bound;
    }

    /**
     * Handle Result
     * 
     * @params outputList List<SpenObjectBase>
     */
    private void handleResult(List<SpenObjectBase> outputList) {

        ArrayList<SpenObjectBase> addObjectList = new ArrayList<SpenObjectBase>();

        if (outputList.size() > 0) {
            for (SpenObjectBase obj : outputList) {
                if (obj instanceof SpenObjectContainer) {
                    ArrayList<SpenObjectBase> itemList = ((SpenObjectContainer) obj)
                            .getObjectList();

                    if (itemList != null) {
                        for (SpenObjectBase item : itemList) {
                            mSpenPageDoc.appendObject(item);
                            addObjectList.add(item);
                        }
                    }
                }
            }
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG,
                        "handleResult: outputlist size = " + outputList.size());
            }

            if (mSpenPageDoc.getObjectList().containsAll(addObjectList)) {
                mSpenPageDoc.selectObject(addObjectList);
            }
            mSpenSurfaceView.update();
        }
    }
    
    /**
     * To clear SpenPage Doc and Lists
     * 
     */
    private void clear() {
        if (mSelectedList != null) {
            for (SpenObjectBase obj : mSelectedList) {
                mSpenPageDoc.removeObject(obj);
            }
            mSelectedList.clear();
        }
        if (mInputList != null) {
            mInputList.clear();
        }
    }

    /**
     * Change Button on TrayBar after relaunch
     *
     * @params options SpenTrayBarOptions
     */
    public void changeButtonsOnSurfaceView(SpenTrayBarOptions options) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside changeButtonsOnSurfaceView after relaunch");
        }
        mOptions = options;
        addOptionalButtonsToSurfaceView();
        if (mButtonBg != null) {
            if ((mOptions.getsPenFlags() & Utils.FLAG_BACKGROUND) != Utils.FLAG_BACKGROUND) {
                mButtonBg.setVisibility(View.VISIBLE);
                mButtonBg.setVisibility(View.GONE);
            }
        }
        if (mButtonDone != null) {
            mButtonDone.setVisibility(View.VISIBLE);
        }
        if (mButtonCancel != null) {
            mButtonCancel.setVisibility(View.VISIBLE);
        }
        if (mpenAndFinger != null) {
            mpenAndFinger.setVisibility(View.VISIBLE);
        }

        if (mOptions.getSurfaceType() == Utils.SURFACE_INLINE) {
            if (mButtonPen != null) {
                if ((mOptions.getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN) {
                    mButtonPen.setVisibility(View.VISIBLE);
                } else {
                    mButtonPen.setVisibility(View.GONE);
                }
            }

            if (mButtonEraser != null) {
                if ((mOptions.getsPenFlags() & Utils.FLAG_ERASER) == Utils.FLAG_ERASER) {
                    mButtonEraser.setVisibility(View.VISIBLE);
                } else {
                    mButtonEraser.setVisibility(View.GONE);
                }
            }

            if ((mOptions.getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
                if (mButtonUndo != null) {
                    mButtonUndo.setVisibility(View.VISIBLE);
                }

                if (mButtonRedo != null) {
                    mButtonRedo.setVisibility(View.VISIBLE);
                }
            } else {
                if (mButtonUndo != null) {
                    mButtonUndo.setVisibility(View.GONE);
                }

                if (mButtonRedo != null) {
                    mButtonRedo.setVisibility(View.GONE);
                }
            }
        }

        if (mSpenSettingPenLayout != null) {
            if ((mBasicColorView != null)
                    && (mOptions.getsPenFlags() & Utils.FLAG_PEN_SETTINGS) == Utils.FLAG_PEN_SETTINGS) {
                mBasicColorView.setVisibility(View.GONE);
            }
            if (mSpenSurfaceView != null) {
                mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                        SpenSurfaceView.ACTION_STROKE);
                if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                            SpenSurfaceView.ACTION_STROKE);
                }
            }
        }

        if ((mBasicColorView != null)) {
            if ((mOptions.getsPenFlags() & Utils.FLAG_PEN_SETTINGS) == Utils.FLAG_PEN_SETTINGS) {
                mBasicColorView.setVisibility(View.GONE);
            }
            if (mSpenSurfaceView != null) {
                mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeSpen,
                        SpenSurfaceView.ACTION_STROKE);
                if (isSpenAndFinger || !mOptions.getIsFeatureEnabled()) {
                    mSpenSurfaceView.setToolTypeAction(Utils.mToolTypeFinger,
                            SpenSurfaceView.ACTION_STROKE);
                }
            }
        }

        if (mButtonSelect != null && mSelectionSettingView != null) {
            if ((mOptions.getsPenFlags() & Utils.FLAG_SELECTION) != Utils.FLAG_SELECTION) {
                mButtonSelect.setVisibility(View.GONE);
                mSelectionSettingView.setVisibility(View.GONE);
            }
        }

        if (mButtonDeletePage != null && mButtonPageNavigateLeft != null
                && mButtonPageNavigateRight != null
                && mCurrentPageNumberTextView != null && mButtonAddPage != null) {
            if ((mOptions.getsPenFlags() & Utils.FLAG_ADD_PAGE) != Utils.FLAG_ADD_PAGE) {
                mButtonDeletePage.setVisibility(View.GONE);
                mButtonPageNavigateLeft.setVisibility(View.GONE);
                mButtonPageNavigateRight.setVisibility(View.GONE);
                mCurrentPageNumberTextView.setVisibility(View.GONE);
                mButtonAddPage.setVisibility(View.GONE);
            }
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_TEXT_RECOGNITION) == Utils.FLAG_TEXT_RECOGNITION
                || (mOptions.getReturnType() == Utils.RETURN_TYPE_TEXT)) {
            setTextRecognition();
        }

        if ((mOptions.getsPenFlags() & Utils.FLAG_SHAPE_RECOGNITION) == Utils.FLAG_SHAPE_RECOGNITION) {
            setShapeRecognition();
        }

        if (mOptions.getSurfaceType() == Utils.SURFACE_INLINE
                && mRelativeLayout != null) {
            if (mButtonEdit != null && mButtonEdit.isShown()) {
                mRelativeLayout.removeView(mButtonEdit);
            }
        }
    }

    /**
     * Change Surface Color
     * 
     * @params mOptions2 SpenTrayBarOptions
     */
    public void changeSurfaceColor(SpenTrayBarOptions mOptions2) {
        // Change color for traybar
        if (mRelativeLayout != null) {
            mRelativeLayout.setBackgroundColor(mOptions2.getColor());
        }
        // change color for pages
        int PageCount = mSpenSurface.getSpenNoteDoc().getPageCount();
        for (int i = 0; i < PageCount; i++) {
            mSpenSurface.getSpenNoteDoc().getPage(i)
                    .setBackgroundColor(mOptions2.getColor());
            mSpenSurface.getSpenNoteDoc().getPage(i).clearHistory();
        }
        mSpenSurfaceView.update();
    }

    /**
     * 
     * Create TrayBar
     */
    public void createTrayBar() {
        initTrayBarButtons();
        addButtonsToSurfaceView();
        createSettingsLayout();
        setSurfaceListeners();
    }

    private RectF convertRelative(RectF srcRect) {
        RectF dstRect = srcRect;
        PointF pan = mSpenSurfaceView.getPan();
        float ratio = mSpenSurfaceView.getZoomRatio();
        if (pan != null) {
            dstRect.left = (srcRect.left - pan.x) / ratio + pan.x;
            dstRect.right = (srcRect.right - pan.x) / ratio + pan.x;
            dstRect.top = (srcRect.top - pan.y) / ratio + pan.y;
            dstRect.bottom = (srcRect.bottom - pan.y) / ratio + pan.y;
        }
        return dstRect;
    }

    private void pasteObjects() {
        mSpenNoteDoc.reviseObjectList(mClipboard);
        RectF boundRect = mClipboard.get(0).getRect();
        for (int i = 1; i < mClipboard.size(); i++) {
            RectF childRect = mClipboard.get(i).getRect();
            if (boundRect.left > childRect.left) {
                boundRect.left = childRect.left;
            }
            if (boundRect.right < childRect.right) {
                boundRect.right = childRect.right;
            }
            if (boundRect.top > childRect.top) {
                boundRect.top = childRect.top;
            }
            if (boundRect.bottom < childRect.bottom) {
                boundRect.bottom = childRect.bottom;
            }
        }
        PointF middlePoint = new PointF();
        middlePoint.x = (boundRect.left + boundRect.right) / 2f;
        middlePoint.y = (boundRect.top + boundRect.bottom) / 2f;

        float delX = mPasteContextMenu.getRect().left - middlePoint.x;
        float delY = mPasteContextMenu.getRect().top - middlePoint.y;

        ArrayList<SpenObjectBase> pasteList = new ArrayList<SpenObjectBase>();

        for (int j = 0; j < mClipboard.size(); j++) {
            SpenObjectBase childObject = mClipboard.get(j);
            int childObject_Type = childObject.getType();
            RectF rectF;

            switch (childObject_Type) {

                case SpenObjectBase.TYPE_STROKE :
                    SpenObjectBase stroke = new SpenObjectStroke();
                    stroke.copy(childObject);
                    rectF = stroke.getRect();
                    rectF.left = rectF.left + delX;
                    rectF.right = rectF.right + delX;
                    rectF.bottom = rectF.bottom + delY;
                    rectF.top = rectF.top + delY;
                    stroke.setRect(rectF, false);
                    pasteList.add(stroke);
                    break;

                case SpenObjectBase.TYPE_TEXT_BOX :
                    SpenObjectTextBox textBox = new SpenObjectTextBox();
                    textBox.copy(childObject);
                    rectF = textBox.getRect();
                    rectF.left = rectF.left + delX;
                    rectF.right = rectF.right + delX;
                    rectF.bottom = rectF.bottom + delY;
                    rectF.top = rectF.top + delY;
                    textBox.setRect(rectF, true);
                    pasteList.add(textBox);
                    break;

                case SpenObjectBase.TYPE_IMAGE :
                    SpenObjectBase image = new SpenObjectImage();
                    image.copy(childObject);
                    rectF = image.getRect();
                    rectF.left = rectF.left + delX;
                    rectF.right = rectF.right + delX;
                    rectF.bottom = rectF.bottom + delY;
                    rectF.top = rectF.top + delY;
                    image.setRect(rectF, true);
                    pasteList.add(image);
                    break;

                case SpenObjectBase.TYPE_CONTAINER :
                    SpenObjectBase container = new SpenObjectContainer();
                    container.copy(childObject);
                    rectF = container.getRect();
                    rectF.left = rectF.left + delX;
                    rectF.right = rectF.right + delX;
                    rectF.bottom = rectF.bottom + delY;
                    rectF.top = rectF.top + delY;
                    container.setRect(rectF, true);
                    pasteList.add(container);
                    break;

                default :
                    break;
            }
        }
        mSpenPageDoc.appendObjectList(pasteList);
        mSpenSurfaceView.update();
    }

    /**
     * change ContextParams
     * 
     * @params contextParams SpenContextParams
     */
    public void changeContextParams(SpenContextParams contextParams) {
        mContextParams = contextParams;
    }
}