package adminclerksdts.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private int scheduleId;
    private int learnerId;
    private LocalDate sessionDate;
    private LocalTime sessionStartTime;
    private LocalTime sessionEndTime;

    public Schedule(int scheduleId, int learnerId, LocalDate sessionDate, LocalTime sessionStartTime, LocalTime sessionEndTime) {
        this.scheduleId = scheduleId;
        this.learnerId = learnerId;
        this.sessionDate = sessionDate;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
    }

    public Schedule(int learnerId, LocalDate sessionDate, LocalTime sessionStartTime, LocalTime sessionEndTime) {
        this.learnerId = learnerId;
        this.sessionDate = sessionDate;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(int learnerId) {
        this.learnerId = learnerId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(LocalTime sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public LocalTime getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(LocalTime sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    @Override
    public String toString() {
        return "Schedule{" + "scheduleId=" + scheduleId + ", learnerId=" + learnerId + ", sessionDate=" + sessionDate + ", sessionStartTime=" + sessionStartTime + ", sessionEndTime=" + sessionEndTime + '}';
    }

   
}
