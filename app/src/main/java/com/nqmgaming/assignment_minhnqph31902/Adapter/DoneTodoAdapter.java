package com.nqmgaming.assignment_minhnqph31902.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.nqmgaming.assignment_minhnqph31902.Permission.CreateNotification;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.DAO.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.Fragment.HomeFragment;
import com.nqmgaming.assignment_minhnqph31902.UI.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.cutelibs.cutedialog.CuteDialog;

public class DoneTodoAdapter extends RecyclerView.Adapter<DoneTodoAdapter.ViewHolder> {
    //init variable
    private final Context context;
    private final ArrayList<TodoDTO> notDoneItemsList;
    private final ArrayList<TodoDTO> doneItemsList;

    //init view binder
    private final ViewBinder viewBinderNotDone = new ViewBinder();

    //constructor
    public DoneTodoAdapter(Context context, ArrayList<TodoDTO> notDoneItemsList, ArrayList<TodoDTO> doneItemsList) {
        this.context = context;
        this.notDoneItemsList = notDoneItemsList;
        this.doneItemsList = doneItemsList;
    }

    //create view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.not_done_item, parent, false);
        return new DoneTodoAdapter.ViewHolder(view);
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        //get todo item and set data
        TodoDTO todoDTO = notDoneItemsList.get(position);
        holder.txtNameNotTodo.setText(todoDTO.getName());
        holder.txtDateEdit.setText(todoDTO.getEndDate());
        setItemAppearance(holder, todoDTO.getStatus());

