import java.text.DecimalFormat;
import java.util.ArrayList;


//papier
// zysk ze sprzedazy
// koszty zakupu
// ilosc posiadanego papieru
// na tej posdtawie
// koszt przez pozostaly papier daje nam cene zerowa (czyli bezstratna)


public class PapierGlowna {

	
	private ArrayList<Stock> tablicaHistoriaTranzakcji = new ArrayList<Stock>();
	private double poIleJest = 0;//cena aktualna
	private Integer ileMamTeraz = 0;
	private double naReceWPLN = 0;
	private double zyskLubStrata = 0;
	private double total;
	private int ile = 0;
	private double sredniaCenaZakupuTegoCoZostalo=1;
	private double ileDostalemZaSprzedaz;
	private double ileWydalemNaZakupy;

	private static DecimalFormat df2 = new DecimalFormat(".##");
	

	//TODO dodac tablice historii transakcji
	//															null,				 0, 			 0, 				0, 						0, 			  0, 	  									  0,									 0,					0
	public PapierGlowna(ArrayList<Stock> tablicaHistoriaTranzakcji, double poIleJest, int ileMamTeraz, double naReceWPLN, double zyskLubStrata , double total, double sredniaCenaKupnaTegoCoZostaloNaRece, double ileDostalemZaSprzedaz, double ileWydalemNaZakupy){
	
		this.tablicaHistoriaTranzakcji = tablicaHistoriaTranzakcji;

		this.poIleJest = poIleJest ;
		this.ileMamTeraz = ileMamTeraz;
		this.naReceWPLN = naReceWPLN;
		this.zyskLubStrata = zyskLubStrata;
		this.total = total;
		this.sredniaCenaZakupuTegoCoZostalo = sredniaCenaKupnaTegoCoZostaloNaRece;
		this.ileDostalemZaSprzedaz = ileDostalemZaSprzedaz;
		this.ileWydalemNaZakupy = ileWydalemNaZakupy;
		
		
	}

	public double getPoIleJest() {
		return poIleJest;
	}



	public double setPoIleJest(double poIleJest) {
		return this.poIleJest = poIleJest;
	}

	public Integer getIleMamTeraz() {
		return ileMamTeraz;
	}

	public int setIleMamTeraz(int ileMamTeraz) {
		return this.ileMamTeraz = ileMamTeraz;
	}

	public double getNaReceWPLN() {
		return naReceWPLN;
	}

	public double setNaReceWPLN(double naReceWPLN) {
		return this.naReceWPLN = naReceWPLN;
	}

	public double getZyskLubStrata() {
		return zyskLubStrata;
	}

	public double setZyskLubStrata(double zyskLubStrata) {
		return this.zyskLubStrata = zyskLubStrata;
	}

	public double getTotal() {
		return total;
	}

	public double setTotal(double total) {
		return this.total = total;
	}

	public ArrayList<Stock> getTablicaHistoriaTranzakcji() {
		return tablicaHistoriaTranzakcji;
	}

	public ArrayList<Stock> setTablicaHistoriaTranzakcji(
			ArrayList<Stock> tablicaHistoriaTranzakcji) {
		return this.tablicaHistoriaTranzakcji = tablicaHistoriaTranzakcji;
	}

	public double getSredniaCenaZakupuTegoCoZostalo() {
		return sredniaCenaZakupuTegoCoZostalo;
	}

	public void setSredniaCenaZakupuTegoCoZostalo(
			double sredniaCenaZakupuTegoCoZostalo) {
		this.sredniaCenaZakupuTegoCoZostalo = sredniaCenaZakupuTegoCoZostalo;
	}

	public void sort(CompareByQuantity compareByQuantity) {
		// TODO Auto-generated method stub
		
		
	}

	public double getIleDostalemZaSprzedaz() {
		return ileDostalemZaSprzedaz;
	}

	public void setIleDostalemZaSprzedaz(double wartoscDotychczasowejSprzedazy) {
		this.ileDostalemZaSprzedaz = wartoscDotychczasowejSprzedazy;
	}

	public double getIleWydalemNaZakup() {
		return ileWydalemNaZakupy;
	}

	public void setIleWydalemNaZakup(double zaIleKupileDotychczas) {
		this.ileWydalemNaZakupy = zaIleKupileDotychczas;
	}

	

/*	@Override
	public String toString() {
		return "ile: " + this.ile() + " po/ cena: "
				+ df2.format(this.getKosztyKupna()) + " total: "
				+ df2.format(this.getZyskZeSprzedazy());

	}
*/
}
