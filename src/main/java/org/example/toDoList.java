package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class toDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String name;

    public toDoList() {}

    public toDoList(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {}

    public String getName() {
        return name;
    }
    public void setName(String name) {}

    @Override
    public String toString() {
        return "toDoList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
