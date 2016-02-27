package in.maharaja.controllers;

import in.maharaja.gui.DayReport;
import in.maharaja.gui.MainUI;
import in.maharaja.gui.Products;
import in.maharaja.main.MainApp;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Controller for MainUI
 */
public class MainController extends Controller<MainUI>{

    private String clientName;

    public MainController(MainUI app){

        super( app );

        getAccountList();

        Runnable getDataTask = ()->{

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
                getApp().showWarning("Error", "Unknown Host" + e.toString());
                clientName = "Unknown";
            }
        });

        getApp().createGUI();
        getApp().showGUI();


    }

    private void getAccountList(){

        try{
            ResultSet rs = MainApp.executeQuery("SELECT * FROM COUNTER.PRODUCTS");

            // false to overwrite.
            PrintWriter pw = new PrintWriter("D:/COUNTER/ProductList.txt", "UTF-8");
            List<String> items = new ArrayList<>();

            assert rs != null;
            while( rs.next() ){
                String a = rs.getString("TITLE");
                items.add( a );
                pw.println( a );
            }
            pw.close();

            getApp().populateComboBox( items.toArray( new String[items.size()]) );
        } catch (Exception ex){
            try{
                TxtReader txtReader = new TxtReader(new File("D:\\COUNTER\\ProductList.txt") );
                String products[] = txtReader.getLines();
                getApp().populateComboBox( products );
            } catch(Exception ex1){
                getApp().showWarning("Exception", ex1.toString());
            } finally {
                getApp().showError("Database Error", "Unable to Connect to Database, getting Data from AccountList.txt" );
            }
        }
    }

    private void getLastEntry(){
        String fileName = "";
        try {
            String working_directory = MainApp.appData.get("working_directory", "D:/COUNTER/");
            TxtReader txtReader = new TxtReader(working_directory + "\\" + TxtWriter.getFileName() );
            fileName = txtReader.getFileName();
            String[] lines = txtReader.getLines();

            String output = lines[lines.length - 1];

            String[] formatted = output.split(",");
            if (formatted.length > 2) {
                DateTime lastEntryTime = new DateTime(formatted[formatted.length - 1]);
                getApp().showNotice("Task", String.format("Rs. %s - %s at %s", formatted[1], formatted[2], lastEntryTime.toLocalTime().toString()));
            }
        } catch (Exception ex){
            return;
        }
    }

    private void install(){

    }

    /**
     * All the events are registered through this method
     */
    public void registerEvents() {
        ((JButton)getElement("Main Submit")).addActionListener( e1 -> {
            try{
                new DataUpdater(getApp()).execute();
            } catch ( Exception ex ) {
                getApp().showNotice("Data Inserted", ex.toString());
            }
        });

        ((JMenuItem)getElement("Exit")).addActionListener(e1 -> System.exit(0));

        ((JTextField)getElement("Amount")).addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                int keycode = e.getKeyChar();
                if (!( ( keycode >= '0' && keycode <= '9' ) || ( keycode == 46 ) ) ) {
                    e.consume();
                }
            }
        });

        ((JMenuItem)getElement("Daily Report")).addActionListener(dr->{
            DayReport dayReport = new DayReport();
            DayReportController controller = new DayReportController( dayReport );
            try {
                controller.registerEvents();
            } catch(Exception ex){
                getApp().showError("Illegal State Exception", ex.toString());
            }
            dayReport.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        });

        ((JMenuItem)getElement("Get Last Entries")).addActionListener(e2 -> getLastEntry());
        ((JMenuItem) getElement("Get Products")).addActionListener(e2 -> getAccountList());

        ((JMenuItem)getElement("Products")).addActionListener(e -> {
            Products p = new Products();
            ProductController controller = new ProductController(p);
            try{
                controller.registerEvents();
            } catch (IllegalStateException e1){
                MainApp.showNotice("Illegal State Exception", e1.toString());
            }
        });

        ((JMenuItem) getElement("Working Dir")).addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("Select Working Directory");
            jFileChooser.setCurrentDirectory( new File(MainApp.appData.get("working_directory", "D:/COUNTER/")));
            jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );



            if( jFileChooser.showDialog( getApp(), "Select Directory") == JFileChooser.APPROVE_OPTION){
                String dir = jFileChooser.getSelectedFile().getAbsolutePath();
                MainApp.appData.put("working_directory", dir);
                MainApp.showNotice("Working Directory", dir);

            }

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

                getApp().showNotice("Inserted", String.format("Rs.%s - %s at %s", amount, product, new DateTime(time).toString(DateTimeFormat.forPattern("h:m:s a")) ));
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
