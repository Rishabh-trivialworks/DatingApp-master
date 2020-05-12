package com.quintus.labs.datingapp.xmpp.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;


import com.quintus.labs.datingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by MyU10 on 1/23/2017.
 */

public class TimeUtils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context context) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = getCurrentTime(context);

        if (time > now || time <= 0) {
            return context.getString(R.string.just_now);
        }

        long diff = now - time;


        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.just_now);
        } else {
            if (diff < 2 * MINUTE_MILLIS) {
//                return AppConstants.singularPlural(context, 1, context.getString(R.string.min)) + " " + context.getString(R.string.ago);
                return context.getString(R.string.a_minute_ago);
            } else if (diff < 50 * MINUTE_MILLIS) {
//                return AppConstants.singularPlural(context, diff / MINUTE_MILLIS, context.getString(R.string.minutes_ago));
                return AppConstants.formatNumber(context, diff / MINUTE_MILLIS) + " " + context.getString(R.string.minutes_ago);
            } else if (diff < 90 * MINUTE_MILLIS) {
//                return AppConstants.singularPlural(context, 1, context.getString(R.string.hr)) + " " + context.getString(R.string.ago);
                return context.getString(R.string.an_hour_ago);
            } else if (diff < 24 * HOUR_MILLIS) {
//                return AppConstants.singularPlural(context, diff / HOUR_MILLIS, context.getString(R.string.hours_ago));
                return AppConstants.formatNumber(context, diff / HOUR_MILLIS) + " " + context.getString(R.string.hours_ago);
            } /*else if (diff < 48 * HOUR_MILLIS) {
//            return "yesterday";
                return AppConstants.singularPlural(context, 1, context.getString(R.string.day)) + " " + context.getString(R.string.ago);
            }*/ else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 1);

                time = calendar.getTimeInMillis();

                now = getCurrentTime(context);

                if (time > now || time <= 0) {
                    return context.getString(R.string.just_now);
                }

                diff = now - time;

                if (diff / DAY_MILLIS > 365) {
                    return chatHeaderFormatter.format(time);
                } else if ((diff / DAY_MILLIS > 6)) {
                    Calendar timeCal = Calendar.getInstance();
                    timeCal.setTimeInMillis(time);
                    Calendar nowCal = Calendar.getInstance();
                    if (nowCal.get(Calendar.YEAR) == timeCal.get(Calendar.YEAR)) {
                        chatHeaderFormatterNoYear = new SimpleDateFormat("MMM d", Locale.getDefault());
                        return chatHeaderFormatterNoYear.format(time);
                    } else {
                        chatHeaderFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                        return chatHeaderFormatter.format(time);
                    }
                } else {
                    chatHeaderFormatterWeekDays = new SimpleDateFormat("EEE", Locale.getDefault());
                    return chatHeaderFormatterWeekDays.format(time);
                }
            }
        }
    }

    public static String getTimeAgoNotifications(long time, Context context) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = getCurrentTime(context);
        if (time > now || time <= 0) {
            return context.getString(R.string.now);
        }

        final long diff = now - time;
