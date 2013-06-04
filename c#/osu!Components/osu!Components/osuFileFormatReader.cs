using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

/*
 * Copyright (C) 2013 mario182 and contributors
 *
 * Gets this program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gets this program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gets this program.  If not, see <http://www.gnu.org/licenses/>.
 */

namespace osuComponents {
    
    /// <summary>
    /// Class for reading osu! beatmap files.
    /// Currently tested with v10 beatmaps, but should work with any other versions too.
    /// </summary>
    public class osuFileFormatReader {

        private static osuFileScanStatus position;

        /// <summary>
        /// Reads a beatmap.
        /// </summary>
        /// <param name="osuFile">The .osu file to read</param>
        /// <returns>Key-value pairs of the beatmap's data.</returns>
        public static Dictionary<string, string> Read(string osuFile) {
            try {
                Dictionary<string, string> b = new Dictionary<string, string>();
                position = osuFileScanStatus.FORMAT_UNKNOWN;
                foreach(var row in File.ReadAllLines(osuFile)) {
                    if(row.StartsWith("//") || row.Length == 0) { continue; }
                    if(row.StartsWith("[")) {
                        position = (osuFileScanStatus)System.Enum.Parse(typeof(osuFileScanStatus), row.Substring(1, row.Length-2).ToUpper());
                        continue;
                    }
                    switch(position) {
                        case osuFileScanStatus.FORMAT_UNKNOWN:
                            b.Add("FileFormat", row.Substring(17));
                            break;
                        case osuFileScanStatus.GENERAL:
                        case osuFileScanStatus.EDITOR:
                        case osuFileScanStatus.METADATA:
                        case osuFileScanStatus.DIFFICULTY:
                            string[] s = row.Split(':');
                            b.Add(s[0].Trim(), s[1].Trim());
                            break;
                        case osuFileScanStatus.EVENTS:
                            if (row.StartsWith("//")){
                                break;
                            }else if(row.StartsWith("0,0,\"")) {
                                string str = row.Substring(5, row.Length - 6);
                                if(str.EndsWith("\",0,")) {
                                    str = str.Substring(0, str.Length - 4);
                                }
                                b.Add("Background", str);
                            } else if (row.StartsWith("1,") || row.StartsWith("Video")){
                                string[] vdata = row.Split(',');
                                b.Add("VideoOffset", vdata[1]);
                                b.Add("Video", vdata[2].Substring(1, vdata[2].Length-2));
                            } else {
                                string r = row.Trim();
                                // TODO: storyboards
                                if(r.StartsWith("Animation")) {

                                } else if(r.StartsWith("Sprite")) {

                                } else {

                                }
                            }
                            break;
                    }
                }
                if(!b.ContainsKey("Background")) {
                    b.Add("Background", "none");
                }
                return b;
            } catch(SystemException e) {
                Console.WriteLine(e.StackTrace);
                throw new FormatException("Failed to read .osu file", e);
            }
        }
    }

    enum osuFileScanStatus {
        FORMAT_UNKNOWN,
        GENERAL,
        EDITOR,
        METADATA,
        DIFFICULTY,
        EVENTS,
        TIMINGPOINTS,
        COLOURS,
        HITOBJECTS,
    }
}
