package com.quintus.labs.datingapp.mediapicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.RecyclerListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.DialogUtils;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.mediapicker.imageloader.MediaImageLoader;
import com.quintus.labs.datingapp.mediapicker.utils.MediaUtils;
import com.quintus.labs.datingapp.mediapicker.widget.PickerImageView;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author TUNGDX
 */

/**
 * Adapter for display media item list.
 */
public class MediaAdapter extends CursorAdapter implements RecyclerListener {
    private int mMediaType;
    private MediaImageLoader mMediaImageLoader;
    private List<MediaItem> mMediaListSelected = new ArrayList<MediaItem>();
    private MediaOptions mMediaOptions;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;
    private List<PickerImageView> mPickerImageViewSelected = new ArrayList<PickerImageView>();

    public MediaAdapter(Context context, Cursor c, int flags,
                        MediaImageLoader mediaImageLoader, int mediaType, MediaOptions mediaOptions) {
        this(context, c, flags, null, mediaImageLoader, mediaType, mediaOptions);
    }

    public MediaAdapter(Context context, Cursor c, int flags,
                        List<MediaItem> mediaListSelected, MediaImageLoader mediaImageLoader,
                        int mediaType, MediaOptions mediaOptions) {
        super(context, c, flags);
        if (mediaListSelected != null)
            mMediaListSelected = mediaListSelected;
        mMediaImageLoader = mediaImageLoader;
        mMediaType = mediaType;
        mMediaOptions = mediaOptions;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        final Uri uri;
        if (mMediaType == MediaItem.PHOTO) {
            uri = MediaUtils.getPhotoUri(cursor);
            holder.thumbnail.setVisibility(View.GONE);
        } else {
            uri = MediaUtils.getVideoUri(cursor);
            holder.thumbnail.setVisibility(View.VISIBLE);
        }
        boolean isSelected = isSelected(uri);
        holder.imageView.setSelected(isSelected);
        if (isSelected) {
            mPickerImageViewSelected.add(holder.imageView);
        }
        mMediaImageLoader.displayImage(uri, holder.imageView);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        View root = View
                .inflate(context, R.layout.list_item_mediapicker, null);
        holder.imageView = (PickerImageView) root.findViewById(R.id.thumbnail);
        holder.thumbnail = root.findViewById(R.id.overlay);

        holder.imageView.setLayoutParams(mImageViewLayoutParams);
        // Check the height matches our calculated column width
        if (holder.imageView.getLayoutParams().height != mItemHeight) {
            holder.imageView.setLayoutParams(mImageViewLayoutParams);
        }
        root.setTag(holder);
        return root;
    }

    private class ViewHolder {
        PickerImageView imageView;
        View thumbnail;
    }

    public boolean hasSelected() {
        return mMediaListSelected.size() > 0;
    }

    /**
     * Check media uri is selected or not.
     *
     * @param uri Uri of media item (photo, video)
     * @return true if selected, false otherwise.
     */
    public boolean isSelected(Uri uri) {
        if (uri == null)
            return false;
        for (MediaItem item : mMediaListSelected) {
            if (item.getUriOrigin().equals(uri))
                return true;
        }
        return false;
    }

    /**
     * Check {@link MediaItem} is selected or not.
     *
     * @param item {@link MediaItem} to check.
     * @return true if selected, false otherwise.
     */
    public boolean isSelected(MediaItem item) {
        return mMediaListSelected.contains(item);
    }

    /**
     * Set {@link MediaItem} selected.
     *
     * @param item {@link MediaItem} to selected.
     */
    public void setMediaSelected(MediaItem item) {
        syncMediaSelectedAsOptions();
        if (!mMediaListSelected.contains(item))
            mMediaListSelected.add(item);
    }

