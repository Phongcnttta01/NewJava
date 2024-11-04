package BTVN3;

import java.util.Scanner;

public class Student {
    private String studentCode;

    private String fullName;
    private byte age;
    private String gender;
    private String phoneNumber;
    private String email;

    public Student(){}

    public Student(String studentCode, String fullName, byte age, String gender, String phoneNumber, String email) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public byte getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void inputData(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Student code : ");
        System.out.print("Full name : ");
        System.out.print("Age : ");
        System.out.print("Gender : ");
        System.out.print("Phone number : ");
        System.out.print("Email : ");
    }
    public String display(){
        return "Student code : " + studentCode + "\nFull name : " + fullName + "\nAge : " + age + "\nGender : " + gender + "\nPhone number : " + phoneNumber + "\nEmail : "+email;
    }
}
