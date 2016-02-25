package in.maharaja.controllers;

import in.maharaja.gui.AddEditProduct;
import in.maharaja.main.MainApp;
import sun.applet.Main;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Prateek on 14-02-2016.
 */
public class EditProductController extends Controller<AddEditProduct> {

    private String selectedProduct;

    public EditProductController(AddEditProduct app, String productName){
        super(app);
        selectedProduct = productName;
        app.createGUI();
        getProductDetails();
        app.showGUI();
    }

    private void getProductDetails(){
        String query = String.format("SELECT * FROM COUNTER.PRODUCTS WHERE TITLE = '%s'", selectedProduct );
        try{
            ResultSet rs = MainApp.executeQuery( query );
            while( rs.next() ){
                getApp().setVariable("Product Id", rs.getString("ID"));
                getApp().setVariable("Product Name", rs.getString("TITLE"));
                getApp().setVariable("Rate", rs.getString("RATE"));
                getApp().setVariable("Description", rs.getString("DESCRIPTION"));

                Boolean type = rs.getBoolean("IS_LIMITED");

                if( type == true ){
                    ((JComboBox) getElement("Type")).setSelectedIndex( 0 );
                    ((JTextField) getElement("Quantity")).setEnabled(true);
                    query = String.format("SELECT QUANTITY FROM COUNTER.QUANTITY WHERE PID = '%s'", getApp().getVariable("Product Id"));
                    ResultSet r2 = MainApp.executeQuery(query);
                    while( r2.next() ){
                        getApp().setVariable("Initial Quantity", r2.getString("QUANTITY"));
                    }
                } else{
                    ((JTextField) getElement("Quantity")).setEnabled(false);
                    ((JComboBox)getElement("Type")).setSelectedIndex(1);
                }
            }
        } catch(Exception ex1){
            getApp().showError("SQL Error", ex1.toString());
        }
    }

    @Override
    public void registerEvents() throws IllegalStateException {
        ((JButton) getElement("Submit")).addActionListener(e -> {
            AddEditProduct app = getApp();
            String id = app.getVariable("Product Id"),
                    name = app.getVariable("Product Name"),
                    rate = app.getVariable("Rate"),
                    type = app.getVariable("Type"),
                    desc = app.getVariable("Description"),
                    qty = app.getVariable("Quantity");

            Boolean is_limited = type.equalsIgnoreCase("LIMITED") ? Boolean.TRUE : Boolean.FALSE;

            try {
                String query = "UPDATE COUNTER.PRODUCTS SET TITLE = ?, RATE = ?, IS_LIMITED = ?, DESCRIPTION = ? WHERE ID = ?";
                PreparedStatement stmt = MainApp.getConnection().prepareStatement(query);

                stmt.setString(1, name);
                stmt.setDouble(2, Double.parseDouble(rate));
                stmt.setBoolean(3, is_limited);
                stmt.setString(4, desc);
                stmt.setInt(5, Integer.parseInt(id));

                MainApp.executeUpdate(stmt);

            } catch (Exception ex1) {
                System.out.println(ex1.toString());
            }


            if (is_limited) {
                String query = String.format("UPDATE COUNTER.QUANTITY SET QUANTITY = '%s'", qty);
                if (MainApp.executeUpdate(query) == MainApp.Result.SUCCESS)
                    getApp().showNotice("Success", "Data Updated Successfully.");
            }

        });

        ((JComboBox) getElement("Type")).addItemListener(e1 -> {
            if (e1.getStateChange() == ItemEvent.SELECTED) {
                int item = ((JComboBox) getElement("Type")).getSelectedIndex();
                if (item == 0)
                    ((JTextField) getElement("Quantity")).setEnabled(true);
                else
                    ((JTextField) getElement("Quantity")).setEnabled(false);
            }
        });
    }
}
