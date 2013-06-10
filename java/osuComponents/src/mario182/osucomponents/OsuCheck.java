package mario182.osucomponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

/**
 * Class to check if osu! is running.
 * @author mario182
 */
public class OsuCheck {

    /**
     * Checks if osu! is running.
     * @return true if osu! is opened, false if not.
     * @throws IOException If an error occurs while trying to read from the output streams of sub-processes
     * @throws UnsupportedOperationException If an unsupported operating system is used.
     */
    public static boolean checkIfOsuIsRunning() throws IOException{
        if (System.getProperty("os.name", "unknown").equals("Windows")){
            return checkIfOsuIsRunningWindows();
        }else{
            throw new UnsupportedOperationException("Unsupported OS");
        }
    }
    
    private static boolean checkIfOsuIsRunningWindows() throws IOException{
        Process p = Runtime.getRuntime().exec("tasklist /fi \"IMAGENAME eq osu!.exe\"");
        try {
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            br.readLine();
            if (br.readLine() != null){
                if (br.readLine() != null){
                    return true;
                }
            }
            return false;
        }catch(InterruptedException e){
            return false;
        }
    }

    private OsuCheck() {
    }
    
    
}
