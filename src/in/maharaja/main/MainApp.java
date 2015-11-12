package in.maharaja.main;

import in.maharaja.controllers.MainController;
import in.maharaja.gui.MainUI;
import in.maharaja.sql.Connector;
import in.maharaja.sql.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prateek on 26-07-2015.
 */
public class MainApp {
    private static MainUI app;
    public static int mode = 1;
    public static String working_directory = "D:\\COUNTER\\";

    public static void main(String[] args) {
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

            QueryBuilder q = new QueryBuilder("MAIN.VARIABLES");
            Map<String, String> conditions = new HashMap<>();
            conditions.put("TITLE", "WORKING_DIRECTORY");

            ResultSet rs = stmt.executeQuery( new QueryBuilder("MAIN.VARIABLES").getDataQuery(new String[]{}, conditions));

            while(rs.next()){
                working_directory = rs.getString("DATA");
            }

        } catch (SQLException ex1){
            System.out.println(ex1.toString());
        } catch (ClassNotFoundException ex2){
            System.out.println(ex2.toString());
        }
    }
}

