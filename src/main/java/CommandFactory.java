import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CommandFactory {
    public static Command createCommand(String commandType, String[] commandEncoding) {

        if (commandType.equals("ADD")) {
            int streamerId = Integer.parseInt(commandEncoding[0]);
            int streamType = Integer.parseInt(commandEncoding[2]);
            int streamId = Integer.parseInt(commandEncoding[3]);
            int streamGenre = Integer.parseInt(commandEncoding[4]);
            long streamDurationSeconds = Long.parseLong(commandEncoding[5]);
            String streamName = "";
            long noOfStreams = 0;
            for (int i = 6; i < commandEncoding.length; i++) {
                streamName += commandEncoding[i];
                if (i < commandEncoding.length - 1) {
                    streamName += " ";
                }
            }
            long dateAdded = System.currentTimeMillis() / 1000L;
            return new AddStreamCommand(new Streams(streamType, streamId, streamGenre, noOfStreams, streamerId, streamDurationSeconds, dateAdded, streamName));

        }
        if (commandType.equals("LIST")) {
            int id = Integer.parseInt(commandEncoding[0]);
            Database database = Database.getInstance();
            for (Streamer streamer : database.getStreamers()) {
                if (streamer.getId() == id) {
                    ArrayList<Streams> streamsList = new ArrayList<Streams>();
                    for (Streams stream : Database.getInstance().getStreams()) {
                        if (stream.getStreamerId() == id) {
                            streamsList.add(stream);
                        }
                    }
                    return new GetStreamsListCommand(streamsList);
                }
            }
            for (User user : Database.getInstance().getUsers()) {
                if (user.getId() == id) {
                    ArrayList<Streams> streamsList = new ArrayList<Streams>();
                    for (int streamId : user.getStreams()) {
                        streamsList.add(Streams.getStreamById(streamId));
                    }
                    return new GetStreamsListCommand(streamsList);
                }
            }
        }
        if (commandType.equals("DELETE")) {
            int id = Integer.parseInt(commandEncoding[2]);
            int streamerId = Integer.parseInt(commandEncoding[0]);
            Database database = Database.getInstance();
            for (Streams streams : Database.getInstance().getStreams()) {
                if (streams.getId() == id && streams.getStreamerId() == streamerId) {
                    return new DeleteStreamCommand(streams);
                }
            }

        }
        if (commandType.equals("LISTEN")) {
            int id = Integer.parseInt(commandEncoding[2]);
            int userId = Integer.parseInt(commandEncoding[0]);
            Database database = Database.getInstance();
            for (Streams streams : Database.getInstance().getStreams()) {
                if (streams.getId() == id) {
                    return new ListenCommand(streams, userId);
                }
            }

        }
        if (commandType.equals("RECOMMEND")) {
            int userId = Integer.parseInt(commandEncoding[0]);
            Database database = Database.getInstance();
            ArrayList<Streams> sortedStreams = new ArrayList<Streams>();
            ArrayList<Streams> streamsToRecommend = new ArrayList<Streams>();
            sortedStreams = Database.getInstance().getStreams();
            Collections.sort(sortedStreams, new Streams.ListenCountCompare());
            String streamTypeString = commandEncoding[2];
            int streamType = 0;
            if (streamTypeString.equals("SONG")) {
                streamType = 1;
            } else if (streamTypeString.equals("PODCAST")) {
                streamType = 2;
            } else if (streamTypeString.equals("AUDIOBOOK")) {
                streamType = 3;
            }
//            Streams currentStream = null;
            for (User user : Database.getInstance().getUsers()) {
                if (user.getId() == userId) {
                    for (Streams currentStream : sortedStreams) {
                        if (!user.getStreams().contains(currentStream.getId()) && currentStream.getStreamType() == streamType) {
                            for (int streamId : user.getStreams()) {
                                Streams streamToCheck = Streams.getStreamById(streamId);
                                if (streamToCheck.getStreamerId() == currentStream.getStreamerId()) {
                                    streamsToRecommend.add(currentStream);
                                }
                            }
                        }
                    }
                }
            }
            return new RecommendCommand(streamsToRecommend);
        }
        if (commandType.equals("SURPRISE")) {
            int userId = Integer.parseInt(commandEncoding[0]);
            Database database = Database.getInstance();
            ArrayList<Streams> sortedStreams = new ArrayList<Streams>();
            ArrayList<Streams> streamsToRecommend = new ArrayList<Streams>();
            ArrayList<Streamer> listenedStreamers = new ArrayList<Streamer>();
            sortedStreams = Database.getInstance().getStreams();
            Collections.sort(sortedStreams, new Streams.DateAddedCompare());
            String streamTypeString = commandEncoding[2];
            int streamType = 0;
            if (streamTypeString.equals("SONG")) {
                streamType = 1;
            } else if (streamTypeString.equals("PODCAST")) {
                streamType = 2;
            } else if (streamTypeString.equals("AUDIOBOOK")) {
                streamType = 3;
            }
            for (User user : Database.getInstance().getUsers()) {
                if (user.getId() == userId) {
                    for (int streamId : user.getStreams()) {
                        Streams streamToCheck = Streams.getStreamById(streamId);
                        if (!listenedStreamers.contains(Streamer.getStreamerById(streamToCheck.getStreamerId()))) {
                            listenedStreamers.add(Streamer.getStreamerById(streamToCheck.getStreamerId()));
                        }
                    }

                    for (Streams currentStream : sortedStreams) {
                        if (!user.getStreams().contains(currentStream.getId()) && currentStream.getStreamType() == streamType) {
                            if (!listenedStreamers.contains(Streamer.getStreamerById(currentStream.getStreamerId()))) {
                                streamsToRecommend.add(currentStream);
                            }
                        }
                    }
                }
            }
            return new SurpriseCommand(streamsToRecommend);
        }
        return null;
    }

    static public ArrayList<Command> getCommandArray(String numeFisier) {
        String linie;
        String[] linieFormatata;
        BufferedReader br;
        ArrayList<Command> commands = new ArrayList<Command>();
        try {
            br = new BufferedReader(new FileReader(numeFisier));
            while ((linie = br.readLine()) != null) {
                linieFormatata = linie.split(" ");
                commands.add(CommandFactory.createCommand(linieFormatata[1], linieFormatata));
                commands.get(commands.size() - 1).execute();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return commands;
    }
}
