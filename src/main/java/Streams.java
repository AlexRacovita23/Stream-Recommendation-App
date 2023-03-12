import com.opencsv.bean.CsvBindByName;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class Streams {
    @CsvBindByName(column = "streamType")
    private int streamType;
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "streamGenre")
    private int streamGenre;
    @CsvBindByName(column = "noOfStreams")
    private long noOfStreams;
    @CsvBindByName(column = "streamerId")
    private int streamerId;
    @CsvBindByName(column = "length")
    private long length;
    @CsvBindByName(column = "dateAdded")
    private long dateAdded;
    @CsvBindByName(column = "name")
    private String name;

    Streams(int streamType, int id, int streamGenre, long noOfStreams, int streamerId, long length, long dateAdded, String name) {
        this.streamType = streamType;
        this.id = id;
        this.streamGenre = streamGenre;
        this.noOfStreams = noOfStreams;
        this.streamerId = streamerId;
        this.length = length;
        this.dateAdded = dateAdded;
        this.name = name;
    }

    static public Streams getStreamById(int id) {
        for (Streams stream : Database.getInstance().getStreams()) {
            if (stream.getId() == id) {
                return stream;
            }
        }
        return null;
    }

    static public ArrayList<Streams> citireStreams(String numeFisier) {
        String linie;
        BufferedReader br;
        ArrayList<Streams> streams = new ArrayList<Streams>();
        try {
            br = new BufferedReader(new FileReader(numeFisier));
            linie = br.readLine();
            while ((linie = br.readLine()) != null) {
                streams.add(prelucrareStream(linie));
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return streams;
    }

    static public Streams prelucrareStream(String linie) {
        String[] linieFormatata;
        String[] streamTypeString;
        int streamType, id, streamGenre, streamerId;
        long noOfStreams, length, dateAdded;
        String[] name;
        linieFormatata = linie.split("\",\"");
        streamTypeString = linieFormatata[0].split("\"");
        streamType = Integer.parseInt(streamTypeString[1]);
        id = Integer.parseInt(linieFormatata[1]);
        streamGenre = Integer.parseInt(linieFormatata[2]);
        noOfStreams = Long.parseLong(linieFormatata[3]);
        streamerId = Integer.parseInt(linieFormatata[4]);
        length = Long.parseLong(linieFormatata[5]);
        dateAdded = Long.parseLong(linieFormatata[6]);
        name = linieFormatata[7].split("\"");
        return new Streams(streamType, id, streamGenre, noOfStreams, streamerId, length, dateAdded, name[0]);
    }

    public int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStreamGenre() {
        return streamGenre;
    }

    public void setStreamGenre(int streamGenre) {
        this.streamGenre = streamGenre;
    }

    public long getNoOfStreams() {
        return noOfStreams;
    }

    public void setNoOfStreams(long noOfStreams) {
        this.noOfStreams = noOfStreams;
    }

    public int getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(int streamerId) {
        this.streamerId = streamerId;
    }

    public String getLength() {
        if (length < 3600)
            return String.format("%02d:%02d", length / 60, (length % 60));
        return String.format("%02d:%02d:%02d", length / 3600, length / 60, (length % 60));
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDateAdded() {
        Date date = new Date(dateAdded * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ListenCountCompare implements java.util.Comparator<Streams> {
        @Override
        public int compare(Streams o1, Streams o2) {
            if (o1.getNoOfStreams() > o2.getNoOfStreams())
                return -1;
            else if (o1.getNoOfStreams() < o2.getNoOfStreams())
                return 1;
            return 0;
        }
    }

    public static class DateAddedCompare implements Comparator<Streams> {
        @Override
        public int compare(Streams o1, Streams o2) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = sdf.parse(o1.getDateAdded());
                date2 = sdf.parse(o2.getDateAdded());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (date1.before(date2))
                return 1;
            if (date1.after(date2))
                return -1;
            if (date1.equals(date2)) {
                if (o1.getNoOfStreams() > o2.getNoOfStreams())
                    return -1;
                if (o1.getNoOfStreams() < o2.getNoOfStreams())
                    return 1;
            }
            return 0;
        }
    }
}
