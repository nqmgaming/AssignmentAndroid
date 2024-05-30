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
import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.ui.main.fragment.SearchFragment;
import com.nqmgaming.assignment_minhnqph31902.permission.CreateNotification;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.cutelibs.cutedialog.CuteDialog;

public class SearchTodoAdapter extends RecyclerView.Adapter<SearchTodoAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TodoDTO> todoDTOArrayList;
    TodoDAO todoDAO;
    private final ViewBinder viewBinderSearch = new ViewBinder();

    public SearchTodoAdapter(Context context, ArrayList<TodoDTO> todoDTOArrayList) {
        this.context = context;
        this.todoDTOArrayList = todoDTOArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View itemView = inflater.inflate(R.layout.item_seach, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        TodoDTO todoDTO = todoDTOArrayList.get(position);
        holder.tvNameSearch.setText(todoDTO.getName());
        holder.tvDateSearch.setText(todoDTO.getEndDate());
        setItemAppearance(holder, todoDTO.getStatus());
        holder.cbSearch.setOnClickListener(v -> {
            if (holder.cbSearch.isChecked()) {
                todoDTO.setStatus(0);
                setCompletedAppearance(holder);
                todoDAO = new TodoDAO(context);
                todoDAO.setStatusTodo(todoDTO);

            } else {
                setIncompleteAppearance(holder);
                todoDTO.setStatus(1);
                todoDAO = new TodoDAO(context);
                todoDAO.setStatusTodo(todoDTO);
            }
        });

        viewBinderSearch.bind(holder.swipeLayout, String.valueOf(todoDTO.getId()));
        holder.ibDelete.setOnClickListener(v -> {
            todoDAO = new TodoDAO(context);
            int status = todoDAO.deleteTodo(todoDTO);
            if (status > 0) {
                todoDTOArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, todoDTOArrayList.size());
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
                String title = "Delete success";
                String message = "Delete todo id: " + todoDTO.getId() + " success" + "\n" + "Name: " + todoDTO.getName();
                int id = todoDTO.getId();
                CreateNotification createNotification = new CreateNotification();
                createNotification.postNotification(context, title, message, id);
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
            }
        });
        holder.ibEdit.setOnClickListener(v -> {
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
                    NotificationCompat.Builder builderNotif;
                    String notificationTitle;
                    String notificationText;
                    int notificationId;

                    //check status
                    if (status > 0) {
                        //replace fragment to show done list
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.replaceFragment(new SearchFragment());
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
                        notifyItemRangeChanged(position, todoDTOArrayList.size());
                        alertDialog.dismiss();

                        notificationTitle = "Update success";
                        notificationText = "Updated todo with ID: " + todoDTO.getId();
                        notificationId = 2;  // Choose appropriate ID
                    } else {
                        new CuteDialog.withAnimation(context)
                                .setAnimation(R.raw.error)
                                .setTitle("Update fail")
                                .setTitleTextColor(R.color.black)
                                .setPositiveButtonColor(R.color.black)
                                .setPositiveButtonText("OK", v15 -> {
                                })
                                .hideNegativeButton(true)
                                .hideCloseIcon(true)
                                .show();

                        notificationTitle = "Update fail";
                        notificationText = "Failed to update todo with ID: " + todoDTO.getId();
                        notificationId = 3;  // Choose appropriate ID
                    }
                    // Create an intent to open the desired activity when notification is clicked
                    CreateNotification createNotification = new CreateNotification();
                    createNotification.postNotification(context, notificationTitle, notificationText, notificationId);
                }
            });
            alertDialog.show();
        });

        //set event for item show content
        holder.constraintSearch.setOnLongClickListener(v -> {
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
            btnOK.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return todoDTOArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbSearch;
        TextView tvNameSearch;
        TextView tvDateSearch;

        SwipeLayout swipeLayout;
        LinearLayout llDelete;
        ImageButton ibDelete, ibEdit;
        CardView cvSearch;
        ConstraintLayout constraintSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cbSearch = itemView.findViewById(R.id.checkboxAnimationSearch);
            tvNameSearch = itemView.findViewById(R.id.tvNameTodoSearch);
            tvDateSearch = itemView.findViewById(R.id.tvDateTodoEarch);
            swipeLayout = itemView.findViewById(R.id.swipeLayoutSearch);
            ibDelete = itemView.findViewById(R.id.ibDeleteSearch);
            ibEdit = itemView.findViewById(R.id.ibEditSearch);
            cvSearch = itemView.findViewById(R.id.cardViewTodoSearch);
            llDelete = itemView.findViewById(R.id.layoutCutomizeSearch);
            constraintSearch = itemView.findViewById(R.id.constraintTodoSearch);

        }

    }

    private void setItemAppearance(SearchTodoAdapter.ViewHolder holder, int status) {
        if (status == 1) {
            setIncompleteAppearance(holder);
        } else {
            setCompletedAppearance(holder);
        }
    }

    //set appearance for item when it is done
    private void setCompletedAppearance(SearchTodoAdapter.ViewHolder holder) {
        holder.cbSearch.setChecked(true);
        holder.tvNameSearch.setAlpha(0.5f);
        holder.constraintSearch.setBackgroundColor(context.getResources().getColor(R.color.gray, null));
        holder.cvSearch.setCardBackgroundColor(context.getResources().getColor(R.color.gray, null));
        holder.tvNameSearch.setPaintFlags(holder.tvNameSearch.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cbSearch.setButtonTintList(context.getResources().getColorStateList(R.color.active, null));
    }

    //set appearance for item when it is not done
    private void setIncompleteAppearance(SearchTodoAdapter.ViewHolder holder) {
        holder.cbSearch.setChecked(false);
        holder.tvNameSearch.setAlpha(1f);
        holder.constraintSearch.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.cvSearch.setCardBackgroundColor(context.getResources().getColor(R.color.white, null));
        holder.tvNameSearch.setPaintFlags(holder.tvNameSearch.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        holder.cbSearch.setButtonTintList(context.getResources().getColorStateList(R.color.black, null));
    }
}
