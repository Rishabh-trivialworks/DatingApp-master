package com.quintus.labs.datingapp.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Main.Cards;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Main.ProfileCheckinMain;
import com.quintus.labs.datingapp.Matched.Users;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.SplashActivity;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.Utils;
import com.quintus.labs.datingapp.chatview.data.Message;
import com.quintus.labs.datingapp.chatview.data.MessageBody;
import com.quintus.labs.datingapp.chatview.utils.ChatUtils;
import com.quintus.labs.datingapp.chatview.widget.ChatView;
import com.quintus.labs.datingapp.rest.ProgressRequestBody;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.MessageModel;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.User;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.CommonMethods;
import com.quintus.labs.datingapp.xmpp.MyService;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.quintus.labs.datingapp.chatview.utils.ChatUtils.MESSAGE_TYPE_AUDIO;
import static com.quintus.labs.datingapp.chatview.utils.ChatUtils.MESSAGE_TYPE_IMAGE;
import static com.quintus.labs.datingapp.chatview.utils.ChatUtils.MESSAGE_TYPE_MULTIPLE_IMAGE;
import static com.quintus.labs.datingapp.chatview.utils.ChatUtils.MESSAGE_TYPE_TEXT;
import static com.quintus.labs.datingapp.chatview.utils.ChatUtils.MESSAGE_TYPE_VIDEO;

public class ChatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

