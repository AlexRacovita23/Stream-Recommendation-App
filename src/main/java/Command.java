import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public interface Command {
    void execute();
}

class GetStreamsListCommand implements Command {
    ArrayList<Streams> streamsFromCommand;

    public GetStreamsListCommand(ArrayList<Streams> streamsFromCommand) {
        this.streamsFromCommand = streamsFromCommand;
    }

    public void execute() {
        JsonObject myLittleJsonObject = new JsonObject();
        String jsonString = "[";
        for (Streams stream : streamsFromCommand) {
            JsonObject streamObject = new JsonObject();
            String noOfListeningsString = String.valueOf(stream.getNoOfStreams());
            String idString = Integer.toString(stream.getId());
            myLittleJsonObject.addProperty("id", idString);
            myLittleJsonObject.addProperty("name", stream.getName());
            myLittleJsonObject.addProperty("streamerName", Streamer.getStreamerById(stream.getStreamerId()).getName());
            myLittleJsonObject.addProperty("noOfListenings", noOfListeningsString);
            myLittleJsonObject.addProperty("length", stream.getLength());
            myLittleJsonObject.addProperty("dateAdded", stream.getDateAdded());
            jsonString += myLittleJsonObject.toString();
            if (streamsFromCommand.indexOf(stream) < streamsFromCommand.size() - 1) {
                jsonString += ",";
            }
        }
        jsonString += "]";
        try (FileWriter fw = new FileWriter("src/main/resources/DEBUG.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(jsonString);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonString);
    }
}

class AddStreamCommand implements Command {
    private final Streams stream;

    public AddStreamCommand(Streams stream) {
        this.stream = stream;
    }

    public void execute() {
        Database database = Database.getInstance();
        (Database.getInstance().getStreams()).add(stream);
    }
}

class DeleteStreamCommand implements Command {
    private final Streams stream;

    public DeleteStreamCommand(Streams stream) {
        this.stream = stream;
    }

    public void execute() {
        Database.getInstance().getStreams().remove(stream);
        for (User user : Database.getInstance().getUsers()) {
            if (user.getStreams().contains(stream.getId())) {
                user.getStreams().remove(stream.getId());
            }
        }
    }
}

class ListenCommand implements Command {
    private final Streams stream;
    private final int userId;

    public ListenCommand(Streams stream, int userId) {
        this.stream = stream;
        this.userId = userId;
    }

    public void execute() {
        Database database = Database.getInstance();
        for (Streams streams : database.getStreams()) {
            if (streams.getId() == stream.getId()) {
                streams.setNoOfStreams(streams.getNoOfStreams() + 1);
            }
        }
        for (User user : database.getUsers()) {
            if (user.getId() == userId) {
                if (!user.getStreams().contains(stream.getId())) {
                    user.getStreams().add(stream.getId());
                }
            }
        }
    }
}

class RecommendCommand implements Command {
    private int userId;
    private final ArrayList<Streams> streamsToRecommend;

    public RecommendCommand(ArrayList<Streams> streamsToRecommend) {
        this.streamsToRecommend = streamsToRecommend;
    }

    public void execute() {

        JsonObject myLittleJsonObject = new JsonObject();
        String jsonString = "[";
        int contor = 0;
        for (Streams stream : streamsToRecommend) {
            if (contor < 5) {
                JsonObject streamObject = new JsonObject();
                String noOfListeningsString = String.valueOf(stream.getNoOfStreams());
                String idString = Integer.toString(stream.getId());
                myLittleJsonObject.addProperty("id", idString);
                myLittleJsonObject.addProperty("name", stream.getName());
                myLittleJsonObject.addProperty("streamerName", Streamer.getStreamerById(stream.getStreamerId()).getName());
                myLittleJsonObject.addProperty("noOfListenings", noOfListeningsString);
                myLittleJsonObject.addProperty("length", stream.getLength());
                myLittleJsonObject.addProperty("dateAdded", stream.getDateAdded());
                jsonString += myLittleJsonObject.toString();
                if (streamsToRecommend.indexOf(stream) < streamsToRecommend.size() - 1 && contor < 4) {
                    jsonString += ",";
                }
                contor++;
            }
        }
        jsonString += "]";
        try (FileWriter fw = new FileWriter("src/main/resources/DEBUG.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(jsonString);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonString);
    }
}

class SurpriseCommand implements Command {
    private int userId;
    private final ArrayList<Streams> streamsToRecommend;

    public SurpriseCommand(ArrayList<Streams> streamsToRecommend) {
        this.streamsToRecommend = streamsToRecommend;
    }

    public void execute() {

        JsonObject myLittleJsonObject = new JsonObject();
        String jsonString = "[";
        int contor = 0;
        for (Streams stream : streamsToRecommend) {
            if (contor < 3) {
                JsonObject streamObject = new JsonObject();
                String noOfListeningsString = String.valueOf(stream.getNoOfStreams());
                String idString = Integer.toString(stream.getId());
                myLittleJsonObject.addProperty("id", idString);
                myLittleJsonObject.addProperty("name", stream.getName());
                myLittleJsonObject.addProperty("streamerName", Streamer.getStreamerById(stream.getStreamerId()).getName());
                myLittleJsonObject.addProperty("noOfListenings", noOfListeningsString);
                myLittleJsonObject.addProperty("length", stream.getLength());
                myLittleJsonObject.addProperty("dateAdded", stream.getDateAdded());
                jsonString += myLittleJsonObject.toString();
                if (streamsToRecommend.indexOf(stream) < streamsToRecommend.size() - 1 && contor < 2) {
                    jsonString += ",";
                }
                contor++;
            }
        }
        jsonString += "]";
        try (FileWriter fw = new FileWriter("src/main/resources/DEBUG.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(jsonString);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonString);
    }
}

