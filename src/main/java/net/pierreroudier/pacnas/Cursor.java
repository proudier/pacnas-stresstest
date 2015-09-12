package net.pierreroudier.pacnas;

/**
 * Created by pierre on 9/3/15.
 */
public class Cursor {
    private int position = 0;

    public int getPosition() {
        position++;
        return position;
    }

}
