package in.maharaja.controllers;

import in.maharaja.gui.DayReport;
import in.maharaja.main.MainApp;
import in.maharaja.utilities.TxtReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.*;

/**
 * Created by Prateek on 11-11-2015.
 */
public class DayReportController extends Controller<DayReport> {

    private HashMap<String, ArrayList<File>> monthWise;
    private String[] data;


    public DayReportController(DayReport app){
        super(app);

        app.createGUI();
        app.showGUI();

        monthWise = new HashMap<>();
        String working_directory = MainApp.appData.get("working_directory", "D:/COUNTER/");
        File working_dir = new File(working_directory );
        File[] allFiles = working_dir.listFiles(TxtReader.getFileFilter());

        for(File f : allFiles){
            try {
                String name = f.getName(),
                        fileDate = name.substring(name.indexOf("_") + 1, name.length() - 4),
                        month = DateTime.parse(fileDate, DateTimeFormat.forPattern("yyyy_MM_dd")).toString("MMM yyyy");

                if (monthWise.containsKey(month))
                    monthWise.get(month).add(f);
                else {
                    monthWise.put(month, new ArrayList<>());
                    monthWise.get(month).add(f);
                }
            } catch(IllegalArgumentException ex1){
                System.out.println(ex1.toString());
            } catch(ArrayIndexOutOfBoundsException ex2){
                System.out.println(ex2.toString());
            }
        }

        Iterator iterator = monthWise.entrySet().iterator();
        while( iterator.hasNext() ){
            Map.Entry pair = (Map.Entry) iterator.next();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(pair.getKey());
            List<File> files = (ArrayList<File>) pair.getValue();
            app.getRootNode().add(node);
            for(File file : files){
                node.add( new DefaultMutableTreeNode( file.getName() ));
            }
        }


    }

    @Override
    public void registerEvents() {
        getApp().registerTreeSelectionListener(e -> {
            String fileName = e.getPath().getLastPathComponent().toString();
            String working_directory = MainApp.appData.get("working_directory", "D:/COUNTER/");
            if( fileName.matches(".*(_\\d{1,4}){3}[.]txt") ){
                try {
                    TxtReader reader = new TxtReader(working_directory + "\\" + fileName);
                    data = reader.getLines();
                    drawTable();
                } catch(Exception ex1){

                }
            }
        });

        getApp().registerChangeListener(e -> {
            try{
                if( data.length > 0 )
                    drawTable();
            } catch (NullPointerException ex1){
            }
        });


    }

    public void drawTable(){
        int reportType = getApp().getReportType();

        if( reportType == 0 ){
            //Draw Detailed Report
            Object[] columnHeaders = {"Time", "Product", "Amount"};
            String[][] rows = new String[data.length][3];
            for(int i =0; i< data.length; i++){
                String[] cell = data[i].split(",");
                rows[i][0] = DateTime.parse(cell[3]).toString( DateTimeFormat.forPattern("hh:mm:ss a") );
                rows[i][1] = cell[2];
                rows[i][2] = cell[1];
            }

            getApp().tablePane.setColumnHeaders(columnHeaders);
            getApp().tablePane.setData(rows);
        } else {
            if (reportType == 1) {
                Object[] columnHeaders = {"Product", "Count", "Amount"};
                Hashtable<String, int[]> products = new Hashtable<>();

                for (int i = 0; i < data.length; i++) {
                    String[] cell = data[i].split(",");
                    if( products.containsKey(cell[2])){
                        products.get(cell[2])[0]++;
                        products.get(cell[2])[1] += Integer.parseInt(cell[1]);
                    } else{
                        products.put(cell[2], new int[]{1, Integer.parseInt(cell[1])});
                    }
                }

                Object[][] rows = new Object[products.size()][3];
                int counter = 0;

                Iterator iterator = products.entrySet().iterator();

                while( iterator.hasNext() ){
                    Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) iterator.next();
                    rows[counter++] = new Object[]{
                            pair.getKey(),
                            Integer.toString( pair.getValue()[0] ),
                            Integer.toString( pair.getValue()[1] ),
                    };
                }

                getApp().tablePane.setColumnHeaders(columnHeaders);
                getApp().tablePane.setData(rows);
            }
        }

    }
}
