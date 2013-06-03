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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Class to scan for common osu! paths or ask the user to specify the location where osu! is installed.
 * @author mario182
 */
public class OsuFinder {

    /** Array of common osu! paths. */
    private final static File[] usualOsuPaths = new File[]{new File("C:\\Program Files/osu!"), new File("C:\\Programme/osu!"), new File("C:\\Programme (x86)/osu!"), new File("C:\\Program Files (x86)/osu!"), new File("C:\\osu!"), new File("C:\\Games/osu!"), new File("D:\\Games/osu!"), new File("E:\\Games/osu!"), new File("C:\\Spiele/osu!"), new File("D:\\Spiele/osu!"), new File("E:\\Spiele/osu!"), new File("D:\\osu!"), new File("E:\\osu!"), new File("F:\\osu!"), new File("G:\\osu!"), new File("H:\\osu!"), new File("."), new File("..")};
    
    /**
     * Searches the array of common osu! paths for the user's osu! installation.
     * @return The path where osu! was found or null if osu! was not found in one of the default paths.
     */
    public static File findOsu(){
        for (File f : usualOsuPaths){
            if (new File(f, "osu!.exe").exists()){
                return f.getAbsoluteFile();
            }
        }
        return null;
    }

    /**
     * Prompts the user to select his osu! installation folder.
     * @param message Whether to display a message that osu! could not be found.
     * @param exitoncancel Whether to call System.exit if the user selects "Cancel"
     * @return The selected path where osu! is installed. (Is checked if the path is valid)
     * @see System#exit(int) 
     */
    public static File selectOsu(boolean message, boolean exitoncancel){
        if (message){
            JOptionPane.showMessageDialog(null, "Could not automatically find your osu! installation, please specify it.", "o!rganizer", JOptionPane.WARNING_MESSAGE);
        }
        boolean invalid = true;
        File osupath = null;
        while (invalid){
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select your osu! installation directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                if (exitoncancel){
                    System.exit(0);
                }else{
                    invalid = false;
                }
            }else{
                osupath = chooser.getSelectedFile();
                if (new File(osupath, "osu!.exe").exists()){
                    invalid = false;
                }else{
                    JOptionPane.showMessageDialog(null, osupath+" is not a valid osu! directory. Does it contain osu!.exe?", "o!rganizer", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        return osupath;
    }
    
    private OsuFinder(){}

}
