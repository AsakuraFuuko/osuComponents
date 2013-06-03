package mario182.osucomponents;

/*
 * Copyright (C) 2013 mario182 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class holding beatmap informations.
 * @author mario182
 */
public class Beatmap implements Comparable<Beatmap> {

    private HashMap<String, String> beatmap;
    private File path;
    private File song;
    private File background;
    private int beatmapID;
    private String artist;
    private String artistRomanized;
    private String title;
    private String titleRomanized;
    private String video;
    private ArrayList<String> tags;
    private String source;
    private String creator;
    private int videoOffset;
    private File videoFile;
    private String tagsString;

    /**
     * Reads the given beatmap file and creates a new Beatmap object.
     * @param beatmapfile The beatmap to read.
     * @throws IOException If an I/O error occurs or when there is a problem with the beatmap format.
     */
    public Beatmap(File beatmapfile) throws IOException{
        path = beatmapfile.getAbsoluteFile().getParentFile();
        beatmap = OsuFileFormatReader.read(beatmapfile);
        if(beatmap.containsKey("BeatmapID")) {
            beatmapID = Integer.parseInt(beatmap.get("BeatmapID"));
        } else {
            try{
                beatmapID = Integer.parseInt(path.getName().split(" ")[0]);
            }catch(NumberFormatException e){
                beatmapID = 0;
            }
        }
        if(beatmap.containsKey("Artist")) {
            artistRomanized = beatmap.get("Artist");
        } else {
            artistRomanized = "<unknown artist>";
        }
        if(beatmap.containsKey("Title")) {
            titleRomanized = beatmap.get("Title");
        } else {
            titleRomanized = "<unknown title>";
        }
        tags = new ArrayList<>();
        if (beatmap.containsKey("Tags")){
            tagsString = beatmap.get("Tags");
            String[] t = tagsString.split(" ");
            Collections.addAll(tags, t);
        }else{
            tags = new ArrayList<>();
            tagsString = "";
        }
        if (beatmap.containsKey("Source")){
            source = beatmap.get("Source");
        }else{
            source = "<none>";
        }
        if (beatmap.containsKey("Creator")){
            creator = beatmap.get("Creator");
        }else{
            creator = "<none>";
        }
        if(beatmap.containsKey("ArtistUnicode") && beatmap.containsKey("TitleUnicode")) {
            artist = beatmap.get("ArtistUnicode");
            title = beatmap.get("TitleUnicode");
        } else {
            artist = artistRomanized;
            title = titleRomanized;
        }
        if(beatmap.containsKey("Video")) {
            video = beatmap.get("Video");
            videoOffset = Integer.parseInt(beatmap.get("VideoOffset"));
            videoFile = new File(path, video);
        }
        song = new File(path, beatmap.get("AudioFilename"));
        background = new File(path, beatmap.get("Background"));
    }

    /**
     * Returns the raw beatmap data in key-value pairs.
     * @return The raw beatmap data in key-value pairs.
     */
    public HashMap<String, String> getBeatmap() {
        return beatmap;
    }

    /**
     * Returns the path where the beatmap file is located.
     * @return The path where the beatmap file is located.
     */
    public File getPath() {
        return path;
    }

    /**
     * Returns the song file of this beatmap.
     * @return The song file of this beatmap.
     */
    public File getSong() {
        return song;
    }

    /**
     * Returns this beatmap's ID.
     * @return This beatmap's ID.
     */
    public int getBeatmapID() {
        return beatmapID;
    }

    /**
     * Returns the artist of this beatmap's song.
     * @return The artist of this beatmap's song.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Returns the romanized name of the artist of this beatmap's song.
     * @return the romanized name of the artist of this beatmap's song.
     */
    public String getArtistRomanized() {
        return artistRomanized;
    }

    /**
     * Returns the title of this beatmap's song.
     * @return The title of this beatmap's song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the romanized title of this beatmap's song.
     * @return the romanized title of this beatmap's song.
     */
    public String getTitleRomanized() {
        return titleRomanized;
    }

    /**
     * Returns the file name of this beatmap's video.
     * @return The file name of this beatmap's video. (which may be null)
     */
    public String getVideo() {
        return video;
    }

    /**
     * Returns the starting offset of this beatmap's video.
     * @return The starting offset of this beatmap's video.
     */
    public int getVideoOffset() {
        return videoOffset;
    }

    /**
     * Returns this beatmap's video file.
     * @return This beatmap's video file. (which may be null)
     */
    public File getVideoPath() {
        return videoFile;
    }

    /**
     * Returns this beatmap's background file.
     * @return This beatmap's background file.
     */
    public File getBackground() {
        return background;
    }

    /**
     * Returns a list of this beatmap's tags.
     * @return A unmodifiable list of this beatmap's tags.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Returns the source of this beatmap's song.
     * @return The source of this beatmap's song.
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the creator of this beatmap.
     * @return The creator of this beatmap.
     */
    public String getCreator() {
        return creator;
    }
    
    /**
     * Returns this beatmap's tags.
     * @return This beatmap's tags as a space-seperated string;
     */
    public String getTagsAsString() {
        return tagsString;
    }

    /**
     * Returns a string representation of this beatmap.
     * @return A string in the format BeatmapArtist - BeatmapTitle (both romanized)
     */
    @Override
    public String toString() {
        return artistRomanized + " - " + titleRomanized;
    }

    /**
     * Compares this beatmap to another beatmap.
     * @param other The other beatmap.
     * @return -1 if this beatmap's artist is before the other beatmap's artist<br />
     * 0 if this beatmap's artist AND the beatmap's title is before the other beatmap's artist/title<br />
     * 1 if this beatmap's artist is after the other beatmap's artist
     * If the artists equal eachother, the title of the song is checked.
     */
    @Override
    public int compareTo(Beatmap other) {
        int cmp = this.artistRomanized.compareTo(other.artistRomanized);
        if(cmp == 0) {
            return this.titleRomanized.compareTo(other.titleRomanized);
        } else {
            return cmp;
        }
    }

    /**
     * Checks if this beatmap equals another beatmap.
     * @param obj The other beatmap.
     * @return true if this beatmap's artist and title equal the other beatmap.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Beatmap) {
            Beatmap b = (Beatmap)obj;
            return this.artistRomanized.equals(b.artistRomanized) && this.titleRomanized.equals(b.titleRomanized);
        } else {
            return false;
        }
    }

}
