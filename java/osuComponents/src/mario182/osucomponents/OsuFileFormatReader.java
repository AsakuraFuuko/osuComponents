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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

/**
 * Class for reading osu! beatmap files.
 * Currently tested with v10 beatmaps, but should work with any other versions too.
 * @author mario182
 */
public class OsuFileFormatReader {

    /**
     * Reads a beatmap.
     * @param osuFile The .osu file to read
     * @return Key-value pairs of the beatmap's data.
     * @throws IOException If an I/O error occurs or there is a format error in the .osu file.
     */
    public static HashMap<String, String> read(File osuFile) throws IOException {
        String row = null;
        LineNumberReader br = null;
            try {
            HashMap<String, String> b = new HashMap<>();
            OsuFileScanStatus position = OsuFileScanStatus.FORMAT_UNKNOWN;
            br = new LineNumberReader(new FileReader(osuFile));
            while((row = br.readLine()) != null) {
                if(row.startsWith("//") || row.length() == 0) { continue; }
                if(row.startsWith("[")) {
                    position = OsuFileScanStatus.valueOf(row.substring(1, row.length()-1).toUpperCase());
                    continue;
                }
                switch(position) {
                    case FORMAT_UNKNOWN:
                        b.put("FileFormat", row.substring(17));
                        break;
                    case GENERAL:
                    case EDITOR:
                    case METADATA:
                    case DIFFICULTY:
                        String[] s = row.split(":");
                        if (s.length>1){
                            b.put(s[0].trim(), s[1].trim());
                        }
                        break;
                    case EVENTS:
                        if (row.startsWith("//")){
                            break;
                        }else if(row.startsWith("0,0,\"")) {
                            String str = row.substring(5, row.length() - 6);
                            if(str.endsWith("\",0,")) {
                                str = str.substring(0, str.length() - 4);
                            }
                            b.put("Background", str);
                        } else if (row.startsWith("1,") || row.startsWith("Video")){
                            String[] vdata = row.split(",");
                            b.put("VideoOffset", vdata[1]);
                            b.put("Video", vdata[2].substring(1, vdata[2].length()-2));
                        } else {
                            String r = row.trim();
                            // TODO: storyboard parsing
                            if(r.startsWith("Animation")) {

                            } else if(r.startsWith("Sprite")) {

                            } else {

                            }
                        }
                        break;
                }
            }
            if(!b.containsKey("Background")) {
                b.put("Background", "nonexistent.png");
            }
            br.close();
            return b;
        } catch(Exception e) {
            throw new IOException("Failed to read .osu file ("+osuFile+") - Line "+(br!=null?br.getLineNumber():"0")+": "+row, e);
        }
    }

    private enum OsuFileScanStatus {
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

    private OsuFileFormatReader() {
    }
    
}
