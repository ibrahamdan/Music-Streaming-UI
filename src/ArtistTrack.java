import javafx.scene.control.Button;

public class ArtistTrack {
    private Artist artist;
    private Track track;
    private String trackTitle;
    private String artistName;
    private int trackID;
    private int artistID;

    public ArtistTrack(Track track, Artist artist) {
        this.track = track;
        this.artist = artist;
        this.trackTitle = track.getTrackTitle();
        this.artistName = artist.getArtistName();
        this.trackID = track.getTrackID();
        this.artistID = artist.getArtistID();
    }

    public Artist getArtist() {
        return artist;
    }

    public Track getTrack() {
        return track;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getTrackID() {
        return trackID;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    @Override
    public String toString() {
        return "ArtistTrack{" +
                "trackTitle='" + trackTitle + '\'' +
                ", artistName='" + artistName + '\'' +
                ", trackID=" + trackID +
                ", artistID=" + artistID +
                '}';
    }
}