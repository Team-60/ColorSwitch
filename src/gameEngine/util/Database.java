package gameEngine.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Database<T> {

    private final int MAX_SIZE = 6; // number of records to store in arraylist
    private final ArrayList<T> data;

    public Database() {
        this.data = new ArrayList<>();
    }

    public void form(String path) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            ArrayList temp = (ArrayList) in.readObject();
            for (Object obj : temp) {
                this.data.add((T) obj);
            }
            System.out.println(this.getClass().toString());
            System.out.println(this.data.size());
            for (T t : this.data)
                System.out.println(t);

            assert (this.data.size() <= MAX_SIZE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(this.getClass().toString() + " failed to form database");
            System.out.println(e.toString());
        }
    }

    public void save(String path) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            System.out.println(this.getClass().toString() + " sz data: "  + this.data.size());
            out.writeObject(this.data);
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " save failed");
            System.out.println(e.toString());
        }
    }

    public boolean update(T entity, String path) { // updates and calls save and returns true in case of overwrite
        boolean ret = this.data.size() == MAX_SIZE;
        ArrayList<Comparable> temp = new ArrayList<>();
        for (T t : this.data)
            temp.add((Comparable) t);
        temp.add((Comparable) entity);
        Collections.sort(temp);
        this.data.clear();
        for (int cnt = 0; cnt < Math.min(temp.size(), MAX_SIZE); ++ cnt)
            this.data.add((T) temp.get(cnt));
        assert (this.data.size() <= MAX_SIZE);
        this.save(path);
        return ret; // overwrite
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void reset(String path) {
        this.data.clear();
        this.save(path);
    }
}
