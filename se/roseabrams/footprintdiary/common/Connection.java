package se.roseabrams.footprintdiary.common;

public class Connection<C1 extends Connectable<C1, C2>, C2 extends Connectable<C2, C1>> {

    private final C1 O1;
    private final C2 O2;

    public Connection(C1 o1, C2 o2) {
        O1 = o1;
        O2 = o2;
    }
}
