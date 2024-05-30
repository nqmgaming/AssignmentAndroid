package com.nqmgaming.assignment_minhnqph31902.ui.main.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.assignment_minhnqph31902.permission.CreateNotification;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.adapter.DoneTodoAdapter;
import com.nqmgaming.assignment_minhnqph31902.adapter.TodoAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.github.cutelibs.cutedialog.CuteDialog;

public class HomeFragment extends Fragment {
    TodoDAO todoDAO;
    ArrayList<TodoDTO> todoDTOArrayList;
    ArrayList<TodoDTO> doneItemsList = new ArrayList<>();
    ArrayList<TodoDTO> notDoneItemsList = new ArrayList<>();
    TodoAdapter todoAdapter;
    DoneTodoAdapter doneTodoAdapter;
    UserPreferences userPreferences;
    FloatingActionButton fabAddTodo;
    FloatingActionButton fabDeleteAll;
    FloatingActionButton fabDoneAll;
    private boolean isSubMenuOpen = false;
    LottieAnimationView lottieEmpty;
    ScrollView scrollTodoList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            fabAddTodo = view.findViewById(R.id.fabAddTodo);
            fabDeleteAll = view.findViewById(R.id.fabDeleteAll);
            fabDoneAll = view.findViewById(R.id.doneAll);
            lottieEmpty = view.findViewById(R.id.empty_anim);
            scrollTodoList = view.findViewById(R.id.scrollTodoList);

            RecyclerView todoRecyclerView = view.findViewById(R.id.rcTodo);
            RecyclerView notTodoRecyclerView = view.findViewById(R.id.rcNotTodo);

