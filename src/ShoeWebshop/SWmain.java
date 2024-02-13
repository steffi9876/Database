package ShoeWebshop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class SWmain {

    public static void main(String[] args) throws SQLException, IOException {

        RepositoryWebshop repositoryWebshop = new RepositoryWebshop();
        Scanner scanner = new Scanner(System.in);

        String[] logIn = repositoryWebshop.logIn(scanner); // Anropa login-metoden
        if (logIn != null) { // Om inlogg lyckas!
            String name = logIn[0]; // Hämta namnet från login
            String password = logIn[1]; // hämta lösen från login

            repositoryWebshop.chooseProductWithSP(name, password, scanner); // Anropa metod för att lägga till produkt i varukorg
        }
    }
}
