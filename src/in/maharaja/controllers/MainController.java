package in.maharaja.controllers;

import in.maharaja.gui.AddProduct;
import in.maharaja.gui.DayReport;
import in.maharaja.gui.MainUI;
import in.maharaja.main.MainApp;
import in.maharaja.sql.Connector;
import in.maharaja.sql.QueryBuilder;
import in.maharaja.utilities.TxtReader;
import in.maharaja.utilities.TxtWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Prateek on 01-11-2015.
 */
public class MainController extends Controller<MainUI>{

    private String clientName;

    public MainController(MainUI app){

        super( app );

        Runnable getDataTask = ()->{
            getAccountList();
            getLastEntry();
        };

        SwingUtilities.invokeLater(() -> {
            Thread getDataThread = new Thread(getDataTask);
            getDataThread.start();

            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
                clientName = address.getHostName();
            } catch (UnknownHostException e) {
                getApp().setStatus("Unknown Host" + e.toString());
                clientName = "Unknown";
            }
        });

        getApp().createGUI();
        getApp().showGUI();


    }

    private void getAccountList(){

        getApp().setStatus("Getting Account List");

        try{
            String username = "sa",
                    password = "";


            Connector con = new Connector(username, password);
            Statement stmt = con.getConnection().createStatement();
            QueryBuilder q = new QueryBuilder("COUNTER.PRODUCTS");

            ResultSet rs = stmt.executeQuery( q.getDataQuery()   );

            // false to overwrite.
            File accountList = new File("D:\\COUNTER\\AccountList.txt");
            PrintWriter pw = new PrintWriter( new BufferedOutputStream( new FileOutputStream(accountList, false)));
            List<String> items = new ArrayList<>();

            while( rs.next() ){
                String a = rs.getString("TITLE");
                items.add( a );
                pw.println( a );
            }

            pw.flush();
            pw.close();

            getApp().populateComboBox( items.toArray( new String[items.size()]) );
        } catch (Exception ex){
            try{
                TxtReader txtReader = new TxtReader(new File("D:\\COUNTER\\AccountList.txt") );
                String products[] = txtReader.getLines();
                getApp().populateComboBox( products );
            } catch(Exception ex1){
                getApp().setStatus(ex1.toString());
            }
            getApp().setStatus( "Unable to Connect to Database, getting Data from AccountList.txt" );
        }
    }

    private void getLastEntry(){
        getApp().setStatus("Getting Last Entries from Record");
        String fileName = "";
        try {
            TxtReader txtReader = new TxtReader(MainApp.working_directory + "\\" + TxtWriter.getFileName() );
            fileName = txtReader.getFileName();
            String[] lines = txtReader.getLines();

            String output = lines[lines.length - 1];

            String[] formatted = output.split(",");
            if (formatted.length > 2) {
                DateTime lastEntryTime = new DateTime(formatted[formatted.length - 1]);
                getApp().setStatus(String.format("Rs. %s - %s at %s", formatted[1], formatted[2], lastEntryTime.toLocalTime().toString()));
            }
        } catch (Exception ex){
            getApp().setStatus( "Unable to Retrieve Last Entries from File " + fileName);
        }
    }

    private void install(){
        try {
            Connector con = new Connector("sa", "");
            Statement stmt = con.getConnection().createStatement();
        } catch(Exception ex){

        }
    }

    /**
     * All the events are registered through this method
     */
    public void registerEvents() {


        getApp().registerActionEvent("Main Submit", updateData->{
            try{
                new DataUpdater(getApp()).execute();
            } catch ( Exception ex ) {
                getApp().setStatus(ex.toString());
            }
        });

        getApp().registerActionEvent("Exit", exitSystem->System.exit(0));

        getApp().registerKeyAdapter("Amount", new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keycode = e.getKeyChar();
                if (!( ( keycode >= '0' && keycode <= '9' ) || ( keycode == 46 ) ) ) {
                    e.consume();
                }
            }
        });

        getApp().registerActionEvent("Daily Report", dr->{
            DayReport dayReport = new DayReport();
            DayReportController controller = new DayReportController( dayReport );
            controller.registerEvents();
            dayReport.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        });

        getApp().registerActionEvent("Products", e -> {
            AddProduct product = new AddProduct();
            product.createGUI();
            product.showGUI();
        });

    }
}

class DataUpdater extends SwingWorker<Integer, Integer>{

    private MainUI app;

    public DataUpdater( MainUI app ){
        this.app = app;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        if( !getApp().isEmpty() ) {
            try {

                TxtWriter writer = new TxtWriter(TxtWriter.getFileName());
                String clientName   = app.getVariable("clientName"),
                        amount      = app.getVariable("Amount"),
                        product     = app.getVariable("Product"),
                        time        = DateTime.now().toString(),
                        output      = String.format("%s,%s,%s,%s\n", clientName, amount,product,time );

                writer.write( output );
                writer.close();

                getApp().setStatus(String.format("Rs.%s - %s at %s", amount, product, new DateTime(time).toString(DateTimeFormat.forPattern("h:m:s a")) ));
                getApp().reset();
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(getApp(), "IO Error while Witting file");
            } catch (NullPointerException e2) {
                JOptionPane.showMessageDialog(getApp(), "Null Pointer Exception Occurred");
            } catch (Exception e3) {
                JOptionPane.showMessageDialog(getApp(), e3.toString());
            }
        }

        return 0;
    }

    private MainUI getApp(){
        return app;
    }
}
