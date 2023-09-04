package com.example.booknexus;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.booknexus.Models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class Utility {

    private static final String PREFERENCE_NAME = "alternative_db";
    private static final String ALL_BOOKS_KEY = "all_books";
    private static final String FAVOURITE_BOOKS_KEY = "favourite_books";
    private static final String ALREADY_READ_BOOKS_KEY = "already_read_books";
    private static final String WISHLIST_BOOKS_KEY = "wishlist_books";
    private static final String CURRENTLY_READING_BOOKS_KEY = "currently_reading_books";
    private static Utility instance;
    private SharedPreferences sharedPreferences;

    private Utility(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if (null == getAllBooks()) {
            initData();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        if (null == getFavouriteBooks()) {
            editor.putString(FAVOURITE_BOOKS_KEY, gson.toJson(new ArrayList<Book>()));
            editor.commit();
        }

        if (null == getAlreadyReadBooks()) {
            editor.putString(ALREADY_READ_BOOKS_KEY, gson.toJson(new ArrayList<Book>()));
            editor.commit();
        }

        if (null == getWishListBooks()) {
            editor.putString(WISHLIST_BOOKS_KEY, gson.toJson(new ArrayList<Book>()));
            editor.commit();
        }

        if (null == getCurrentlyReadingBooks()) {
            editor.putString(CURRENTLY_READING_BOOKS_KEY, gson.toJson(new ArrayList<Book>()));
            editor.commit();
        }
    }

    public boolean editBook(Book editedBook) {
        ArrayList<Book> allBooks = getAllBooks();
        if (null != allBooks) {
            for (Book b : allBooks) {
                if (b.getId() == editedBook.getId()) {
                    if (allBooks.remove(b)) {
                        if (allBooks.add(editedBook)) {
                            Gson gson = new Gson();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(ALL_BOOKS_KEY);
                            editor.putString(ALL_BOOKS_KEY, gson.toJson(allBooks));
                            editor.commit();

                            if (removeFromWishListBooks(b)) {
                                addWishListBook(editedBook);
                            }

                            if (removeFromFavouriteBooks(b)) {
                                addFavouriteBook(editedBook);
                            }

                            if (removeFromCurrentlyReadingBooks(b)) {
                                addCurrentlyReadingBook(editedBook);
                            }

                            if (removeFromAlreadyReadBooks(b)) {
                                addAlreadyReadBook(editedBook);
                            }

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean addNewBook(Book book) {
        ArrayList<Book> books = getAllBooks();
        if (null != books) {
            if (books.add(book)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_BOOKS_KEY);
                editor.putString(ALL_BOOKS_KEY, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addCurrentlyReadingBook(Book book) {
        ArrayList<Book> books = getCurrentlyReadingBooks();
        if (null != books) {
            if (books.add(book)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(CURRENTLY_READING_BOOKS_KEY);
                editor.putString(CURRENTLY_READING_BOOKS_KEY, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addFavouriteBook(Book book) {
        ArrayList<Book> books = getFavouriteBooks();
        if (null != books) {
            if (books.add(book)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(FAVOURITE_BOOKS_KEY);
                editor.putString(FAVOURITE_BOOKS_KEY, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addAlreadyReadBook(Book book) {
        ArrayList<Book> books = getAlreadyReadBooks();
        if (null != books) {
            if (books.add(book)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALREADY_READ_BOOKS_KEY);
                editor.putString(ALREADY_READ_BOOKS_KEY, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addWishListBook(Book book) {
        ArrayList<Book> books = getWishListBooks();
        if (null != books) {
            if (books.add(book)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(WISHLIST_BOOKS_KEY);
                editor.putString(WISHLIST_BOOKS_KEY, gson.toJson(books));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean removeFromAllBooks(Book book) {
        ArrayList<Book> books = getAllBooks();
        if (null != books) {
            for (Book b : books) {
                if (b.getId() == book.getId()) {
                    if (books.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(ALL_BOOKS_KEY);
                        editor.putString(ALL_BOOKS_KEY, gson.toJson(books));
                        editor.commit();
                        //Also remove from all the lists
                        removeFromAlreadyReadBooks(book);
                        removeFromCurrentlyReadingBooks(book);
                        removeFromFavouriteBooks(book);
                        removeFromWishListBooks(book);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeFromCurrentlyReadingBooks(Book book) {
        ArrayList<Book> books = getCurrentlyReadingBooks();
        if (null != books) {
            for (Book b : books) {
                if (b.getId() == book.getId()) {
                    if (books.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(CURRENTLY_READING_BOOKS_KEY);
                        editor.putString(CURRENTLY_READING_BOOKS_KEY, gson.toJson(books));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeFromAlreadyReadBooks(Book book) {
        ArrayList<Book> books = getAlreadyReadBooks();
        if (null != books) {
            for (Book b : books) {
                if (b.getId() == book.getId()) {
                    if (books.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(ALREADY_READ_BOOKS_KEY);
                        editor.putString(ALREADY_READ_BOOKS_KEY, gson.toJson(books));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeFromWishListBooks(Book book) {
        ArrayList<Book> books = getWishListBooks();
        if (null != books) {
            for (Book b : books) {
                if (b.getId() == book.getId()) {
                    if (books.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(WISHLIST_BOOKS_KEY);
                        editor.putString(WISHLIST_BOOKS_KEY, gson.toJson(books));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeFromFavouriteBooks(Book book) {
        ArrayList<Book> books = getFavouriteBooks();
        if (null != books) {
            for (Book b : books) {
                if (b.getId() == book.getId()) {
                    if (books.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(FAVOURITE_BOOKS_KEY);
                        editor.putString(FAVOURITE_BOOKS_KEY, gson.toJson(books));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<Book> minimize(ArrayList<Book> books) {
        for (Book book : books) {
            book.setExpended(false);
        }
        return books;
    }

    public Book getBookById(int id) {

        ArrayList<Book> books = getAllBooks();
        if (null != books) {
            for (Book book : books) {
                if (book.getId() == id) {
                    return book;
                }
            }
        }
        return null;
    }

    public int getNextId() {
        ArrayList<Book> books = getAllBooks();
        ArrayList<Integer> ids = new ArrayList<>();
        if (null != books) {
            for (Book b : books) {
                ids.add(b.getId());
            }
            if (!ids.isEmpty()) {
                int maxId = 0;
                for (int i : ids) {
                    if (i > maxId) {
                        maxId = i;
                    }
                }
                return maxId + 1;
            } else {
                return 1;
            }
        }

        return -1;
    }

    public static synchronized Utility getInstance(Context context) {
        if (null == instance) {
            instance = new Utility(context);
        }
        return instance;
    }

    private void initData() {

        ArrayList<Book> books = new ArrayList<>();

        books.add(new Book(1,
                "Vodič kroz Galaksiju za autostopere",
                "Douglas Adams",
                200,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/7/176539.jpg",
                "Vodič kroz Galaksiju za autostopere autora Douglasa Adamsa je neodoljiva i urnebesno smiješna avantura koja vas vodi na nevjerojatno putovanje kroz beskrajni svemir. Knjiga prati nevjerojatne dogodovštine nespretnog Zafoda Biblbroksoxa, tvrdoglave Zemljanke Arthure Dent, i njihovog izvanzemaljskog vodiča, električnog debelog Forda Prefecta. Kroz seriju apsurdnih događaja, čitatelji će se upustiti u smijeh i filozofski razmišljati o životu, svemiru i svemu ostalom. Ova knjiga je nezaobilazna za sve ljubitelje znanstvene fantastike, humorističke proze i ludih avantura u svemiru.",
                "\"Douglas Adams' Vodič kroz Galaksiju za autostopere\" je jedinstvena i nezaboravna knjiga koja vas vodi na putovanje kroz svemir i vrijeme, uz puno smijeha i dubokih filozofskih razmišljanja. Glavni junak, Arthur Dent, nespretni i zbunjeni Zemljanin, se slučajno nađe u svemiru nakon što mu dom sruši buldožer kako bi napravio mjesta za izgradnju obilaznice. Sretne se s Fordom Prefectom, njegovim prijateljem i vanzemaljskim vodičem za autostopere, i zajedno kreću na nevjerojatno smiješno i često neshvatljivo putovanje.\n" +
                        "\n" +
                        "Kroz seriju absurdnih događaja, čitatelji će se suočiti s izvanzemaljcima, kompjuterima s ludim idejama, depresivnim robotima i beskrajno smiješnim situacijama koje postavljaju pitanja o smislu života, svemiru i svemu ostalom. Adamsova inventivna i britka proza ne samo da će vas nasmijati do suza, već će vas potaknuti na razmišljanje o dubokim temama i filozofskim konceptima.\n" +
                        "\n" +
                        "\"Vodič kroz Galaksiju za autostopere\" nije samo knjiga za ljubitelje znanstvene fantastike, već za sve one koji cijene inteligentan humor i briljantnu satiru. Ova knjiga je klasik žanra i ostavlja neizbrisiv dojam na svakog čitatelja koji se upusti u svoju nevjerojatnu priču.",
                "https://shop.skolskaknjiga.hr/knjizevnost/romani/fantasy-i-zf/vodic-kroz-galaksiju-za-autostopere.html"));
        books.add(new Book(2,
                "Restoran na kraju svemira",
                "Douglas Adams",
                228,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/7/176543.jpg",
                "\"Restoran na kraju svemira\" je drugi dio nevjerojatno popularnog serijala \"Vodič kroz Galaksiju za autostopere\" autora Douglasa Adamsa. Ova knjiga nastavlja avanture Arthura Denta, Forda Prefecta i njihovih izvanredno neobičnih prijatelja dok putuju kroz beskrajni svemir. Knjiga je prepuna britkog humora, nevjerojatnih situacija i dubokih filozofskih razmišljanja o životu, svemiru i svemu ostalom.",
                "\"Restoran na kraju svemira\" je drugi roman u serijalu \"Vodič kroz Galaksiju za autostopere\" autora Douglasa Adamsa, koji donosi nastavak nevjerojatnih pustolovina Arthura Denta, Forda Prefecta i njihovih izvanredno čudnih saputnika. Nakon što su nekako preživjeli uništenje Zemlje u prvom dijelu serijala, sada se nalaze na putovanju kroz beskrajni svemir s ciljem pronalaska najboljeg mjesta za večeru - restorana na kraju svemira.\n" +
                        "\n" +
                        "Kroz svoje avanture, junaci će naići na brojne izvanzemaljske kulture, vanzemaljce čija su ponašanja i običaji često čudni i smiješni. Priča je ispričana s nevjerojatno duhovitim i britkim humorom koji je postao zaštitni znak serijala. Adamsova proza istovremeno nudi duboke filozofske refleksije o prirodi postojanja, smislu života i svemira.\n" +
                        "\n" +
                        "\"Restoran na kraju svemira\" nije samo nastavak priče, već još jedno remek-djelo britkog i inteligentnog humora koje nudi čitateljima priliku da se izgube u svijetu beskrajnih mogućnosti i apsurdnih događanja. Ova knjiga je nezaobilazna za sve ljubitelje znanstvene fantastike, humorističke proze i avantura koje će ih odvesti daleko izvan granica poznatog svemira.",
                "https://shop.skolskaknjiga.hr/knjizevnost/romani/fantasy-i-zf/restoran-na-kraju-svemira-drugi-dio-vodica-kroz-galaksiju-za-autostopere.html"));

        books.add(new Book(3,
                "Život, univerzum i sve ostalo",
                "Douglas Adams",
                212,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/7/176544.jpg",
                "\"Život, univerzum i sve ostalo\" je treći dio kultnog serijala \"Vodič kroz Galaksiju za autostopere\" autora Douglasa Adamsa. Knjiga donosi nove nevjerojatne pustolovine Arthura Denta i njegovih izvanredno neobičnih prijatelja dok istražuju tajne svemira i pokušavaju otkriti odgovor na pitanje o životu, svemiru i svemu ostalom.",
                "\"Život, univerzum i sve ostalo\" je treći roman u kultnom serijalu \"Vodič kroz Galaksiju za autostopere\" autora Douglasa Adamsa. Ova knjiga donosi novo putovanje kroz svemir uz neizbježan humor, apsurdne situacije i duboka filozofska razmišljanja koja su postala zaštitni znak serijala.\n" +
                        "\n" +
                        "Priča prati Arthura Denta, Forda Prefecta i ostale članove njihove ekscentrične ekipe dok lutaju kroz svemir i suočavaju se s izvanredno čudnim izazovima. U središtu knjige je pitanje o \"Odgovoru na pitanje o životu, svemiru i svemu ostalom\", koje se otkriva kao centralna tajna univerzuma. Kroz svoje avanture, junaci će naići na brojne izvanzemaljske rase, tehničke čudnovatosti i filozofske paradokse.\n" +
                        "\n" +
                        "Douglas Adams nastavlja oduševljavati čitatelje svojim britkim i satiričnim stilom pisanja, koji kombinira humor i duboke misli o prirodi postojanja. \"Život, univerzum i sve ostalo\" nije samo zabavna knjiga, već i poziv na razmišljanje o smislu života, svemira i svih nerazumnih stvari između. Ova knjiga je klasik znanstvene fantastike i humorističke književnosti koja će vas nasmijati i potaknuti na razmišljanje, ostavljajući neizbrisiv dojam na svoje čitatelje.",
                "https://shop.skolskaknjiga.hr/knjizevnost/romani/fantasy-i-zf/zivot-univerzum-i-sve-ostalo.html"));

        books.add(new Book(4,
                "Fahrenheit 451",
                "Ray Bradbury",
                208,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/8/180230.jpg",
                "\"Fahrenheit 451\" autora Rayja Bradburyja je distopijski klasik koji istražuje opasnosti od cenzure i nedostatka slobode govora u društvu. Smješten u budućnosti gdje su knjige zabranjene, prati vatrogasca Guya Montaga koji se budi iz svoje apatije prema društvu te postaje protivnik sistema koji potiskuje kritičko razmišljanje. Ova knjiga je duboko provokativna meditacija o moći književnosti i slobode uma.",
                "\"Fahrenheit 451\" Rayja Bradburyja je remek-djelo distopijske književnosti koje se bavi ključnim temama cenzure, slobode govora i moći književnosti. Radnja se odvija u totalitarnom društvu gdje je čitanje i posjedovanje knjiga označeno kao zločin. Glavni junak, Guy Montag, je vatrogasac čiji je posao spaljivati knjige umjesto gasiti požare. No, Montag počinje postavljati pitanja o prirodi njegove uloge u društvu i duboko je nezadovoljan uniformnošću i besmislenošću svijeta oko sebe.\n" +
                        "\n" +
                        "Kroz Montagovu transformaciju od poslušnog vatrogasca u pobunjenika, knjiga istražuje moć književnosti da potakne kritičko razmišljanje i podstakne ljude na djelovanje. Fahrenheit 451 simbolizira temperaturu na kojoj knjige izgorijevaju, ali također označava i vrelu točku društvenog sukoba između represivnog sistema i pojedinca koji se usudi razmišljati drugačije.\n" +
                        "\n" +
                        "Bradburyjev stil pisanja je poetski i simboličan, a knjiga izaziva duboka razmišljanja o vrijednosti slobode izražavanja i kulturne raznolikosti. \"Fahrenheit 451\" ostaje klasično djelo koje je i dalje relevantno u današnjem svijetu gdje se suočavamo s pitanjima cenzure, slobode govora i individualizma. Ova knjiga potiče čitatelje da razmisle o važnosti kritičkog razmišljanja i borbe za slobodu mišljenja.",
                "https://shop.skolskaknjiga.hr/knjizevnost/romani/fantasy-i-zf/fahrenheit-451.html"));

        books.add(new Book(5,
                "Čovjek u visokom dvorcu",
                "Philip K. Dick",
                312,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/7/174220.jpg",
                "\"Čovjek u visokom dvorcu\" Philipa K. Dicka je alternativna povijesna distopija koja se odvija u svijetu gdje su Saveznici izgubili Drugi svjetski rat, a Sjedinjene Američke Države su podijeljene između Nacističke Njemačke i Japana. Knjiga prati sudbine različitih likova koji otkrivaju postojanje knjige koja opisuje alternativni svemir u kojem su Saveznici pobijedili. Ova knjiga istražuje teme stvarnosti, moći i interpretacije povijesti.",
                "\"Čovjek u visokom dvorcu\" Philipa K. Dicka je provokativna i složena alternativna povijesna distopija koja postavlja pitanja o stvarnosti, moći i povijesti. Radnja romana odvija se u alternativnom svijetu u kojem su Nacistička Njemačka i Japanska Imperija pobijedili u Drugom svjetskom ratu, a Sjedinjene Američke Države su podijeljene između tih dva totalitarna režima.\n" +
                        "\n" +
                        "Knjiga prati sudbine različitih likova, uključujući Juliana, koji živi pod japanskom vlašću u San Franciscu, i Roberta Childana, trgovca antikvitetima koji se bavi prodajom falsificiranih predmeta \"starog svijeta\" Nacističkim časnicima. Radnja se dodatno komplicira kada se otkrije postojanje knjige nazvane \"I Ching\", koja opisuje alternativni svemir u kojem su Saveznici pobijedili. Ova knjiga unosi elemente misterija i konspiracije jer likovi pokušavaju shvatiti stvarnost svojeg svijeta i ulogu te knjige u promjeni povijesti.\n" +
                        "\n" +
                        "Philip K. Dick istražuje duboke teme u \"Čovjeku u visokom dvorcu\", uključujući prirodu stvarnosti, povijest kao konstrukt, i moć manipulacije informacijama. Knjiga postavlja pitanje kako tumačimo povijest i kako se kolektivno sjećanje oblikuje i kontrolira. \"Čovjek u visokom dvorcu\" je klasično djelo znanstvene fantastike koje će vas potaknuti na razmišljanje o prirodi stvarnosti i moći narativa u oblikovanju svijeta oko nas.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n",
                "https://shop.skolskaknjiga.hr/knjizevnost/romani/fantasy-i-zf/covjek-u-visokom-dvorcu.html"));

        books.add(new Book(6,
                "Projekt Spasitelj",
                "Andy Weir",
                480,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/9/194736.jpg",
                "\"Projekt Spasitelj\" Andyja Weira je uzbudljiva znanstvena fantastika koja prati iskupljujuću misiju astronauta Ryana Cartwrighta. Smještena u bliskoj budućnosti, priča prati Cartwrighta dok se suočava s izazovima i opasnostima kako bi spasio posadu svoje svemirske letjelice. Weirov karakteristični humor i vjernost znanstvenim konceptima čine ovu knjigu neodoljivom avanturom za ljubitelje svemirske znanstvene fantastike.",
                "\"Projekt Spasitelj\" Andyja Weira, autora svjetski poznatog romana \"Marsovac,\" je uzbudljiva i napeta znanstvena fantastika koja pruža čitateljima duboko uronjen pogled u svijet svemirske istraživačke tehnologije. Radnja se odvija u bliskoj budućnosti, gdje je astronaut Ryan Cartwright poslan na rizičnu misiju kako bi spasio posadu svemirske letjelice koja je zaglavila na putu prema Marsu.\n" +
                        "\n" +
                        "Weir se ističe svojim vjernim prikazom znanstvenih aspekata svemirskih putovanja, koristeći tehnološke detalje i fizikalne principe kako bi stvorio uvjerljiv svemir. Glavni junak, Ryan Cartwright, je neustrašiv i odlučan astronaut čiji lik razvija duboku emocionalnu složenost tijekom misije.\n" +
                        "\n" +
                        "Autor kombinira napetost s humorom, što je postala njegova prepoznatljiva karakteristika, čime čitatelje drži zainteresiranima i nasmijanima dok se Cartwright suočava s brojnim izazovima u svemiru. \"Projekt Spasitelj\" je knjiga koja istovremeno oduševljava i edukira, omogućujući čitateljima da se upuste u nevjerojatno putovanje kroz svemirsku avanturu ispunjenu znanstvenim čudima i ljudskim heroizmom.",
                "https://shop.skolskaknjiga.hr/projekt-spasitelj.html"));

        books.add(new Book(7,
                "Zelena milja",
                "Stephen King",
                312,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/9/195680.jpg",
                "\"Zelena milja\" Stephena Kinga je emotivna i nadnaravna priča smještena u zatvoru u južnom SAD-u 1930-ih godina. Knjiga prati Paul Edgecombea, čuvara zatvora, dok svjedoči neobičnom događaju povezanom s osuđenikom Johnom Coffeyem, čovjekom s izvanrednim darom. Ova priča istražuje teme pravde, suosjećanja i ljudske prirode kroz bogato razvijene likove i napetu radnju.",
                "\"Zelena milja\" Stephena Kinga je knjiga koja istovremeno zadivljuje i potresa svojom izvanrednom naracijom i dubokim emocionalnim slojevima. Radnja se odvija u južnim SAD-ima tijekom 1930-ih godina, u zatvoru s nadimkom \"Zelena milja,\" gdje radnici osuđenici obavljaju teške poslove. Priča je ispričana iz perspektive Paula Edgecombea, iskusnog čuvara zatvora, čiji svakodnevni život postaje neočekivano ispunjen događajima kada na njegovu jedinicu stigne John Coffey, osuđenik s izvanrednim darom.\n" +
                        "\n" +
                        "John Coffey je tih i nježan čovjek koji posjeduje nadnaravnu moć da liječi bolesti i ozljede. Kroz Coffeyevu priču, King istražuje teme pravde, suosjećanja i ljudske prirode. Radnja se fokusira na nepravdu koju ljudi doživljavaju u svijetu, dok se istovremeno suočava s moralnim dilemama i posljedicama njihovih odluka.\n" +
                        "\n" +
                        "\"Zelena milja\" je knjiga koja vas će vas ostaviti duboko ganutima i potaknutima na razmišljanje. Stephen King vješto gradi likove, postavlja atmosferu i stvara napetu radnju koja istražuje ljudsku psihozu u ekstremnim situacijama. Ova priča je remek-djelo koje spaja nadnaravno s dubokim ljudskim emocijama, čineći je jednim od najprepoznatljivijih i najnagrađivanijih djela u Kingovom opusu.",
                "https://shop.skolskaknjiga.hr/zelena-milja-23506.html"));

        books.add(new Book(8,
                "1984.",
                "George Orwell",
                352,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/2/121368_1.jpg",
                "\"1984.\" Georgea Orwella je klasična distopija koja se odvija u totalitarnom društvu Oceania, gdje vlada Veliki Brat. Roman prati život Winston Smitha, službenika Ministarstva Istine koji se bori protiv opresivnog režima i pokušava sačuvati svoju individualnost i slobodu mišljenja u svijetu gdje su istina i prošlost podložni manipulaciji. Ova knjiga duboko istražuje teme nadzora, cenzure i moći države.",
                "\"1984.\" Georgea Orwella je knjiga koja je postala sinonim za totalitarizam i nadzor države nad pojedincem. Radnja se odvija u distopijskom društvu Oceania, gdje se sloboda mišljenja i privatnost potpuno potiskuju. Veliki Brat, neuhvatljivi lider Oceania, nadzire svaki aspekt života svojih građana putem masovnog nadzora, propagande i cenzure.\n" +
                        "\n" +
                        "Glavni junak, Winston Smith, je službenik Ministarstva Istine koji počinje sumnjati u vlastitu stvarnost i odlučuje se suprotstaviti režimu. Kroz njegovu priču, Orwell istražuje moć manipulacije informacijama, lažnih vijesti i prisile na konformizam. Knjiga također duboko analizira psihologiju straha i kontrole te propituje granice pojedinačne slobode.\n" +
                        "\n" +
                        "\"1984.\" je klasik distopije koji nije izgubio svoju relevantnost ni desetljećima nakon objave. Knjiga se bavi temama koje su univerzalne i duboko emocionalne, poput borbe pojedinca protiv dehumanizirajuće moći države i istine koja je podložna manipulaciji. Orwellov izvanredan stil pisanja, slikoviti opisi i jasna poruka čine ovu knjigu obaveznom lektirom za one koji žele razumjeti prirodu totalitarizma i važnost obrane slobode mišljenja i individualnih prava.",
                "https://shop.skolskaknjiga.hr/1984.html"));

        books.add(new Book(9,
                "Dina",
                "Frank Herbert",
                600,
                "https://shop.skolskaknjiga.hr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/1/7/178821.jpg",
                "\"Dina\" Franka Herberta je znanstveno-fantastični ep prve knjige u serijalu \"Dina,\" smješten u daleku budućnost gdje se nalazi galaksija ispunjena političkim spletkama, sukobima i dubokim filozofskim pitanjima. Knjiga prati sudbinu Paula Atreidesa, mladog nasljednika moćne obitelji, dok se suočava s opasnostima i tajnama pustinje planeta Arrakis, poznate kao \"Dina.\" Ovo je priča o politici, religiji, moći i sudbini, koja istražuje duboke teme i složene likove.",
                "\"Dina\" Franka Herberta je znanstveno-fantastični epski roman koji uvodi čitatelje u složeni i duboko razrađen svijet budućnosti. Radnja se odvija u galaksiji gdje se različite obitelji, planeti i kulture bore za kontrolu nad resursima i političkom moći. Glavna priča prati mladog Paula Atreidesa, nasljednika moćne obitelji Atreides, koji je prisiljen napustiti svoj dom i suočiti se s opasnostima planeta Arrakis, poznate kao \"Dina.\"\n" +
                        "\n" +
                        "Arrakis je jedini planet u galaksiji koji proizvodi dragocjeni resurs poznat kao \"melanž\" ili \"čarobna začina,\" koji ima ogroman politički i ekonomski značaj. Knjiga prati Paulovu transformaciju od mladića do lidera pustinjskog plemena i proroka, što je isprepletano s dubokim filozofskim pitanjima o sudbini, religiji, moći i politici.\n" +
                        "\n" +
                        "Frank Herbert je stvorio kompleksan svijet s bogatom poviješću, različitim kulturama i intrigantnim likovima. \"Dina\" je ne samo znanstvena fantastika, već i knjiga koja istražuje duboke teme i potiče čitatelje na razmišljanje o prirodi ljudske sudbine i kolektivnog iskustva. Ova knjiga je klasik žanra koja je ostavila dubok i trajan utisak na čitatelje i inspirirala mnoge druge autore i umjetnike.",
                "https://shop.skolskaknjiga.hr/dina.html"));


        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_BOOKS_KEY, gson.toJson(books));
        editor.commit();
    }

    public ArrayList<Book> getAllBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(ALL_BOOKS_KEY, null), type);
        if (books != null) {
            Collections.sort(books, Book.bookIdComparator);
        }
        return books;
    }

    public ArrayList<Book> getFavouriteBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(FAVOURITE_BOOKS_KEY, null), type);
        if (books != null) {
            Collections.sort(books, Book.bookIdComparator);
        }
        return books;
    }

    public ArrayList<Book> getAlreadyReadBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(ALREADY_READ_BOOKS_KEY, null), type);
        if (books != null) {
            Collections.sort(books, Book.bookIdComparator);
        }
        return books;
    }

    public ArrayList<Book> getWishListBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(WISHLIST_BOOKS_KEY, null), type);
        if (books != null) {
            Collections.sort(books, Book.bookIdComparator);
        }
        return books;
    }

    public ArrayList<Book> getCurrentlyReadingBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        ArrayList<Book> books = gson.fromJson(sharedPreferences.getString(CURRENTLY_READING_BOOKS_KEY, null), type);
        if (books != null) {
            Collections.sort(books, Book.bookIdComparator);
        }
        return books;
    }
}
