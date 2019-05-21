package Hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;

public class Doctor {

 //   private Map<LocalDate, Map<LocalTime, String>> Data;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String id;
    private DataBase db = new DataBase("test");

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;

        return name.equals(doctor.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    private Doctor() {
    }

    public Doctor(String name, LocalTime start, LocalTime end, String id) {
            this.name = name;
       // Data = new HashMap<>();
        this.startTime = start;
        this.endTime = end;
        this.id = id;
    }

    public boolean reserveDate(LocalDate date, LocalTime time, String name) {
        if (!db.getTime(this.getId(), date.toString()).containsKey(time) && (time.isAfter(this.getStartTime()) || time.equals(this.getStartTime())) && (time.isBefore(this.getEndTime()) || time.equals(this.getEndTime()))) {
            db.addTime(id, date.toString(), time.toString(), name);
        } else {
            System.out.println("Time is reserved!");
            printHours(date);
            return false;
        }
        System.out.println("You have reserved Dr. " + getName() + "'s examination for " + name + " at " + time.toString());
        return true;
    }


    public String getName() {
        return name;
    }

    public void printHours(LocalDate date) {
        System.out.println("> " + date);
        for (HashMap.Entry<LocalTime, String> d : db.getTime(this.getId(), date.toString()).entrySet()) {
            System.out.println("> > " + d.getKey().toString() + ": " + d.getValue());
        }
    }

    public String getHours(LocalDate date) {
        StringBuilder sb = new StringBuilder();

        sb.append("> " + date + "\n");
        for (HashMap.Entry<LocalTime, String> d : db.getTime(this.getId(), date.toString()).entrySet()) {
            sb.append("> > " + d.getKey().toString() + ": " + d.getValue() + "\n");
        }

        return sb.toString();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getId() {
        return id;
    }
}
