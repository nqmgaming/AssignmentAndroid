package com.nqmgaming.assignment_minhnqph31902.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.assignment_minhnqph31902.dto.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.database.DataBaseTodoAppHelper;

public class UserDAO {

    //create variables
    DataBaseTodoAppHelper dataBaseTodoAppHelper;
    SQLiteDatabase sqLiteDatabase;

    //constructor
    public UserDAO(Context context) {

        dataBaseTodoAppHelper = new DataBaseTodoAppHelper(context);
        sqLiteDatabase = dataBaseTodoAppHelper.getWritableDatabase();

    }

    //Insert new user return long
    public long insertUser(UserDTO userDTO) {

        //create result variable
        long result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("username", userDTO.getUsername());
        contentValues.put("email", userDTO.getEmail());
        contentValues.put("password", userDTO.getPassword());
        contentValues.put("firstname", userDTO.getFirstname());
        contentValues.put("lastname", userDTO.getLastname());

        //try catch
        try {

            //insert data to database
            result = sqLiteDatabase.insertOrThrow("user_table", null, contentValues);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Delete user return int
    public int deleteUser(UserDTO userDTO) {

        //create result variable
        int result = -1;

        //create condition
        String[] condition = new String[]{
                String.valueOf(userDTO.getId())
        };

        //try catch
        try {

            //delete user
            result = sqLiteDatabase.delete("user_table", "id = ?", condition);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;

    }

    //Update user return int
    public int updateUser(UserDTO userDTO) {

        //create result variable
        int result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("username", userDTO.getUsername());
        contentValues.put("email", userDTO.getEmail());
        contentValues.put("password", userDTO.getPassword());
        contentValues.put("firstname", userDTO.getFirstname());
        contentValues.put("lastname", userDTO.getLastname());

        //create condition
        String[] condition = new String[]{String.valueOf(userDTO.getId())};

        try {

            //update user
            result = sqLiteDatabase.update("user_table", contentValues, "id = ?", condition);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;

    }

    //Update Password where id

    //Check user exist return boolean
    public boolean checkUserExist(String username) {

        //create result variable
        boolean result = false;

        //create condition
        String[] condition = new String[]{
                username
        };

        //try catch
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ?", condition);

            //check cursor if move to first return true
            if (cursor.moveToFirst()) {

                //set result to true if cursor move to first because user exist
                result = true;

            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Check email exist return boolean
    public boolean checkEmailExist(String email) {
        boolean result = false;
        String[] condition = new String[]{
                email
        };

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE email = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //check account match return boolean
    public boolean checkAccount(String email, String password) {

        //create result variable
        boolean result = false;

        //create condition
        String[] condition = new String[]{
                email, password
        };

        //try catch
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE email = ? AND password = ?", condition);

            //check cursor if move to first return true
            if (cursor.moveToFirst()) {

                //set result to true if cursor move to first because account match
                result = true;

            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        //return result
        return result;
    }

    //check username and email match return boolean
    public boolean checkUsernameAndEmail(String username, String email) {

        //create result variable
        boolean result = false;

        //create condition
        String[] condition = new String[]{
                username, email
        };

        //try catch
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ? AND email = ?", condition);

            //check cursor if move to first return true
            if (cursor.moveToFirst()) {
                result = true;
            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;

    }

    //check username and email after return UserDTO
    public UserDTO checkUsernameAndEmailAfter(String username, String email) {

        //create userDTO variable
        UserDTO userDTO = null;

        //create condition
        String[] condition = new String[]{
                username, email
        };
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ? AND email = ?", condition);

            //check cursor if move to first set data to userDTO
            if (cursor.moveToFirst()) {

                //create userDTO
                userDTO = new UserDTO();

                //set data to userDTO
                userDTO.setId(cursor.getInt(0));
                userDTO.setUsername(cursor.getString(1));
                userDTO.setEmail(cursor.getString(2));
                userDTO.setPassword(cursor.getString(3));
                userDTO.setFirstname(cursor.getString(4));
                userDTO.setLastname(cursor.getString(5));

            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return userDTO
        return userDTO;

    }

    //Get id by email
    public int getIdByEmail(String email) {

        //create result variable
        int result = -1;

        //create condition
        String[] condition = new String[]{
                email
        };

        //try catch
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE email = ?", condition);

            //check cursor if move to first return true
            if (cursor.moveToFirst()) {

                //set result to true if cursor move to first because account match
                result = cursor.getInt(0);

            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Get user by id return UserDTO
    public UserDTO getUserById(int id) {

        //create userDTO variable
        UserDTO userDTO = null;

        //create condition
        String[] condition = new String[]{
                String.valueOf(id)
        };

        //try catch
        try {

            //create cursor
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE id = ?", condition);

            //check cursor if move to first set data to userDTO
            if (cursor.moveToFirst()) {

                //create userDTO
                userDTO = new UserDTO();

                //set data to userDTO
                userDTO.setId(cursor.getInt(0));
                userDTO.setUsername(cursor.getString(1));
                userDTO.setEmail(cursor.getString(2));
                userDTO.setPassword(cursor.getString(3));
                userDTO.setFirstname(cursor.getString(4));
                userDTO.setLastname(cursor.getString(5));

            }

            //close cursor because we don't need it anymore
            cursor.close();

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return userDTO
        return userDTO;

    }
}
