/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.astrid.actfm;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.todoroo.andlib.service.Autowired;
import com.todoroo.andlib.service.DependencyInjectionService;
import com.todoroo.astrid.actfm.ActFmCameraModule.CameraResultCallback;
import com.todoroo.astrid.actfm.ActFmCameraModule.ClearImageCallback;
import com.todoroo.astrid.actfm.sync.messages.NameMaps;
import com.todoroo.astrid.activity.AstridActivity;
import com.todoroo.astrid.activity.TaskListActivity;
import com.todoroo.astrid.adapter.UpdateAdapter;
import com.todoroo.astrid.dao.UserActivityDao;
import com.todoroo.astrid.data.RemoteModel;
import com.todoroo.astrid.data.UserActivity;
import com.todoroo.astrid.helper.AsyncImageView;

import org.json.JSONObject;
import org.tasks.R;

import edu.mit.mobile.android.imagecache.ImageCache;

public abstract class CommentsFragment extends SherlockListFragment {

//    private TagData tagData;
    protected UpdateAdapter updateAdapter;
    protected EditText addCommentField;
    protected ViewGroup listHeader;
    protected Button footerView = null;

    protected ImageButton pictureButton;

    protected Bitmap picture = null;

    public static final String TAG_UPDATES_FRAGMENT = "tagupdates_fragment"; //$NON-NLS-1$

    //Append tag data remote id to this preference
    public static final String UPDATES_LAST_VIEWED = "updates_last_viewed_"; //$NON-NLS-1$

    protected static final int MENU_REFRESH_ID = Menu.FIRST;

    protected final ImageCache imageCache;

    protected Resources resources;

    @Autowired UserActivityDao userActivityDao;

    public CommentsFragment() {
        DependencyInjectionService.getInstance().inject(this);
        imageCache = AsyncImageView.getImageCache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(getLayout(), container, false);

        return v;
    }

    protected abstract int getLayout();

    protected abstract void loadModelFromIntent(Intent intent);

    protected abstract boolean hasModel();

    protected abstract String getModelName();

    protected abstract Cursor getCursor();

    protected abstract String getSourceIdentifier();

    protected abstract void addHeaderToListView(ListView listView);

    protected abstract void populateListHeader(ViewGroup header);

    protected abstract UserActivity createUpdate();

    protected abstract boolean canLoadMoreHistory();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        loadModelFromIntent(getActivity().getIntent());

