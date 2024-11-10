CREATE TABLE albums (
	id serial NOT NULL,
	band_id integer NOT NULL,
	"name" varchar NOT NULL,
	"year" integer NULL,
	"type" varchar NULL,
	i_have boolean NULL,
	high_quality boolean NULL,
	spotify_link varchar NULL,
	bandcamp_link varchar NULL,
	CONSTRAINT albums_pk PRIMARY KEY (id)
);
ALTER TABLE albums ADD CONSTRAINT albums_bands_fk FOREIGN KEY (band_id) REFERENCES bands(id);