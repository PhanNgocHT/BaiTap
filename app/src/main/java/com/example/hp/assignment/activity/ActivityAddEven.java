package com.example.hp.assignment.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hp.assignment.R;
import com.example.hp.assignment.custom.EvenAdapter;
import com.example.hp.assignment.database.DatabaseEvenImpl;
import com.example.hp.assignment.database.DatabaseImageImpl;
import com.example.hp.assignment.model.Even;
import com.example.hp.assignment.model.Image;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.example.hp.assignment.R.string.alarm;
import static com.example.hp.assignment.activity.Constant.REQUEST_TAKE_PHOTO;

/**
 * Created by hp on 10/30/2017.
 */

public class ActivityAddEven extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private List<Even> evens;
    private EvenAdapter adapter;
    private Toolbar toolbar;
    private Dialog dialogBackground;
    private Dialog dialogPicture;

    private LinearLayout llAddActivity;
    private TextView tvTakePhoto;
    private TextView tvChoosePhoto;
    private TextView tvColorWhile;
    private TextView tvColorOrange;
    private TextView tvColorCyan;
    private TextView tvColorBlue;

    private TextView tvAlarmAdd;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDate;
    private TextView tvTime;
    private ImageView ivCancel;
    private DatabaseEvenImpl databaseEven;
    private DatabaseImageImpl databaseImage;
    private int indexColor=1;
    private int stateDate=0;
    private String nextDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_even);
        initView();
        initData();
        setupActionbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_even, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tvAlarmAdd= (TextView) findViewById(R.id.tv_alarm_add);
        etTitle= (EditText) findViewById(R.id.et_title);
        etContent= (EditText) findViewById(R.id.et_content);
        tvDate= (TextView) findViewById(R.id.tv_date);
        tvTime= (TextView) findViewById(R.id.tv_time);
        ivCancel= (ImageView) findViewById(R.id.iv_cancel);
        llAddActivity= (LinearLayout) findViewById(R.id.ll_activity_add);
    }

    private void setupActionbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        databaseEven=new DatabaseEvenImpl(this);
        databaseImage=new DatabaseImageImpl(this);
        final Handler someHandler = new Handler();
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!etTitle.getText().toString().isEmpty()) {
                    getSupportActionBar().setTitle(etTitle.getText().toString());
                }else {
                    getSupportActionBar().setTitle(R.string.note);
                }
                tvAlarmAdd.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 100);
            }
        }, 10);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }
    public View initDialogPicture() {
        dialogPicture = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        dialogPicture.setTitle(R.string.pick_image_intent_text);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_picture, null);

        tvTakePhoto=view.findViewById(R.id.tv_take_photo);
        tvChoosePhoto=view.findViewById(R.id.tv_choose_photo);
        tvColorWhile=view.findViewById(R.id.tv_color_while);
        tvColorBlue=view.findViewById(R.id.tv_color_blue);
        tvColorCyan=view.findViewById(R.id.tv_color_cyan);
        tvColorOrange=view.findViewById(R.id.tv_color_orange);

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, Constant.REQUEST_TAKE_PHOTO);
            }
        });
        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent choosePhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePhoto , Constant.REQUEST_CHOOSE_PHOTO);
            }
        });

        dialogPicture.setCancelable(true);
        dialogPicture.setContentView(view);
        dialogPicture.show();
        return view;
    }

    public View initDialogBackground() {
        dialogBackground = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        dialogBackground.setTitle(R.string.choose_color);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_background, null);

        tvColorWhile=view.findViewById(R.id.tv_color_while);
        tvColorOrange=view.findViewById(R.id.tv_color_orange);
        tvColorCyan=view.findViewById(R.id.tv_color_cyan);
        tvColorBlue=view.findViewById(R.id.tv_color_blue);
        tvColorWhile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=1;
                llAddActivity.setBackgroundColor(getResources().getColor(android.R.color.white));
                dialogBackground.dismiss();
            }
        });
        tvColorCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=2;
                llAddActivity.setBackgroundColor(getResources().getColor(R.color.cyan));
                dialogBackground.dismiss();
            }
        });
        tvColorOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=3;
                llAddActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                dialogBackground.dismiss();
            }
        });
        tvColorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=4;
                llAddActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                dialogBackground.dismiss();
            }
        });

        dialogBackground.setCancelable(true);
        dialogBackground.setContentView(view);
        dialogBackground.show();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                actionClickDate();
                break;
            case R.id.tv_time:
                actionClickTime();
                break;
            case R.id.iv_cancel:
                actionCancel();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_camera_add:
                initDialogPicture();
                break;
            case R.id.mn_set_background_add:
                initDialogBackground();
                break;
            case R.id.mn_save_add:
                saveEven();
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEven() {
        String id=UUID.randomUUID().toString();
        String title=etTitle.getText().toString();
        String content=etContent.getText().toString();
        String date="";
        String time="";
        if (stateDate!=0) {
            date = tvDate.getText().toString();
            time = tvTime.getText().toString();
        }
        String alarm=tvAlarmAdd.getText().toString();
        int background=indexColor;
        Even even=new Even(id, title, content, date, time, alarm, background);
        databaseEven.save(even);

    }


    private void actionClickDate() {
        stateDate++;
        if (stateDate==1) {
            tvTime.setVisibility(View.VISIBLE);
            tvDate.setText(R.string.today);
            tvTime.setText(R.string.time_night);
            ivCancel.setVisibility(View.VISIBLE);
        }
        if (stateDate>1) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            PopupMenu pMenuDate = new PopupMenu(this, tvDate);
            nextDay="Next "+dayOfTheWeek;
            pMenuDate.getMenuInflater().inflate(R.menu.menu_date, pMenuDate.getMenu());
            pMenuDate.getMenu().findItem(R.id.mn_next_sunday).setTitle(nextDay);
            pMenuDate.setOnMenuItemClickListener(this);
            pMenuDate.show();

        }
    }

    private void actionClickTime() {
        PopupMenu pMenuTime=new PopupMenu(this, tvTime);
        pMenuTime.getMenuInflater().inflate(R.menu.menu_time, pMenuTime.getMenu());
        pMenuTime.setOnMenuItemClickListener(this);
        pMenuTime.show();
    }

    private void actionCancel() {
        tvDate.setText(alarm);
        ivCancel.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        stateDate=0;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //menu date
            case R.id.mn_today:
                tvDate.setText(R.string.today);
                break;
            case R.id.mn_tomorrow:
                tvDate.setText(R.string.tomorrow);
                break;
            case R.id.mn_next_sunday:
                tvDate.setText(nextDay);
                break;
            case R.id.mn_other_date:
                showDatePickerDialog();
                break;
            //menu time
            case R.id.mn_night:
                tvTime.setText(R.string.time_night);
                break;
            case R.id.mn_fifteen:
                tvTime.setText(R.string.time_fifteen);
                break;
            case R.id.mn_thirteen:
                tvTime.setText(R.string.time_thirteen);
                break;
            case R.id.mn_twenty:
                tvTime.setText(R.string.time_twenty);
                break;
            case R.id.mn_other_time:
                showTimePickerDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void showTimePickerDialog() {
        Calendar calendar=Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
            }
        }, hour, minute, true);

        timePickerDialog.setTitle(R.string.choose_time);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvDate.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(monthOfYear).append("/").append(year));

            }
        }, year, month, day);

        datePickerDialog.setTitle(R.string.choose_date);
        datePickerDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
                String id= UUID.randomUUID().toString();
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image=stream.toByteArray();
                String index=evens.get(evens.size()).getmId();
                Image image1=new Image(id, index, image);
                 databaseImage.save(image1);
        }
    }

}
