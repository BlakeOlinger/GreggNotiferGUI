package com.practice;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Frame implements ActionListener {
    private final TextField fileField;
    private final TextField messageField;
    private final static String configFile = "run-time.config";
    private final static String debugRoot = "C:\\Users\\bolinger\\Desktop\\SolidWorks Notifier\\";
    private final static String notifyFile = "notify-file-list.config";
    private final static String notificationsFile = "notifications.config";

    private Main() {
        setLayout(new FlowLayout());
        var enterNewFileLabel = new Label("Enter New File: ");

        add(enterNewFileLabel);

        fileField = new TextField(20);

        add(fileField);

        var messageLabel = new Label("Enter Message for Part: ");

        messageField = new TextField(50);

        add(messageLabel);

        add(messageField);

        var confirm = new Button("Save");

        confirm.addActionListener(this);

        add(confirm);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendCommand(configFile, "1");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }

                sendCommand(configFile, "0");

                System.exit(0);
            }
        });
    }
    private void sendCommand(String path, String command) {
        try {
            Files.writeString(Paths.get(path), command);
        } catch (IOException ignore) {
        }
    }

    public static void main(String[] args) {

        var window = new Main();

        window.setSize(600, 400);
        window.setTitle("TOPP SolidWorks Notifier");
        window.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var notify = Paths.get(notifyFile);
        var messages = Paths.get(notificationsFile);

        var rawNotifyFileString = readFile(notify);
        var rawNotificationsString = readFile(messages);

        if(fileField.getText() != null && fileField.getText().compareTo("") != 0 &&
        messageField.getText() != null && messageField.getText().compareTo("") != 0) {

            writeFile(notify, rawNotifyFileString + fileField.getText());
            writeFile(messages, rawNotificationsString + messageField.getText());
        }
    }

    private void writeFile(Path notify, String text) {
        try {
            Files.writeString(notify, text + "$");
        } catch (IOException ignore) {
        }
    }

    private String readFile(Path notify) {
        try {
            return Files.readString(notify);
        } catch (IOException ignore) {
            return null;
        }
    }
}