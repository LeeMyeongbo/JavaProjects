package item50;

import java.util.Date;

/**
 * This Period class is unmodifiable when start and end is set
 */
public class Period {

    private final Date start;
    private final Date end;

    /**
     * @param start start time.
     * @param end end time. It must be after than start time.
     * @throws IllegalArgumentException occurs when start time is after than end time.
     * @throws NullPointerException occurs when start or end time is null.
     */
    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0)
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦음..");
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
