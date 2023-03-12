import com.opencsv.bean.CsvBindByName;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "Streams")
    private List<Integer> StreamsList;

    User(int id, String name, List<Integer> Streams) {
        this.id = id;
        this.name = name;
        this.StreamsList = Streams;
    }

    static public ArrayList<User> citireUsers(String numeFisier) {
        String linie;
        BufferedReader br;
        ArrayList<User> users = new ArrayList<User>();
        try {
            br = new BufferedReader(new FileReader(numeFisier));
            linie = br.readLine();
            while ((linie = br.readLine()) != null) {
                users.add(prelucrareUser(linie));
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    static public User prelucrareUser(String linie) {
        User user;
        String[] linieFormatata;
        String[] idString;
        int id;
        String streamsString;
        int stream;
        ArrayList<Integer> streams = new ArrayList<Integer>();
        linieFormatata = linie.split("\",\"");
        idString = linieFormatata[0].split("\"");
        id = Integer.parseInt(idString[1]);
        streamsString = linieFormatata[2].split("\"")[0];
        String[] streamsStringArray = streamsString.split(" ");
        for (int i = 0; i < streamsStringArray.length; i++) {
            stream = Integer.parseInt(streamsStringArray[i]);
            streams.add(stream);
        }
        return new User(id, linieFormatata[1], streams);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getStreams() {
        return StreamsList;
    }

    public void setStreams(List<Integer> Streams) {
        this.StreamsList = Streams;
    }

}
