package com.nqmgaming.assignment_minhnqph31902.ui.main.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.permission.CreateNotification;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.github.cutelibs.cutedialog.CuteDialog;

public class CalendarFragment extends Fragment {

    private TextView selectedDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        // Set the initial date to the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the date on the TextView when the DatePicker is initialized
        selectedDateTextView.setText(getFormattedDate(year, month, dayOfMonth));

        // Set an OnDateChangedListener to handle date selection
        datePicker.init(year, month, dayOfMonth, (view1, year1, month1, dayOfMonth1) -> {
            // Display the selected date on the TextView
            selectedDateTextView.setText(getFormattedDate(year1, month1, dayOfMonth1));
            showDateAlert(year1, month1, dayOfMonth1);
        });
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        // Format the date in the desired format (e.g., dd/MM/yyyy)
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(calendar.getTime());
    }

    private void showDateAlert(int year, int month, int dayOfMonth) {
        // Create and show the DatePickerDialog with the selected date
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_todo, null);
        EditText edtTitle = dialogView.findViewById(R.id.etTodo);
        EditText edtContent = dialogView.findViewById(R.id.etDesc);
        TextView tvDate = dialogView.findViewById(R.id.txtDate);
        ImageButton imgSendAdd = dialogView.findViewById(R.id.imgSendAdd);
        ImageButton imgCalendar = dialogView.findViewById(R.id.calendarAdd);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeView(dialogView);
        }
        final AlertDialog alertDialog = builder.create();
        tvDate.setText(getFormattedDate(year, month, dayOfMonth));
        alertDialog.setView(dialogView);

        imgCalendar.setOnClickListener(v -> {
            try {
                // Date picker
                Calendar calendar = Calendar.getInstance();
                int year12 = calendar.get(Calendar.YEAR);
                int month12 = calendar.get(Calendar.MONTH);
                int dayOfMonth12 = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth1) -> {
                    String endDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    tvDate.setText(endDate);
                    //get date from system
                }, year12, month12, dayOfMonth12);
                datePickerDialog.show();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //get id from shared preferences
        imgSendAdd.setOnClickListener(v -> {
            UserPreferences userPreferences = new UserPreferences(requireContext());
            int id = userPreferences.getIdUser();
            int idTodo = id + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) % 1000000;
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();

            //validate
            if (TextUtils.isEmpty(title)) {
                edtTitle.setError("Please enter title");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                edtContent.setError("Please enter content");
                return;
            }

            TodoDTO todoDTO = new TodoDTO();
            todoDTO.setUserId(id);
            todoDTO.setName(title);
            todoDTO.setContent(content);
            todoDTO.setEndDate(tvDate.getText().toString());
            todoDTO.setStartDate(tvDate.getText().toString());
            todoDTO.setId(idTodo);
            todoDTO.setStatus(1);
            TodoDAO todoDAO = new TodoDAO(getContext());
            long result = todoDAO.insertTodo(todoDTO);
            if (result > 0) {
                new CuteDialog.withAnimation(getContext())
                        .setAnimation(R.raw.suc)
                        .setTitle("Add success")
                        .setTitleTextColor(R.color.black)
                        .setPositiveButtonColor(R.color.black)
                        .setPositiveButtonText("OK", v15 -> {

                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();
                //create notification
                String titleNoti = "Todo: " + title;
                String contentNoti = "Content: " + content;
                int idNoti = idTodo;
                CreateNotification createNotification = new CreateNotification();
                createNotification.postNotification(requireContext(), titleNoti, contentNoti, idNoti);
            } else {
                new CuteDialog.withAnimation(getContext())
                        .setAnimation(R.raw.error)
                        .setTitle("Add fail")
                        .setTitleTextColor(R.color.black)
                        .setPositiveButtonColor(R.color.black)
                        .setPositiveButtonText("OK", v14 -> {

                        })
                        .hideNegativeButton(true)
                        .hideCloseIcon(true)
                        .show();
            }
        });

        alertDialog.show();
    }
}
