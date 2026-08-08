import java.util.ArrayList;
import java.util.LinkedList;

public class Hashing {

    // ============================================================
    // Teil 1: Offene Adressierung — Lineare Sondierung
    // ============================================================

    /**
     * Hash-Tabelle mit offener Adressierung und linearer Sondierung.
     *
     * Prinzip: Bei Kollision wird die nächste freie Stelle im Array gesucht
     * (g(m) = (h(k) + m) % N).
     *
     * Klausur-Fallstrick: Gelöschte Einträge werden mit DELETE_MARKER markiert,
     * nicht einfach auf null gesetzt — sonst brechen Sondierungsketten ab!
     */
    public static class LinearProbeTable {
        private static final int EMPTY = -1;
        private static final int DELETE_MARKER = -2;

        private int[] table;
        private int size;
        private final int capacity;
        private final double loadFactorThreshold;

        public LinearProbeTable(int capacity) {
            this.capacity = capacity;
            this.table = new int[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = EMPTY;
            }
            this.size = 0;
            this.loadFactorThreshold = 0.7;
        }

        /**
         * Hashfunktion: h(k) = k % N
         */
        private int hash(int key) {
            return key % capacity;
        }

        private void resize(){
            this.capacity = 2 * this.capacity; // Double the capacity
            int[] res = new int[this.capacity];
            for (int i=0; i < this.capacity; i++){
                if (this.table[i] != null){
                    res[i] = this.table[i]
                }
                res[i] = EMPTY;
            }
            this.table = res;
        }

        /**
         * Fügt einen key ein. Bei Kollision: lineare Sondierung.
         * Resizing wenn Load Factor > Threshold.
         */
        public void insert(int key) {
            int retries = 1; // Anzahl der neuen Versuche
            if (this.size == this.capacity){ // Tabelle resizen, wenn kein Platz mehr ist
                resize();
            }
            if (this.table[hash(key)] == EMPTY && this.table[hash(key)] != DELETE_MARKER){
                this.table[hash(key)] = key; // Wenn es frei ist
            }
            while (this.table[hash(key + retries)] != EMPTY){
                retries++; // Es wird solange der Index erhöht bis es ein freies Element gibt
            }
            this.table[hash(key + retries)] = key; // Wenn der hashed_key auf ein leeres Element zeigt -> key am Index einfügen

        }

        /**
         * Sucht einen key. Gibt den Index zurück oder -1 wenn nicht gefunden.
         * Sondierungskette wird abgearbeitet bis EMPTY getroffen wird.
         */
        public int search(int key) {
            // TODO: Implementiere Suche mit linearer Sondierung
            // WICHTIG: Endlosschleife vermeiden! Abbruch wenn EMPTY getroffen.
            // DELETE_MARKER überspringen und weiter suchen.
            for (int i=0; i < this.capacity; i++){
                if (this.table[i] == key){
                    return i;
                }
                if (this.table[i] == EMPTY){
                    return -1;
                }
            }
            return -1;
        }

        /**
         * Löscht einen key. Setzt auf DELETE_MARKER (nicht null!).
         * DELETE_MARKER markiert "ehemals belegt, jetzt frei für neue Einfügungen",
         * aber NICHT "frei für Sondierungsketten" — Suchen müssen trotzdem weiterlaufen.
         */
        public void delete(int key) {
            int pos = search(key);
            this.table[pos] = DELETE_MARKER;
        }

        public int size() { return size; }
        public int capacity() { return capacity; }
        public double loadFactor() { return (double) size / capacity; }

