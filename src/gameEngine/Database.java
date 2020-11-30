package gameEngine;

import java.util.ArrayList;

public class Database<T> {

    private final int MAX_SIZE = 6; // number of records to store in arraylist
    private final ArrayList<T> data;

    public Database() {
        this.data = new ArrayList<>();
    }

    public void form(String path) {
        // TODO
    }
}
