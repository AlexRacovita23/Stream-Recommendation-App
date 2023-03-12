import java.util.ArrayList;

public class Database {
    private static Database instance = null;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Streams> streams = new ArrayList<Streams>();
    private ArrayList<Streamer> streamers = new ArrayList<Streamer>();

    private Database(ArrayList<User> users, ArrayList<Streams> streams, ArrayList<Streamer> streamer) {
        this.streamers = streamer;
        this.streams = streams;
        this.users = users;
    }

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Streams> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<Streams> streams) {
        this.streams = streams;
    }

    public ArrayList<Streamer> getStreamers() {
        return streamers;
    }

    public void setStreamers(ArrayList<Streamer> streamers) {
        this.streamers = streamers;
    }
}
