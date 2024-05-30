package com.nqmgaming.assignment_minhnqph31902.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.HomeFragment;
import com.nqmgaming.assignment_minhnqph31902.permission.CreateNotification;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.cutelibs.cutedialog.CuteDialog;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    //create context and arraylist
    private final Context context;
    private final ArrayList<TodoDTO> doneItemsList;
    private final ArrayList<TodoDTO> notDoneItemsList;

    //create viewfinder
    private final ViewBinder viewBinderDone = new ViewBinder();


    //create constructor
    public TodoAdapter(Context context, ArrayList<TodoDTO> doneItemsList, ArrayList<TodoDTO> notDoneItemsList) {
        this.context = context;
        this.doneItemsList = doneItemsList;
        this.notDoneItemsList = notDoneItemsList;
    }

    //create view-holder
    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view);
    }

    //bind data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //create todoDTO and set data
        TodoDTO todoDTO = doneItemsList.get(position);
        holder.txtNameTodo.setText(todoDTO.getName());
        holder.tvDate.setText(todoDTO.getEndDate());
        setItemAppearance(holder, todoDTO.getStatus());

        // Set status for checkbox when clicked
        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                todoDTO.setStatus(0);
                notDoneItemsList.add(todoDTO);

                TodoDAO todoDAO = new TodoDAO(context);
                int status = todoDAO.setStatusTodo(todoDTO);

                // If status > 0, update success
                if (status > 0) {
                    doneItemsList.remove(todoDTO);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, doneItemsList.size());
                } else {
                    new CuteDialog.withAnimation(context)
                            .setAnimation(R.raw.error)
                            .setTitle("Update fail")
                            .setTitleTextColor(R.color.black)
                            .setPositiveButtonColor(R.color.black)
                            .setPositiveButtonText("OK", v13 -> {

                            })
                            .hideNegativeButton(true)
                            .hideCloseIcon(true)
                            .show();
                }

            }
        });

        // Set swipe layout
        viewBinderDone.bind(holder.swipeLayout, String.valueOf(todoDTO.getId()));

        // Set event for swipe layout
        holder.iBDelete.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(context);
            int result = todoDAO.deleteTodo(todoDTO);

            // Build and show the notification
            NotificationCompat.Builder builderNotif;
            String notificationTitle;
            String notificationText;
            int notificationId;

            // If result > 0, delete success
            if (result > 0) {
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
                doneItemsList.remove(todoDTO);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, doneItemsList.size());

                notificationTitle = "Delete success";
                notificationText = "Deleted todo with ID: " + todoDTO.getId();
                notificationId = 4;  // Choose appropriate ID
            } else {
                new CuteDialog.withAnimation(context)
                        .setAnimation(R.raw.error)
                        .setTitle("Delete fail")
                        .setTitleTextColor(R.color.black)
                        .setPositiveButtonColor(R.color.black)
                        .setPositiveButtonText("OK", v15 -> {

                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();

                notificationTitle = "Delete fail";
                notificationText = "Failed to delete todo with ID: " + todoDTO.getId();
                notificationId = 5;  // Choose appropriate ID
            }
            CreateNotification createNotification = new CreateNotification();
            createNotification.postNotification(context, notificationTitle, notificationText, notificationId);
        });

        // Set event for swipe layout
        holder.iBEdit.setOnClickListener(v -> {
            // Create alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.edit_todo, null);

            // Create edit text, text view, image button
            EditText edtTitleEdit = view.findViewById(R.id.etTodoEdit);
            EditText edtDescriptionEdit = view.findViewById(R.id.etDescEdit);
            TextView tvDateEdit = view.findViewById(R.id.tvDateEdit);
            ImageButton btnPickDateEdit = view.findViewById(R.id.btnPickDateEdit);
            ImageButton btnSendEdit = view.findViewById(R.id.imgSendEit);

            // Get parent view
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();

            // Set data for edit text, text view
            edtTitleEdit.setText(todoDTO.getName());
            edtDescriptionEdit.setText(todoDTO.getContent());
            tvDateEdit.setText(todoDTO.getEndDate());

            // Set event for button pick date
            btnPickDateEdit.setOnClickListener(v1 -> {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Create date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view1, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDateEdit.setText(date);
                }, year, month, day);
                datePickerDialog.show();
            });

            // Set event for button send
            btnSendEdit.setOnClickListener(v12 -> {
                String title = edtTitleEdit.getText().toString().trim();
                String description = edtDescriptionEdit.getText().toString().trim();
                String date = tvDateEdit.getText().toString().trim();

                // If title, description, date is empty, show toast
                if (TextUtils.isEmpty(title)) {
                    edtTitleEdit.setError("Title is empty");
                    edtTitleEdit.requestFocus();
                } else if (TextUtils.isEmpty(description)) {
                    edtDescriptionEdit.setError("Description is empty");
                    edtDescriptionEdit.requestFocus();
                } else {
                    todoDTO.setName(title);
                    todoDTO.setContent(description);
                    todoDTO.setEndDate(date);
                    TodoDAO todoDAO = new TodoDAO(context);
                    int status = todoDAO.updateTodo(todoDTO);

                    // Build and show the notification
                    NotificationCompat.Builder builderNotif;
                    String notificationTitle;
                    String notificationText;
                    int notificationId = (int) System.currentTimeMillis();

                    if (status > 0) {
                        //replace fragment to show done list
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.replaceFragment(new HomeFragment());
                        new CuteDialog.withAnimation(context)
                                .setAnimation(R.raw.suc)
                                .setTitle("Update success")
                                .setTitleTextColor(R.color.black)
                                .setPositiveButtonColor(R.color.black)
                                .setPositiveButtonText("OK", v16 -> {

                                })
                                .hideNegativeButton(true)
                                .hideCloseIcon(true)
                                .show();
                        notifyItemRangeChanged(position, doneItemsList.size());
                        alertDialog.dismiss();

                        notificationTitle = "Update success";
                        notificationText = "Updated todo with ID: " + todoDTO.getId();
                    } else {
                        notificationTitle = "Update fail";
                        notificationText = "Failed to update todo with ID: " + todoDTO.getId();

                    }
                    CreateNotification createNotification = new CreateNotification();
                    createNotification.postNotification(context, notificationTitle, notificationText, notificationId);
                }
            });
            alertDialog.show();
        });
        //Show content when long click
        holder.constraintTodo.setOnLongClickListener(v -> {
            // Create alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.content, null);

            // Create text view, button
            TextView tvContent = view.findViewById(R.id.content);
            Button btnOK = view.findViewById(R.id.btnOK);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }

            // Set data for text view
            tvContent.setText(todoDTO.getContent());
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            btnOK.setOnClickListener(v1 -> alertDialog.dismiss());
            alertDialog.show();
            return true;
        });
    }

    // Get item count of done items list
    @Override
    public int getItemCount() {
        return doneItemsList.size();
    }

    // Create view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Create view
        CheckBox checkBox;
        TextView txtNameTodo, tvDate;
        ImageButton iBEdit, iBDelete;
        ConstraintLayout constraintTodo;
        CardView cardViewTodo;
        SwipeLayout swipeLayout;
        LinearLayout linearLayout;

        // Create constructor
        public ViewHolder(@NonNull View itemView) {
            // Init view
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxAnimation);
            txtNameTodo = itemView.findViewById(R.id.tvNameTodo);
            constraintTodo = itemView.findViewById(R.id.constraintTodo);
            cardViewTodo = itemView.findViewById(R.id.cardViewTodo);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            linearLayout = itemView.findViewById(R.id.layoutCutomize);
            iBEdit = itemView.findViewById(R.id.iBEdit);
            iBDelete = itemView.findViewById(R.id.iBDelete);
            tvDate = itemView.findViewById(R.id.tvDateTodo);
        }
    }

    // Set item appearance
    private void setItemAppearance(ViewHolder holder, int status) {
        if (status == 0) {
            setCompletedAppearance(holder);
        } else {
            setIncompleteAppearance(holder);
        }
    }

    // Set completed appearance
    private void setCompletedAppearance(ViewHolder holder) {
        holder.checkBox.setChecked(true);
        holder.txtNameTodo.setAlpha(0.5f);
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.grayCon, null));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.grayCon, null));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.gray, null));
    }

    // Set incomplete appearance
    private void setIncompleteAppearance(ViewHolder holder) {
        holder.checkBox.setChecked(false);
        holder.txtNameTodo.setAlpha(1f);
        holder.constraintTodo.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.cardViewTodo.setCardBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.txtNameTodo.setPaintFlags(holder.txtNameTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }

}
