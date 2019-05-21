package Hospital;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Window {
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
    private JScrollPane textPane;
    private JTextArea text;
    private JButton changeTime;
    private JTextField startTimeValue;
    private JTextField endTimeValue;

    private static final Locale en = new Locale("en", "US");
    private static final ResourceBundle myBundle = ResourceBundle.getBundle("resources.myProp", en);


    @SuppressWarnings("ConstantConditions")
    Window() {
        JFrame frame = new JFrame("Window");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        DataBase db = new DataBase();
        db.createNewDatabase();

        for (String name : db.getDoctors().keySet()) {
            doctorList.addItem(name);
            doctorList2.addItem(name);
        }

        if (doctorList.getItemCount() > 0) {
            Doctor doc = db.getDoctor(doctorList.getSelectedItem().toString());
            startTimeValue.setText(doc.getStartTime().toString());
            endTimeValue.setText(doc.getEndTime().toString());
        }

        addDoctor.addActionListener((ActionEvent e) -> {
            String name = doctorName.getText();
            String regex = myBundle.getString("time.reg.exp");
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
                String regex = myBundle.getString("date.reg.exp");
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(date.getText());
                if (matcher.matches()) {
                    regex = myBundle.getString("time.reg.exp");
                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(time.getText());
                    if (matcher.matches()) {
                        LocalDate d = LocalDate.parse(date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        LocalTime t = LocalTime.parse(time.getText(), DateTimeFormatter.ofPattern("HH:mm"));

                        Doctor doc = db.getDoctor(doctorList2.getSelectedItem().toString());
                        if (doc.reserveDate(d, t, name.getText())) {
                            if ((t.isAfter(doc.getStartTime()) || t.equals(doc.getStartTime())) && (t.isBefore(doc.getEndTime()) || t.equals(doc.getEndTime()))) {
                                text.setText(myBundle.getString("you.have.reserved.dr") + doc.getName() + myBundle.getString("s.examination.for") + name.getText() + myBundle.getString("at") + t.toString());
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
            String regex = myBundle.getString("time.reg.exp");
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


