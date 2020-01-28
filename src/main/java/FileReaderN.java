import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FileUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReaderN {




    public ArrayList<Transaction> addTransactionsFromCSVFile(File savedTransactionsFile) {

        final char MY_SEPARATOR = ';';
        final int SKIP_LINES_NUMBER = 35;         // [0-35] smieci z pliku csv
        ArrayList<Transaction> transactionTable = new ArrayList<>();
        String[] lineFromFile;
        CSVParser parser = new CSVParserBuilder().withSeparator(MY_SEPARATOR).build();
        try (
                java.io.FileReader reader = new java.io.FileReader(savedTransactionsFile);
                CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(SKIP_LINES_NUMBER).build()
        ) {
            while ((lineFromFile = csvReader.readNext()) != null) {
                transactionTable.add(new Transaction(lineFromFile)); //

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionTable;
    }


    public ArrayList<Stock> createListOfStockWithActualPrizes(File fileOfActualStockPrices) {

        final String SEPARATOR = ",";
        String stockName;
        String line;
        String[] lineValues;
        double actualPrize;
        int index;
        ArrayList<Stock> actualPrices = new ArrayList<>();

        try (Scanner fileWithActualPrices = new Scanner(fileOfActualStockPrices)) {

            while (fileWithActualPrices.hasNext()) {
                line = fileWithActualPrices.next();
                lineValues = line.split(SEPARATOR);
                stockName = lineValues[0];
                if (stockName.equals("EFENERGII")){
                    System.out.println(stockName);

                }
                actualPrize = Double.parseDouble(lineValues[5]);
                stockName = Stock.correctStockNameIfHasChanged(stockName);
                actualPrices.add(new Stock(stockName,actualPrize));
                //do skasowania TODO
                if (actualPrize==0) {
                    System.out.println(stockName+"cena zero, sprawdzic nazwe cen aktualnych z nazwa w pliku tranzakcje");
                }
               // index = isStockInVallet(stockName); // returns StockIndex
              //  valletsStock.get(index).setActualPrize(actualPrize);
              //  if (index > 0) {                }//TODO delete
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return actualPrices;
    }



    public File downloadNewFile(URL url, File file) throws IOException {

        //if downloaded today then stop don't download again, don't download on saturday or sun the friday file
        LocalDate today = new LocalDate(System.currentTimeMillis());
        LocalDate lastModified = new LocalDate(file.lastModified());

        if ((!today.isEqual(lastModified)) || ((today.getDayOfWeek() > 5) &
                (Days.daysBetween(today, lastModified).getDays() > 2))) {
//            downloadNewFile(url, file);
            file.createNewFile();
            FileUtils.copyURLToFile(url, file);
        }
        //      lastModified = new LocalDate(file.lastModified());
        System.out.println("Ceny akcji z aktualne na dzień: " + lastModified);

        return file;


    }


}
