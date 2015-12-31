package in.maharaja.main;

import in.maharaja.controllers.MainController;
import in.maharaja.gui.MainUI;
import in.maharaja.sql.Connector;
import in.maharaja.sql.QueryBuilder;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prateek on 26-07-2015.
 */
public class MainApp {
    public static int mode = 1;
    public static String working_directory = "D:\\COUNTER\\";
    private static MainUI app;
    private static Image img = Toolkit.getDefaultToolkit().getImage("res/counter-icon.png");
    private static TrayIcon trayIcon = new TrayIcon(img, "Counter");

    public static void main(String[] args) {
        SystemTray systemTray = SystemTray.getSystemTray();

        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException ex) {
            System.err.println("Tray Could Not Be Loaded");
        }

        initialize();
        app = new MainUI();
        MainController mainController = new MainController( app );
        mainController.registerEvents();


    }


    /**
     *
     */
    private static void initialize(){
        try{
            Connector con = new Connector("sa", "");
            Statement stmt = con.getConnection().createStatement();

            QueryBuilder q = new QueryBuilder("COUNTER.VARIABLES");
            Map<String, String> conditions = new HashMap<>();
            conditions.put("TITLE", "WORKING_DIRECTORY");

            ResultSet rs = stmt.executeQuery( new QueryBuilder("COUNTER.VARIABLES").getDataQuery(new String[]{}, conditions));

            while(rs.next()){
                working_directory = rs.getString("DATA");
            }

        } catch (SQLException ex1){
            showNotice("SQL Error", ex1.toString(), TrayIcon.MessageType.ERROR);
        } catch (ClassNotFoundException ex2){
            showNotice("Error", ex2.toString(), TrayIcon.MessageType.ERROR);
        }
    }

    public static void showNotice(String caption, String message) {
        showNotice(caption, message, TrayIcon.MessageType.INFO);
    }

    public static void showNotice(String caption, String message, TrayIcon.MessageType messageType) {
        trayIcon.displayMessage(caption, message, messageType);
    }
}

