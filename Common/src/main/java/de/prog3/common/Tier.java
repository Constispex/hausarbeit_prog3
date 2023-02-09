package de.prog3.common;

public class Tier {
    String name;
    int age;

    public Tier() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Tier{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
