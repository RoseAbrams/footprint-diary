package se.roseabrams.footprintdiary.common;

public interface Connectable<E1 extends Connectable<E1, E2>, E2 extends Connectable<E2, E1>> extends DiaryEntryData {
}
