public class Artist {
    private int artistID;
    private String artistName;

    public Artist(int artistID, String artistName) {
        this.artistID = artistID;
        this.artistName = artistName;
    }

    public int getArtistID() {
        return artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
