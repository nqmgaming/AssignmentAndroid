package com.nqmgaming.assignment_minhnqph31902.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.assignment_minhnqph31902.dto.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.database.DataBaseTodoAppHelper;

import java.util.ArrayList;

public class TodoDAO {
    DataBaseTodoAppHelper dataBaseTodoAppHelper;
    SQLiteDatabase sqLiteDatabase;

    public TodoDAO(Context context) {

        dataBaseTodoAppHelper = new DataBaseTodoAppHelper(context);
        sqLiteDatabase = dataBaseTodoAppHelper.getWritableDatabase();

    }

    //Insert new todo return long
    public long insertTodo(TodoDTO todoDTO) {

        //create result variable
        long result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("id", todoDTO.getId());
        contentValues.put("name", todoDTO.getName());
        contentValues.put("content", todoDTO.getContent());
        contentValues.put("status", todoDTO.getStatus());
        contentValues.put("start_date", todoDTO.getStartDate());
        contentValues.put("end_date", todoDTO.getEndDate());
        contentValues.put("user_id", todoDTO.getUserId());

        //try catch
        try {

            //insert data to database
            result = sqLiteDatabase.insertOrThrow("todo_table", null, contentValues);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Delete todo return int
    public int deleteTodo(TodoDTO todoDTO) {

        //create result variable
        int result = -1;

        //try catch
        try {

            //delete data from database
            result = sqLiteDatabase.delete("todo_table", "id=?", new String[]{String.valueOf(todoDTO.getId())});

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Update todo return int
    public int updateTodo(TodoDTO todoDTO) {

        //create result variable
        int result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("name", todoDTO.getName());
        contentValues.put("content", todoDTO.getContent());
        contentValues.put("status", todoDTO.getStatus());
        contentValues.put("start_date", todoDTO.getStartDate());
        contentValues.put("end_date", todoDTO.getEndDate());
        contentValues.put("user_id", todoDTO.getUserId());

        //try catch
        try {

            //update data to database
            result = sqLiteDatabase.update("todo_table", contentValues, "id=?", new String[]{String.valueOf(todoDTO.getId())});

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Get all todo return array list
    public ArrayList<TodoDTO> getAllTodo() {

        //create array list
        ArrayList<TodoDTO> todoDTOArrayList = new ArrayList<>();

        //create query string
        String query = "SELECT * FROM todo_table";

        //create cursor
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        //check cursor
        if (cursor != null) {

            //check cursor count
            if (cursor.getCount() > 0) {

                //move to first
                cursor.moveToFirst();

                //loop
                do {

                    //create todo dto
                    TodoDTO todoDTO = new TodoDTO();

                    //set data to todo dto
                    todoDTO.setId(cursor.getInt(0));
                    todoDTO.setName(cursor.getString(1));
                    todoDTO.setContent(cursor.getString(2));
                    todoDTO.setStatus(cursor.getInt(3));
                    todoDTO.setStartDate(cursor.getString(4));
                    todoDTO.setEndDate(cursor.getString(5));
                    todoDTO.setUserId(cursor.getInt(6));

                    //add todo dto to array list
                    todoDTOArrayList.add(todoDTO);

                } while (cursor.moveToNext());

            }

        }
        assert cursor != null;
        cursor.close();

        //return array list
        return todoDTOArrayList;
    }

    //Set status todo return int
    public int setStatusTodo(TodoDTO todoDTO) {

        //create result variable
        int result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("status", todoDTO.getStatus());

        //try catch
        try {

            //update data to database
            result = sqLiteDatabase.update("todo_table", contentValues, "id=?", new String[]{String.valueOf(todoDTO.getId())});

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //delete all todo return int
    public int deleteAllTodo() {

        //create result variable
        int result = -1;

        //try catch
        try {

            //delete data from database
            result = sqLiteDatabase.delete("todo_table", null, null);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Done all set status todo equal 1 return int
    public int doneAllTodo() {

        //create result variable
        int result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("status", 0);

        //try catch
        try {

            //update data to database
            result = sqLiteDatabase.update("todo_table", contentValues, null, null);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //get all todo by user id return array list
    public ArrayList<TodoDTO> getAllTodoByUserId(int userId) {

        //create array list
        ArrayList<TodoDTO> todoDTOArrayList = new ArrayList<>();

        //create query string
        String query = "SELECT * FROM todo_table WHERE user_id = " + userId;

        //create cursor
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        //check cursor
        if (cursor != null) {

            //check cursor count
            if (cursor.getCount() > 0) {

                //move to first
                cursor.moveToFirst();

                //loop
                do {

                    //create todo dto
                    TodoDTO todoDTO = new TodoDTO();

                    //set data to todo dto
                    todoDTO.setId(cursor.getInt(0));
                    todoDTO.setName(cursor.getString(1));
                    todoDTO.setContent(cursor.getString(2));
                    todoDTO.setStatus(cursor.getInt(3));
                    todoDTO.setStartDate(cursor.getString(4));
                    todoDTO.setEndDate(cursor.getString(5));
                    todoDTO.setUserId(cursor.getInt(6));

                    //add todo dto to array list
                    todoDTOArrayList.add(todoDTO);

                } while (cursor.moveToNext());

            }

            cursor.close();


        }

        //return array list
        return todoDTOArrayList;
    }
}
