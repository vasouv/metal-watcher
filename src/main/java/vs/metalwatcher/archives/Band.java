package vs.metalwatcher.archives;

import java.util.List;

public record Band(
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
        List<Album> albums
) {
}
