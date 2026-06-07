package com.example.todolist;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GoogleNaptarSzolgaltatas {

    private static final String APPLICATION_NAME = "Teendőlista";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleNaptarSzolgaltatas.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void addNaptarEsemeny(String cim, String leiras) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(cim)
                .setDescription(leiras);

        DateTime startDateTime = new DateTime(new Date());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Budapest");
        event.setStart(start);

        DateTime endDateTime = new DateTime(new Date(System.currentTimeMillis() + 3600000));
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Budapest");
        event.setEnd(end);

        service.events().insert("primary", event).execute();
    }

    public void addNaptarEsemeny(String cim, String leiras, java.time.LocalDateTime hatarido) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(cim)
                .setDescription(leiras);

        DateTime startDateTime = new DateTime(new Date());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Budapest");
        event.setStart(start);

        // Ha van határidő, azt használjuk, különben +1 óra
        Date vegDatum;
        if (hatarido != null) {
            vegDatum = java.util.Date.from(hatarido.atZone(java.time.ZoneId.of("Europe/Budapest")).toInstant());
        } else {
            vegDatum = new Date(System.currentTimeMillis() + 3600000);
        }

        DateTime endDateTime = new DateTime(vegDatum);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Budapest");
        event.setEnd(end);

        service.events().insert("primary", event).execute();
    }
}