        public void printTable() {
            System.out.print("Tabelle [");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == EMPTY) {
                    System.out.print(" . ");
                } else if (table[i] == DELETE_MARKER) {
                    System.out.print(" x ");
                } else {
                    System.out.print(" " + table[i] + " ");
                }
            }
            System.out.println("]");
            System.out.print("        " + " ".repeat(capacity) + "\n");
            System.out.print("         0");
            for (int i = 1; i < capacity; i++) {
                System.out.printf("%3d", i % 10);
            }
            System.out.println("\n");
        }
    }

    // ============================================================
    // Teil 2: Geschlossene Adressierung — Chaining (Verkettung)
    // ============================================================

    /**
     * Hash-Tabelle mit separater Kettenbildung (Chaining).
     *
     * Prinzip: Jeder Array-Slot verweist auf eine verkettete Liste.
     * Überbelegung (α > 1) ist problemlos möglich.
     *
     * Aufwand:
     * - Erfolgreiche Suche: 1 + α/2 Schritte
     * - Erfolglose Suche: 1 + α Schritte
     * - α = size / capacity (Load Factor)
     */
    public static class ChainingTable {
        private static class Node {
            int key;
            Node next;

            Node(int key) {
                this.key = key;
                this.next = null;
            }
        }

        private Node[] buckets;
        private int size;
        private final int capacity;

        public ChainingTable(int capacity) {
            this.capacity = capacity;
            this.buckets = new Node[capacity];
            this.size = 0;
        }

        private int hash(int key) {
            return key % capacity;
        }

        /**
         * Fügt einen key ein (am Anfang der Kette).
         */
        public void insert(int key) {
            // 1. Index im Array berechnen (inkl. Absicherung für negative Keys)
            int index = hash(key);
            if (index < 0) {
                index += capacity;
            }

            // Optional: Prüfen, ob der Schlüssel bereits existiert (falls Duplikate vermieden werden sollen)
            Node current = buckets[index];
            while (current != null) {
                if (current.key == key) {
                    return; // Schlüssel existiert schon, Abbruch
                }
                current = current.next;
            }

            // 2. Neuen Knoten erzeugen
            Node newNode = new Node(key);

            // 3. Am Anfang der verketteten Liste einfügen (O(1))
            newNode.next = buckets[index]; // Der neue Knoten zeigt auf das bisherige erste Element
            buckets[index] = newNode;     // Das Ziel-Bucket zeigt jetzt auf den neuen Knoten

            // 4. Elementanzahl erhöhen
            size++;
        }

        /**
         * Sucht einen key in der entsprechenden Kette.
         */
        public boolean contains(int key) {
            int index = hash(key);
            if (index < 0){
                index += capacity;
            }

            Node current = buckets[index];

            while (current != null){
                if (current.key == key){
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        /**
         * Löscht einen key aus der entsprechenden Kette.
         * ACHTUNG: Sonderfall — zu löschender Knoten ist der Kopf der Kette!
         */
        public void delete(int key) {
            int index = hash(key);
            if (index < 0) index += capacity; // Negativ-Schutz für Java-Modulo

            Node current = buckets[index];
            Node prev = null; // Speichert den vorherigen Knoten

            while (current != null) { // Liste durchlaufen bis zum Ende
                if (current.key == key) { // Match gefunden!

                    if (prev == null) {
                        // Sonderfall: Element ist der Kopf -> Bucket zeigt direkt auf Nachfolger
                        buckets[index] = current.next;
                    } else {
                        // Normalfall: Element liegt mitten/am Ende -> Vorgänger überspringt current
                        prev.next = current.next;
                    }

                    size--; // Elementanzahl verringern
                    return; // Löschen abgeschlossen, Abbrechen
                }

                // Schritt weitergehen: current wird zu prev, current rückt eins vor
                prev = current;
                current = current.next;
            }
        }

        public int size() { return size; }
        public int capacity() { return capacity; }
        public double loadFactor() { return (double) size / capacity; }

        public void printTable() {
            System.out.println("Chaining-Tabelle (α = " + String.format("%.2f", loadFactor()) + "):");
            for (int i = 0; i < capacity; i++) {
                System.out.print("  [" + i + "] -> ");
                Node current = buckets[i];
                while (current != null) {
                    System.out.print(current.key + " -> ");
                    current = current.next;
                }
                System.out.println("null");
            }
            System.out.println();
        }
    }

    // ============================================================
    // Teil 3: Horner-Schema für String-Hashing
    // ============================================================

    /**
     * Berechnet einen Hash-Wert für einen String using Horner-Schema.
     *
     * Verhindert arithmetische Überläufe bei langen Zeichenketten,
     * indem nach JEDEM Zeichenschritt modulo gerechnet wird.
     *
     * Formel: h(s) = (s[0]*p^(n-1) + s[1]*p^(n-2) + ... + s[n-1]*p^0) mod m
     * Horner: h = 0; für jedes Zeichen c: h = (h * p + c) mod m
     *
     * Typische Werte: p = 31 (Primzahl), m = 10^9 + 7 (große Primzahl)
     */
    public static class StringHasher {
        private static final int BASE = 31;
        private static final int MODULUS = 1_000_000_007;

        /**
         * Berechnet den Hash eines Strings.
         */
        public static int hash(String s) {
            for (char c : s.toCharArray()) {
                hash = (hash * BASE + c) % MODULUS;
            }
            return hash;
        }

        /**
         * Vergleicht zwei Strings auf Hash-Gleichheit (Vorfilter).
         * Bei ungleichem Hash sind die Strings garantiert verschieden.
         * Bei gleichem Hash MUSS noch char-vor-Char-Vergleich erfolgen!
         */
        public static boolean fastEquals(String a, String b) {
            if (a.length() != b.length()){
                return false;
            }
            a_charArray = a.toCharArray();
            b_charArray = b.toCharArray();

            if (hash(a) == hash(b)){
                for (int i=0; i < a_charArray.length(); i++){
                    if (a_charArray[i] != b_charArray[i]){
                        return false;
                    }
                }
                return true;
            }
        }
    }

    // ============================================================
    // Hauptmethode — Test-Runner
    // ============================================================

    public static void main(String[] args) {

        // ----------------------------------------------------------
        // Test 1: Lineare Sondierung (Teil 1)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 1: Lineare Sondierung");
        System.out.println("========================================\n");

        LinearProbeTable table = new LinearProbeTable(9);

        // Klausur-Beispiel: h(k) = k % 9
        // Schlüssel: 11, 2, 21, 30, 41, 5
        int[] keys1 = {11, 2, 21, 30, 41, 5};

        System.out.println("--- Einfüge-Sequenz ---");
        for (int key : keys1) {
            System.out.print("Einfügen " + key + " (h=" + (key % 9) + "): ");
            table.insert(key);
            table.printTable();
        }

        System.out.println("--- Suche ---");
        int[] searchKeys = {11, 2, 21, 99, 5};
        for (int key : searchKeys) {
            int pos = table.search(key);
            System.out.println("search(" + key + ") = " + (pos == -1 ? "nicht gefunden" : "Index " + pos));
        }

        System.out.println("--- Löschen ---");
        System.out.println("Vor delete(2):");
        table.printTable();
        table.delete(2);
        System.out.println("Nach delete(2) (x = Tombstone):");
        table.printTable();

        System.out.println("Suche nach 21 nach Löschung von 2:");
        int pos = table.search(21);
        System.out.println("search(21) = " + (pos == -1 ? "nicht gefunden" : "Index " + pos));
        System.out.println("(Muss funktionieren — Tombstone darf Kette nicht unterbrechen!)\n");

        // ----------------------------------------------------------
        // Test 2: Chaining (Teil 2)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 2: Chaining (Verkettung)");
        System.out.println("========================================\n");

        ChainingTable chainTable = new ChainingTable(5);

        int[] keys2 = {12, 17, 22, 7, 2, 13, 23};
        System.out.println("--- Einfüge-Sequenz ---");
        for (int key : keys2) {
            chainTable.insert(key);
            System.out.println("Eingefügt: " + key + " (bucket=" + (key % 5) + ")");
        }
        chainTable.printTable();

        System.out.println("--- Suche ---");
        int[] searchKeys2 = {12, 7, 99};
        for (int key : searchKeys2) {
            System.out.println("contains(" + key + ") = " + chainTable.contains(key));
        }

        System.out.println("--- Löschen ---");
        System.out.println("Vor delete(12):");
        chainTable.printTable();
        chainTable.delete(12);
        System.out.println("Nach delete(12):");
        chainTable.printTable();

        System.out.println("Load Factor α = " + String.format("%.2f", chainTable.loadFactor()));
        System.out.println("Erwartete erfolgreiche Suche: 1 + α/2 = " + String.format("%.2f", 1 + chainTable.loadFactor() / 2));
        System.out.println("Erwartete erfolglose Suche: 1 + α = " + String.format("%.2f", 1 + chainTable.loadFactor()) + "\n");

        // ----------------------------------------------------------
        // Test 3: String-Hashing (Teil 3)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 3: String-Hashing (Horner-Schema)");
        System.out.println("========================================\n");

        String[] strings = {"hello", "world", "hello", "Hello", ""};

        System.out.println("--- Hash-Werte ---");
        for (String s : strings) {
            int h = StringHasher.hash(s);
            System.out.println("hash(\"" + s + "\") = " + h);
        }

        System.out.println("\n--- Gleichheits-Test ---");
        String a = "hello";
        String b = "hello";
        String c = "world";
        String d = "Hello";

        System.out.println("fastEquals(\"hello\", \"hello\") = " + StringHasher.fastEquals(a, b));
        System.out.println("fastEquals(\"hello\", \"world\") = " + StringHasher.fastEquals(a, c));
        System.out.println("fastEquals(\"hello\", \"Hello\") = " + StringHasher.fastEquals(a, d));
        System.out.println("(Groß/Kleinschreibung muss unterschieden werden!)\n");

        // ----------------------------------------------------------
        // Klausur-Frage zum Nachdenken
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  KLAUSUR-FRAGE");
        System.out.println("========================================");
        System.out.println("Warum sollte die Tabellengröße N eine Primzahl sein?");
        System.out.println("Was passiert bei h(k) = k % N mit N = 8 (Zweierpotenz)");
        System.out.println("und Schlüsseln 8, 16, 24, 32?");
        System.out.println("========================================\n");
    }
}
