public class Track {
    private int trackID;
    String trackTitle;
    private int artistID;

    public Track(int trackID, String trackTitle, int artistID) {
        this.trackID = trackID;
        this.trackTitle = trackTitle;
        this.artistID = artistID;
    }

    public int getTrackID() {
        return trackID;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackID=" + trackID +
                ", trackTitle='" + trackTitle + '\'' +
                ", artistID=" + artistID +
                '}';
    }
}