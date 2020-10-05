import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.*;

public class Program {

    private final File FILE_PATH_TO_RESOURCE = new File("src/resources");
    private final String NEW_CONNECT_CURRENT_PRICE_FILE = "\\NCndohlcv.txt";
    private final String PARKIET_GLOWNY_CURRENT_PRICE_FILE = "\\ndohlcv.txt";
    private final String F2_CURRENT_PRICE_FILE = "\\f2.txt";
    private final String TRANSACTIONS_FILE = "\\HistoryczneDaneWszystkichTranzakcji.Csv";
    private final String TRANSACTIONS_FILE_NEW = "\\tranzakcja.csv";

    private final URL NEW_CONNECT_ACTUAL_PRICES_URL = new URL("https://bossa.pl/pub/newconnect/omega/ncn/ndohlcv.txt");

    private final URL PARKIET_GLOWNY_ACTUAL_PRICES_URL = new URL("https://bossa.pl/pub/ciagle/omega/cgl/ndohlcv.txt"); //parkiet glowny

    private final URL F2_GLOWNY_URL = new URL("https://bossa.pl/pub/jednolity/f2/omega/f2/ndohlcv.txt"); //parkiet glowny

//TODO dodac zapis danych historycznych wynikow z data.txt plus moze automatyczne uruchamianie kazdego dnia?!


    public Program() throws MalformedURLException {
    }


