package in.maharaja.main;

import in.maharaja.controllers.MainController;
import in.maharaja.gui.MainUI;

import java.awt.*;
import java.sql.*;

/**
 * Main App Handles everything in Background
 */
public class MainApp {
    public static int mode = 1;
    public static String working_directory = "D:\\COUNTER\\";
    private static Image img = Toolkit.getDefaultToolkit().getImage("res/counter-icon.png");
    private static TrayIcon trayIcon = new TrayIcon(img, "Counter");
    private static final PopupMenu popupMenu = new PopupMenu();

    public static void main(String[] args) {
        SystemTray systemTray = SystemTray.getSystemTray();

        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(e -> System.exit(0));
            popupMenu.add(exit);
            trayIcon.setPopupMenu(popupMenu);
        } catch (AWTException ex) {
            System.err.println("Tray Could Not Be Loaded");
        }

        initialize();
        MainUI app = new MainUI();
        MainController mainController = new MainController(app);
        mainController.registerEvents();
    }


    /**
     *
     */
    private static void initialize(){
        ResultSet rs = executeQuery("SELECT * FROM COUNTER.VARIABLES WHERE TITLE = 'WORKING_DIRECTORY'");
        try {
            while(rs.next()){
                working_directory = rs.getString("DATA");
                showNotice("Working Directory", working_directory);
            }
        } catch (SQLException e) {
            showNotice("SQLException", e.toString());
        } catch (NullPointerException ex){
            showNotice("NullPointerException", ex.toString(), TrayIcon.MessageType.ERROR);
        }

    }

    public static void showNotice(String caption, String message) {
        showNotice(caption, message, TrayIcon.MessageType.INFO);
    }

    public static void showNotice(String caption, String message, TrayIcon.MessageType messageType) {
        trayIcon.displayMessage(caption, message, messageType);
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    }

    public static void executeUpdate(String query){
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch(SQLException ex){
            showNotice("SQLException Occured", ex.toString(), TrayIcon.MessageType.ERROR);
        } catch (ClassNotFoundException e) {
            showNotice("ClassNotFoundException", e.toString(), TrayIcon.MessageType.ERROR);
        }
    }

    public static ResultSet executeQuery(String query){
        try{
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (Exception e) {
            showNotice("Exception", e.toString());
        }
        return null;
    }
}

