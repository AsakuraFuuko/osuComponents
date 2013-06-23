using osuComponents.Storyboard;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.IO;
using System.Text;

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

namespace osuComponents {

    /// <summary>
    /// Class holding beatmap informations.
    /// </summary>
    [Serializable()]
    public class Beatmap : IComparable<Beatmap> {

        /// <summary>
        /// Number of read beatmaps.
        /// </summary>
        public static int read;
        /// <summary>
        /// Beatmap thread lock object.
        /// </summary>
        public static object locker = new Object();
        /// <summary>
        /// Count of beatmaps without ID numbers
        /// </summary>
        public static int nonumbers = -1;
        
        [field: NonSerialized()]
        private Dictionary<string, string> beatmap;
        private List<StoryboardEvent> sbevents;
        private List<String> tags;
        private String path;
        private String song;
        private int b_id;
        private String artist;
        private String artist_r;
        private String title;
        private String title_r;
        private String video;
        private String tagsString;
        private String source;
        private int video_offset;
        private string video_path;

        /// <summary>
        /// Gets the artist of this beatmap's song.
        /// </summary>
        public String Artist { get { return artist; } }
        /// <summary>
        /// Gets the romanized name of the artist of this beatmap's song.
        /// </summary>
        public String ArtistRomanized { get { return artist_r; } }
        /// <summary>
        /// Gets the title of this beatmap's song.
        /// </summary>
        public String Title { get { return title; } }
        /// <summary>
        /// Gets the romanized title of this beatmap's song.
        /// </summary>
        public String TitleRomanized { get { return title_r; } }
        /// <summary>
        /// Gets this beatmap's tags as a space-seperated string.
        /// </summary>
        public String TagsString { get { return tagsString; } }
        /// <summary>
        /// Gets a list of this beatmap's tags.
        /// </summary>
        public List<String> Tags { get { return tags; } }
        /// <summary>
        /// Gets the source of this beatmap's song.
        /// </summary>
        public String Source { get { return source; } }
        /// <summary>
        /// Gets the path where the beatmap file is located.
        /// </summary>
        public String Beatmap_Path { get { return path; } }
        /// <summary>
        /// Gets the song file of this beatmap.
        /// </summary>
        public String Song_Path { get { return song; } }
        /// <summary>
        /// Gets this beatmap's ID.
        /// </summary>
        public int Beatmap_ID { get { return b_id; } }
        /// <summary>
        /// Gets the file name of this beatmap's video. (which may be null)
        /// </summary>
        public String Video { get { return video; } }
        /// <summary>
        /// Gets the starting offset of this beatmap's video.
        /// </summary>
        public int VideoOffset { get { return video_offset; } }
        /// <summary>
        /// Gets this beatmap's video file. (which may be null)
        /// </summary>
        public string VideoPath { get { return video_path; } }
        /// <summary>
        /// Gets or sets this beatmap's background file.
        /// </summary>
        public string BackgroundPath { get; set; }

        /// <summary>
        /// Reads the given beatmap file and creates a Beatmap object.
        /// </summary>
        /// <param name="beatmap_path">The beatmap to read.</param>
        public Beatmap(String beatmap_path) {
            path = beatmap_path;
            beatmap = osuFileFormatReader.Read(beatmap_path);
            if(beatmap.ContainsKey("BeatmapID")) {
                b_id = Convert.ToInt32(beatmap["BeatmapID"]);
            } else {
                b_id = nonumbers--;
            }
            if(beatmap.ContainsKey("Artist")) {
                artist_r = beatmap["Artist"];
            } else {
                artist_r = "<unknown artist>";
            }
            if(beatmap.ContainsKey("Title")) {
                title_r = beatmap["Title"];
            } else {
                title_r = "<unknown title>";
            }
            if(beatmap.ContainsKey("Source")) {
                source = beatmap["Source"];
            } else {
                source = "<unknown source>";
            }
            tags = new List<string>();
            if(beatmap.ContainsKey("Tags")) {
                tagsString = beatmap["Tags"];
                foreach (string s in beatmap["Tags"].Split('=')){
                    tags.Add(s);
                }
            } else {
                tagsString = "";
            }
            if(beatmap.ContainsKey("ArtistUnicode") && beatmap.ContainsKey("TitleUnicode")) {
                artist = beatmap["ArtistUnicode"];
                title = beatmap["TitleUnicode"];
            } else {
                artist = artist_r;
                title = title_r;
            }
            string[] pathparts = beatmap_path.Split('\\');
            if(beatmap.ContainsKey("Video")) {
                video = beatmap["Video"];
                video_offset = int.Parse(beatmap["VideoOffset"]);
                video_path = String.Join("\\", pathparts, 0, pathparts.Length - 1) + "\\" + video;
            }
            song = String.Join("\\", pathparts, 0, pathparts.Length - 1) + "\\" + beatmap["AudioFilename"];
            BackgroundPath = String.Join("\\", pathparts, 0, pathparts.Length - 1) + "\\" + beatmap["Background"];
        }

        /// <summary>
        /// Searches the given folder for beatmaps and constructs Beatmap objects.
        /// </summary>
        /// <param name="path">The path to search for beatmaps</param>
        /// <returns>A Dictionary object where the key is the beatmap ID and the value is the Beatmap object.</returns>
        public static Dictionary<int, Beatmap> SearchDirectory(string path) {
            Dictionary<int, Beatmap> b = new Dictionary<int, Beatmap>();
            lock(locker) {
                read = 0;
            }
            string[] files = Directory.GetFiles(path, "*.osu", SearchOption.AllDirectories);
            foreach(string beatmap in files) {
                lock(locker) {
                    read++;
                }
                Beatmap bm;
                try {
                    bm = new Beatmap(beatmap);
                } catch(SystemException) {
                    Console.WriteLine("Failed to read beatmap: " + beatmap);
                    continue;
                }
                if(!b.ContainsKey(bm.b_id) && !b.ContainsValue(bm)) {
                    b.Add(bm.b_id, bm);
                }
            }
            return b;
        }

        /// <summary>
        /// Returns a string representation of this beatmap.
        /// </summary>
        /// <returns>A string in the format BeatmapArtist - BeatmapTitle (both romanized)</returns>
        public override string ToString() {
            return artist_r + " - " + title_r;
        }

        /// <summary>
        /// Compares this beatmap to another beatmap.
        /// </summary>
        /// <param name="other">The other beatmap.</param>
        /// <returns>-1 if this beatmap's artist is before the other beatmap's artist<br />* 0 if this beatmap's artist AND the beatmap's title is before the other beatmap's artist/title<br />* 1 if this beatmap's artist is after the other beatmap's artist<br />* If the artists equal eachother, the title of the song is checked.</returns>
        public int CompareTo(Beatmap other) {
            int artist = this.artist_r.CompareTo(other.artist_r);
            if(artist == 0) {
                return this.title_r.CompareTo(other.title_r);
            } else {
                return artist;
            }
        }

        /// <summary>
        /// Checks if this beatmap equals another beatmap.
        /// </summary>
        /// <param name="obj">The other beatmap.</param>
        /// <returns>true if this beatmap's artist and title equal the other beatmap.</returns>
        public override bool Equals(object obj) {
            if(obj is Beatmap) {
                Beatmap b = (Beatmap)obj;
                return this.artist_r.Equals(b.artist_r) && this.title_r.Equals(b.title_r);
            } else {
                return false;
            }
        }

    }
}