    public void init() throws IOException {

        FileReaderN fileReaderN = new FileReaderN();

          File fileOfNewConnectActualPrices = new File(FILE_PATH_TO_RESOURCE + NEW_CONNECT_CURRENT_PRICE_FILE);
          File fileOfMainStockActualPrices = new File(FILE_PATH_TO_RESOURCE + PARKIET_GLOWNY_CURRENT_PRICE_FILE);
          File fileOfF2ActualPrices = new File(FILE_PATH_TO_RESOURCE + F2_CURRENT_PRICE_FILE);


        fileOfNewConnectActualPrices = fileReaderN.downloadNewFile(NEW_CONNECT_ACTUAL_PRICES_URL, fileOfNewConnectActualPrices);
        fileOfMainStockActualPrices = fileReaderN.downloadNewFile(PARKIET_GLOWNY_ACTUAL_PRICES_URL, fileOfMainStockActualPrices);
        fileOfF2ActualPrices = fileReaderN.downloadNewFile(F2_GLOWNY_URL, fileOfF2ActualPrices);
        File fileOfTransactions = new File(FILE_PATH_TO_RESOURCE + TRANSACTIONS_FILE);

        //TODO dodac dociaganie traznakcji ze strony


        ArrayList<Transaction> transactions = fileReaderN.addTransactionsFromCSVFile(fileOfTransactions);
        Transaction.correctTheNameOfStock(transactions);

        ArrayList<Stock> actualPricesMainStock = fileReaderN.createListOfStockWithActualPrizes(fileOfMainStockActualPrices);
        ArrayList<Stock> actualPricesNewConnect = fileReaderN.createListOfStockWithActualPrizes(fileOfNewConnectActualPrices);
        ArrayList<Stock> actualPricesF2 = fileReaderN.createListOfStockWithActualPrizes(fileOfF2ActualPrices);

        Vallet vallet = new Vallet();
        vallet.addTransactionsToTheValletsStocks(transactions);//LISTA
        vallet.addActualPrices(actualPricesMainStock);
        vallet.addActualPrices(actualPricesNewConnect);
        vallet.addActualPrices(actualPricesF2);
        vallet.sortTransactionsByTime();//LISTA sprawdzic czy dziala sortowanie

        vallet.addSplit(); //LISTA  //TODO

        vallet.countValletStock(); //LISTA this is it!!
//        vallet.sortValletsStockByLossAndIfActualPrizeIsZero();
        //vallet.sortByHandsProfit();
 //       vallet.sortByHandsLossAndIfActualPrizeIsZero();
//        vallet.sortValletsStockIfActualPrizeIsZeroAndByLossAndByLossOnAHand();

        //vallet.sortValletsStockByLossAndIfActualPrizeIsZero();
//        vallet.sortValletsStockByProfitIncludesHandANDStockActualPrize();
        //vallet.sortValletsStockByLossIncludesHandANDStockActualPrize();
        //posortowac po getValueAtHand()-getVolumeAtHand()*getAvaragePrizePerStockOnAHand() i po zysku najpierw TODO
        vallet.sortByLossWithoutHand();
        vallet.printItAllOut();


        //--------------------------------------------
        //ready
        //dopisac w miare potrzeb nowe akcje ktore trzeba zmienic
        ////old
        // utworz pelna strukture danych
        // posortowac przed wyswietleniem; te co nie maja akcji na poczatek ! :)

        // TODO
        // wymyslic sposob na granie na jednym walorze na krotki i dlugi termin !>

        // dopisac uaktualnienie transakcji z dzisiaj - doczytanie !?

        //1. TODO zrobic czytanie plikow historycznych i zapisywanie wynikow do tablicy
        //ewentualnie umozliwic poruszanie sie po lini czasu jakos...
        // pokazac jakie wyniki mialem w portfelu na przestrzeni czasu
        // zarobek calkowity z akcjami na rece i tym podobne!

        //2. porownac z biznes radar czy mam wszystkie wartosci co tam
        // 3. przechodzenie po historii transakcji
//	2. ikle musz sprzedac akcji by wyujsc na zera na rece !?
        // 1. doczytac biezace zlecenia ?

        // 1. zautomatyzowac uaktualnianie danych
        // - pracuje nad tym pozostalo sprawdzic czy dzien aktualizaji na serwie jest poprawny (pokazuje czwartek zamiast pt

        // 2. wprowadzic zapis tak by mierzyc total zysk strate w czasie
        //skoro nie umiem patrzec w przeszlosc to moze zapisywac na biezaco wyniki i tworzyc historie dzien po dniu...

        // 3. ewentualnie zmierzyc total po rouku dwoch 3; pomyslec czy to jest mozliwe by mierzyc performance przezz rok od poczatku.?

        // 2. wprowadzic swinga - okienka
        // wyswietlanie w okienku

        // 3. mozliwosc wyboru pliku  - dla csv chyba zrobione

        // 4. ewentualnie zamienic wartosc wyswietlana na zakupiona niz na
        // calosciowa

        // wyswietlanie
        // wyswietlanie skrocone tylko te papiery ktore mam na rece?
        // ? opcje sortowania wyswietlania jakos!? poczumakac od ceny 0, od tych
        // co mam na rece, od najwiekszej wartosci, zakupu, straty, zysku etc

        // TODO dodac do transakcji oplaty za tranzakcje

        // TODO zysk strata + to co na rece
        // podzielic na lata, miesiace zysk/strate - > potrzebne dane
        // historyczne: moze je gdzies zapisywac?

        // TODO powinno pokazywac po ile kupilem tyle akcji ile sprzedale wtedy
        // byloby bardziej czytelne

        /* TODO ustawic cene aktualna na zero i obrabiac to jakos komunikatem przy liczeniu traktowac jako przypadek
         * szczegolny */


    }


    private static String zwrocMiDzienTygZLonga(Long dataModyfikacjiPliku1L) {
        Date dataModyfikacjiPliku1 = new Date(dataModyfikacjiPliku1L);
        String dzienTygEdycjiPliku1 = new SimpleDateFormat("EEE").format(dataModyfikacjiPliku1);// dzien zmieny pliku1
        return dzienTygEdycjiPliku1;
    }

    private static void metodkaKtoraMiZapiszeNajnowszyPlikNaDysk(URL url, String file) throws IOException {

        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();

    }


}
