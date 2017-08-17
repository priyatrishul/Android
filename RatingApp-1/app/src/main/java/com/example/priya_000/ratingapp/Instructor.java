package com.example.priya_000.ratingapp;

public class Instructor {


    public Instructor() {
    }

    private int _id;

    public Instructor(int _id, String _firstName, String _lastName, String _email, String _phone, String _office, String _average, String _totalRating) {
        this._id = _id;
        this._firstName = _firstName;
        this._lastName = _lastName;
        this._email = _email;
        this._phone = _phone;
        this._office = _office;
        this._average = _average;
        this._totalRating = _totalRating;
    }

    private String _firstName;
    private String _lastName;
    private String _email;
    private String _phone;
    private String _office;
    private String _average;
    private String _totalRating;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_office() {
        return _office;
    }

    public void set_office(String _office) {
        this._office = _office;
    }

    public String get_average() {
        return _average;
    }

    public void set_average(String _average) {
        this._average = _average;
    }

    public String get_totalRating() {
        return _totalRating;
    }

    public void set_totalRating(String _totalRating) {
        this._totalRating = _totalRating;
    }


}


