package item50;

import java.util.Date;

/**
 * This DefensivePeriod class is unmodifiable when start and end is set
 */
public class DefensivePeriod {

    private final Date start;
    private final Date end;

    /**
     * @param start start time.
     * @param end end time. It must be after than start time.
     * @throws IllegalArgumentException occurs when start time is after than end time.
     * @throws NullPointerException occurs when start or end time is null.
     */
    public DefensivePeriod(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if (this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException(this.start + "가 " + this.end + "보다 늦음..");
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