//        Log.d("timediff", "diff: " + diff);
        if (diff < MINUTE_MILLIS) {
//            return "just now";
            return context.getString(R.string.now);
        } else if (diff < 2 * MINUTE_MILLIS) {
//            return "a min";
            return AppConstants.formatNumber(context, 1) + " " + context.getString(R.string.m);
        } else if (diff < 50 * MINUTE_MILLIS) {
//            return diff / MINUTE_MILLIS + " mins";
            return AppConstants.formatNumber(context, diff / MINUTE_MILLIS) + " " + context.getString(R.string.m);
        } else if (diff < 90 * MINUTE_MILLIS) {
//            return "an hr";
            return AppConstants.formatNumber(context, 1) + " " + context.getString(R.string.h);
        } else if (diff < 24 * HOUR_MILLIS) {
//            return diff / HOUR_MILLIS + " hrs";
            return AppConstants.formatNumber(context, diff / HOUR_MILLIS) + " " + context.getString(R.string.h);
        } else if (diff < 48 * HOUR_MILLIS) {
//            return "yesterday";
            return AppConstants.formatNumber(context, 1) + " " + context.getString(R.string.d);
        } else {
            if ((diff / DAY_MILLIS > 7))
                return AppConstants.formatNumber(context, ((diff / DAY_MILLIS) / 7)) + " " + context.getString(R.string.w);
            else
                return AppConstants.formatNumber(context, (diff / DAY_MILLIS)) + " " + context.getString(R.string.d);
        }
    }

    private static long getCurrentTime(Context context) {
        return System.currentTimeMillis();
    }

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());

    public static String getTimeFromMillis(long millis) {
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(millis));
    }

    private static SimpleDateFormat chatListTimeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private static SimpleDateFormat chatListDateFormatter = new SimpleDateFormat("d/M/yy", Locale.getDefault());

    public static String getChatListTimeFromMillis(long millis, Context context) {

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(millis);

        Calendar now = Calendar.getInstance();

        try {
            if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR) &&
                    now.get(Calendar.MONTH) == time.get(Calendar.MONTH)) {
                if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1)
                    return context.getString(R.string.yesterday);
                if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
                    chatListTimeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    return chatListTimeFormatter.format(new Date(millis)).toLowerCase().replace(".", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatListDateFormatter = new SimpleDateFormat("d/M/yy", Locale.getDefault());
        return chatListDateFormatter.format(new Date(millis));
    }

    private static SimpleDateFormat chatFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());

    public static String getChatTimeFromMillis(long millis) {
        chatFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return chatFormatter.format(new Date(millis)).toLowerCase().replace(".", "");
    }

    public static String getDateForwardTime(long millis) {
        chatFormatter = new SimpleDateFormat("d/M/yy, h:mm a", Locale.getDefault());
        return chatFormatter.format(new Date(millis)).toUpperCase().replace(".", "");
    }

    public static String mamTimeFormat(long millis) {
        chatFormatter = new SimpleDateFormat("d/MMM/yyyy, h:mm a", Locale.getDefault());
        return chatFormatter.format(new Date(millis)).toUpperCase().replace(".", "");
    }

    public static String getChatListTimeFromMillis(Context context, long time) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        Calendar now = Calendar.getInstance();

        try {
            if (now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    now.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                if (now.get(Calendar.DATE) - cal.get(Calendar.DATE) == 1)
                    return context.getString(R.string.yesterday);
                if (now.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
                    chatFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    return chatFormatter.format(new Date(time)).toLowerCase().replace(".", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatListDateFormatter = new SimpleDateFormat("d/M/yy", Locale.getDefault());
        return chatListDateFormatter.format(new Date(time));
    }

    private static SimpleDateFormat boardFilesTimeFormat = new SimpleDateFormat("d MMM , h:mm aaa", Locale.getDefault());

    public static String getBoardFilesTimeFormat(long millis, Context context) {
        if (millis < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            millis *= 1000;
        }
        try {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(millis);

            chatHeaderFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return boardFilesTimeFormat.format(time.getTime()).replace(",", "at");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static SimpleDateFormat assigmentTimeFormat = new SimpleDateFormat("d MMM yyyy,h:mm aaa", Locale.getDefault());
    private static SimpleDateFormat defaultFormat = new SimpleDateFormat();

    public static String[] getAssignmentTimeFormat(long millis, Context context) {

        if (millis < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            millis *= 1000;
        }
        try {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(millis);

            String formatting = defaultFormat.format(time.getTime());

            return new String[]{assigmentTimeFormat.format(time.getTime()).split(",")[0], formatting.substring(formatting.indexOf(" ") + 1)};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getReadBy(long millis, Context context) {
        if (millis < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            millis *= 1000;
        }

        if (millis > 100000000000000L) {
            millis = millis / 1000;
        }

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(millis);

        Calendar now = Calendar.getInstance();

        try {
            if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR) &&
                    now.get(Calendar.MONTH) == time.get(Calendar.MONTH)) {

                if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1)
                    return context.getString(R.string.yesterday) + "," + chatListTimeFormatter.format(new Date(millis)).toLowerCase();
                if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
                    return chatListTimeFormatter.format(new Date(millis));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return boardFilesTimeFormat.format(time.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static SimpleDateFormat chatHeaderFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private static SimpleDateFormat chatHeaderFormatterNoYear = new SimpleDateFormat("MMM d", Locale.getDefault());
    private static SimpleDateFormat chatHeaderFormatterWeekDays = new SimpleDateFormat("EEE", Locale.getDefault());

    public static String getChatHeaderTimeFromMillis(long millis, Context context) {

        try {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(millis);

            Calendar now = Calendar.getInstance();

            try {
                if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR) &&
                        now.get(Calendar.MONTH) == time.get(Calendar.MONTH)) {
                    if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1)
                        return context.getString(R.string.yesterday);
                    if (now.get(Calendar.DATE) == time.get(Calendar.DATE))
                        return context.getString(R.string.today);
                } /*else {
                    chatHeaderFormatter.format(time.getTime());
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            chatHeaderFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return chatHeaderFormatter.format(time.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setDateForAgeCal(Context context, final EditText editTextDate, final String TAG) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        sb.append(mYear);
        String str = "";
        sb.append(str);
        Log.d("@@@@SystemYEAR", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(mMonth + 1);
        sb2.append(str);
        Log.d("@@@@SystemmMonth", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(mDay);
        sb3.append(str);
        Log.d("@@@@System mDay", sb3.toString());

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                StringBuilder sb = new StringBuilder();
                sb.append(year);
                String str = "-";
                sb.append(str);
                sb.append(monthOfYear + 1);
                sb.append(str);
                sb.append(dayOfMonth);

                String selectedDate = sb.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append("@@@@selectedDate");
                Log.d(sb2.toString(), selectedDate);

                try {
                    Date DateDOB = format.parse(selectedDate);

                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(DateDOB);
                    sb3.append("");
                    String selectedDate2 = format.format(DateDOB);
                    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                    String date = "";
                    try {
                        Date d = dft.parse(selectedDate2);
                        long output = d.getTime() / 1000L;
                        String strr = Long.toString(output);
                        long timestamp = Long.parseLong(strr) * 1000;
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(timestamp);
                        date = DateFormat.format("dd MMM yyyy ", cal).toString();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    editTextDate.setText(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
//                String str = "";
//                editTextDate.setText(str);
//                SpannableString spannable = new SpannableString(context.getString(R.string.assigment_date));
//                // here we set the color
//                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.red_color)), 7, context.getString(R.string.assigment_date).length(), 0);
//                editTextDate.setHint(spannable);

            }
        });

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private static SimpleDateFormat subscriptiondateResponseFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat packageDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());

    public static String getSubscriptionDates(String date){
        try {
            Date fromTimeDate = subscriptiondateResponseFormat.parse(date.split("T")[0]);
            return packageDateFormat.format(fromTimeDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
