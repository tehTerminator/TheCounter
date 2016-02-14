package in.maharaja.utilities;

import in.maharaja.gui.MainUI;
import in.maharaja.main.MainApp;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Prateek on 26-07-2015.
 *
 */
public class TxtWriter {

    private PrintWriter out;

    public TxtWriter(String f) throws Exception {

        String working_directory = MainApp.appData.get("working_directory", "D://COUNTER//");
        File directory = new File( working_directory );

        if( !directory.exists() ){
            boolean result = false;

            try{
                directory.mkdir();
                result = true;
            } catch (SecurityException ex){
                throw new Exception("No Proper Security");
            }

            if( !result )
                throw new FileNotFoundException("File Not found, unable to create Directory");
        }

        String fileName = String.format("%s\\%s", working_directory, f );
        out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fileName, true)));
    }

    public static String getFileName(){
        String clientName = null;
        try {
            clientName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            clientName = "Unknown";
        }
        return String.format("%s_%s.txt", clientName, DateTime.now().toString(DateTimeFormat.forPattern("yyyy_MM_dd")));
    }

    public void write(String s) {
        out.println(s);
        out.flush();
    }

    public void close() {
        out.close();
    }

}
