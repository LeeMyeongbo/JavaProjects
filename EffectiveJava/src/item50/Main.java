package item50;

import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        Date start = new Date(123, Calendar.SEPTEMBER, 10);
        Date end = new Date(123, Calendar.SEPTEMBER, 18);
        Period p = new Period(start, end);
        System.out.println("Period : " + p.getStart() + " ~ " + p.getEnd());

        end.setDate(20);            // Period 내 불변식이 깨짐
        System.out.println("Period : " + p.getStart() + " ~ " + p.getEnd());

        // 가변인 Date 대신 Instant 또는 LocalDateTime, ZonedDateTime 활용하면 됨
        Date start2 = new Date(123, Calendar.SEPTEMBER, 10);
        Date end2 = new Date(123, Calendar.SEPTEMBER, 18);
        DefensivePeriod dp = new DefensivePeriod(start2, end2);
        System.out.println("DefensivePeriod : " + dp.getStart() + " ~ " + dp.getEnd());

        end2.setDate(20);           // DefensivePeriod 내 불변식 깨지지 않음
        System.out.println("DefensivePeriod : " + dp.getStart() + " ~ " + dp.getEnd());

        dp.getEnd().setDate(20);    // DefensivePeriod 인스턴스 내 가변 필드 변경
        System.out.println("DefensivePeriod : " + dp.getStart() + " ~ " + dp.getEnd());

        Date start3 = new Date(123, Calendar.SEPTEMBER, 10);
        Date end3 = new Date(123, Calendar.SEPTEMBER, 18);
        DefensivePeriod2 dp2 = new DefensivePeriod2(start3, end3);
        System.out.println("DefensivePeriod2 : " + dp2.getStart() + " ~ " + dp2.getEnd());

        end3.setDate(20);
        System.out.println("DefensivePeriod2 : " + dp2.getStart() + " ~ " + dp2.getEnd());

        dp2.getEnd().setDate(20);    // DefensivePeriod2 인스턴스 내 가변 필드 변경되지 않음
        System.out.println("DefensivePeriod2 : " + dp2.getStart() + " ~ " + dp2.getEnd());
    }
}