        //set status for checkbox when click
        holder.uncheckBox.setOnClickListener(v -> {
            if (!holder.uncheckBox.isChecked()) {
                todoDTO.setStatus(1);
                doneItemsList.add(todoDTO);
                TodoDAO todoDAO = new TodoDAO(context);

                int status = todoDAO.setStatusTodo(todoDTO);
                if (status > 0) {
                    notDoneItemsList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, notDoneItemsList.size());

                    //replace fragment to show done list
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.replaceFragment(new HomeFragment());
                } else {
                    new CuteDialog.withAnimation(context)
                            .setAnimation(R.raw.error)
                            .setTitle("Update fail")
                            .setTitleTextColor(R.color.black)
                            .setPositiveButtonColor(R.color.black)
                            .setPositiveButtonText("OK", v1 -> {

                            })
                            .hideNegativeButton(true)
                            .hideCloseIcon(true)
                            .show();
                }

            }
        });

        //set swipe layout
        viewBinderNotDone.bind(holder.swipeLayoutNotDone, String.valueOf(todoDTO.getId()));

        //set event for swipe layout
        holder.iBDeleteNotDone.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(context);
            int result = todoDAO.deleteTodo(todoDTO);
            if (result > 0) {
                notDoneItemsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notDoneItemsList.size());

                new CuteDialog.withAnimation(context)
                        .setAnimation(R.raw.suc)
                        .setTitle("Delete success")
                        .setTitleTextColor(R.color.black)
                        .setPositiveButtonColor(R.color.black)
                        .setPositiveButtonText("OK", v14 -> {
                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();


                // Create an intent to open the app when notification is clicked
                Intent openAppIntent = new Intent(context, MainActivity.class); // Change MainActivity to your desired activity
                openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // Build and show the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CreateNotification.CHANNEL_ID)
                        .setSmallIcon(R.drawable.minh)
                        .setContentTitle("Delete success")
                        .setContentText("Deleted todo with ID: " + todoDTO.getId() + "\n" + "Name: " + todoDTO.getName())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(appPendingIntent)  // Open main activity when notification is clicked
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    //yêu cầu quyền
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
                notificationManager.notify(0, builder.build());

            } else {
                new CuteDialog.withAnimation(context)
                        .setAnimation(R.raw.error)
                        .setTitle("Delete fail")
                        .setTitleTextColor(R.color.black)
                        .setPositiveButtonColor(R.color.black)
                        .setPositiveButtonText("OK", v13 -> {
                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();
                // Create an intent to open the desired activity when notification is clicked
                Intent openAppIntent = new Intent(context, MainActivity.class); // Change MainActivity to your desired activity
                openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                // You can also show a notification when the deletion fails
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CreateNotification.CHANNEL_ID)
                        .setSmallIcon(R.drawable.minh)
                        .setContentTitle("Delete fail")
                        .setContentText("Failed to delete todo with ID: " + todoDTO.getId())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        });

        //set event for edit button
        holder.iBEditNotDone.setOnClickListener(v -> {
            //create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.edit_todo, null);
            builder.setView(view);

            //init view
            EditText edtTitleEdit = view.findViewById(R.id.etTodoEdit);
            EditText edtDescriptionEdit = view.findViewById(R.id.etDescEdit);
            TextView tvDateEdit = view.findViewById(R.id.tvDateEdit);
            ImageButton btnPickDateEdit = view.findViewById(R.id.btnPickDateEdit);
            ImageButton btnSendEdit = view.findViewById(R.id.imgSendEit);

            //set data
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();

            edtTitleEdit.setText(todoDTO.getName());
            edtDescriptionEdit.setText(todoDTO.getContent());
            tvDateEdit.setText(todoDTO.getEndDate());

            //set event for button
            btnPickDateEdit.setOnClickListener(v1 -> {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //create date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view1, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDateEdit.setText(date);
                }, year, month, day);
                datePickerDialog.show();
            });

            //set event for button send edit
            btnSendEdit.setOnClickListener(v12 -> {
                //get data
                String title = edtTitleEdit.getText().toString().trim();
                String description = edtDescriptionEdit.getText().toString().trim();
                String date = tvDateEdit.getText().toString().trim();

                //validate data
                if (TextUtils.isEmpty(title)) {
                    edtTitleEdit.setError("Title is not empty");
                } else if (TextUtils.isEmpty(description)) {
                    edtDescriptionEdit.setError("Description is not empty");
                } else {
                    //set data to todoDTO
                    todoDTO.setName(title);
                    todoDTO.setContent(description);
                    todoDTO.setEndDate(date);
                    TodoDAO todoDAO = new TodoDAO(context);
                    int status = todoDAO.updateTodo(todoDTO);

                    // Build and show the notification
                    String notificationTitle;
                    String notificationText;
                    int notificationId;

                    //check status
                    if (status > 0) {
                        //replace fragment to show done list
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.replaceFragment(new HomeFragment());
                        new CuteDialog.withAnimation(context)
                                .setAnimation(R.raw.suc)
                                .setTitle("Update success")
                                .setTitleTextColor(R.color.black)
                                .setPositiveButtonColor(R.color.black)
                                .setPositiveButtonText("OK", v15 -> {
                                })
                                .hideNegativeButton(true)
                                .hideCloseIcon(true)
                                .show();
                        notifyItemRangeChanged(position, notDoneItemsList.size());
                        alertDialog.dismiss();

                        notificationTitle = "Update success";
                        notificationText = "Updated todo with ID: " + todoDTO.getId() + " successfully.";
                        notificationId = 2;  // Choose appropriate ID
                    } else {
                        new CuteDialog.withAnimation(context)
                                .setAnimation(R.raw.error)
                                .setTitle("Update fail")
                                .setTitleTextColor(R.color.black)
                                .setPositiveButtonColor(R.color.black)
                                .setPositiveButtonText("OK", v16 -> {
                                })
                                .hideNegativeButton(true)
                                .hideCloseIcon(true)
                                .show();

                        notificationTitle = "Update fail";
                        notificationText = "Failed to update todo with ID: " + todoDTO.getId() + ". Please try again later.";
                        notificationId = 3;  // Choose appropriate ID
                    }

                    // Create an intent to open the desired activity when notification is clicked
                    Intent openAppIntent = new Intent(context, MainActivity.class); // Change MainActivity to your desired activity
                    openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    // Build and show the notification
                    NotificationCompat.Builder builderNotifications = new NotificationCompat.Builder(context, CreateNotification.CHANNEL_ID)
                            .setSmallIcon(R.drawable.minh)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationText)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(notificationId, builderNotifications.build());

                }
            });
            alertDialog.show();
        });

        //set event for item show content
        holder.constraintNotTodo.setOnLongClickListener(v -> {
            //create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.content, null);

            //init view
            TextView tvContent = view.findViewById(R.id.content);
            Button btnOK = view.findViewById(R.id.btnOK);

            //set data
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            tvContent.setText(todoDTO.getContent());


            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            btnOK.setOnClickListener(v1 -> alertDialog.dismiss());
            alertDialog.show();
            return true;
        });
    }

    //get size of list item
    @Override
    public int getItemCount() {
        return notDoneItemsList.size();
    }

    //init view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox uncheckBox;
        TextView txtNameNotTodo, txtDateEdit;
        ConstraintLayout constraintNotTodo;
        CardView cardViewMotTodo;
        ImageButton iBEditNotDone, iBDeleteNotDone;
        SwipeLayout swipeLayoutNotDone;
        LinearLayout linearLayoutNotDone;


        //init view
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uncheckBox = itemView.findViewById(R.id.unCheckBox);
            txtNameNotTodo = itemView.findViewById(R.id.tvNameNotTodo);
            constraintNotTodo = itemView.findViewById(R.id.constraintNotTodo);
            cardViewMotTodo = itemView.findViewById(R.id.cardViewNotTodo);
            iBEditNotDone = itemView.findViewById(R.id.iBEditNotDone);
            iBDeleteNotDone = itemView.findViewById(R.id.iBDeleteNotDone);
            swipeLayoutNotDone = itemView.findViewById(R.id.swipeLayoutNotDone);
            linearLayoutNotDone = itemView.findViewById(R.id.layoutCutomizeNotDone);
            txtDateEdit = itemView.findViewById(R.id.txtDateNotDone);

        }
    }

    //set appearance for item
    private void setItemAppearance(ViewHolder holder, int status) {
        if (status == 1) {
            setIncompleteAppearance(holder);
        } else {
            setCompletedAppearance(holder);
        }
    }

    //set appearance for item when it is done
    private void setCompletedAppearance(DoneTodoAdapter.ViewHolder holder) {
        holder.uncheckBox.setChecked(true);
        holder.txtNameNotTodo.setAlpha(0.5f);
        holder.constraintNotTodo.setBackgroundColor(context.getResources().getColor(R.color.gray, null));
        holder.cardViewMotTodo.setCardBackgroundColor(context.getResources().getColor(R.color.gray, null));
        holder.txtNameNotTodo.setPaintFlags(holder.txtNameNotTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.uncheckBox.setButtonTintList(context.getResources().getColorStateList(R.color.active, null));
    }

    //set appearance for item when it is not done
    private void setIncompleteAppearance(DoneTodoAdapter.ViewHolder holder) {
        holder.uncheckBox.setChecked(false);
        holder.txtNameNotTodo.setAlpha(1f);
        holder.constraintNotTodo.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.cardViewMotTodo.setCardBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.txtNameNotTodo.setPaintFlags(holder.txtNameNotTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.uncheckBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }
}