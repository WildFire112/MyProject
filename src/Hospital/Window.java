package Hospital;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Window {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel docs;
    private JComboBox<String> doctorList;
    private JComboBox<String> doctorList2;
    private JButton removeDoctor;
    private JButton addDoctor;
    private JTextField doctorName;
    private JTextField name;
    private JPanel res;
    private JTextField date;
    private JButton reserve;
    private JTextField time;
    private JScrollPane textpane;
    private JTextArea text;
    private JButton changeTime;
    private JTextField startTimeValue;
    private JTextField endTimeValue;

    private static ResourceBundle myBundle = ResourceBundle.getBundle("myProp");

    //private static Map<String, Doctor> doctors;


    public Window() {
        JFrame frame = new JFrame("Window");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        DataBase db = new DataBase("test");
        db.createNewDatabase();
        //doctors = db.getData();
        for (String name : db.getDoctors().keySet()) {
            doctorList.addItem(name);
            doctorList2.addItem(name);
        }

        if (doctorList.getItemCount() > 0) {
            Doctor doc = db.getDoctor(doctorList.getSelectedItem().toString());
            startTimeValue.setText(doc.getStartTime().toString());
            endTimeValue.setText(doc.getEndTime().toString());

            db.getTime(doc.getId(), "2019-04-10");
        }

        addDoctor.addActionListener((ActionEvent e) -> {
            String name = doctorName.getText();

            String regex = "^([01]\\d|2[0-3]):?([03]0)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(startTimeValue.getText());
            Matcher matcher2 = pattern.matcher(endTimeValue.getText());

            if (matcher.matches() && matcher2.matches()) {
                LocalTime start = LocalTime.parse(startTimeValue.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime end = LocalTime.parse(endTimeValue.getText(), DateTimeFormatter.ofPattern("HH:mm"));

                //Doctor doc = new Doctor(name, start, end, id);
                if (db.getDoctor(name) == null) {
                    //doctors.put(name, doc);
                    doctorList.addItem(name);
                    doctorList2.addItem(name);

                    db.addDoctor(name, startTimeValue.getText(), endTimeValue.getText());
                }
            }
        });

        removeDoctor.addActionListener((ActionEvent e) -> {
            if (doctorList.getSelectedItem() != null) {
                String name = doctorList.getSelectedItem().toString();
                db.deleteDoctor(db.getDoctor(name).getId());
                //doctors.remove(name);
                doctorList.removeItem(name);
                doctorList2.removeItem(name);
            }
        });

        reserve.addActionListener(e -> {
            if (date.getText() != null && time.getText() != null && doctorList2.getSelectedItem() != null && name.getText() != null) {
                String regex = "^(?:(?:31(/)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(/)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(/)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(/)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(date.getText());
                if (matcher.matches()) {
                    regex = "^([01]\\d|2[0-3]):?([03]0)$";
                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(time.getText());
                    if (matcher.matches()) {
                        LocalDate d = LocalDate.parse(date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        LocalTime t = LocalTime.parse(time.getText(), DateTimeFormatter.ofPattern("HH:mm"));

                        Doctor doc = db.getDoctor(doctorList2.getSelectedItem().toString());
                        if (doc.reserveDate(d, t, name.getText())) {
                            if ((t.isAfter(doc.getStartTime()) || t.equals(doc.getStartTime())) && (t.isBefore(doc.getEndTime()) || t.equals(doc.getEndTime()))) {
                                text.setText("You have reserved Dr. " + doc.getName() + "'s examination for " + name.getText() + " at " + t.toString());
                            } else {
                                text.setText(myBundle.getString("you.can.reserve.time.from") + doc.getStartTime().toString() + myBundle.getString("to") + doc.getEndTime().toString());
                            }
                        } else {
                            text.setText(myBundle.getString("time.is.reserved.n") + doc.getHours(d));
                        }
                    } else text.setText(myBundle.getString("time.must.be.hh.mm.mm.can.be.00.or.30"));
                } else text.setText(myBundle.getString("date.must.be.dd.mm.yyyy"));
            } else text.setText(myBundle.getString("some.fields.are.empty"));

        });

        changeTime.addActionListener(e -> {
            String regex = "^([01]\\d|2[0-3]):?([03]0)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(startTimeValue.getText());
            Matcher matcher2 = pattern.matcher(endTimeValue.getText());

            if (matcher.matches() && matcher2.matches()) {
                LocalTime start = LocalTime.parse(startTimeValue.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime end = LocalTime.parse(endTimeValue.getText(), DateTimeFormatter.ofPattern("HH:mm"));

                Doctor doc = db.getDoctor(doctorList.getSelectedItem().toString());
                db.changeTime(doc.getId(), start.toString(), end.toString());
            }
        });

        doctorList.addActionListener(e -> {
            if (doctorList.getItemCount() > 0) {
                Doctor doc = db.getDoctor(doctorList.getSelectedItem().toString());
                if (doc != null) {
                    startTimeValue.setText(doc.getStartTime().toString());
                    endTimeValue.setText(doc.getEndTime().toString());
                }
            }
        });
    }
}