    /**
     * If item selected then change to unselected and unselected to selected.
     *
     * @param item Item to update.
     */
    public void updateMediaSelected(MediaItem item,
                                    final PickerImageView pickerImageView) {
        if (mMediaListSelected.contains(item)) {
            mMediaListSelected.remove(item);
            pickerImageView.setSelected(false);
            this.mPickerImageViewSelected.remove(pickerImageView);
        } else {
            if (!mMediaOptions.canSelectPhotoAndVideo() && mMediaOptions.getMaxImageSelectionLimit() != 0
                    && mMediaOptions.canSelectPhoto() && mMediaOptions.getMaxImageSelectionLimit() <= mMediaListSelected.size()) {
                Toast.makeText(pickerImageView.getContext(), pickerImageView.getContext().getString(R.string.image_selection_limit), Toast.LENGTH_SHORT).show();

                    final String renewButtonText = "Upgrade";

                    if(mMediaOptions.getMaxImageSelectionLimit()==4){
                        DialogUtils.showBasicMessage(pickerImageView.getContext(), String.format(pickerImageView.getContext().getString(R.string.photo_limit_upgrade),
                                mMediaOptions.getMaxImageSelectionLimit() + ""));
                    }else {
                        //DialogUtils.showBasicMessage(context, getString(R.string.file_capacity), getString(R.string.you_ran_out_of_space_in_the_app));
                        DialogUtils.showBasicwithTwoButton(pickerImageView.getContext(),
                                pickerImageView.getContext().getString(R.string.photo_limit),
                                String.format(pickerImageView.getContext().getString(R.string.photo_limit_upgrade),
                                        mMediaOptions.getMaxImageSelectionLimit() + ""),
                                renewButtonText,
                                AppContext.getInstance().getActivity().getString(R.string.cancel),
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                       dialog.dismiss();
                                    }
                                });
                    }

            } else if (!mMediaOptions.canSelectPhotoAndVideo() && mMediaOptions.getMaxVideoSelectionLimit() != 0
                    && mMediaOptions.canSelectVideo() && mMediaOptions.getMaxVideoSelectionLimit() <= mMediaListSelected.size()) {
                Toast.makeText(pickerImageView.getContext(), pickerImageView.getContext().getString(R.string.video_selection_limit), Toast.LENGTH_SHORT).show();
            } else {
                boolean value = syncMediaSelectedAsOptions();
                if (value) {
                    for (PickerImageView picker : this.mPickerImageViewSelected) {
                        picker.setSelected(false);
                    }
                    this.mPickerImageViewSelected.clear();
                }

                mMediaListSelected.add(item);
                pickerImageView.setSelected(true);
                this.mPickerImageViewSelected.add(pickerImageView);

                if (item.isVideo()) {
                    try {
                        String video = item.getPathOrigin(pickerImageView.getContext());
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(pickerImageView.getContext(), Uri.fromFile(new File(video)));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                        if ((int) (Long.parseLong(time) / 1000) > AppConstants.Limits.VIDEO_RECORD_DURATION_LIMIT) {
                                final String renewButtonText="Upgrade";

                                //DialogUtils.showBasicMessage(context, getString(R.string.file_capacity), getString(R.string.you_ran_out_of_space_in_the_app));
                                DialogUtils.show2OptionsDialog(pickerImageView.getContext(),
                                        pickerImageView.getContext().getString(R.string.video_limit),
                                        String.format(pickerImageView.getContext().getString(R.string.video_limit_upgrade),
                                                AppConstants.Limits.VIDEO_RECORD_DURATION_LIMIT + ""),
                                        renewButtonText,
                                        AppContext.getInstance().getActivity().getString(R.string.video_trim),
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }, new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                EventBroadcastHelper.sendMediaPickerDone();
                                            }
                                        } );

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        }
    }

    /**
     * @return List of {@link MediaItem} selected.
     */
    public List<MediaItem> getMediaSelectedList() {
        return mMediaListSelected;
    }

    /**
     * Set list of {@link MediaItem} selected.
     *
     * @param list
     */
    public void setMediaSelectedList(List<MediaItem> list) {
        mMediaListSelected = list;
    }

    /**
     * Whether clear or not media selected as options.
     *
     * @return true if clear, false otherwise.
     */
    private boolean syncMediaSelectedAsOptions() {
        switch (mMediaType) {
            case MediaItem.PHOTO:
                if (!mMediaOptions.canSelectMultiPhoto()) {
                    mMediaListSelected.clear();
                    return true;
                }
                break;
            case MediaItem.VIDEO:
                if (!mMediaOptions.canSelectMultiVideo()) {
                    mMediaListSelected.clear();
                    return true;
                }

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * {@link MediaItem#VIDEO} or {@link MediaItem#PHOTO}
     *
     * @param mediaType
     */
    public void setMediaType(int mediaType) {
        mMediaType = mediaType;
    }

    // set numcols
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    // set photo item height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams.height = height;
        mImageViewLayoutParams.width = height;
        notifyDataSetChanged();
    }

    @Override
    public void onMovedToScrapHeap(View view) {
        PickerImageView imageView = (PickerImageView) view
                .findViewById(R.id.thumbnail);
        mPickerImageViewSelected.remove(imageView);
    }

    public void onDestroyView() {
        mPickerImageViewSelected.clear();
    }
}