            //try catch
            try {

                //Get id user from shared preferences
                userPreferences = new UserPreferences(requireContext());
                int idUser = userPreferences.getIdUser();

                todoDAO = new TodoDAO(getContext());
                todoDTOArrayList = todoDAO.getAllTodoByUserId(idUser);
                userPreferences = new UserPreferences(requireContext());
                //chỉ tạo duy nhất 1 lần cho 1 người dùng

                if (todoDTOArrayList.size() == 0 && !userPreferences.getFirstTime()) {
                    // Create 4 data entries
                    for (int i = 0; i < 3; i++) {
                        int id = idUser + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + i;

                        TodoDTO todoDTO = new TodoDTO();
                        todoDTO.setId(id);
                        todoDTO.setName("Todo " + (i + 1));
                        todoDTO.setContent("Content " + (i + 1));
                        if (id % 2 == 0) {
                            todoDTO.setStatus(0);
                        } else {
                            todoDTO.setStatus(1);
                        }
                        todoDTO.setStartDate("01/01/2021");
                        todoDTO.setEndDate("01/01/2021");
                        todoDTO.setUserId(idUser);
                        long status = todoDAO.insertTodo(todoDTO);
                        if (status > 0) {
                            Toast.makeText(getContext(), "Add todo success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Add todo fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    userPreferences.setFirstTime(true);
                    todoDTOArrayList = todoDAO.getAllTodoByUserId(idUser);
                    doneItemsList.clear();
                    notDoneItemsList.clear();
                    for (TodoDTO todo : todoDTOArrayList) {
                        if (todo.getStatus() == 1) {
                            doneItemsList.add(todo);
                        } else {
                            notDoneItemsList.add(todo);
                        }
                    }
                } else {
                    if (todoDTOArrayList.size() == 0) {
                        scrollTodoList.setVisibility(View.GONE);
                        lottieEmpty.setVisibility(View.VISIBLE);
                    } else {
                        scrollTodoList.setVisibility(View.VISIBLE);
                        lottieEmpty.setVisibility(View.GONE);
                        for (TodoDTO todo : todoDTOArrayList) {
                            if (todo.getStatus() == 1) {
                                doneItemsList.add(todo);
                            } else {
                                notDoneItemsList.add(todo);
                            }
                        }
                    }

                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //try catch
            try {
                // Set item cho notTodoRecyclerView
                doneTodoAdapter = new DoneTodoAdapter(getContext(), notDoneItemsList, doneItemsList);
                notTodoRecyclerView.setAdapter(doneTodoAdapter);
                notTodoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // Set item cho todoRecyclerView
                todoAdapter = new TodoAdapter(getContext(), doneItemsList, notDoneItemsList);
                todoRecyclerView.setAdapter(todoAdapter);
                todoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            } catch (Exception e) {

                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            fabAddTodo.setOnLongClickListener(v -> {

                toggleSubMenu();
                return true;

            });

            fabAddTodo.setOnClickListener(v -> {
                //Build a custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View view1 = getLayoutInflater().inflate(R.layout.add_todo, null);

                EditText edtTitle = view1.findViewById(R.id.etTodo);
                EditText edtContent = view1.findViewById(R.id.etDesc);
                TextView tvDate = view1.findViewById(R.id.txtDate);
                ImageButton imgSendAdd = view1.findViewById(R.id.imgSendAdd);
                ImageButton imgCalendar = view1.findViewById(R.id.calendarAdd);

                ViewGroup parent = (ViewGroup) view1.getParent();
                if (parent != null) {
                    parent.removeView(view1);
                }
                if (!isSubMenuOpen) {
                    builder.setView(view1);

                }
                final AlertDialog alertDialog = builder.create();
                //if user click fabAddTodo when sub menu is open do nothing
                if (isSubMenuOpen) {
                    alertDialog.dismiss();
                    return;
                }

                //get date from system
                String startDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

                imgCalendar.setOnClickListener(v12 -> {
                    try {
                        // Date picker
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view11, year1, month1, dayOfMonth1) -> {
                            String endDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                            tvDate.setText(endDate);
                            //get date from system
                        }, year, month, dayOfMonth);
                        datePickerDialog.show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                imgSendAdd.setOnClickListener(v1 -> {


                    String title = edtTitle.getText().toString().trim();
                    String content = edtContent.getText().toString().trim();
                    String endDate = tvDate.getText().toString().trim();
                    userPreferences = new UserPreferences(requireContext());
                    int idUser = userPreferences.getIdUser();
                    int id;
                    if (todoDTOArrayList.isEmpty()) {
                        id = idUser + 2004 + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                    } else {
                        // Access the last element of todoDTOArrayList
                        id = idUser + 2004 + todoDTOArrayList.get(todoDTOArrayList.size() - 1).getId();
                    }

                    if (TextUtils.isEmpty(title)) {
                        edtTitle.setError("Please enter title");
                        edtTitle.requestFocus();
                    } else if (TextUtils.isEmpty(content)) {
                        edtTitle.setError("Please enter title");
                        edtTitle.requestFocus();
                    } else {
                        try {
                            TodoDTO todoDTO = new TodoDTO();
                            todoDTO.setId(id);
                            todoDTO.setName(title);
                            todoDTO.setContent(content);
                            todoDTO.setStatus(1);
                            todoDTO.setStartDate(startDate);
                            todoDTO.setEndDate(endDate);
                            todoDTO.setUserId(userPreferences.getIdUser());
                            long status = todoDAO.insertTodo(todoDTO);
                            if (status > 0) {
                                scrollTodoList.setVisibility(View.VISIBLE);
                                lottieEmpty.setVisibility(View.GONE);
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
                                todoDTOArrayList = todoDAO.getAllTodo();
                                notDoneItemsList.clear();
                                doneItemsList.clear();
                                for (TodoDTO todo : todoDTOArrayList) {
                                    if (todo.getStatus() == 1) {
                                        doneItemsList.add(todo);
                                    } else {
                                        notDoneItemsList.add(todo);
                                    }
                                }
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                // Dismiss dialog
                                alertDialog.dismiss();
                                String title_add = "Add success item todo";
                                String content_add = "Add success item todo: " + todoDTO.getName();
                                int id_add = todoDTO.getId();
                                CreateNotification createNotification = new CreateNotification();
                                createNotification.postNotification(requireContext(), title_add, content_add, id_add);

                            } else {
                                new CuteDialog.withAnimation(getContext())
                                        .setAnimation(R.raw.error)
                                        .setTitle("Add fail")
                                        .setTitleTextColor(R.color.black)
                                        .setPositiveButtonColor(R.color.black)
                                        .setPositiveButtonText("OK", v15 -> {

                                        })
                                        .hideNegativeButton(true)
                                        .hideCloseIcon(true)
                                        .show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
                alertDialog.show();

            });


        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        fabDeleteAll.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(getContext());
            new CuteDialog.withIcon(getContext())
                    .setIcon(R.drawable.trash)
                    .hideCloseIcon(true)
                    .setTitleTextColor(R.color.black)
                    .setPositiveButtonColor(R.color.black)
                    .setTitle("Delete all?")
                    .setPositiveButtonText("Yes", v13 -> {
                        try {
                            int result = todoDAO.deleteAllTodo();
                            if (result > 0) {
                                todoDTOArrayList = todoDAO.getAllTodo();
                                notDoneItemsList.clear();
                                doneItemsList.clear();
                                for (TodoDTO todo : todoDTOArrayList) {
                                    if (todo.getStatus() == 1) {
                                        doneItemsList.add(todo);
                                    } else {
                                        notDoneItemsList.add(todo);
                                    }
                                }
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                new CuteDialog.withAnimation(getContext())
                                        .setAnimation(R.raw.suc)
                                        .setTitle("Delete all success")
                                        .setTitleTextColor(R.color.black)
                                        .setPositiveButtonColor(R.color.black)
                                        .setPositiveButtonText("OK", v14 -> {

                                        })
                                        .hideNegativeButton(true)
                                        .hideCloseIcon(true)
                                        .show();
                                scrollTodoList.setVisibility(View.GONE);
                                lottieEmpty.setVisibility(View.VISIBLE);
                            } else {
                                new CuteDialog.withAnimation(getContext())
                                        .setAnimation(R.raw.error)
                                        .setTitle("Delete all fail")
                                        .setTitleTextColor(R.color.black)
                                        .setPositiveButtonColor(R.color.black)
                                        .setPositiveButtonText("OK", v14 -> {

                                        })
                                        .hideNegativeButton(true)
                                        .hideCloseIcon(true)
                                        .show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButtonText("No", v14 -> Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show())
                    .show();
        });

        fabDoneAll.setOnClickListener(v -> {
            TodoDAO todoDAO = new TodoDAO(getContext());
            new CuteDialog.withAnimation(getContext())
                    .setAnimation(R.raw.question)
                    .hideCloseIcon(true)
                    .setTitleTextColor(R.color.black)
                    .setPositiveButtonColor(R.color.black)
                    .setTitle("Done all?")
                    .setPositiveButtonText("Yes", v15 -> {
                        try {
                            int result = todoDAO.doneAllTodo();
                            if (result > 0) {

                                todoDTOArrayList = todoDAO.getAllTodo();
                                notDoneItemsList.clear();
                                doneItemsList.clear();
                                for (TodoDTO todo : todoDTOArrayList) {
                                    if (todo.getStatus() == 1) {
                                        doneItemsList.add(todo);
                                    } else {
                                        notDoneItemsList.add(todo);
                                    }
                                }
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                doneTodoAdapter.notifyDataSetChanged();
                                todoAdapter.notifyDataSetChanged();
                                new CuteDialog.withAnimation(getContext())
                                        .setAnimation(R.raw.suc)
                                        .setTitle("Done all success")
                                        .setTitleTextColor(R.color.black)
                                        .setPositiveButtonColor(R.color.black)
                                        .setPositiveButtonText("OK", v16 -> {

                                        })
                                        .hideNegativeButton(true)
                                        .hideCloseIcon(true)
                                        .show();
                            } else {
                                new CuteDialog.withAnimation(getContext())
                                        .setAnimation(R.raw.error)
                                        .setTitle("Done all fail")
                                        .setTitleTextColor(R.color.black)
                                        .setPositiveButtonColor(R.color.black)
                                        .setPositiveButtonText("OK", v16 -> {

                                        })
                                        .hideNegativeButton(true)
                                        .hideCloseIcon(true)
                                        .show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButtonText("No", v16 -> Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show())
                    .show();
        });


    }


    private void toggleSubMenu() {
        if (isSubMenuOpen) {
            hideSubMenu();
        } else {
            showSubMenu();
        }
    }

    private void showSubMenu() {
        fabDoneAll.setVisibility(View.VISIBLE);
        fabDeleteAll.setVisibility(View.VISIBLE);
        fabAddTodo.setImageDrawable(getResources().getDrawable(R.drawable.close, null));
        isSubMenuOpen = true;
    }

    private void hideSubMenu() {
        fabDoneAll.setVisibility(View.GONE);
        fabDeleteAll.setVisibility(View.GONE);
        fabAddTodo.setImageDrawable(getResources().getDrawable(R.drawable.add, null));
        isSubMenuOpen = false;
    }


}
