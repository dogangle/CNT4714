/*
Name: Logan Bohan-Moulton
Course: CNT 4714 - Spring 2019
Assignment title: Program 1 - Event-driven Programming
Date: Sunday Janurary 27, 2019
 */
package shop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import userInterface.UserInterface;


public class Shop extends UserInterface
{
    int itemNum = 1;
    int orderNum = 0;
    int bookID;
    int bookNum;
    double subtotal;
    double price;
    String info = "";
    ArrayList<Book> outputInfo = new ArrayList<Book>();
    
    public Shop()
    {
        viewButton.setEnabled(false);
        finishButton.setEnabled(false);
        
        orderNumLabel.setText("Enter the number of items in this order:");
        bookIDLabel.setText("Enter Book ID for Item #" + itemNum + ":");
        quantityLabel.setText("Enter quantity for Item #" + itemNum + ":");
        infoLabel.setText("Item #" + itemNum + " info:");
        subtotalLabel.setText("Order subtotal for " + (itemNum-1) + " item(s):");
        processButton.setText("Process Item #" + itemNum);
        confirmButton.setText("Confirm Item #" + itemNum);
        viewButton.setText("View Order");
        finishButton.setText("Finish Order");
        newButton.setText("New Order");
        exitButton.setText("Exit");

        // Extends variables created from GUI builder
        // Replace placeholder text from GUI builder with proper labels

        confirmButton.setEnabled(false);
        infoTextField.setEnabled(false);
        subtotalTextField.setEnabled(false);

        // Disable buttons and text fields that user can't access yet


        
        processButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Book tempBook;
                double tempPrice;
                
                String line;
                orderNum = Integer.parseInt(orderNumTextField.getText());
                bookID = Integer.parseInt(bookIDTextField.getText());
                bookNum = Integer.parseInt(quantityTextField.getText());
                
                // Parse info from text fields
                
                tempBook = readFile(bookID);
                
                // Search inventory using book ID
                
                if (bookNum >= 1 && bookNum <= 4)
                {
                    tempBook.subtotal = Double.parseDouble(tempBook.cost) * bookNum;
                    tempBook.discount = 0;
                }
                else if (bookNum >= 5 && bookNum <=9)
                {
                    tempPrice = Double.parseDouble(tempBook.cost) * bookNum;
                    tempBook.subtotal = tempPrice - (tempPrice * 0.10);
                    tempBook.discount = 10;
                }
                else if (bookNum >= 10 && bookNum <= 14)
                {
                    tempPrice = Double.parseDouble(tempBook.cost) * bookNum;
                    tempBook.subtotal = tempPrice - (tempPrice * 0.15);
                    tempBook.discount = 15;
                }
                else
                {
                    tempPrice = Double.parseDouble(tempBook.cost) * bookNum;
                    tempBook.subtotal = tempPrice - (tempPrice * 0.20);
                    tempBook.discount = 20;
                }
                
                // Apply any discounts
               
                line = new String(tempBook.bookID + tempBook.title + " $" + tempBook.cost + " " + bookNum + " " + String.format("%.0f", tempBook.discount) + "% " + String.format("$%.2f", tempBook.subtotal));
                info += itemNum + ". " + line + "\n";
                price = tempBook.subtotal;
                tempBook.qty = bookNum;
                infoTextField.setText(line);
                
                // As each book is processed, add information to string of completed books so far
                
                
                outputInfo.add(tempBook);
                processButton.setEnabled(false);
                confirmButton.setEnabled(true);
                orderNumTextField.setEnabled(false);
                
                // Update textfields and buttons
            }});
        
        
        confirmButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (itemNum == 1)
                {
                    subtotal = price;
                    subtotalTextField.setText(String.format("$%.2f", subtotal));
                }
                else
                {
                    subtotal = price + subtotal;
                    subtotalTextField.setText(String.format("$%.2f", subtotal));
                }
                
                // Set initial subtotal if this is the first book
                // Update subtotal elsewise
                
                JOptionPane.showMessageDialog(null, "Item #" + itemNum + "Accepted");
                
                // Confirmation message
                
                if (itemNum == orderNum)
                {
                    processButton.setText("Process Item");
                    confirmButton.setText("Confirm Item");
                    bookIDLabel.setText("");
                    quantityLabel.setText("");
                    bookIDTextField.setEnabled(false);
                    bookIDTextField.setText("");
                    quantityTextField.setEnabled(false);
                    quantityTextField.setText("");
                    confirmButton.setEnabled(false);
                }
                
                // If all items have been complete, end switching between process button and confirm button
                
                else
                {
                    itemNum++;
                    processButton.setEnabled(true);
                    confirmButton.setEnabled(false);
                    bookIDTextField.setText("");
                    quantityTextField.setText("");
                    updateUI();
                    
                }
                
                // Ready UI to process another book
                
                viewButton.setEnabled(true);
                finishButton.setEnabled(true);
            
            }});
        
        
        viewButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, info);
            }});
        
        // Display order info in message
        
        finishButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Date time = new Date();
                SimpleDateFormat date1 = new SimpleDateFormat("dd/MM/yy, hh:mm:ss a z");
                SimpleDateFormat date2 = new SimpleDateFormat("ddMMyyyyhhmm");
                String strDate1 = date1.format(time);
                String strDate2 = date2.format(time);
                JOptionPane.showMessageDialog(null, "Date: " + strDate1 + "\n\nNumber of line items: " + orderNum + 
                        "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n" + info + "\n\nOrder subtotal: $" + String.format("%.2f", subtotal) + 
                        "\n\nTax rate:    6%\n\nTax amount:     $" + String.format("%.2f", 0.06*subtotal) + "\n\nOrder total:     $" 
                        + String.format("%.2f", ((0.06*subtotal)+subtotal)) + "\n\nThanks for shopping at the Ye Olde Book Shoppe!");
                
                // Display finished order receipt
                for (Book books: outputInfo)
                {
                    try(FileWriter fw = new FileWriter("transactions.txt", true);
                        BufferedWriter writer = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(writer))
                    {

                            out.println(strDate2 + ", " + books.bookID + ", " + books.title + ", " + books.cost + ", " + books.qty + ", " + String.format("%.1f", books.discount) + ", "
                                    + String.format("$%.2f", books.subtotal) + ", " + strDate1);

                    } catch (IOException ex) {
                        Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                }});
        
                // Outputs file with purchase information
        
        newButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                orderNumTextField.setEnabled(true);
                orderNumTextField.setText("");
                bookIDTextField.setText("");
                bookIDTextField.setEnabled(true);
                quantityTextField.setText("");
                quantityTextField.setEnabled(true);
                infoTextField.setText("");
                subtotalTextField.setText("");
                processButton.setEnabled(true);
                confirmButton.setEnabled(false);
                viewButton.setEnabled(false);
                finishButton.setEnabled(false);
                itemNum = 1;
                updateUI();
                subtotal = 0;
                price = 0;
                info = "";
                outputInfo.clear();
                
            }});
        
        // Clear all values and reset back to beginning of program
        
        
    }
    
    
    public Book readFile(int bookID)
    {
        try(BufferedReader in = new BufferedReader(new FileReader("inventory.txt"))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] arr = str.split(",");
                int i = Integer.parseInt(arr[0]);
                if (bookID == i)
                {
                    Book book = new Book();
                    book.setBookID(arr[0]);
                    book.setTitle(arr[1]);
                    book.setCost(arr[2]);
                    book.setLine(arr[0] + arr[1] + arr[2]);
                    return book;
                }
                
            }
            JOptionPane.showMessageDialog(null, "Book ID " + bookID + " not in file");
            return null;
            
        } catch (IOException e) {}return null;
}
    
    // Read in file line by line and seperate strings by commas
    // Determine if book ID matches any of the IDs listed in inventory, if so return book
    // If not, return error message and return nothing
    

    public void updateUI()
    {
        bookIDLabel.setText("Enter Book ID for Item #" + itemNum + ":");
        quantityLabel.setText("Enter quantity for Item #" + itemNum + ":");
        infoLabel.setText("Item #" + itemNum + " info:");
        subtotalLabel.setText("Order subtotal for " + (itemNum-1) + " item(s):");
        processButton.setText("Process Item #" + itemNum);
        confirmButton.setText("Confirm Item #" + itemNum);
        
    }
    
    // Updates UI with new item #
    
    public static void main(String[] args) 
    {
        UserInterface userinterface = new UserInterface();
        userinterface.setContentPane(new Shop().jPanel1);
        userinterface.setVisible(true);
        
        // Replace layout from GUI builder with changes made in Shop()
        // Display GUI
    }
    

}
