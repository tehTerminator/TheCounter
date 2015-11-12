package in.maharaja.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek on 04-11-2015.
 */
public class TxtReader {
    private BufferedReader bufferedReader;
    private List<String> lines;
    private String fileName = "";


    public TxtReader(File file) throws IOException{
        fileName = file.getAbsolutePath();
        bufferedReader = null;
        lines = new ArrayList<>();

        bufferedReader = new BufferedReader( new FileReader(file) );
        String currentLine;

        while( (currentLine = bufferedReader.readLine() ) != null){
            if( currentLine.compareTo("") != 0 )
                lines.add(currentLine);
        }

        bufferedReader.close();
    }

    public TxtReader(String fileName) throws IOException{
        this( new File(fileName ) );
    }

    public String getFileName(){ return fileName; }

    public String[] getLines(){
        return lines.toArray(new String[ lines.size() ] );
    }

    public static boolean fileExist(File f){
        return f.isFile();
    }

    public static boolean fileExist(String f){
        File file = new File(f);
        return file.isFile();
    }

    public static FileFilter getFileFilter(String extension, String regex){
        return pathname -> {
            String ex = pathname.getName().substring( pathname.getName().lastIndexOf(".") );
            if( ex.equals(extension) && pathname.getName().matches(regex))
                return true;
            return false;
        };
    }
}
