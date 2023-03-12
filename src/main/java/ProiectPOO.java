public class ProiectPOO {

    public static void clearDatabase() {
        Database.getInstance().getStreamers().clear();
        Database.getInstance().getStreams().clear();
        Database.getInstance().getUsers().clear();
    }

    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Nothing to read here");
            return;
        }
        clearDatabase();
        Facade facade = new Facade();
        facade.setareDate(args);
    }
}
