import com.opencsv.bean.CsvBindByName;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Streamer {
    Command command;
    @CsvBindByName(column = "streamerType")
    private int streamerType;
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "name")
    private String name;

    Streamer(int streamerType, int id, String name) {
        this.streamerType = streamerType;
        this.id = id;
        this.name = name;
    }

    static public Streamer getStreamerById(int id) {
        for (Streamer streamer : Database.getInstance().getStreamers()) {
            if (streamer.getId() == id) {
                return streamer;
            }
        }
        return null;
    }

    static public ArrayList<Streamer> citireStreamers(String numeFisier) {
        String linie;
        BufferedReader br;
        ArrayList<Streamer> streamers = new ArrayList<Streamer>();
        try {
            br = new BufferedReader(new FileReader(numeFisier));
            linie = br.readLine();
            while ((linie = br.readLine()) != null) {
                streamers.add(prelucrareStreamer(linie));
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return streamers;
    }

    static public Streamer prelucrareStreamer(String linie) {
        Streamer streamer;
        String[] linieFormatata;
        String[] streamerTypeString;
        int streamerType, id;
        String[] name;
        linieFormatata = linie.split("\",\"");
        streamerTypeString = linieFormatata[0].split("\"");
        streamerType = Integer.parseInt(streamerTypeString[1]);
        id = Integer.parseInt(linieFormatata[1]);
        name = linieFormatata[2].split("\"");
        streamer = new Streamer(streamerType, id, name[0]);
        return streamer;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }

    public int getStreamerType() {
        return streamerType;
    }

    public void setStreamerType(int streamerType) {
        this.streamerType = streamerType;
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
}
