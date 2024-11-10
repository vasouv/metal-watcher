package vs.metalwatcher.archives;

import java.util.List;

public record ArchivesBand(
        String id,
        String name,
        String country,
        String location,
        String formedIn,
        String yearsActive,
        String genre,
        String themes,
        String label,
        String bandCover,
        List<ArchivesAlbum> archivesAlbums
) {
}
