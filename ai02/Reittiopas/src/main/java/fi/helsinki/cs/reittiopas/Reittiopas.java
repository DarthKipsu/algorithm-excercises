package fi.helsinki.cs.reittiopas;

import fi.helsinki.cs.reittiopas.logiikka.Pysakki;
import fi.helsinki.cs.reittiopas.logiikka.Tila;
import java.util.Collection;
import java.util.PriorityQueue;

public class Reittiopas {

    private Vertailija vertailija;

    /**
     * Toteuta A*-haku. Palauta reitti taaksepäin linkitettynä listana
     * Tila-olioita, joista ensimmäinen osoittaa maalipysäkkiin ja jokainen
     * tuntee pysäkin ja tilan, josta kyseiseen tilaan päästiin (viimeisen
     * solmun Pysäkki on lähtöpysäkki ja edellinen tila on null). Pidä myös
     * reitin ajallisesta kestosta kirjaa Tila-olioissa.
     *
     * Voit selvittää pysäkin naapuripysäkit, eli pysäkit joihin pysäkiltä on
     * suora yhteys, kutsumalla pysäkin getNaapurit() -metodia.
     *
     * Voit selvittää pysäkiltä nopeimman siirtymän keston jollekin sen
     * naapuripysäkeistä kutsumalla pysäkin nopeinSiirtyma(Pysakki p, int aika)
     * -metodia, joka ottaa parametrina naapuripysäkin ja tähän saakka kuljetun
     * reitin keston.
     *
     * Alussa luodaan Vertailija-olio (jonka toiminnallisuuden olet toteuttanut)
     * ja annetaan se konstruktorin parametrina PriorityQueue:lle. Nyt
     * PriorityQueue käyttää kirjoittamaasi vertailijaa ja palauttaa tilat
     * A*-haun kannalta järkevässä järjestyksessä.
     *
     * @param lahto Lahtopysakin koodi
     * @param maali Maalipysakin koodi
     * @param aikaAlussa Aika lähtöhetkellä
     * @return Tila-olioista koostuva linkitetty lista maalista lähtötilaan
     */
    public Tila haku(Pysakki lahto, Pysakki maali, int aikaAlussa) {
        this.vertailija = new Vertailija(maali);
        PriorityQueue<Tila> tutkittavat = new PriorityQueue<>(vertailija);
        Tila alkutila = new Tila(lahto, null, aikaAlussa); //(pysäkki, edellinen tila, kulunut aika)
        tutkittavat.add(alkutila);
        
        while (tutkittavat.peek() != null) {
            Tila tila = tutkittavat.poll();
            Pysakki pysakki = tila.getPysakki();
            if (pysakki.isNotVisited()) {
                pysakki.visit();
                if (pysakki.equals(maali)) {
                    return tila;
                }
                Collection<Pysakki> naapurit = pysakki.getNaapurit();
                for (Pysakki naapuri : naapurit) {
                    if (naapuri.isNotVisited()) {
                        int nykyinenAika = tila.getNykyinenAika();
                        int aikaNaapuriin = nykyinenAika + 
                                pysakki.nopeinSiirtyma(naapuri, nykyinenAika);
                        tutkittavat.add(new Tila(naapuri, tila, aikaNaapuriin));
                    }
                }
            }
        }
        return null;
    }
}
