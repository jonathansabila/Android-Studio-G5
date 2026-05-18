package com.example.dietreport;

public class Record {
    private int id;
    private String name;
    private String age;
    private String room;
    private String dateTime;
    private String religion;
    private String diet;
    private byte[] signature;

    public Record(int id, String name, String age, String room,
                  String dateTime, String religion, String diet, byte[] signature) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.room = room;
        this.dateTime = dateTime;
        this.religion = religion;
        this.diet = diet;
        this.signature = signature;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getRoom() { return room; }
    public String getDateTime() { return dateTime; }
    public String getReligion() { return religion; }
    public String getDiet() { return diet; }
    public byte[] getSignature() { return signature; }
}