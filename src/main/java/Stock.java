import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


//papier
// zysk ze sprzedazy
// koszty zakupu
// ilosc posiadanego papieru
// na tej posdtawie
// koszt przez pozostaly papier daje nam cene zerowa (czyli bezstratna)

public class Stock {

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private DecimalFormat df2 = new DecimalFormat(".##");


    private int ile = 0;
    private double poIle = 0;
    private String kupnoCzySprzedaz;
    private String kiedy;//rok, miesiac, dzien, godzina.minuta.sekunda
    private Date dataWykonaniaTransackji = null;


    public Stock(String kiedy, int ile, double poIle, String kupnoCzySprzedaz) throws ParseException {
        this.ile = ile;
        this.poIle = poIle;
        this.kupnoCzySprzedaz = kupnoCzySprzedaz;
        this.kiedy = kiedy;
        this.dataWykonaniaTransackji = format.parse(kiedy);
        //this.dataWykonaniaTransackji = format.parse(sformatujDate(kiedy));
    }

    private String sformatujDate(String[] kiedy) {
        String t[] = kiedy;
        t[3] = " " + kiedy[3];
        String kiedyNowy = Arrays.toString(kiedy);
        kiedyNowy = kiedyNowy.substring(1, kiedyNowy.length() - 1);
        kiedyNowy = kiedyNowy.replace(", ", "/");
        kiedyNowy = kiedyNowy.replace(".", ":");
        kiedyNowy = kiedyNowy.replace("/ ", " ");
        return kiedyNowy;
    }

    public int getIle() {
        return ile;
    }

    public void setIle(int ile) {
        this.ile = ile;
    }

    public double getPoIle() {
        return poIle;
    }

    public void setPoIle(double poIle) {
        this.poIle = poIle;
    }

    public String getKupnoCzySprzedaz() {
        return kupnoCzySprzedaz;
    }

    public void setKupnoCzySprzedaz(String kupnoCzySprzedaz) {
        this.kupnoCzySprzedaz = kupnoCzySprzedaz;
    }

    public String getKiedy() {
        return kiedy;
    }

    public void setKiedy(String kiedy) {
        this.kiedy = kiedy;
    }

    public Date getDataWykonaniaTransackji() {
        return dataWykonaniaTransackji;
    }

    public void setDataWykonaniaTransackji(Date dataWykonaniaTransackji) {
        this.dataWykonaniaTransackji = dataWykonaniaTransackji;
    }


    @Override
    public String toString() {
        return "ile: " + this.getIle() + " po/ cena: "
                + df2.format(this.getPoIle()) + " total: "
                + df2.format(this.getKupnoCzySprzedaz());

    }
}


