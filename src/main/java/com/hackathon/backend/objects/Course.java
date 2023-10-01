package com.hackathon.backend.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Course {
    private final String institutionName;
    private final String leadingInstitutionName;
    private final String courseName;
    private final double amount;
    private final String level;
    private final String form;
    private final String address;
    private final String description;
    private final String www;
    private final String rectuitmentWWW;
    private final List<String> requiredSubjects;
    private final List<String> requiredSubjectsToSelect;
    private final String iconId;
    private final String phone;
    private final String email;


    public Course(String institutionName, String leadingInstitutionName, String courseName, double amount, String level, String form, String address, String description,
                  String www, String rectuitmentWWW, List<String> requiredSubjects, List<String> requiredSubjectsToSelect, String iconId, String phone, String email) {
        this.institutionName = institutionName;
        this.leadingInstitutionName = leadingInstitutionName;
        this.courseName = courseName;
        this.amount = amount;
        this.level = level;
        this.form = form;
        this.address = address;
        this.description = description;
        this.www = www;
        this.rectuitmentWWW = rectuitmentWWW;
        this.requiredSubjects = requiredSubjects;
        this.requiredSubjectsToSelect = requiredSubjectsToSelect;
        this.iconId = iconId;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "institutionName: " + institutionName + "\n" +
                "leadingInstitutionName: " + leadingInstitutionName + "\n" +
                "courseName: " + courseName + "\n" +
                "amount: " + amount + "\n" +
                "level: " + level + "\n" +
                "form: " + form + "\n" +
                "address: " + address + "\n" +
                "description: " + description + "\n" +
                "www: " + www + "\n" +
                "rectuitmentWWW: " + rectuitmentWWW + "\n" +
                "requiredSubjects: " + requiredSubjects + "\n" +
                "requiredSubjectsToSelect: " + requiredSubjectsToSelect + "\n" +
                "iconId: " + iconId + "\n" +
                "phone: " + phone + "\n" +
                "email: " + email + "\n";
    }

    public boolean equals(final Course course) {
        return this.institutionName.equals(course.institutionName) &&
                this.leadingInstitutionName.equals(course.leadingInstitutionName) &&
                this.courseName.equals(course.courseName) &&
                this.amount == course.amount &&
                this.level.equals(course.level) &&
                this.form.equals(course.form) &&
                this.address.equals(course.address) &&
                this.description.equals(course.description) &&
                this.www.equals(course.www) &&
                this.rectuitmentWWW.equals(course.rectuitmentWWW) &&
                this.requiredSubjects.equals(course.requiredSubjects) &&
                this.requiredSubjectsToSelect.equals(course.requiredSubjectsToSelect) &&
                this.iconId.equals(course.iconId) &&
                this.phone.equals(course.phone) &&
                this.email.equals(course.email);
    }

}
