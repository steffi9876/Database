package ShoeWebshop;

import ShoeWebshop.T.Customer;
import ShoeWebshop.T.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RepositoryWebshop {

    private final Connection connection;
    public RepositoryWebshop() throws IOException, SQLException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("/Users/stephanie/Desktop/Javamapp/Database/src/ShoeWebshop/Settings.properties"));

        connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
        }

   public String[] logIn(Scanner scanner) throws SQLException, IOException {
       String name; // var. lagra namn
       String password; // var. lagra lösen

       System.out.println("Hej och välkommen till Stephanies sko-butik, " +
               "för att komma vidare, vänligen logga in.");
       System.out.println("Skriv in namn: ");
       name = scanner.nextLine(); // läs in namn fr anv
       System.out.println("Skriv in lösenord");
       password = scanner.nextLine(); // // läs in lösen fr anv

       if (isValidUser(name, password)) { // Kontrollera om namn, lösen är giltiga med hjälp av metod isValidUser
           List<Product> products = allProductsFromDatabase(); //Hämta produkterna från databasen
           printProducts(products); // skriv ut alla produkter

           return new String[]{name, password}; // Returnera namn och lösen i en strängarray

       } else {
           System.out.println("Ogiltigt namn eller lösenord");
           return null;
       }
   }

    public boolean isValidUser(String name, String password) throws SQLException {
        String query = "select fullName, password from Customer where fullName = ? and password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name); //
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery(); // executa och lagra i resultset
            return resultSet.next(); // Returnera boolean true om det finns en rad i resultatet
        }
    }

  public List<Customer> allCustomers(String name, String password) throws SQLException {
      List<Customer> customers = new ArrayList<>(); // Skapa en tom lista för kunder

      String query = "SELECT id, fullName, adress, locality FROM Customer WHERE fullName = ? AND password = ?";
      try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, name);
          ps.setString(2, password);
          ResultSet resultSet = ps.executeQuery();
          while (resultSet.next()) { // Loopa igenom varje rad i resultset
              /*final int id = resultSet.getInt("id");
              String fullName = resultSet.getString("fullName");
              String address = resultSet.getString("adress");
              String locality = resultSet.getString("locality");*/

              final Customer customer = new Customer( // Här stoppar jag in utläsningarna direkt in i kunden, och gör Customer till final
                      resultSet.getInt("id"),
                      resultSet.getString("fullName"),
                      resultSet.getString("adress"),
                      resultSet.getString("locality")); // Skapa nytt customer-obj med info från resultat

              customers.add(customer); //lägg till kunden i listan av kunder
          }
          if (customers.isEmpty()) {

              throw new SQLException("User not found with the provided credentials.");
          }
      }
      return customers;
  }

   public List<Product> allProductsFromDatabase() throws SQLException {
       List<Product> products = new ArrayList<>(); // Skapa en tom lista för produkter

       try (PreparedStatement preparedStatement = connection.prepareStatement(
               "SELECT id, color, size, price, inventoryBalance FROM Product")) {

           ResultSet resultSet = preparedStatement.executeQuery();
           int index = 1; // Skapa en räknare för indexering av produkter

           while (resultSet.next()) { // Loopa genom raderna
               final int id = resultSet.getInt("id"); // ---- Här mellanlagrar jag variablerna med final!
               final String color = resultSet.getString("color");
               final int size = resultSet.getInt("size");
               final int price = resultSet.getInt("price");
               final int inventoryBalance = resultSet.getInt("inventoryBalance");

               // Create a new Product object with an index and add it to the list
               final Product product = new Product(id, color, price, size, inventoryBalance); // Skapa produkt obj med info från resultat
               products.add(product); // lägg till produkt i listan
               index++; // Öka index för varje produkt
           }
       }

       return products; // returnera listan av produkter
   }
    public void printProducts(List<Product> products) {

        IntStream.range(0, products.size()) // Skapa en ström av intar som börjar på 0, av index för varje produkt i listan
                .forEach(index -> { // itererar över varje index i strömmen
                    Product product = products.get(index); // Hämta prod. med akktuellt index fr listan
                    System.out.println("Shoe index: " + (index + 1)); // hämtar produkten från listan products, det det ektuella indexet som itereras just nu (1 åt gången)
                    System.out.println("Color: " + product.getColor());
                    System.out.println("Size: " + product.getSize());
                    System.out.println("Price: " + product.getPrice());
                    System.out.println("Inventory Balance: " + product.getBalance());
                    System.out.println(); // tom rad!
                });
    }

    public void chooseProductWithSP(String name, String password, Scanner scanner) throws SQLException {
        System.out.println("Välj en produkt genom att ange sko index:");
        int selectedIndex = scanner.nextInt();


        final List<Customer> customers = allCustomers(name, password); // Hämta listan över kunder, baserat på namn och lösen!


        customers.forEach(customer -> { // bearbeta listan över kunder med lambda uttryck

            int userId = customer.getId(); // Hämta user id!


            try {
                CallableStatement callableStatement = connection.prepareCall("CALL AddToCart(?, ?, ?)"); // Ropa på min SP
                callableStatement.setInt(1, userId); // Customer-Id
                callableStatement.setInt(2, 1); // Orders id
                callableStatement.setInt(3, selectedIndex); // Product-id

                callableStatement.executeUpdate();
                System.out.println("Produkten har lagts till i varukorgen.");
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
}

