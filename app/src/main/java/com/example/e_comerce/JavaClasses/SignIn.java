package com.example.e_comerce.JavaClasses;

import com.example.e_comerce.DatabaseAccess.AccessingDataBase.DbAccsesUser;

public class SignIn {


    private DbAccsesUser UserDBManager;
    public SignIn( DbAccsesUser UserDBManager)
    {
        this.UserDBManager = UserDBManager;
    }

    public User  Authenticate(String username,String password )
    {
        
        return UserDBManager.CheckUserExists(username,password);

    }





}
