package com.example.fp.androidapp.model;

import com.example.fp.androidapp.MyApplication;

import java.util.List;

/**
 * Created by menachi on 19/04/2017.
 */

public class Model {
   ModelSql Sql;
    public final static Model instace = new Model();

    private Model()
    {
        Sql = new ModelSql(MyApplication.getMyContext());
    }
    public List<Student> getAllStudents(){
        //StudentSql.onUpgrade(Sql.getReadableDatabase(),0,0);
        return StudentSql.getAllStudents(
                Sql.getReadableDatabase());

    }

    public List<Student> getAllStudentsByFilter(String content , String field){
        return StudentSql.getAllStudentsByFilter(
                Sql.getReadableDatabase(),content,field);

    }

    public void addStudent(Student st){
        StudentSql.addStudent(Sql.getWritableDatabase(),st);
    }
    public  void deleteStudent(Student st){StudentSql.deleteStudent(Sql.getWritableDatabase(),st); }
    public  void updateStudent(Student st){StudentSql.updateStudent(Sql.getWritableDatabase(),st); }
    public Student getStudent(String stId) {
        return StudentSql.getStudent(Sql.getReadableDatabase(),stId);

    }

}
