package com.unict.dieei.psd.springexample.userservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    private ObjectId _id;
    private String name;
    private String surname;
    private String address;
    private String phoneNo;
/*
    @JsonCreator
    public User(String name) {
        this.name = name;
        this._id = new ObjectId();
    }
    
 */
    @JsonCreator
    public User(String name, String surname, String address, String phoneNo) {
        this.name = name;
        this._id = new ObjectId();
        this.surname = surname;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    public ObjectId get_id() {
        return _id;
    }

    @JsonGetter("_id")
    public String get_id_string() {
        return _id.toHexString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
