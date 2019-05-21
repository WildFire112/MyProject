package Hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@SuppressWarnings("CanBeFinal")
class Doctor {

 //   private Map<LocalDate, Map<LocalTime, String>> Data;
    @SuppressWarnings("CanBeFinal")
    private String name;
    @SuppressWarnings("CanBeFinal")
    private LocalTime startTime;
    @SuppressWarnings("CanBeFinal")
    private LocalTime endTime;
    @SuppressWarnings("CanBeFinal")
    private String id;
    private final DataBase db = new DataBase();

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

    Doctor(String name, LocalTime start, LocalTime end, String id) {
            this.name = name;
       // Data = new HashMap<>();
        this.startTime = start;
        this.endTime = end;
        this.id = id;
    }

    boolean reserveDate(LocalDate date, LocalTime time, String name) {
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

    private void printHours(LocalDate date) {
        System.out.println("> " + date);
        for (HashMap.Entry<LocalTime, String> d : db.getTime(this.getId(), date.toString()).entrySet()) {
            System.out.println("> > " + d.getKey().toString() + ": " + d.getValue());
        }
    }

    String getHours(LocalDate date) {
        StringBuilder sb = new StringBuilder();

        sb.append("> ").append(date).append("\n");
        for (HashMap.Entry<LocalTime, String> d : db.getTime(this.getId(), date.toString()).entrySet()) {
            sb.append("> > ").append(d.getKey().toString()).append(": ").append(d.getValue()).append("\n");
        }

        return sb.toString();
    }

    LocalTime getStartTime() {
        return startTime;
    }

    LocalTime getEndTime() {
        return endTime;
    }

    String getId() {
        return id;
    }
}
