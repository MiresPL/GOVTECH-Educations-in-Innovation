package com.hackathon.backend.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ApiHelper {
    public static String getVoivodeshipCode(final String voivodeship) {
        try {
            URL localURL = new URL("https://radon.nauka.gov.pl/opendata/polon/dictionaries/shared/voivodeships");
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) return "{" +
                    "\"result\": \"Bad Request (Voivodeship Code)\"" +
                    "}";
            else {
                return new Gson().fromJson(str, JsonArray.class).asList().stream().filter(element -> element.getAsJsonObject().get("namePl").getAsString().equals(voivodeship))
                        .toList().get(0).getAsJsonObject().get("code").getAsString();
            }
        } catch (Exception e) {
            return "{" +
                    "\"result\": \"File Not Found (Voivodeship Code)\"" +
                    "}";
        }
    }

    public static double getFees(final String courseUuid, final boolean isAbroad) {
        try {
            URL localURL = new URL("https://radon.nauka.gov.pl/opendata/polon/courseFees?courseUuid=" + courseUuid);
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) return -1;
            else {
                AtomicReference<Double> sum = new AtomicReference<>((double) 0);
                final JsonArray fees = new Gson().fromJson(str, JsonObject.class).get("results").getAsJsonArray();
                if (fees.isEmpty()) return 0;
                fees.get(0).getAsJsonObject().get("fees").getAsJsonArray().asList()
                        .stream().filter(fee -> fee.getAsJsonObject().get("kindCode").getAsInt() == 1 || fee.getAsJsonObject().get("kindCode").getAsInt() == 6
                                || (isAbroad && fee.getAsJsonObject().get("kindCode").getAsInt() == 5)).toList().forEach(fee -> {
                            sum.updateAndGet(v -> v + fee.getAsJsonObject().get("amount").getAsDouble());
                });

                return sum.get().doubleValue();
            }
        } catch (FileNotFoundException var6) {
            return -1;
        } catch (Exception var6) {
            var6.printStackTrace();
            return -3;
        }
    }


    public static String getAddress(final String institutionUuid) {
        try {
            URL localURL = new URL("https://radon.nauka.gov.pl/opendata/polon/institutions?institutionUuid=" + institutionUuid);
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) return "{" +
                    "\"result\": \"Bad Request (Address)\"" +
                    "}";
            else {
                final JsonArray json = new Gson().fromJson(str, JsonObject.class).get("results").getAsJsonArray();
                final JsonObject address = json.get(0).getAsJsonObject().get("addresses").getAsJsonArray().get(0).getAsJsonObject();

                return address.get("street").getAsString() + " " + address.get("bNumber").getAsString() + ", " + address.get("city").getAsString();
            }
        } catch (Exception e) {
            return "{" +
                    "\"result\": \"File Not Found (Address)\"" +
                    "}";
        }
    }



    public static JsonObject getSecApiDocument(final String institutionName, final String path) {
        final JsonObject error = new JsonObject();
        try {
            URL localURL = new URL("https://aplikacje.edukacja.gov.pl/api/internal-data-hub/public/opi/university");
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) {
                error.add("result", new Gson().toJsonTree( "Bad Request (SecApiDocument)"));
                return error;
            }
            else {
                final JsonArray list = new Gson().fromJson(str, JsonObject.class).get("list").getAsJsonArray();

                final String institutionUuid = list.asList().stream().filter(listContent -> listContent.getAsJsonObject().get("universityName").getAsString().equals(institutionName))
                        .toList().get(0).getAsJsonObject().get("id").getAsString();

                return getPathInfo(institutionUuid, path);
            }
        } catch (Exception e) {
            error.add("result", new Gson().toJsonTree( "File Not Found (Sec Api)"));
            return error;
        }
    }

    private static JsonObject getSemiPathInfo(final String institutionUuid, final String path) {
        final JsonObject error = new JsonObject();
        try {
            URL localURL = new URL("https://aplikacje.edukacja.gov.pl/api/internal-data-hub/public/opi/university/" + institutionUuid + "/course");
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) {
                error.add("result", new Gson().toJsonTree( "Bad Request (Get Semi Path Info)"));
                return error;
            }
            else {
                final JsonArray list = new Gson().fromJson(str, JsonObject.class).get("list").getAsJsonArray();

                return list.asList().stream().filter(listContent -> listContent.getAsJsonObject().get("name").getAsString().equals(path))
                        .toList().get(0).getAsJsonObject();
            }
        } catch (Exception e) {
            error.add("result", new Gson().toJsonTree( "File Not Found (Get Semi Path Info)"));
            return error;
        }
    }
    private static JsonObject getIconId(final String institutionUuid) {
        final JsonObject error = new JsonObject();
        try {
            URL localURL = new URL("https://aplikacje.edukacja.gov.pl/api/internal-data-hub/public/opi/university/" + institutionUuid);
            InputStream localInputStream = localURL.openStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) {
                error.add("result", new Gson().toJsonTree("Bad Request (Get Icon Id)"));
                return error;
            }
            else {
                final JsonObject info =  new Gson().fromJson(str, JsonObject.class);

                final JsonObject toReturn = new JsonObject();
                toReturn.add("iconId", new Gson().toJsonTree(info.asMap().get("iconId").getAsString()));
                toReturn.add("phone", new Gson().toJsonTree(info.asMap().get("phone").getAsString()));
                toReturn.add("email", new Gson().toJsonTree(info.asMap().get("email").getAsString()));


                return toReturn;
            }
        } catch (Exception e) {
            error.add("result", new Gson().toJsonTree("File Not Found (Get Icon Id)"));
            return error;
        }
    }

    public static JsonObject getPathInfo(final String institutionUuid, final String path) {
        final JsonObject error = new JsonObject();
        final String pathUuid = getSemiPathInfo(institutionUuid, path).get("id").getAsString();


        final Request request = new Request.Builder()
                .url("https://aplikacje.edukacja.gov.pl/api/internal-data-hub/public/opi/university/" + institutionUuid +"/course/" + pathUuid)
                .method("GET", null)
                .addHeader("Cookie", "JSESSIONID=11004839C937E552BBED8C1815DF77CA")
                .build();
        try {
            final Response response = new OkHttpClient().newCall(request).execute();
            final InputStream localInputStream = response.body().byteStream();
            final BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            final String str = localBufferedReader.lines().collect(Collectors.joining());
            localInputStream.close();
            if (str == null || str.isEmpty()) {
                error.add("result", new Gson().toJsonTree( "Bad Request (Get Semi Path Info)"));
                return error;
            }
            else {
                final JsonObject list = new Gson().fromJson(str, JsonObject.class);

                final JsonObject iconId = getIconId(institutionUuid);

                final JsonObject returnObject = new JsonObject();
                returnObject.add("requiredSubjects", list.get("requiredSubjects").getAsJsonArray());
                returnObject.add("requiredSubjectsToSelect", list.get("requiredSubjectsToSelect").getAsJsonArray());
                returnObject.add("courseDescription", new Gson().toJsonTree(list.get("courseDescription").getAsString()));
                returnObject.add("recruitmentPageUrl", new Gson().toJsonTree(list.get("recruitmentPageUrl").getAsString()));
                returnObject.add("www", new Gson().toJsonTree(list.get("www").getAsString()));
                returnObject.add("iconId", new Gson().toJsonTree(iconId.get("iconId").getAsString()));
                returnObject.add("phone", new Gson().toJsonTree(iconId.get("phone").getAsString()));
                returnObject.add("email", new Gson().toJsonTree(iconId.get("email").getAsString()));

                return returnObject;
            }
        } catch (IOException e) {
            System.out.println("2");
            error.add("result", new Gson().toJsonTree( "File Not Found (Get Semi Path Info)"));
            return error;
        }

    }
}
