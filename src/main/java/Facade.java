import java.util.ArrayList;

public class Facade {
    public void setareDate(String[] args) {
        ArrayList<Streamer> streamers = new ArrayList<Streamer>();
        ArrayList<Streams> streams = new ArrayList<Streams>();
        ArrayList<User> user = new ArrayList<User>();
        ArrayList<Command> commands = new ArrayList<Command>();
        streamers = Streamer.citireStreamers("src/main/resources/" + args[0]);
        streams = Streams.citireStreams("src/main/resources/" + args[1]);
        user = User.citireUsers("src/main/resources/" + args[2]);
        Database.getInstance();
        Database.getInstance().setStreamers(streamers);
        Database.getInstance().setStreams(streams);
        Database.getInstance().setUsers(user);
        commands = CommandFactory.getCommandArray("src/main/resources/" + args[3]);

    }
}
