package com.hackathon.backend.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hackathon.backend.objects.Course;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseHelper {
    private final static String dictionariesURL = "https://radon.nauka.gov.pl/opendata/polon/dictionaries";
    private final static String coursesURL = "https://radon.nauka.gov.pl/opendata/polon/courses";

    public static String getCoursesCat(final String voievodeship, final String form, final String level, final String category, final boolean isAbroad, final double maxPrice) {
        final List<Course> courses = new ArrayList<>();
        final String voivodshipCode = ApiHelper.getVoivodeshipCode(voievodeship);
        try {
            URL localURL = new URL(coursesURL + "?" +
                    "disciplineName=" + category + "&" +
                    "leadingInstitutionVoivodeshipCode=" + voivodshipCode + "&" +
                    "levelCode=" + level + "&" +
                    "formCode=" + form
            );
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) return "{" +
                    "\"result\": \"Bad Request\"" +
                    "}";
            else {
                final JsonArray json = new Gson().fromJson(str, JsonObject.class).get("results").getAsJsonArray();
                for (int i = 0; i < json.size(); i++) {
                    final JsonElement element = json.get(i);
                    final String courseUuid = element.getAsJsonObject().get("courseUuid").getAsString();
                    final double fees = ApiHelper.getFees(courseUuid, isAbroad);
                    final String address = ApiHelper.getAddress(element.getAsJsonObject().get("leadingInstitutionUuid").getAsString());
                    final String formCode = element.getAsJsonObject().get("courseInstances").getAsJsonArray().get(0).getAsJsonObject().get("formCode").getAsString();

                    final JsonObject secApiDocument = ApiHelper.getSecApiDocument(element.getAsJsonObject().get("leadingInstitutionName").getAsString().trim(),
                            element.getAsJsonObject().get("courseName").getAsString());

                    if (secApiDocument.asMap().containsKey("result")) continue;

                    final Course course = new Course(
                            (element.getAsJsonObject().get("coLeadingInstitutions").getAsJsonArray().isEmpty() ? "Brak" : element.getAsJsonObject().get("coLeadingInstitutions").getAsJsonArray().get(0).getAsString()),
                            element.getAsJsonObject().get("leadingInstitutionName").getAsString(),
                            element.getAsJsonObject().get("courseName").getAsString(),
                            fees,
                            json.get(i).getAsJsonObject().get("levelCode").getAsString(),
                            formCode,
                            address,
                            secApiDocument.get("courseDescription").getAsString(),
                            secApiDocument.get("www").getAsString(),
                            secApiDocument.get("recruitmentPageUrl").getAsString(),
                            secApiDocument.get("requiredSubjects").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList(),
                            secApiDocument.get("requiredSubjectsToSelect").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList(),
                            secApiDocument.get("iconId").getAsString(),
                            secApiDocument.get("phone").getAsString(),
                            secApiDocument.get("email").getAsString()
                    );

                    boolean isSame = false;
                    for (final Course course1 : courses) {
                        if (course1.equals(course)) {
                            isSame = true;
                            break;
                        }
                    }
                    if (fees > maxPrice) continue;
                    if (isSame) continue;
                    courses.add(course);
                }
                final JsonObject jsonCourses = new JsonObject();
                int i = 0;
                for (Course course : courses) {
                    jsonCourses.add("Course-" + i, new Gson().toJsonTree(course));
                    i++;
                }
                return jsonCourses.toString();
            }
        } catch (Exception var6) {
            return "{" +
                    "\"result\": \"File Not Found\"" +
                    "}";
        }
    }


    public static String getCourses(final String voievodeship, final String form, final String level, final boolean isAbroad, final double maxPrice) {
        final List<Course> courses = new ArrayList<>();
        final String voivodshipCode = ApiHelper.getVoivodeshipCode(voievodeship);
        try {
            URL localURL = new URL(coursesURL + "?" +
                    "leadingInstitutionVoivodeshipCode=" + voivodshipCode + "&" +
                    "levelCode=" + level + "&" +
                    "formCode=" + form
            );
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) return "error";
            else {
                final JsonArray json = new Gson().fromJson(str, JsonObject.class).get("results").getAsJsonArray();
                for (int i = 0; i < json.size(); i++) {
                    final JsonElement element = json.get(i);
                    final String courseUuid = element.getAsJsonObject().get("courseUuid").getAsString();
                    final double fees = ApiHelper.getFees(courseUuid, isAbroad);
                    final String address = ApiHelper.getAddress(element.getAsJsonObject().get("leadingInstitutionUuid").getAsString());
                    final String formCode = element.getAsJsonObject().get("courseInstances").getAsJsonArray().get(0).getAsJsonObject().get("formCode").getAsString();

                    final JsonObject secApiDocument = ApiHelper.getSecApiDocument(element.getAsJsonObject().get("leadingInstitutionName").getAsString().trim(),
                            element.getAsJsonObject().get("courseName").getAsString());

                    final Course course = new Course(
                            (element.getAsJsonObject().get("coLeadingInstitutions").getAsJsonArray().isEmpty() ? "Brak" : element.getAsJsonObject().get("coLeadingInstitutions").getAsJsonArray().get(0).getAsString()),
                            element.getAsJsonObject().get("leadingInstitutionName").getAsString(),
                            element.getAsJsonObject().get("courseName").getAsString(),
                            fees,
                            json.get(i).getAsJsonObject().get("levelCode").getAsString(),
                            formCode,
                            address,
                            secApiDocument.get("courseDescription").getAsString(),
                            secApiDocument.get("www").getAsString(),
                            secApiDocument.get("recruitmentPageUrl").getAsString(),
                            secApiDocument.get("requiredSubjects").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList(),
                            secApiDocument.get("requiredSubjectsToSelect").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList(),
                            secApiDocument.get("iconId").getAsString(),
                            secApiDocument.get("phone").getAsString(),
                            secApiDocument.get("email").getAsString()
                    );

                    boolean isSame = false;
                    for (final Course course1 : courses) {
                        if (course1.equals(course)) {
                            isSame = true;
                            break;
                        }
                    }
                    if (fees > maxPrice) continue;
                    if (isSame) continue;
                    courses.add(course);
                }
                final JsonObject jsonCourses = new JsonObject();
                int i = 0;
                for (Course course : courses) {
                    jsonCourses.add("Course-" + i, new Gson().toJsonTree(course));
                    i++;
                }
                return jsonCourses.toString();
            }
        } catch (Exception var6) {
            return "error";
        }
    }
}
