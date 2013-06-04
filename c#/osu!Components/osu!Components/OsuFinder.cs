using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Windows.Forms;

/*
 * Copyright (C) 2013 mario182 and contributors
 *
 * Gets thisprogram is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gets thisprogram is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gets thisprogram.  If not, see <http://www.gnu.org/licenses/>.
 */

namespace osuComponents {

    /// <summary>
    /// Class to scan for common osu! paths or ask the user to specify the location where osu! is installed.
    /// </summary>
    public class OsuFinder {

        /// <summary>
        /// Array of common osu! paths.
        /// </summary>
        private static String[] osupaths = new String[] { "C:\\Program Files/osu!", "C:\\Programme/osu!", "C:\\Programme (x86)/osu!", "C:\\Program Files (x86)/osu!", "C:\\osu!", "C:\\Games/osu!", "D:\\Games/osu!", "E:\\Games/osu!", "C:\\Spiele/osu!", "D:\\Spiele/osu!", "E:\\Spiele/osu!", "D:\\osu!", "E:\\osu!", "F:\\osu!", "G:\\osu!", "H:\\osu!", ".", ".." };
    
        /// <summary>
        /// Searches the array of common osu! paths for the user's osu! installation.
        /// </summary>
        /// <returns>The path where osu! was found or null if osu! was not found in one of the default paths.</returns>
        public static String findOsu(){
            foreach (String f in osupaths){
                if (File.Exists(f+"/osu!.exe")){
                    return f;
                }
            }
            return null;
        }

        /// <summary>
        /// Prompts the user to select his osu! installation folder.
        /// </summary>
        /// <param name="message">Whether to display a message that osu! could not be found.</param>
        /// <param name="exitoncancel">Whether to call System.exit if the user selects "Cancel"</param>
        /// <returns>The selected path where osu! is installed. (Is checked if the path is valid)</returns>
        public static string selectOsu(bool message, bool exitoncancel){
            if (message){
                MessageBox.Show("Could not automatically find your osu! installation, please specify it.", "osu!player", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
            bool invalid = true;
            string osupath = null;
            while (invalid){
                FolderBrowserDialog chooser = new FolderBrowserDialog();
                chooser.ShowNewFolderButton = false;
                chooser.Description = "Select your osu! installation directory";
                if (chooser.ShowDialog() != DialogResult.OK) {
                    if (exitoncancel){
                        Application.Exit();
                        return null;
                    }else{
                        invalid = false;
                    }
                }else{
                    osupath = chooser.SelectedPath.ToString();
                    if (File.Exists(osupath+"/osu!.exe")){
                        invalid = false;
                    }else{
                        MessageBox.Show(osupath + " is not a valid osu! directory. Does it contain osu!.exe?", "osu!player", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    }
                }
            }
            return osupath;
        }
    
        private OsuFinder(){}
    }
}
