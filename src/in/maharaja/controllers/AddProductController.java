package in.maharaja.controllers;

import in.maharaja.gui.AddEditProduct;
import in.maharaja.main.MainApp;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for AddEditProduct Form
 */
public class AddProductController extends Controller<AddEditProduct> {


    public AddProductController(AddEditProduct app) {
        super(app);
        app.createGUI();
        app.showGUI();
    }

    @Override
    public void registerEvents() {

        ( (JComboBox) getElement("Type") ).addItemListener( e1 -> {
            if( e1.getStateChange() == ItemEvent.SELECTED ){
                int item = ((JComboBox) getElement("Type")).getSelectedIndex();
                if( item == 0 )
                    ((JTextField) getElement("Quantity")).setEnabled( true );
                else
                    ((JTextField) getElement("Quantity")).setEnabled( false );
            }
        });

        ((JButton) getElement("Submit") ).addActionListener(e->{
            AddEditProduct app = getApp();
            String name = app.getVariable("Product Name"),
                    rate = app.getVariable("Rate"),
                    type = app.getVariable("Type"),
                    desc = app.getVariable("Description"),
                    qty = app.getVariable("Quantity");


            int isLimited = type.compareTo("LIMITED") == 1 ? 1 : 0; //if Limited set True else set False
            String query = String.format("INSERT INTO COUNTER.PRODUCTS(TITLE, DESCRIPTION, RATE, IS_LIMITED) VALUES('%s', '%s', '%s', '%d')", name, desc, rate, isLimited);
            MainApp.Result result = MainApp.executeUpdate(query);

            if (result == MainApp.Result.SUCCESS) {
                if (isLimited == 1) {
                    query = "SELECT MAX(ID) AS ID FROM COUNTER.PRODUCTS";
                    ResultSet rs = MainApp.executeQuery(query);
                    int lastID = 0;
                    try {
                        while (rs.next()) {
                            lastID = rs.getInt("ID");
                        }
                        if (lastID > 0) {
                            query = String.format("INSERT INTO COUNTER.QUANTITY(PID, QUANTITY) VALUES('%d', '%d')", lastID, qty);
                            MainApp.Result r2 = MainApp.executeUpdate(query);
                        }
                    } catch (SQLException ex) {
                        getApp().showError("SQL Exception", ex.toString());
                    }
                }
            }
        });
    }
}
