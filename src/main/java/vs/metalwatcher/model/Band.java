package vs.metalwatcher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.StringJoiner;

@Table(name = "bands")
public class Band {

    @Id
    private Long id;
    private String name;
    private Long metalArchivesId;
    private String spotifyLink;
    private String bandcampLink;

    public Band() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMetalArchivesId() {
        return metalArchivesId;
    }

    public void setMetalArchivesId(Long metalArchivesId) {
        this.metalArchivesId = metalArchivesId;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }

    public String getBandcampLink() {
        return bandcampLink;
    }

    public void setBandcampLink(String bandcampLink) {
        this.bandcampLink = bandcampLink;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Band.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("metalArchivesId=" + metalArchivesId)
                .add("spotifyLink='" + spotifyLink + "'")
                .add("bandcampLink='" + bandcampLink + "'")
                .toString();
    }
}
