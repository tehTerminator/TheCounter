package in.maharaja.controllers;

import in.maharaja.gui.AddEditProduct;
import in.maharaja.main.MainApp;

import javax.swing.*;
import java.sql.ResultSet;

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
                    ((JTextField)getElement("Quantity")).setEnabled( false );
                    query = String.format("SELECT QUANTITY FROM COUNTER.QUANTITY WHERE PID = '%s'", getApp().getVariable("Product Id"));
                    ResultSet r2 = MainApp.executeQuery(query);
                    while( r2.next() ){
                        getApp().setVariable("Initial Quantity", r2.getString("QUANTITY"));
                    }
                } else{
                    ((JTextField)getElement("Quantity")).setEnabled( true );
                    ((JComboBox)getElement("Type")).setSelectedIndex(1);
                }
            }
        } catch(Exception ex1){
            getApp().showError("SQL Error", ex1.toString());
        }
    }

    @Override
    public void registerEvents() throws IllegalStateException {

    }
}
