package vs.metalwatcher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import vs.metalwatcher.archives.ArchivesAlbum;

import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "albums")
public class Album {

    @Id
    private Long id;

    private String name;
    private Integer year;
    private String type;

    @Column("i_have")
    private Boolean iHave;

    @Column("high_quality")
    private Boolean isHighQuality;

    @Column("spotify_link")
    private String spotifyLink;

    @Column("bandcamp_link")
    private String bandcampLink;

    @Column("metal_archives_id")
    private Long metalArchivesId;

    @Column("metal_archives_link")
    private String metalArchivesLink;

    @Column("band_id")
    private AggregateReference<Band, Long> band;

    public Album() {
    }

    public Album(ArchivesAlbum archivesAlbum, AggregateReference<Band, Long> band) {
        Objects.requireNonNull(archivesAlbum);
        this.name = archivesAlbum.name();
        this.year = archivesAlbum.year();
        this.type = archivesAlbum.type();
        this.iHave = false;
        this.isHighQuality = false;
        this.metalArchivesId = Long.parseLong(archivesAlbum.id());
        this.metalArchivesLink = archivesAlbum.link();
        this.band = band;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getiHave() {
        return iHave;
    }

    public void setiHave(Boolean iHave) {
        this.iHave = iHave;
    }

    public Boolean getHighQuality() {
        return isHighQuality;
    }

    public void setHighQuality(Boolean highQuality) {
        isHighQuality = highQuality;
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

    public Long getMetalArchivesId() {
        return metalArchivesId;
    }

    public void setMetalArchivesId(Long metalArchivesId) {
        this.metalArchivesId = metalArchivesId;
    }

    public String getMetalArchivesLink() {
        return metalArchivesLink;
    }

    public void setMetalArchivesLink(String metalArchivesLink) {
        this.metalArchivesLink = metalArchivesLink;
    }

    public AggregateReference<Band, Long> getBand() {
        return band;
    }

    public void setBand(AggregateReference<Band, Long> band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Album.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("year=" + year)
                .add("type='" + type + "'")
                .add("iHave=" + iHave)
                .add("isHighQuality=" + isHighQuality)
                .add("spotifyLink='" + spotifyLink + "'")
                .add("bandcampLink='" + bandcampLink + "'")
                .add("metalArchivesId='" + metalArchivesId + "'")
                .add("metalArchivesLink='" + metalArchivesLink + "'")
                .toString();
    }
}
