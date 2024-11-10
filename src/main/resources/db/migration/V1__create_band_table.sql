CREATE TABLE bands (
	id serial NOT NULL,
	"name" varchar NOT NULL,
	metal_archives_id integer NULL,
	spotify_link varchar NULL,
	bandcamp_link varchar NULL,
	CONSTRAINT bands_pk PRIMARY KEY (id)
);