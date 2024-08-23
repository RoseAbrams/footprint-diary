package se.roseabrams.footprintdiary;

public class DiaryDateTester {

    public static void main(String[] args) {
        DiaryDate d15may10 = new DiaryDate((short) 2010, (byte) 5, (byte) 15);
        DiaryDate d15april10 = new DiaryDate((short) 2010, (byte) 4, (byte) 15);
        assert d15may10.compareTo(d15april10, true) > 0;
        assert d15may10.compareTo(d15april10, false) > 0;
        assert d15april10.compareTo(d15may10, true) < 0;
        assert d15april10.compareTo(d15may10, false) < 0;
        assert d15may10.compareTo(d15may10, true) == 0;
        assert d15may10.compareTo(d15may10, false) == 0;

        DiaryDate dt15may1011am = new DiaryDateTime((short) 2010, (byte) 5, (byte) 15, (byte) 11, (byte) 20, (byte) 44);
        assert d15may10.compareTo(dt15may1011am, true) < 0;
        assert d15may10.compareTo(dt15may1011am, false) == 0;
        assert dt15may1011am.compareTo(d15may10, true) > 0;
        assert dt15may1011am.compareTo(d15may10, false) == 0;

        DiaryDate dt15may1011pm = new DiaryDateTime((short) 2010, (byte) 5, (byte) 15, (byte) 23, (byte) 20, (byte) 44);
        assert dt15may1011pm.compareTo(dt15may1011am, true) > 0;
        assert dt15may1011pm.compareTo(dt15may1011am, false) == 0;
        assert dt15may1011am.compareTo(dt15may1011pm, true) < 0;
        assert dt15may1011am.compareTo(dt15may1011pm, false) == 0;
    }
}
