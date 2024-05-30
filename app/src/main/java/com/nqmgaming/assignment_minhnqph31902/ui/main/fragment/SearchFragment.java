package com.nqmgaming.assignment_minhnqph31902.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.nqmgaming.assignment_minhnqph31902.dao.TodoDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.adapter.SearchTodoAdapter;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    SearchTodoAdapter searchTodoAdapter;
    ArrayList<TodoDTO> todoDTOArrayList;
    UserPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView svSearch = view.findViewById(R.id.searchView);
        RecyclerView rvSearch = view.findViewById(R.id.rvSearch);
        LottieAnimationView emptySearch = view.findViewById(R.id.emptySearch);

        preferences = new UserPreferences(getContext());
        int userId = preferences.getIdUser();
        TodoDAO todoDAO = new TodoDAO(getContext());
        todoDTOArrayList = todoDAO.getAllTodoByUserId(userId);

        searchTodoAdapter = new SearchTodoAdapter(getContext(), todoDTOArrayList);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearch.setAdapter(searchTodoAdapter);

        emptySearch.setVisibility(View.GONE);

        // Set the OnQueryTextListener to handle search functionality
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the data based on the search query
                ArrayList<TodoDTO> filteredList = new ArrayList<>();
                for (TodoDTO todo : todoDTOArrayList) {
                    if (todo.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(todo);
                    }
                }

                // Update the adapter with new data
                rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
                searchTodoAdapter = new SearchTodoAdapter(getContext(), filteredList);
                rvSearch.setAdapter(searchTodoAdapter);
                searchTodoAdapter.notifyDataSetChanged();

                // Hide or show the empty search animation view based on the result
                if (filteredList.isEmpty()) {
                    emptySearch.setVisibility(View.VISIBLE);
                } else {
                    emptySearch.setVisibility(View.GONE);
                }

                return true;
            }
        });
    }


}