        OnTouchListener onTouch = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        };

        if (!hasModel()) {
            getView().findViewById(R.id.updatesFooter).setVisibility(View.GONE);
        }

        addCommentField = (EditText) getView().findViewById(R.id.commentField);
        addCommentField.setOnTouchListener(onTouch);

        setUpUpdateList();

        resources = getResources();
    }

    protected void setUpUpdateList() {
        if (getActivity() instanceof CommentsActivity) {
            ActionBar ab = ((AstridActivity) getActivity()).getSupportActionBar();
            String title = hasModel() ? getString(R.string.tag_updates_title, getModelName())
                    : getString(R.string.TLA_all_activity);
            ((TextView) ab.getCustomView().findViewById(R.id.title)).setText(title);
        }

        final ImageButton commentButton = (ImageButton) getView().findViewById(R.id.commentButton);
        addCommentField = (EditText) getView().findViewById(R.id.commentField);
        addCommentField.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_NULL && addCommentField.getText().length() > 0) {
                    addComment();
                    return true;
                }
                return false;
            }
        });
        addCommentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                commentButton.setVisibility((s.length() > 0) ? View.VISIBLE : View.GONE);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }
        });
        commentButton.setVisibility(TextUtils.isEmpty(addCommentField.getText()) ? View.GONE : View.VISIBLE);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        final ClearImageCallback clearImage = new ClearImageCallback() {
            @Override
            public void clearImage() {
                picture = null;
                resetPictureButton();
            }
        };
        pictureButton = (ImageButton) getView().findViewById(R.id.picture);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picture != null) {
                    ActFmCameraModule.showPictureLauncher(CommentsFragment.this, clearImage);
                } else {
                    ActFmCameraModule.showPictureLauncher(CommentsFragment.this, null);
                }
            }
        });

        refreshUpdatesList();
    }

    protected void resetPictureButton() {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.ic_action_camera, typedValue, true);
        pictureButton.setImageResource(typedValue.resourceId);
    }

    protected void refreshUpdatesList() {
        Activity activity = getActivity();
        View view = getView();
        if (activity == null || view == null) {
            return;
        }

        Cursor cursor = null;
        ListView listView = ((ListView) view.findViewById(android.R.id.list));
        if(updateAdapter == null) {
            cursor = getCursor();
            activity.startManagingCursor(cursor);
            String source = getSourceIdentifier();

            updateAdapter = new UpdateAdapter(this, R.layout.update_adapter_row,
                    cursor, false, source);
            addHeaderToListView(listView);
            addFooterToListView(listView);
            listView.setAdapter(updateAdapter);
        } else {
            cursor = updateAdapter.getCursor();
            cursor.requery();
            activity.startManagingCursor(cursor);
            if (footerView != null && !canLoadMoreHistory()) {
                listView.removeFooterView(footerView);
                footerView = null;
            } else if (footerView == null && canLoadMoreHistory()) {
                addFooterToListView(listView);
                listView.setAdapter(updateAdapter);
            }
            populateListHeader(listHeader);
        }

        listView.setVisibility(View.VISIBLE);

        if (activity instanceof CommentsActivity) {
            setLastViewed();
        }
    }

    private void addFooterToListView(ListView listView) {
        if (footerView != null) {
            listView.removeFooterView(footerView);
        }
        if (canLoadMoreHistory()) {
            footerView = new Button(getActivity());
            footerView.setText(R.string.TEA_load_more);
            footerView.setBackgroundColor(Color.alpha(0));
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int historyCount = 0;
                    Cursor c = updateAdapter.getCursor();
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        if (NameMaps.TABLE_ID_HISTORY.equals(c.getString(UpdateAdapter.TYPE_PROPERTY_INDEX))) {
                            historyCount++;
                        }
                    }
                }
            });
            listView.addFooterView(footerView);
        } else {
            footerView = null;
        }

    }

    protected void setLastViewed() {
        //
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.size() > 0) {
            return;
        }

        MenuItem item;
        boolean showCommentsRefresh = false;
        if (showCommentsRefresh) {
        Activity activity = getActivity();
            if (activity instanceof TaskListActivity) {
                TaskListActivity tla = (TaskListActivity) activity;
                showCommentsRefresh = tla.getTaskEditFragment() == null;
            }
        }
        if(showCommentsRefresh) {
            item = menu.add(Menu.NONE, MENU_REFRESH_ID, Menu.NONE,
                    R.string.ENA_refresh_comments);
            item.setIcon(R.drawable.icn_menu_refresh_dark);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle my own menus
        switch (item.getItemId()) {

        case MENU_REFRESH_ID: {
            return true;
        }

        default: return false;
        }
    }

    protected void addComment() {
        UserActivity update = createUpdate();
        if (picture != null) {
            JSONObject pictureJson = RemoteModel.PictureHelper.savePictureJson(getActivity(), picture);
            if (pictureJson != null) {
                update.setValue(UserActivity.PICTURE, pictureJson.toString());
            }

        }

        userActivityDao.createNew(update);
        addCommentField.setText(""); //$NON-NLS-1$
        picture = null;

        resetPictureButton();
        refreshUpdatesList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CameraResultCallback callback = new CameraResultCallback() {
            @Override
            public void handleCameraResult(Bitmap bitmap) {
                picture = bitmap;
                pictureButton.setImageBitmap(picture);
            }
        };

        if (ActFmCameraModule.activityResult(getActivity(), requestCode, resultCode, data, callback)) {
            //Handled
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
