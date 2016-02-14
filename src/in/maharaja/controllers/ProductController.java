package in.maharaja.controllers;

import in.maharaja.gui.AddEditProduct;
import in.maharaja.gui.AddProduct;
import in.maharaja.gui.Products;
import in.maharaja.main.MainApp;
import in.maharaja.sql.Connector;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 * Controller for Product Form
 */
public class ProductController extends Controller<Products> {

    public ProductController(Products app) {
        super(app);
        app.createGUI();
        app.showGUI();

    }

    @Override
    public void registerEvents() {
        ((JButton)getElement("Add Button") ).addActionListener(e1 -> {
            AddEditProduct addEditProduct = new AddEditProduct(AddEditProduct.ADD);
            AddProductController controller = new AddProductController(addEditProduct);
            try{
                controller.registerEvents();
            } catch (Exception e){
                getApp().showError("Illegal State Exception", e.toString());
            }
        });

        ((JButton)getElement("Refresh Button")).addActionListener( e -> reloadProducts() );
    }

    private void reloadProducts() {
        try {
            Connector con = new Connector("sa", "");
            Statement stmt = con.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTER.PRODUCTS");

            Vector<String> productList = new Vector<>();

            while (rs.next()) {
                productList.add(rs.getString("TITLE"));
            }

            getApp().addData(productList);
        } catch (Exception ex) {
            getApp().showError("SQL Exception", ex.toString());
        }
    }
}