import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Program {


    //        URL urlOfMainStockFile = new URL("http://bossa.pl/pub/ciagle/omega/cgl/ndohlcv.txt");
    URL urlNC = new URL("http://bossa.pl/pub/newconnect/omega/ncn/ndohlcv.txt");
    URL urlMS = new URL("http://bossa.pl/pub/ciagle/omega/cgl/ndohlcv.txt");

    //        String file = "src/resources/ndohlcv.txt"; //TODO nieuzywane?
    File filesPathToResources = new File("src/resources");


    public Program() throws MalformedURLException {
    }


    public void init() throws ParseException, IOException {


        Map<String, PapierGlowna> tablicaPortfel;
        Map<String, ArrayList<Stock>> tablicaHistoriaTranzakcji;


        File savedFileMainStock = new File(filesPathToResources + "\\ndohlcv.txt");
        File savedFileNewConnect = new File(filesPathToResources + "\\NCndohlcv.txt");
        File savedFileWithTransactions = new File(filesPathToResources + "\\PROD._MAKL_.csv");

        File downloadedFileNewConnect = new File(urlNC.toString()); // New Connect


        downloadNewFile(urlNC, savedFileNewConnect);
        downloadNewFile(urlMS, savedFileMainStock);


        //wirdMethod(savedFileMainStock, downloadedFileNewConnect);

        Map<String, Double> actualStockPrize = addPresentStockPrizeToTheTable(savedFileMainStock, savedFileNewConnect);


        // wirdMethod2(savedFileNewConnect, "New COnnect: \t");
//        actualStockPrize.putAll(addPresentStockPrizeToTheTable(savedFileNewConnect));
        // przeczytaj plik z historii transakcji z pliku csv do tablicy

        ArrayList<String[]> transactionTable = readTransactionTableFromCSVFile(savedFileWithTransactions);//new


        Map<String, ArrayList<Transaction>> mapOfTransactions = createAMapOfTransactions(transactionTable);//new
        ArrayList<StockNew> stockList = createStockList(actualStockPrize, mapOfTransactions);

        sortTransactionsAsceding(stockList);
        fillInTotalVolumeAfterTransaction(stockList);
        calculateSplit(stockList, "DREWEX", 10, new LocalDate(2015, 5, 10));
        calculateSplit(stockList, "HERKULES", 5, new LocalDate(2012, 9, 19));
        calculateSplit(stockList, "CIGAMES", 0.1, new LocalDate(2017, 1, 31));
        calculateSplit(stockList, "RESBUD", 5, new LocalDate(2017, 1, 13));
        calculateSplit(stockList, "01CYBATON", 0.05, new LocalDate(2015, 11, 25));

        calculateStocks(stockList);

        stockList.sort((StockNew s1, StockNew s2) -> s1.getActualVolume().compareTo(s2.getActualVolume()));
        System.out.println(stockList);

        for (ArrayList<Transaction> transactionsList : mapOfTransactions.values()) {
            transactionsList.sort((Transaction t1, Transaction t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()));
        }//new  czy potrzebne?

///old
        tablicaHistoriaTranzakcji = wczytajHistorieTranzakcjiZPlikuCSVDoTablicy(savedFileWithTransactions);
        //ready
        //dopisac w miare potrzeb nowe akcje ktore trzeba zmienic
        poprawNazyAkcji(tablicaHistoriaTranzakcji);//zrobione i nie potrzebne
        posortujHistorieTransakcjiRosnacoWzgledemCzasu(tablicaHistoriaTranzakcji);
        ////old


        // utworz pelna strukture danych
        tablicaPortfel = uzupelnijPortfet(tablicaHistoriaTranzakcji, actualStockPrize);
        // posortowac przed wyswietleniem; te co nie maja akcji na poczatek ! :)

        ArrayList<String> kluczeIlosciowo = sortujIlosciowo(tablicaPortfel);
        ArrayList<String> kluczeAlfabetycznie = sortujAlfabetycznie(tablicaPortfel);
        ArrayList<String> kluczePoZysku = sortujPoZysku(tablicaPortfel);
        ArrayList<String> kluczeHybrydaPoZyskuCalkowitym = sortujHybrydaPoZyskuCalkowitym(tablicaPortfel);
        ArrayList<String> kluczeHybrydaPoZyskuChwilowym = sortujHybrydaPoZyskuChwilowym(tablicaPortfel);

        // wyswietl(tablicaPortfel, kluczeAlfabetycznie);
        // wyswietl(tablicaPortfel, kluczeIlosciowo);
        // wyswietl(tablicaPortfel, kluczeAlfabetycznie);
        // wyswietl(tablicaPortfel, kluczePoZysku);
        // wyswietl(tablicaPortfel, kluczeHybrydaPoZyskuCalkowitym);

        wyswietl(tablicaPortfel, kluczeHybrydaPoZyskuChwilowym);

        int zysk = 0;

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

    private void fillInTotalVolumeAfterTransaction(ArrayList<StockNew> stockList) {
        for (StockNew stock : stockList) {
            ArrayList<Transaction> transactionsList = stock.getTransactionsList();

            for (int i = 0; i < transactionsList.size(); i++) {
                Transaction transaction = transactionsList.get(i);
                if (i > 0) {
                    if (transaction.getTransaction().equals(TRANSACTION_TYPE.KUPNO)) {
                        transaction.setVolumeAfterTransaction(transactionsList.get(i - 1).getVolumeAfterTransaction() + transaction.getVolume());
                    } else {
                        transaction.setVolumeAfterTransaction(transactionsList.get(i - 1).getVolumeAfterTransaction() - transaction.getVolume());
                    }
                } else {
                    transaction.setVolumeAfterTransaction(transaction.getVolume());
                }


            }
        }
    }

    private void sortTransactionsAsceding(ArrayList<StockNew> stockList) {
        for (StockNew stock : stockList) {
            //posortuj tranzakcje po dacie
            stock.getTransactionsList().sort(Comparator.comparing(Transaction::getTransactionDate));
        }
    }

    private void calculateSplit(ArrayList<StockNew> stockList, String splittedStockName, double splitValue, LocalDate splitDate) {
        for (StockNew stock : stockList) {
            if (stock.getStockName().equals(splittedStockName)) {
                ArrayList<Transaction> transactionsList = stock.getTransactionsList();
                boolean splitFalg = false;
                for (int i = 0; i < transactionsList.size(); i++) {
                    //if before split i had some stock

                    Transaction transaction = transactionsList.get(i);

                    if ((transaction.getTransactionDate().isAfter(splitDate))) {
                        Transaction previousTransaction = transactionsList.get(i - 1);
                        if (splitFalg == false) {

                            previousTransaction.setVolumeAfterTransaction((int) (previousTransaction.getVolumeAfterTransaction() / splitValue));
                            if (transaction.getTransaction().equals(TRANSACTION_TYPE.KUPNO)) {
                                transaction.setVolumeAfterTransaction(previousTransaction.getVolumeAfterTransaction() + transaction.getVolume());
                            } else {
                                transaction.setVolumeAfterTransaction(previousTransaction.getVolumeAfterTransaction() - transaction.getVolume());
                            }
                            splitFalg = true;
                        } else // przeliczyc pozostale VolumeAfterTransaction TODO przeniesc to do orginalnej metody
                        {
                            if (transaction.getTransaction().equals(TRANSACTION_TYPE.KUPNO)) {
                                transaction.setVolumeAfterTransaction(previousTransaction.getVolumeAfterTransaction() + transaction.getVolume());
                            } else {
                                transaction.setVolumeAfterTransaction(previousTransaction.getVolumeAfterTransaction() - transaction.getVolume());
                            }
                        }
                    }
                }
            }
        }

    }

    private void calculateStocks(ArrayList<StockNew> stockList) {
        Double totalResultIfSoldToday = 0.0;
        for (StockNew stock : stockList) {
            Double historicalBuyValue = 0.0;
            Double historicalSoldValue = 0.0;

            int buyVolume = 0;
            int sellVolume = 0;


            for (Transaction transaction : stock.getTransactionsList()) {
                int volume = transaction.getVolume();
                double value = transaction.getValue();

                if (transaction.getTransaction().equals(TRANSACTION_TYPE.KUPNO)) {
                    historicalBuyValue += value;
                    buyVolume += volume;
                } else {
                    historicalSoldValue += value;
                    sellVolume += volume;
                }
            }
            int actualVolume = buyVolume - sellVolume;
            stock.setActualVolume(actualVolume);
            stock.setActualValue(actualVolume * stock.getActualPrize());
            stock.setHistoricalBuyValue(historicalBuyValue);
            stock.setHistoricalSoldValue(historicalSoldValue);
            stock.setTotalCashIfSellToday();
            totalResultIfSoldToday += stock.getTotalCashIfSellToday();
        }

        System.out.println("Total since the beginign: " + totalResultIfSoldToday);
    }


    private Map<String, ArrayList<Transaction>> createAMapOfTransactions(ArrayList<String[]> transactionTable) {

        Map<String, ArrayList<Transaction>> aNewHistoryTransactionTable = new HashMap<>();
        ArrayList<Transaction> aNewTransactionTable = new ArrayList<>();
        String stockName;
        int volume = 0;
        double prize = 0;
        String kindOfTransaction = null;
        String[] transactionDate = null;


        for (String[] line : transactionTable) {
            Transaction aNewTransaction = new Transaction(line);

            aNewTransactionTable.add(aNewTransaction);
            stockName = line[1];
            stockName = correctStockNameIfHasChanged(stockName);
            if (aNewHistoryTransactionTable.containsKey(stockName)) {
                aNewHistoryTransactionTable.get(stockName).add(aNewTransaction);

            } else {
                aNewHistoryTransactionTable.put(stockName, new ArrayList() {{
                    add(aNewTransaction);
                }});
            }// this is the new history table with stock name, and it's all transaction in a table
/*

            transactionDate = line[0].split("-");
            stockName = line[1];
            kindOfTransaction = line[2];
            volume = Integer.parseInt(line[3]);
            prize = Double.parseDouble(line[4]);// kurs

            if (kindOfTransaction.equals("KUPNO")) {
            } else {
                kindOfTransaction = "SPRZEDAZ";
            }
*/
        }
        return aNewHistoryTransactionTable;


    }

    private ArrayList<StockNew> createStockList(Map<String, Double> actualStockPrize, Map<String, ArrayList<Transaction>> mapOfTransactions) {

        ArrayList<StockNew> resultTable = new ArrayList<>();

        for (String stockName : mapOfTransactions.keySet()) {
            resultTable.add(new StockNew(stockName, actualStockPrize.get(stockName), mapOfTransactions.get(stockName)));
        }
        return resultTable;
    }

    private void downloadNewFile(URL url, File file) throws IOException {
        file.createNewFile();
        FileUtils.copyURLToFile(url, file);
    }

    private void wirdMethod2(File savedFileNewConnect, String s) {
        String dzienTygEdycjiPliku2 = zwrocMiDzienTygZLonga(savedFileNewConnect.lastModified());
        System.out.println(s + dzienTygEdycjiPliku2);        // PN, WT, SR CZW, PT - stworzyc liste/ enum i jesli mam z dnia poprzedniego to znaczy ze nie trzeba aktualizoawxc			//	System.out.println(dataModyfikacjiPliku2);			//metodkaKtoraMiZapiszeNajnowszyPlikNaDysk(urlNC, file);				// New Connect
    }

    private void wirdMethod(File savedFileMainStock, File downloadedFileNewConnect) {
        wirdMethod2(downloadedFileNewConnect, "plik na serwerze jest z : ");
        wirdMethod2(savedFileMainStock, "Rynek gl:\t");
        String dzienTygTeraz = zwrocMiDzienTygZLonga(new Date().getTime());
        System.out.println("Teraz:   \t" + dzienTygTeraz);        /* Date dataTeraz = new Date(); String dzienTygTeraz = new SimpleDateFormat("EEE").format(dataTeraz);// dzien zmieny		 * pliku1 System.out.println(dataTeraz); */        //System.out.println(dzienTygEdycjiPliku1);		// wystarczy raz dziennie uaktualnic //TODO jak to zautomatyzowac by nie		// trzebabylo komentowac tego
        // metodkaKtoraMiZapiszeNajnowszyPlikNaDysk(urlOfMainStockFile, file);
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

    private static ArrayList<String> sortujHybrydaPoZyskuChwilowym(
            Map<String, PapierGlowna> tablicaPortfel) {

        CompareByQuantity compareByQuantity = new CompareByQuantity();
        Map<String, Integer> mapaIlosci = new HashMap<>();

        ArrayList<String> kluczePoZyskuIloscRownaZero = sortujPoZysku(tablicaPortfel);
        ArrayList<String> kluczePoZyskuChwilowymIloscNieZero = sortujPoZyskuChwilowym(tablicaPortfel);// po Zysku Chwilowym -dopisac
        ArrayList<String> tabelaIloscRownaZero = new ArrayList<String>();
        //	ArrayList<String> tabelaIloscNieZero = new ArrayList<String>();
        ArrayList<String> posortowanePoZyskuIloscRownaZero = new ArrayList<String>();
        ArrayList<String> posortowanePoZyskuIloscNieZero = new ArrayList<String>();
        double zysk = 0;

        // Mapuje <key, IleMamTeraz>

        for (
                String key : tablicaPortfel.keySet()
                ) {
            mapaIlosci.put(key, tablicaPortfel.get(key).getIleMamTeraz());
        }

        // tworzy z tego liste
        List<Map.Entry<String, Integer>> listaPosortowanychIlosci = new ArrayList<Map.Entry<String, Integer>>(
                mapaIlosci.entrySet());
        // sortuje ja po ilosci
        Collections.sort(listaPosortowanychIlosci, compareByQuantity);
        // zapisuje

        for (
                Map.Entry<String, Integer> el : listaPosortowanychIlosci
                ) {
            if (el.getValue().equals(0)) {
                tabelaIloscRownaZero.add(el.getKey());
            }

        }

        // tu trzeba dodac dodawanie elementow do tablicy tabeleIlosciNieZero z
        // tablicy
        for (
                String key : kluczePoZyskuChwilowymIloscNieZero
                ) {
            posortowanePoZyskuIloscNieZero.add(key);
        }
        // trzeba posorwtowac te dwie tabele po zysku i zmergowac

        for (
                String key : kluczePoZyskuIloscRownaZero
                ) {
            if (tabelaIloscRownaZero.contains(key)) {
                posortowanePoZyskuIloscRownaZero.add(key);
            }

        }

        ArrayList<String> newList = new ArrayList<String>(posortowanePoZyskuIloscRownaZero);
        newList.addAll(posortowanePoZyskuIloscNieZero);

        // policz zysk/ strate tych co nie mam
        for (
                String key : posortowanePoZyskuIloscRownaZero
                ) {
            zysk += tablicaPortfel.get(key).getTotal();
        }
        System.out.println(w(zysk));
        return newList;

    }

    private static ArrayList<String> sortujHybrydaPoZyskuCalkowitym(
            Map<String, PapierGlowna> tablicaPortfel) {

        CompareByQuantity compareByQuantity = new CompareByQuantity();

        Map<String, Integer> mapaIlosci = new HashMap<>();
        ArrayList<String> kluczePoZyskuIloscRownaZero = sortujPoZysku(tablicaPortfel);
        ArrayList<String> kluczePoZyskuIloscNieZero = sortujPoZysku(tablicaPortfel);

        ArrayList<String> tabelaIloscRownaZero = new ArrayList<String>();
        ArrayList<String> tabelaIloscNieZero = new ArrayList<String>();

        ArrayList<String> posortowanePoZyskuIloscRownaZero = new ArrayList<String>();
        ArrayList<String> posortowanePoZyskuIloscNieZero = new ArrayList<String>();
        double zysk = 0; // moze byc uzyte ale juz liczy w innej metodzie

        // Mapuje <key, IleMamTeraz>
        for (
                String key : tablicaPortfel.keySet()
                ) {
            mapaIlosci.put(key, tablicaPortfel.get(key).getIleMamTeraz());
        }

        // tworzy z tego liste
        List<Map.Entry<String, Integer>> listaPosortowanychIlosci = new ArrayList<Map.Entry<String, Integer>>(
                mapaIlosci.entrySet());
        // sortuje ja po ilosci
        Collections.sort(listaPosortowanychIlosci, compareByQuantity);
        // zapisuje
        for (
                Map.Entry<String, Integer> el : listaPosortowanychIlosci
                ) {
            if (el.getValue().equals(0)) {

                tabelaIloscRownaZero.add(el.getKey());
            } else {
                tabelaIloscNieZero.add(el.getKey());
            }
            // trzeba posorwtowac te dwie tabele po zysku i zmergowac
        }

        for (
                String key : kluczePoZyskuIloscNieZero
                ) {
            if (tabelaIloscNieZero.contains(key)) {
                posortowanePoZyskuIloscNieZero.add(key);
            }

        }

        for (
                String key : kluczePoZyskuIloscRownaZero
                ) {
            if (tabelaIloscRownaZero.contains(key)) {
                posortowanePoZyskuIloscRownaZero.add(key);
            }

        }

        // posortowac po zysku/stracie
        ArrayList<String> newList = new ArrayList<String>(posortowanePoZyskuIloscRownaZero);
        newList.addAll(posortowanePoZyskuIloscNieZero);
        // Collections.sort(tabelaPosortowana,compareBy);

        // policz zysk/ strate tych co nie mam
        for (
                String key : posortowanePoZyskuIloscRownaZero
                ) {
            zysk += tablicaPortfel.get(key).getTotal();
        }
        // System.out.println(w(zysk));
        return newList;

    }

    private static ArrayList<String> sortujPoZysku(Map<String, PapierGlowna> tablicaPortfel) {

        CompareByQuantity compareByQuantity = new CompareByQuantity();

        Map<String, Integer> mapaIlosci = new HashMap<>();

        // tworzy HashMape <klucz, total>
        for (
                String key : tablicaPortfel.keySet()
                ) {
            mapaIlosci.put(key, (int) tablicaPortfel.get(key).getTotal());
        }

        List<Map.Entry<String, Integer>> listaPosortowanychIlosci = new ArrayList<Map.Entry<String, Integer>>(
                mapaIlosci.entrySet());

        Collections.sort(listaPosortowanychIlosci, compareByQuantity);
        ArrayList<String> tabelaPosortowana = new ArrayList<String>();

        for (
                Map.Entry<String, Integer> el : listaPosortowanychIlosci
                ) {
            tabelaPosortowana.add(el.getKey());

        }

        return tabelaPosortowana;
    }

    private static ArrayList<String> sortujPoZyskuChwilowym(Map<String, PapierGlowna> tablicaPortfel) {

        CompareByQuantityOnAHand compareByQuantityOnAHand = new CompareByQuantityOnAHand();

        Map<String, Integer> mapaIlosci = new HashMap<>();

        // tworzy HashMape <klucz, total>
        for (
                String key : tablicaPortfel.keySet()
                ) {
            /// 	regolka  na zysk  chwilowy
            // ileMamTeraz * (poIleJest - sredniaCenaKupna)
            double zyskStrataChwilowa = tablicaPortfel.get(key).getIleMamTeraz()
                    * (tablicaPortfel.get(key).getPoIleJest()
                    - tablicaPortfel.get(key).getSredniaCenaZakupuTegoCoZostalo());
            if (zyskStrataChwilowa != 0) {
                mapaIlosci.put(key, (int) zyskStrataChwilowa);

            }
        }

        List<Map.Entry<String, Integer>> listaPosortowanychPoZyskuChwilowym = new ArrayList<Map.Entry<String, Integer>>(
                mapaIlosci.entrySet());

        Collections.sort(listaPosortowanychPoZyskuChwilowym, compareByQuantityOnAHand);
        ArrayList<String> tabelaPosortowana = new ArrayList<String>();

        for (
                Map.Entry<String, Integer> el : listaPosortowanychPoZyskuChwilowym
                ) {
            tabelaPosortowana.add(el.getKey());

        }

        return tabelaPosortowana;
    }

    private static ArrayList<String> sortujIlosciowo(Map<String, PapierGlowna> tablicaPortfel) {

        CompareByQuantity compareByQuantity = new CompareByQuantity();

        Map<String, Integer> mapaIlosci = new HashMap<>();

        for (
                String key : tablicaPortfel.keySet()
                ) {
            mapaIlosci.put(key, tablicaPortfel.get(key).getIleMamTeraz());
        }

        List<Map.Entry<String, Integer>> listaPosortowanychIlosci = new ArrayList<Map.Entry<String, Integer>>(
                mapaIlosci.entrySet());

        Collections.sort(listaPosortowanychIlosci, compareByQuantity);
        ArrayList<String> tabelaPosortowana = new ArrayList<String>();

        for (
                Map.Entry<String, Integer> el : listaPosortowanychIlosci
                ) {
            tabelaPosortowana.add(el.getKey());

        }

        return tabelaPosortowana;
    }

    private static ArrayList<String> sortujAlfabetycznie(Map<String, PapierGlowna> tablicaPortfel) {

        ArrayList<String> kluczeAlfabetycznie = new ArrayList<String>(tablicaPortfel.keySet());
        Collections.sort(kluczeAlfabetycznie);
        return kluczeAlfabetycznie;
    }

    private static Map<String, ArrayList<Stock>> posortujHistorieTransakcjiRosnacoWzgledemCzasu(
            Map<String, ArrayList<Stock>> tablicaHistoriaTranzakcji) {
        CompareByDate compareByDate = new CompareByDate();

        // TODO posortowac wzg czasu! potrzebne do liczenia sredniej

        for (
                String key : tablicaHistoriaTranzakcji.keySet()
                ) {
            tablicaHistoriaTranzakcji.get(key).sort(compareByDate);
        }

        return tablicaHistoriaTranzakcji;
    }


    private static double obliczZIleKupileDotychczas(ArrayList<Stock> tablicat) {
        double ZyskLubStrata = 0;// TODO Auto-generated method stub
//	int ileMam = 0;

        for (
                Stock p : tablicat
                ) {
            String co = p.getKupnoCzySprzedaz();
            if (co.equals("KUPNO")) {
                ZyskLubStrata -= p.getIle() * p.getPoIle();
            } // kupuje czyli jestem na minusie

            else {
                // ZyskLubStrata += p.getIle() * p.getPoIle();
            } // sprzedaz
        }
        // TODO w ramach refactoringo zzastanowic sie czy nie da sie ich zrobic
        // w jednej metodzie obliczzysklubstrate i przekalkulujilemam

        return Math.abs(ZyskLubStrata);
    }

    // static potrzzebnmy?
    private static void wyswietl(Map<String, PapierGlowna> tablicaPortfelProper,
                                 ArrayList<String> kluczePosortowane) {
        double totalnyZyskStrata = 0;
        double sredniaCenaKupna = 0;
        double cenaObecna = 0;
        int ileMamTeraz = 0;
        double zyskLubStrata = 0;
        double totalNaPapier = 0;

        double zyskStrataNaRece = 0;
        double zaIleSprzedalem;
        double ileWydalemNaZakup = 0;
        double totalNaRece = 0;
        // pokazuje papier czy mam na rece i ile  zysk/ strata do tej pory
        // wartosc reki po cenie kupna zysk strata obenca na rece
        // totalny zysk / strata (z uwzglednieniem sprzdazy teraz) po cenie jaka
        // jest
        // TODO w ramach refakroeingu napisac metode do wypisywania jako
        // paramery bierze to co chcemy wyswietlic ?
        boolean naglowek = true;

        for (
                String key : kluczePosortowane
                ) {
            PapierGlowna p = new PapierGlowna(null, 0, 0, 0, 0, 0, 0, 0, 0);

            //	  PapierGlowna t = new PapierGlowna(null, totalNaRece, ileMamTeraz, totalNaRece, totalNaRece, totalNaRece, totalNaRece, totalNaRece, totalNaRece);

            p = tablicaPortfelProper.get(key);
            sredniaCenaKupna = p.getSredniaCenaZakupuTegoCoZostalo();
            cenaObecna = p.getPoIleJest();
            ileMamTeraz = p.getIleMamTeraz();
            zyskLubStrata = p.getZyskLubStrata();// chyba chodzi bez
            // uwzglednienia reki
            zaIleSprzedalem = p.getIleDostalemZaSprzedaz();
            totalNaPapier = p.getTotal(); // z reka i cena obecna (?)
            ileWydalemNaZakup = p.getIleWydalemNaZakup();

            if (ileMamTeraz > 0) {
                if (naglowek) {


                    // kupno - srednia cena kupna
                    // obcn - cena obecna
                    // - z_do ter. za ile sprzedalem do teraz
                    // xx - za ile kupilem do teraz
                    // ile zaplacilem za to co mam na rece
                    // za ile moge sprzedac to co na rece
                    // calosciowo plus czy minus
                    // po ile musze sprzedac by wyjsc na zero z reki
                    // po ile musze sprzedac by wyjsc na zero calkiem


                    /* 1 */    // nazwa
                    /* 2 */  // ile mam teraz
                    /* 3 */  // srednia cena zakupu tegi ci na rece
                    /* 4 */    // cena obecna
                    // TODO dodac roznice procentowa miedczy obecna a zero i dac znac w przypadlu wiecej niz 10%!
                    /* 5 */    // procentowa roznica od sredniej od obecnej
                    /* 6 */    // po ile sprzedac reke by wyjsc na zero : total na papier / liczbe papieru na rece
                    /* 7 */    // ile wydalem do teraz
                    /* 8 */    // za ile sprzedalem do teraz
                    /* 9 */    // ile wydalem to co mam na rece [na rece pozostaje]]
                    /* 10 */  // za ile moge sprzedac to co mam na rece
                    /* 11 */  // ile bede do przodu/ tylu na rece?
                    //procentowo do reki jaka czesc TODO
                    /* 12 */  // po sprzedazy po cenie obecnej z uwzglednieniem (historii) dotychczasowego zysku
                    /* 13 */  // po ile sprzedac reke by wyjsc na zero absolutne
                    /* 14 */  // koszt reki wg. sredniej wyliczony z // srednia cena kupna ktora trzeba zmienic orecyzje do .2*/
                    /* 15 */  // srednia cena do ceny obecnej razy ilosc - jak wychodze na danej transakcji - PAPIERZE*/
                    /* 16 */  //" TOTAL na Stock");// zarobek strata na papierze wg srednie ceny...*/

                    //@formatter:off
                    /* 1 */
                    System.out.printf("%10s", "Nazwa:");        // nazwa
                    /* 2 */
                    System.out.printf("%8s", "Ile:");            // ile mam teraz
                    /* 3 */
                    System.out.printf("%14s", " [~kupno /");        // srednia cena zakupu tegi ci na rece
                    /* 4 */
                    System.out.printf("%9s", " obcn /");            // cena obecna
                    /* 5 */
                    System.out.printf("%9s", "   /");            // procentowa roznica od sredniej od obecnej
                    /* 6 */
                    System.out.printf("%8s", "Zero]");    // po ile sprzedac reke by wyjsc na zero : total na papier / liczbe papieru na rece
                    /* 7 */
                    System.out.printf("%10s", "KUPILEM");            // ile wydalem do teraz
                    /* 8 */
                    System.out.printf("%13s", "SPRZEDALEM");            // za ile sprzedalem do teraz
                    /* 9 */
                    System.out.printf("%11s", "Na Rece");        // ile wydalem to co mam na rece [na rece pozostaje]]
                    /* 10 */
                    System.out.printf("%12s", "RekaSPRZED");    // za ile moge sprzedac to co mam na rece
                    /* 11 */
                    System.out.printf("%16s", "strataZyskReki"); // ile bede do przodu/ tylu na rece?
                    /* 12 */
                    System.out.printf("%14s", "zysk_Z_Hist");    // po sprzedazy po cenie obecnej z uwzglednieniem (historii) dotychczasowego zysku
                    /* 13 */
                    System.out.printf("%11s", "Sr_Cena");// po ile sprzedac reke by wyjsc na zero absolutne
                    /* 14 */
                    System.out.printf("%15s", "Reka_wg_sr");// koszt reki wg. sredniej wyliczony z // srednia cena kupna ktora trzeba zmienic orecyzje do .2*/
                    /* 15 */
                    System.out.printf("%16s", "Strata Reki");// srednia cena do ceny obecnej razy ilosc - jak wychodze na danej transakcji - PAPIERZE*/
                    /* 16 */
                    System.out.printf("%14s", "totalPapier");//" TOTAL na Stock");// zarobek strata na papierze wg srednie ceny...*/
                    //na zero przeniesc do kupno obcn - i na zero!?
                    //@formatter:off
                    System.out.println();
                    naglowek = false;

                }
            }

            // w ramach refaktoringu zrobic metode ktora bedzie wyswietlac
            // wartosc pola i dodawac tab o ile taka potrzeba

            /* 1 "Nazwa: \t\t " nazwa	*/
            System.out.printf("%10s", key);
            if (ileMamTeraz > 0) {

                /* 2  Ile:\t"    ile mam teraz	*/
                System.out.printf("%10s", "(" + ileMamTeraz + ")");

                /* 3 "[~kupno/ ");  		// srednia cena zakupu tegi ci na rece */
                System.out.printf("%12s", "[" + w(sredniaCenaKupna) + "   /");

                /* 4 cena obecna 	*/
                System.out.printf("%9s", w(cenaObecna) + " /");

                /* 5 "[%] /");			// procentowa roznica od sredniej od obecnej  */
                int procent = (int) (cenaObecna * 100 / sredniaCenaKupna);
                if ((procent < 100) && (procent > 0)) {
                    procent = (100 - procent) * (-1);
                } else if (procent > 100) {
                    procent = procent - 100;
                } else {
                    procent = 0;
                    //tu troche dziwnie bo i dla 100 i dla 0 jest rowne 0 !!!!!
                    //TODO poprawic

                }
                System.out.printf("%8s", procent + " [%]");

                /* 6 " [naZeroHist]\t ");	// po ile sprzedac reke by wyjsc na zero	 */
                System.out.printf("%9s", w((ileWydalemNaZakup - zaIleSprzedalem) / ileMamTeraz) + " ]");

                /* 7 "KUPILEM ");			// ile wydalem do teraz */
                System.out.printf("%12s", w(ileWydalemNaZakup) + "  ");

                /* 8 "SPRZEDALEM");		 // za ile sprzedalem do teraz */
                if (zaIleSprzedalem > 0) {
                    System.out.printf("%11s", w(zaIleSprzedalem));
                } else {
                    System.out.printf("%11s", "- - -");
                }

                /* 9 "+ Na Rece\t" ile wydalem to ca mam na rece */
                totalNaRece += ileWydalemNaZakup - zaIleSprzedalem;
                System.out.printf("%11s", w(ileWydalemNaZakup - zaIleSprzedalem));

                /* 10 ("RekaSPRZED\t"); 	// za ile moge sprzedac to co mam na rece */
                System.out.printf("%12s", w(cenaObecna * ileMamTeraz));


                //		System.out.print("   *   "+w(sredniaCenaKupna * ileMamTeraz)+"   *   ");  ********************************************************** co to jest? za wartosc

                /*11 "strataZyskReki\t"ile bede do przodu/ tylu na rece?*/
                /* //JAZDA!  //zmergowane do/ z  14 " Strata Reki");// srednia cena do ceny obecnej razy ilosc - jak wychodze na danej transakcji - PAPIERZE*/
                System.out.printf("%15s", "[" + w(ileMamTeraz * (cenaObecna - sredniaCenaKupna)) + " ]");
                // System.out.printf("%16s",w((cenaObecna*ileMamTeraz)-(ileWydalemNaZakup-zaIleSprzedalem)) );  ????

                /* 12   ("zysk_ZHistoria\t" 	// po sprzedazy po cenie obecnej z uwzglednieniem (historii) dotychczasowego zysku */
                System.out.printf("%14s", w(zaIleSprzedalem + cenaObecna * ileMamTeraz - ileWydalemNaZakup));
//		 System.out.print(w((zaIleSprzedalem+ileWydalemNaZakup)/ileMamTeraz)+"\t\t\t");

                /* 13   ("bez straty calkowitej");// po ile sprzedac reke by wyjsc na zero absolutne*/
                System.out.printf("%11s", w(sredniaCenaKupna));
                // System.out.print(w(zyskLubStrata));
                //System.out.print(" *** ");
                /* if (zyskLubStrata >= 0) { System.out.print(" + "); } System.out.print(w(zyskLubStrata)); */


                /* 14  " Reka_wg_sr");// koszt reki wg. sredniej wyliczony z // srednia cena kupna ktora trzeba zmienic orecyzje do .2  */
                // koszt reki
		/*if (sredniaCenaKupna * ileMamTeraz > 0) {
		  System.out.print(" + ");
		}*/
                System.out.printf("%15s", w(sredniaCenaKupna * ileMamTeraz));
                System.out.print(" ");


                /*15  " Strata Reki");// srednia cena do ceny obecnej razy ilosc - jak wychodze na danej transakcji - PAPIERZE*/
                // zysk lub strata na rece TO CO JEST NAPRAWDE WAZNE
/*
		if (cenaObecna > sredniaCenaKupna) {
		  System.out.print("+ ");
		}
*/        //else minus - ?
                //!!!!!!
                // mozna po tym posortowac w sumie tez szacunkowa strata tego co na rece
                //szacunkowa bo cena srednia jest za bardzo dokladna
                // TODO zmienic dla dwoch miejsc po przecinku ~zaokraglic? ! // i dodac jak ma byc cena by z reki wyjsc na zero
                zyskStrataNaRece += ileMamTeraz * (cenaObecna - sredniaCenaKupna);
                System.out.printf("%15s", "[" + w(ileMamTeraz * (cenaObecna - sredniaCenaKupna)) + " ]");
            }

            /* 16 " TOTAL na Stock");// zarobek strata na papierze wg srednie ceny...*/
            System.out.printf("%14s", " = " + w(totalNaPapier));


            //poprawic wyzej i to wywalic
            if (ileMamTeraz > 0) {
                // trzeba poprawic liczenie tego
                // sp[rawdzic czy sie powtarza i wywalic jesli tak jest

                double poIleSprzedac = 0;
                poIleSprzedac = (ileMamTeraz * (cenaObecna - sredniaCenaKupna)) / ileMamTeraz;
                // po ile sprzedac by wyjsc na zero z reki (bez brania pod uwage
                // przeszlosci: zykow czy straty
                System.out.print("\t[: " + w(Math.abs(poIleSprzedac)) + " ] ");
                // po ile sprzedac by wyjsc na zero lacznie z zyskiem strata
                // calkowita
                System.out.print("\t[: " + w(Math.abs(totalNaPapier / ileMamTeraz)) + " ] ");
            }

            System.out.println();
            totalnyZyskStrata += totalNaPapier;

        }

        //TODO
        //zrobic wykres zysk od poczatku w funkcji czasie
        //zrobic zykres popularnosci danych spolek

        System.out.println("Zainwestowane pieniadze w rece:" + "[ " + w(totalNaRece) + " ]");
        System.out.println("Obecnie na rece:" + "[ " + w(zyskStrataNaRece) + " ]");
        System.out.println("Zysk/ strata reki:" + w(zyskStrataNaRece * 100 / totalNaRece) + " %");

        System.out.println("Co daje w sumie:" + "[ " + w(totalNaRece + zyskStrataNaRece) + " ]");
        System.out.println("Zysk od początku  = " + w(totalnyZyskStrata));
    }


    private static String w(double zmienna) {
        return String.format("%.2f", zmienna);
    }


    private static Map<String, PapierGlowna> uzupelnijPortfet(
            Map<String, ArrayList<Stock>> tablicaHistoriaTranzakcji, Map<String, Double> tablicaCen) throws ParseException {

        Map<String, PapierGlowna> t = new HashMap<String, PapierGlowna>();
        double poIleJest = 0;
        int ileMamTeraz;
        double naReceWPLN;
        double zyskLubStrata;
        double wartoscDotychczasowejSprzedazy;
        double total;
        double sredniaCenaKupnaTegoCoZostaloNaRece = 0;
        double ileWydalemNaZakup = 0;

        // 1. dodaj cene aktualna lub 0 jesli nie ma takiej
        for (
                String key : tablicaHistoriaTranzakcji.keySet()
                ) {
            ArrayList<Stock> tablicat = tablicaHistoriaTranzakcji.get(key);
            if (tablicaCen.containsKey(key)) {
                poIleJest = tablicaCen.get(key);
            } else {
                poIleJest = 0;
                // jesli nie ma ceny - tez trzeba dodac taki element w
                // ustalam cene na 0 (to moze je wyroznic)
                // System.out.println(key + " nie ma ceny. [bankrut?]");
            }

            ileMamTeraz = przekalkulujIleMam(tablicat);

            // 2.
            // policz srednia cene zakupu pozostalych na reku akcji
            // inaczej mowiac jest to cena po ktore trzebaby sprzedac by wyjsc na zero!

            if (ileMamTeraz != 0) {
                sredniaCenaKupnaTegoCoZostaloNaRece = policzSredniaCeneZakupuTegoCoMamNaRece(tablicat);
            }
            naReceWPLN = poIleJest * ileMamTeraz;
            zyskLubStrata = obliczZyskLubStrateZDotychczasowejSprzedazy(tablicat);
            wartoscDotychczasowejSprzedazy = obliczZyskZDotychczasowejSprzedazy(tablicat);
            ileWydalemNaZakup = obliczZIleKupileDotychczas(tablicat);

            total = zyskLubStrata + naReceWPLN; // zysk strata dotychczasowa z uwzglednieniem reki i ceny obecnej
            // TODO sprawdzic i porownac z wywwitelanym moze nie trzeba tam tak ciezko tego liczyc
            // sprawdzic i

            if (key.equals("DREWEX") || key.equals("HERKULES")) {
                // byl split wiecej nie mam na rece trzeba ustawic sztucznie
                //TODO zobaczy czy da sie cos zrobic w przypadku splitu
                // 1 do 5 czy da sie to jakos uproscic
                ileMamTeraz = 0;
                naReceWPLN = 0;
                total = zyskLubStrata;
            }

            PapierGlowna p = new PapierGlowna(tablicat, poIleJest, ileMamTeraz, naReceWPLN, zyskLubStrata,
                    total, sredniaCenaKupnaTegoCoZostaloNaRece, wartoscDotychczasowejSprzedazy, ileWydalemNaZakup);
            t.put(key, p);

        }

        return t;
    }


    private static double obliczZyskLubStrateZDotychczasowejSprzedazy(ArrayList<Stock> tablicat) {
        double ZyskLubStrata = 0;// TODO Auto-generated method stub
//	int ileMam = 0;

        for (
                Stock p : tablicat
                ) {
            String co = p.getKupnoCzySprzedaz();
            if (co.equals("KUPNO")) {
                ZyskLubStrata -= p.getIle() * p.getPoIle();
            } // kupuje czyli jestem na minusie

            else {
                ZyskLubStrata += p.getIle() * p.getPoIle();
            } // sprzedaz
        }
        // TODO w ramach refactoringo zzastanowic sie czy nie da sie ich zrobic
        // w jednej metodzie obliczzysklubstrate i przekalkulujilemam (ale chyab nie trzeba)

        return ZyskLubStrata;
    }


    private static double obliczZyskZDotychczasowejSprzedazy(ArrayList<Stock> tablicat) {

        double ZyskLubStrata = 0;

        for (
                Stock p : tablicat
                ) {
            String co = p.getKupnoCzySprzedaz();
            if (co.equals("KUPNO")) {
                // ZyskLubStrata -= p.getIle() * p.getPoIle();
                // TODO dlaczego wyszarzone
            } // kupuje czyli jestem na minusie

            else {
                ZyskLubStrata += p.getIle() * p.getPoIle();
            } // sprzedaz
        }
        // TODO w ramach refactoringo zzastanowic sie czy nie da sie ich zrobic
        // w jednej metodzie obliczzysklubstrate i przekalkulujilemam (chyba nie potrzeba)

        return ZyskLubStrata;
    }


    private static int przekalkulujIleMam(ArrayList<Stock> tablicat) {
        int ileMam = 0;

        for (
                Stock p : tablicat
                ) {
            String co = p.getKupnoCzySprzedaz();
            if (co.equals("KUPNO")) {
                ileMam += p.getIle();

            } else {
                ileMam -= p.getIle();
            }
        }

        return ileMam;
    }

    private static double policzSredniaCeneZakupuTegoCoMamNaRece(ArrayList<Stock> tablicat) throws ParseException {
        double sredniaCenaKupna = 0;

        ArrayList<Stock> tmp = new ArrayList<Stock>();
        tmp = sklonuj(tablicat);
        pominKupioneWLiczbieSprzedaneFIFO(tmp);
        sredniaCenaKupna = policzSredniaWazona(tmp);
        return sredniaCenaKupna;
    }

    private static ArrayList<Stock> sklonuj(ArrayList<Stock> tablicat) throws ParseException {
        ArrayList<Stock> tmp = new ArrayList<Stock>();
        // dopisujemy kopiowanie

        for (
                Stock p : tablicat
                ) {
            p.getDataWykonaniaTransackji();
            p.getIle();
            p.getKupnoCzySprzedaz();
            p.getPoIle();
            // 						kiedy, 	  int ile, double poIle, String kupnoCzySprzedaz
            tmp.add(new Stock(p.getKiedy(), p.getIle(), p.getPoIle(), p.getKupnoCzySprzedaz()));
        }

        return tmp;
    }

    private static double policzSredniaWazona(ArrayList<Stock> tab) {
        // liczy srednia wg wzoru:
        // (cena * ilosc + cena2 * ilosc2 ... cenaN * iloscN) / ilosc1 + ilosc2
        // + ... + iloscN
        int suma = 0;// ilosc
        double sumaIloczynow = 0; // cena * ilosc ....
        for (
                Stock p : tab
                ) {
            String co = p.getKupnoCzySprzedaz();
            if (co.equals("KUPNO")) {
                sumaIloczynow += p.getPoIle() * p.getIle();
                suma += p.getIle();
            } // else dla sprzedanych mozna policzyc zysk
        }

        return sumaIloczynow / suma;
    }

    private static ArrayList<Stock> pominKupioneWLiczbieSprzedaneFIFO(ArrayList<Stock> tab) {
        int ileSprzedanych = 0;

        for (
                Stock p : tab
                ) {
            String co = p.getKupnoCzySprzedaz();
            // liczy sprzedane papiery
            if (co.equals("KUPNO")) {

            } else {
                ileSprzedanych += p.getIle();
            }
        }

        // pomija pierwsze kupione papiery w liczbie sprzedanych
        // kolejka FIFO
        for (
                Stock p : tab
                ) {
            if (ileSprzedanych > 0) {
                String co = p.getKupnoCzySprzedaz();
                if (co.equals("KUPNO")) {
                    int d = p.getIle();
                    ileSprzedanych -= p.getIle();
                    p.setIle(0);
                    if (ileSprzedanych < 0) {
                        p.setIle(ileSprzedanych * (-1));
                    }
                }
            }
        }

        return tab;

    }

    private static Map<String, ArrayList<Stock>> poprawNazyAkcji(Map<String, ArrayList<Stock>> t) {
        Map<String, ArrayList<Stock>> tablicaHistoriaTranzakcjiTmp = new HashMap<String, ArrayList<Stock>>();

        if (t.containsKey("JUJUBEE-PDA")) {
            // trzba zmergowac dwa jujubee bo mialo inna nazwe!
            ArrayList<Stock> tmp = new ArrayList<Stock>();
            tmp.addAll(t.get("JUJUBEE"));
            tmp.addAll(t.get("JUJUBEE-PDA"));
            t.put("JUJUBEE", tmp);
            t.remove("JUJUBEE-PDA");
        }

        if (t.containsKey("SITE-NC")) {
            t.put("SITE", t.remove("SITE-NC"));
        }

        if (t.containsKey("01CYBATON-NC")) {
            t.put("01CYBATON", t.remove("01CYBATON-NC"));
        }

        if (t.containsKey("EKIOSK-NC")) {
            t.put("EKIOSK", t.remove("EKIOSK-NC"));
        }

        if (t.containsKey("PGSSOFT-NC")) {
            t.put("PGSSOFT", t.remove("PGSSOFT-NC"));
        }

        if (t.containsKey("HARPER1")) {
            t.put("HARPER", t.remove("HARPER1"));
        }

        if (t.containsKey("PLANETINN-NC")) {
            t.put("PLANETINN", t.remove("PLANETINN-NC"));
        }

        if (t.containsKey("MGAMES-NC")) {
            t.put("MGAMES", t.remove("MGAMES-NC"));
        }

        if (t.containsKey("VIVID-NC")) {
            t.put("VIVID", t.remove("VIVID-NC"));
        }
        if (t.containsKey("FARM51-NC")) {
            t.put("FARM51", t.remove("FARM51-NC"));
        }
        if (t.containsKey("BIOMAX-NC")) {
            t.put("BIOMAX", t.remove("BIOMAX-NC"));
        }

        return t;
    }

    // wczytuje z csv przekazuje tablice nazwa -> arrayList Papier2
    // TODO refaktor mozna podzielic na dwie czesci czytanie z pliku i
    // wpisywanie do tablicy; przeanalizowac

    private static Map<String, ArrayList<Stock>> wczytajHistorieTranzakcjiZPlikuCSVDoTablicy(File savedTransactionsFile) throws ParseException {
        Map<String, ArrayList<Stock>> tablicaHistoriaTranzakcjiTemp = new HashMap<>();
        Map<String, ArrayList<Transaction>> aNewHistoryTransactionTable = new HashMap<>();
        Scanner inputStream;
        ArrayList<Stock> tablicaTranzakcji = new ArrayList<Stock>();
        ArrayList<Transaction> aNewTransactionTable = new ArrayList<>();

        double aktualnaCena;
        String stockName;
        int volume = 0;
        double prize = 0;
        String kindOfTransaction = null;
        String[] transactionDate = null;
        String[] nextLine = null;
        ArrayList<String[]> transactionTable = new ArrayList<String[]>();
        char MY_SEPARATOR = ';';
        int SKIP_LINES_NUMBER = 30;         // [0-29] smienic
        try {
            CSVReader reader = new CSVReader(new FileReader(savedTransactionsFile), MY_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER, SKIP_LINES_NUMBER);
            try {
                while ((nextLine = reader.readNext()) != null) {
                    transactionTable.add(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String[] line : transactionTable) {
                Transaction aNewTransaction = new Transaction(line);

                aNewTransactionTable.add(new Transaction(line));
                stockName = line[1];
                stockName = correctStockNameIfHasChanged(stockName);
                if (aNewHistoryTransactionTable.containsKey(stockName)) {
                    aNewHistoryTransactionTable.get(stockName).add(aNewTransaction);
                } else {
                    aNewHistoryTransactionTable.put(stockName, new ArrayList() {{
                        add(aNewTransaction);
                    }});
                }// this is the new history table with stock name, and it's all transaction in a table

                transactionDate = line[0].split("-");
                stockName = line[1];
                kindOfTransaction = line[2];
                volume = Integer.parseInt(line[3]);
                prize = Double.parseDouble(line[4]);// kurs
                /* if (stockName.equals("DREWEX")){ System.out.println(stockName); } */
                // zamiana na polskie znaki
                if (kindOfTransaction.equals("KUPNO")) {
                } else {
                    kindOfTransaction = "SPRZEDAZ";
                }
                /* TODO poprawić czytanie polskich znakow z kindOfTransaction = "SPRZEDAZ";// pliku csv */
                //ArrayList wyciagnacz przed ifa / z ifa../..
                // sprawdz czy juz istnieje taki papier
                if (tablicaHistoriaTranzakcjiTemp.containsKey(stockName)) {
                    // jesli istnieje
                    // dodac papier do istniejacego rekordu
                    ArrayList<Stock> t = new ArrayList<Stock>();
                    t = tablicaHistoriaTranzakcjiTemp.get(stockName);
                    t.add(new Stock(transactionDate, volume, prize, kindOfTransaction));
                    tablicaHistoriaTranzakcjiTemp.put(stockName, t);
                    // System.out.println("powtorzylo sie: " + stockName);
                    // TODO kod powtarzajacy sie w obu czesciach ifa wywalic do
                    // czesci wspolnej refactoring
                } else {
                    // dodaj do tablicy jesliu nie instenie
                    ArrayList<Stock> t1 = new ArrayList<>();
                    t1.add(new Stock(transactionDate, volume, prize, kindOfTransaction));
                    tablicaHistoriaTranzakcjiTemp.put(stockName, t1);
                    // System.out.print("nowe: " + stockName);
                    // System.out.println();
                }
            }

/*            for (int i = 0; i < plikWTablicy.volume(); i++) {
                kiedy = plikWTablicy.get(i)[0].split("-");
                stockName = plikWTablicy.get(i)[1];
                kindOfTransaction = plikWTablicy.get(i)[2];
                volume = Integer.parseInt(plikWTablicy.get(i)[3]);
                prize = Double.parseDouble(plikWTablicy.get(i)[4]);// kurs
                *//* if (stockName.equals("DREWEX")){ System.out.println(stockName); } *//*
                // zamiana na polskie znaki
                if (kindOfTransaction.equals("KUPNO")) {
                } else {
                    kindOfTransaction = "SPRZEDAZ";
                }
                *//* TODO poprawić czytanie polskich znakow z kindOfTransaction = "SPRZEDAZ";// pliku csv *//*
                //ArrayList wyciagnacz przed ifa / z ifa../..
                // sprawdz czy juz istnieje taki papier
                if (tablicaHistoriaTranzakcjiTemp.containsKey(stockName)) {
                    // jesli istnieje
                    // dodac papier do istniejacego rekordu
                    ArrayList<Stock> t = new ArrayList<Stock>();
                    t = tablicaHistoriaTranzakcjiTemp.get(stockName);
                    t.add(new Stock(kiedy, volume, prize, kindOfTransaction));
                    tablicaHistoriaTranzakcjiTemp.put(stockName, t);
                    // System.out.println("powtorzylo sie: " + stockName);
                    // TODO kod powtarzajacy sie w obu czesciach ifa wywalic do
                    // czesci wspolnej refactoring
                } else {
                    // dodaj do tablicy jesliu nie instenie
                    ArrayList<Stock> t1 = new ArrayList<Stock>();
                    t1.add(new Stock(kiedy, volume, prize, kindOfTransaction));
                    tablicaHistoriaTranzakcjiTemp.put(stockName, t1);
                    // System.out.print("nowe: " + stockName);
                    // System.out.println();
                }
            }*/
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        // TODO add the record to the arrayList (dont worry about the repetition
        // as this is not a hash map)
        // the buy sell and current state/ number will be sorted later -> logic
        // will follow

        return tablicaHistoriaTranzakcjiTemp;
    }


    private static ArrayList<String[]> readTransactionTableFromCSVFile(File savedTransactionsFile) {

        char MY_SEPARATOR = ';';
        int SKIP_LINES_NUMBER = 30;         // [0-29] smienic
        ArrayList<String[]> transactionTable = new ArrayList<String[]>();
        String[] nextLine;
        //TODO try with resources
        try {
            CSVReader reader = new CSVReader(new FileReader(savedTransactionsFile), MY_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER, SKIP_LINES_NUMBER);
            try {
                while ((nextLine = reader.readNext()) != null) {
                    transactionTable.add(nextLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionTable;
    }


    private static String correctStockNameIfHasChanged(String stockName) {

        if (stockName.equals("JUJUBEE-PDA")) {
            return "JUJUBEE";
        }
        if (stockName.equals("SITE-NC")) {
            return "SITE";
        }
        if (stockName.equals("01CYBATON-NC")) {
            return "01CYBATON";
        }
        if (stockName.equals("EKIOSK-NC")) {
            return "EKIOSK";
        }
        if (stockName.equals("PGSSOFT-NC")) {
            return "PGSSOFT";
        }
        if (stockName.equals("HARPER1")) {
            return "HARPER";
        }
        if (stockName.equals("PLANETINN-NC")) {
            return "PLANETINN";
        }
        if (stockName.equals("MGAMES-NC")) {
            return "MGAMES";
        }
        if (stockName.equals("VIVID-NC")) {
            return "VIVID";
        }
        if (stockName.equals("FARM51-NC")) {
            return "FARM51";
        }
        if (stockName.equals("BIOMAX-NC")) {
            return "BIOMAX";
        }
        return stockName;
    }

    private static Map<String, Double> addPresentStockPrizeToTheTable(File NCfile, File MSfile) {
        Map<String, Double> tablicaCen = new HashMap<String, Double>();

        Scanner inputStreamNC = null, inputStreamMS = null;

        double aktualnaCena;
        String nazwaAkcji;

        try {
            inputStreamMS = new Scanner(MSfile);

            while (inputStreamMS.hasNext()) {
                String data = inputStreamMS.next();
                String[] values = data.split(",");
                nazwaAkcji = values[0];
                aktualnaCena = Double.parseDouble(values[5]);
                tablicaCen.put(nazwaAkcji, aktualnaCena);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            inputStreamMS.close();
        }

        try {
            inputStreamNC = new Scanner(NCfile);

            while (inputStreamNC.hasNext()) {
                String data = inputStreamNC.next();
                String[] values = data.split(",");
                nazwaAkcji = values[0];
                aktualnaCena = Double.parseDouble(values[5]);
                tablicaCen.put(nazwaAkcji, aktualnaCena);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // to ma byc?? ??TODO o co chodzi!?
        finally {
            inputStreamNC.close();
        }
        return tablicaCen;
    }
}
