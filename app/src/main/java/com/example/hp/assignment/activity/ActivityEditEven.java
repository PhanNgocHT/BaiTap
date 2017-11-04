package com.example.hp.assignment.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp on 10/30/2017.
 */

public class ActivityEditEven extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private List<Even> evens=new ArrayList<>();
    private EvenAdapter adapter;
    private Toolbar toolbar;
    private Toolbar toolbarBottom;
    private Dialog dialogBackground;
    private Dialog dialogPicture;

    private LinearLayout llEditActivity;
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
    private int indexColor;
    private int stateDate=0;
    private int position;
    private String alarm;
    private String nextDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_even);
        initView();
        initData();
        setupActionbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_even, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbarBottom= (Toolbar) findViewById(R.id.bottombar);
        tvAlarmAdd= (TextView) findViewById(R.id.tv_alarm_add);
        etTitle= (EditText) findViewById(R.id.et_title);
        etContent= (EditText) findViewById(R.id.et_content);
        tvDate= (TextView) findViewById(R.id.tv_date);
        tvTime= (TextView) findViewById(R.id.tv_time);
        ivCancel= (ImageView) findViewById(R.id.iv_cancel);
        llEditActivity= (LinearLayout) findViewById(R.id.ll_activity_edit);
    }

    private void initData() {
        databaseEven=new DatabaseEvenImpl(this);
        databaseImage=new DatabaseImageImpl(this);
        evens.addAll(databaseEven.getAllData());
        final Handler someHandler = new Handler();
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!etTitle.getText().toString().isEmpty()) {
                    getSupportActionBar().setTitle(etTitle.getText().toString());
                }else {
                    getSupportActionBar().setTitle(R.string.note);
                }
                alarm=new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date());
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
        Intent intent=getIntent();
        position=intent.getIntExtra(Constant.POSITION, 0);
        initDataView();
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }

    private void initDataView() {
        if (position>=0&&position<evens.size()) {
            Even even = evens.get(position);
            tvAlarmAdd.setText(even.getmAlarm());
            if (even.getmTitle().isEmpty()) {
                etTitle.setText(R.string.untitle);
            } else {
                etTitle.setText(even.getmTitle());
            }
            etContent.setText(even.getmContent());
            if (even.getmDate().isEmpty()) {
                tvDate.setText(R.string.alarm);
                ivCancel.setVisibility(View.INVISIBLE);
            } else {
                tvDate.setText(even.getmDate());
                ivCancel.setVisibility(View.VISIBLE);
                stateDate = 1;
            }
            tvTime.setText(even.getmTime());
            indexColor = even.getmBackground();
            switch (indexColor) {
                case 1:
                    llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.white));
                    break;
                case 2:
                    llEditActivity.setBackgroundColor(getResources().getColor(R.color.cyan));
                    break;
                case 3:
                    llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    break;
                case 4:
                    llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                default:
                    break;
            }
        }
    }

    private void setupActionbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_share:
                        actionShare();
                        break;
                    case R.id.mn_left:
                        position--;
                        initDataView();
                        break;
                    case R.id.mn_right:
                        position++;
                        initDataView();
                        break;
                    case R.id.mn_delete:
                        showAlertDialog();
                        break;
                    default:
                        break;
                }
                return true;

            }
        });
        toolbarBottom.inflateMenu(R.menu.menu_bottom_edit_even);
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
                llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.white));
                dialogBackground.dismiss();
            }
        });
        tvColorCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=2;
                llEditActivity.setBackgroundColor(getResources().getColor(R.color.cyan));
                dialogBackground.dismiss();
            }
        });
        tvColorOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=3;
                llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                dialogBackground.dismiss();
            }
        });
        tvColorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexColor=4;
                llEditActivity.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
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
            case R.id.mn_camera_edit:
                initDialogPicture();
                break;
            case R.id.mn_set_background_edit:
                initDialogBackground();
                break;
            case R.id.mn_save_edit:
                updateEven();
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.mn_new:
                View menuItemView = findViewById(R.id.mn_new);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.inflate(R.menu.menu_new_note);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateEven() {
        String title=etTitle.getText().toString();
        String content=etContent.getText().toString();
        String date="";
        String time="";
        if (stateDate!=0) {
            date = tvDate.getText().toString();
            time = tvTime.getText().toString();
        }
        int background=indexColor;
        Even even=new Even(evens.get(position).getmId(), title, content, date, time, alarm, background);
        databaseEven.update(even);
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
            nextDay="Next "+dayOfTheWeek;
            PopupMenu pMenuDate = new PopupMenu(this, tvDate);
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
        tvDate.setText(R.string.alarm);
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
            //menu create:
            case R.id.mn_new_even:
                Intent intent=new Intent(this, ActivityAddEven.class);
                startActivity(intent);
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

    private void actionShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, etTitle.getText().toString()+"\n"+etContent.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_delete).setMessage(R.string.message).setCancelable(true)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseEven.delete(evens.get(position));
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