private static ChatView chatView;
private Toolbar toolbar;
    private Context mContext;
    private ImageView imageViewOptions;
    TextView textViewAction,textViewTitle,progressText;
    LinearLayout progressLayout;

    static Users user;
    Cards card;
    static UserData myuser;
    static String receiverUser;
    private static Random random;
    static CommonMethods commonMethods;
    private SQLiteDatabase mydb;
    public static int imagePickerRequestCode=10;
    List<Uri> mSelected;
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CAMERA_VIDEO_REQUEST_CODE = 7501;
    private static final int GALLERY_REQUEST_CODE = 7502;
    private static final int DOCUMENTS_REQUEST_CODE = 7503;

    private EasyImage easyImage;
    private static String dirPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        random = new Random();

        chatView = (ChatView) findViewById(R.id.chatView);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        progressLayout = findViewById(R.id.layoutProgress);
        progressText = findViewById(R.id.progressText);

        mContext = this;
        dirPath = Utils.getRootDirPath(getApplicationContext());

        commonMethods = new CommonMethods(ChatActivity.this);
        if(getIntent()!=null){
         user= (Users) getIntent().getSerializableExtra("user");
         card=(Cards)getIntent().getSerializableExtra("card");

        }
        else{
            finish();
        }
        receiverUser = user.getUserId()+"";
        myuser = TempStorage.getUser();
        setListeners();
        setToolBar(user.getName());
        if (isTableExists(receiverUser)) {
            loadDataFromLocal(receiverUser);
        }
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Huddle")
                .allowMultiple(true)
                .build();

    }


    private void setListeners(){
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                if(body!=null&&body.length()>0){
                    addRightMessage(ChatUtils.createMessageBody(MESSAGE_TYPE_TEXT,body,null,null,System.currentTimeMillis()+""));
                }
            }
        });
        chatView.setOnClickGalleryButtonListener(new ChatView.OnClickGalleryButtonListener() {
            @Override
            public void onGalleryButtonClick() {
                String[] necessaryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openGallery(ChatActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, GALLERY_REQUEST_CODE);
                }
            }
        });

        chatView.setOnClickCameraButtonListener(new ChatView.OnClickCameraButtonListener() {
            @Override
            public void onCameraButtonClicked() {
                String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openCameraForImage(ChatActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE);
                }
            }
        });

        chatView.setOnClickVideoButtonListener(new ChatView.OnClickVideoButtonListener() {
            @Override
            public void onVideoButtonClick() {
                String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openCameraForVideo(ChatActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, CAMERA_VIDEO_REQUEST_CODE);
                }
            }
        });

    }

    public static void addRightMessage(MessageBody messageBody){
        Message message = new Message();
        String messageStr = ChatUtils.getStringFromMessageBody(messageBody);
        switch (messageBody.getMessageType()){
            case MESSAGE_TYPE_TEXT:
                message.setBody(messageBody.getMessage()); //message body
                message.setMessageType(Message.MessageType.RightSimpleImage);
                message.setType(Message.MessageType.RightSimpleImage.toString()); //message type
                message.setTime(AppConstants.getTimeAgo(Long.parseLong(messageBody.getMessageTime()))); //message time (String)

                break;
            case MESSAGE_TYPE_IMAGE:
                message.setMessageType(Message.MessageType.RightSingleImage);
                message.setType(Message.MessageType.RightSingleImage.toString()); //message type
                message.setTime(AppConstants.getTimeAgo(Long.parseLong(messageBody.getMessageTime())));//message time (String)
                message.setImageList(messageBody.getMediaUrl());
                break;
            case MESSAGE_TYPE_MULTIPLE_IMAGE:
                break;
            case MESSAGE_TYPE_VIDEO:
                message.setMessageType(Message.MessageType.RightVideo);
                message.setType(Message.MessageType.RightVideo.toString()); //message type
                message.setTime(AppConstants.getTimeAgo(Long.parseLong(messageBody.getMessageTime()))); //message time (String)
                message.setVideoUri(Uri.fromFile(new File(messageBody.getLocalUrls().get(0))));

                break;
            case MESSAGE_TYPE_AUDIO:
                break;
        }
        message.setUserName(myuser.getName()); //sender name
        if(myuser.getMedia()!=null&&myuser.getMedia().size()>0){
            message.setUserIcon("http://"+myuser.getMedia().get(0).getUrl());
        }
        chatView.addMessage(message);
        MyService.xmpp.sendMessage(new MessageModel(myuser.getId()+"", receiverUser, messageStr, "TEXT", true, random.nextInt(1000),messageBody.getMessageTime()));
        commonMethods.createTable(receiverUser);
        //  String tablename, String s, String r, String m, String w,String datatype
        commonMethods.insertIntoTable(receiverUser, user.getUserId(), receiverUser, messageStr, "m", "TEXT",messageBody.getMessageTime());

    }

    public static void addLeftMessage(MessageModel messageModel){
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(user!=null){
                                Message msg = processMessageLeft(messageModel.getMsg().trim());
                                if(msg!=null){
                                    chatView.addMessage(msg);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }







    public static void addLeftMessageOne(MessageModel messageModel){
        try {
            if(user!=null){
                Message msg = processMessageLeft(messageModel.getMsg().trim());
                if(msg!=null){
                    chatView.addMessage(msg);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public static void addRightMessage(MessageModel messageModel){
        Message msg = processMessageRight(messageModel.getMsg().trim());
        if(msg!=null)
        chatView.addMessage(msg);

    }

    private void setToolBar(String title) {

        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(mContext).inflate(R.layout.view_app_bar, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        textViewAction = v.findViewById(R.id.textViewAction);
        textViewTitle = v.findViewById(R.id.textViewTitle);
        imageViewOptions.setVisibility(View.VISIBLE);
        textViewAction.setVisibility(View.VISIBLE);

        imageViewOptions.setOnClickListener(v1 -> finish());
        textViewAction.setVisibility(View.GONE);

        textViewTitle.setText(title);
        imageViewOptions.setOnClickListener(v13 -> {
            Helper.showPopMenu(mContext,v13,ChatActivity.this);



        });

        textViewTitle.setOnClickListener(v12 -> {
            Intent i = new Intent(mContext, ProfileCheckinMain.class);
            i.putExtra("name",card.getName());
            i.putExtra("bio",card.getBio());
            i.putExtra("interest",card.getInterest());
            i.putExtra("distance",card.getDistance());
            i.putExtra("photo",card.getProfileImageUrl());
            i.putExtra("id",Integer.parseInt(card.getUserId()));
            i.putExtra("gender",card.getGender());

            startActivity(i);
        });

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
    public void loadDataFromLocal(String tablename) {
        String tblname = "'" + tablename + "'";
        boolean w = false;
        mydb = openOrCreateDatabase(CommonMethods.DBNAME, Context.MODE_PRIVATE, null);
        Cursor allrows = mydb.rawQuery("SELECT * FROM " + tblname+"ORDER BY ID ASC", null);
        if (allrows.moveToFirst()) {
            do {
                int id = allrows.getInt(allrows.getColumnIndex("ID"));
                String sender = allrows.getString(allrows.getColumnIndex("sender"));
                String receiver = allrows.getString(allrows.getColumnIndex("receiver"));
                String msg = allrows.getString(allrows.getColumnIndex("msg"));
                String who = allrows.getString(allrows.getColumnIndex("who"));
                String type = allrows.getString(allrows.getColumnIndex("type"));
                String time = allrows.getString(allrows.getColumnIndex("time"));

                if (who.equals("m")) {
                    w = true;
                } else if (who.equals("r")) {
                    w = false;
                }
                LogUtils.database("ID OF MESSAGE :- "+id+" MESSAGE :-"+msg);
                MessageModel messageModel = new MessageModel(sender, receiver, msg, type, w, id,time);
                if(w){
                    addRightMessage(messageModel);
                }else{
                    addLeftMessageOne(messageModel);
                }

            }
            while (allrows.moveToNext());
        }
    }
    public boolean isTableExists(String tableName) {
        mydb = openOrCreateDatabase(CommonMethods.DBNAME, Context.MODE_PRIVATE, null);
        Cursor cursor = mydb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionBlock:
                MyService.xmpp.blockUnblock(Integer.parseInt(receiverUser),true);
                break;
            case R.id.actionSeeProfile:
                Intent i = new Intent(mContext, ProfileCheckinMain.class);
                i.putExtra("name",card.getName());
                i.putExtra("bio",card.getBio());
                i.putExtra("interest",card.getInterest());
                i.putExtra("distance",card.getDistance());
                i.putExtra("photo",card.getProfileImageUrl());
                i.putExtra("id",Integer.parseInt(card.getUserId()));
                i.putExtra("gender", card.getGender());

                startActivity(i);
                break;
            case R.id.actionCancelCrush:

                break;
            case R.id.actionDeleteHistory:
                commonMethods.clearChat(receiverUser);
                chatView.clearMessages();
                break;

        }
        return false;
    }
    private static Message processMessageLeft(String messageStr){

        try{
            MessageBody body = ChatUtils.getMessageBody(messageStr);
            Message message = new Message();

            switch (body.getMessageType()){
                case MESSAGE_TYPE_TEXT:
                    message.setBody(body.getMessage()); //message body
                    message.setMessageType(Message.MessageType.LeftSimpleMessage);
                    message.setType(Message.MessageType.LeftSimpleMessage.toString()); //message type

                    break;
                case MESSAGE_TYPE_IMAGE:
                    message.setMessageType(Message.MessageType.LeftSingleImage);
                    message.setType(Message.MessageType.LeftSingleImage.toString()); //message type

                    message.setImageList(body.getMediaUrl());
                    break;
                case MESSAGE_TYPE_MULTIPLE_IMAGE:
                    break;
                case MESSAGE_TYPE_VIDEO:
                    message.setMessageType(Message.MessageType.LeftVideo);
                    message.setType(Message.MessageType.LeftVideo.toString()); //message type
                    message.setVideoUri(Uri.fromFile(new File(body.getLocalUrls().get(0))));

                    break;
                case MESSAGE_TYPE_AUDIO:
                    break;
            }
            message.setTime(AppConstants.getTimeAgo(Long.parseLong(body.getMessageTime())));
            message.setUserName(user.getName()); //sender name
            //sender icon
            if(user.getProfileImageUrl()!=null){
                message.setUserIcon(user.getProfileImageUrl());


            }
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private static Message processMessageRight(String messageStr){
        try{
            MessageBody body = ChatUtils.getMessageBody(messageStr);
            Message message = new Message();
            // message.setTime(AppConstants.getTimeAgo(Long.parseLong(messageModel.getTime()))); //message time (String)

            switch (body.getMessageType()){
                case MESSAGE_TYPE_TEXT:
                    message.setBody(body.getMessage()); //message body
                    message.setMessageType(Message.MessageType.RightSimpleImage);
                    message.setType(Message.MessageType.RightSimpleImage.toString()); //message type
                    break;
                case MESSAGE_TYPE_IMAGE:
                    message.setMessageType(Message.MessageType.RightSingleImage);
                    message.setType(Message.MessageType.RightSingleImage.toString()); //message type
                    message.setTime(AppConstants.getTimeAgo(Long.parseLong(body.getMessageTime())));//message time (String)
                    message.setImageList(body.getMediaUrl());
                    break;
                case MESSAGE_TYPE_MULTIPLE_IMAGE:
                    break;
                case MESSAGE_TYPE_VIDEO:
                    message.setMessageType(Message.MessageType.RightVideo);
                    message.setType(Message.MessageType.RightVideo.toString()); //message type
                    message.setTime(AppConstants.getTimeAgo(Long.parseLong(body.getMessageTime()))); //message time (String)
                    message.setVideoUri(Uri.fromFile(new File(body.getLocalUrls().get(0))));
                    break;
                case MESSAGE_TYPE_AUDIO:
                    break;
            }
            message.setTime(AppConstants.getTimeAgo(Long.parseLong(body.getMessageTime())));
            message.setUserName(myuser.getName()); //sender name
            if(myuser.getMedia()!=null&&myuser.getMedia().size()>0){
                message.setUserIcon("http://"+myuser.getMedia().get(0).getUrl());
            }
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(ChatActivity.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(ChatActivity.this);
        } else if (requestCode == CAMERA_VIDEO_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForVideo(ChatActivity.this);
        } else if (requestCode == GALLERY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openGallery(ChatActivity.this);
        } else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(ChatActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
                       if(source.equals(MediaSource.GALLERY)){
                           uploadImage(imageFile.getFile(),"Image");

                       }
                    if(source.equals(MediaSource.CAMERA_IMAGE)){
                        uploadImage(imageFile.getFile(),"Image");

                    }
                    if(source.equals(MediaSource.CAMERA_VIDEO)){
                        uploadImage(imageFile.getFile(),"Video");

                    }


                }
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });


    }

    private void uploadImage(File file,String type){
//        if(imageView.getTag()!=null){
//            int id = (int) imageView.getTag();
//            deleteImage(id);
//        }
        try {
            file = new Compressor.Builder(this)
                    .setDestinationDirectoryPath(mContext.getCacheDir().getPath())
                    .build()
                    .compressToFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                LogUtils.debug("Progress:- "+percentage);
                String message = String.format(mContext.getResources().getString(R.string.sending_message),percentage);
                progressText.setText(message);


            }

            @Override
            public void onError() {
            }

            @Override
            public void onFinish() {
            }
        });

        MultipartBody.Part body = MultipartBody.Part.createFormData("media", file.getName(), fileBody);

        MultipartBody.Part body1 = MultipartBody.Part.createFormData("mediaType",type);


        Call<ResponseModel<ImageModel>> call = RestServiceFactory.createService().chatUpload(body1,body);
        File finalFile = file;
        handleVisibility(1,0);
        call.enqueue(new Callback<ResponseModel<ImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Response<ResponseModel<ImageModel>> response) {
                List<String> list = new ArrayList<>();
                List<String> listLocal = new ArrayList<>();

                if (RestCallBack.isSuccessFull(response.body())) {
                    list.add(response.body().data.getUrl());
                        listLocal.add(finalFile.getAbsolutePath());

                        if(type.equalsIgnoreCase("Video")){
                            addRightMessage(ChatUtils.createMessageBody(MESSAGE_TYPE_VIDEO,"",list,listLocal,System.currentTimeMillis()+""));

                        }
                        if(type.equalsIgnoreCase("Image")){
                            addRightMessage(ChatUtils.createMessageBody(MESSAGE_TYPE_IMAGE,"",list,listLocal,System.currentTimeMillis()+""));

                        }
                    handleVisibility(0,100);

                } else {
                    handleVisibility(0,0);

                    ToastUtils.show(mContext, "Failed to send file.");
                    }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Throwable t) {
                Log.d("Exception: ",t.getMessage());
                handleVisibility(0,100);

            }
        });
    }

private void handleVisibility(int visibility,int progress){
        String message = String.format(mContext.getResources().getString(R.string.sending_message),progress);
        Animation animShow = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation animHide = AnimationUtils.loadAnimation( this, R.anim.slide_up);
    switch (visibility){
        case 0:
            progressLayout.startAnimation( animHide );
            progressLayout.setVisibility(View.GONE);
            break;
        case 1:
            if(progress==100){
                progressText.setText("Message sent");

            }else{
                progressText.setText(message);

            }
            progressLayout.startAnimation( animShow );
            progressLayout.setVisibility(View.VISIBLE);
            break;
    }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return true;
    }

    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(ChatActivity.this, permissions, requestCode);
    }
}
