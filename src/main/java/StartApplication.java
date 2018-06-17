import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class StartApplication {


    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {


    Program program = new Program();

    program.init();//fds
        System.out.println();

    }

}

// w razie zmiany nazwy papieru dodac kolejny wpis do Transaction


// TODO
//zapisac plik transakcji z mBanku do pliku CSV i dodac brakujace rekordy do pliku PROD.MAKL_(2).Csv - notowania sciagaja sie automatycznie
//
// 1. odczyt z pliku wartosci ceny !! historycznej 
// szukanie i przeliczanie portfela na tej podstawie
//poprawic site bo zmienilo nazwe dwa razy
