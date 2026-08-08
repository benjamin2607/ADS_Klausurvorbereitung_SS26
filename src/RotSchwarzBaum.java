import java.util.Stack;

public class RotSchwarzBaum {

    // ============================================================
    // Farbe und Knoten
    // ============================================================

    /**
     * Farben für Rot-Schwarz-Bäume.
     * Rote Knoten sind implizit durch Color.RED definiert.
     * Schwarze Knoten (Color.BLACK) sind der Standard.
     */
    enum Color {
        RED, BLACK
    }

    /**
     * Rot-Schwarz-Knoten — erweiterte BSTNode mit Farbe und Parent-Referenz.
     * 
     * Parent-Referenz ist nötig für:
     * - Rotationen (muss Parent-Referenz des verschobenen Knotens aktualisieren)
     * - Löschen (muss Fallunterscheidung nach Onkel-Position treffen)
     * - Inkrement-Iterator (muss vom Blatt zurück zur Wurzel wandern)
     */
    public static class RBNode {
        int key;
        Color color;
        RBNode left;
        RBNode right;
        RBNode parent;  // Vor BST: nicht nötig, hier aber erforderlich

        public RBNode(int key, Color color) {
            this.key = key;
            this.color = color;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    // ============================================================
    // Teil 1: Rotationen — Die Grundbausteine aller RBT-Operationen
    // ============================================================

    /**
     * Rechts-Rotation um den Knoten y.
     * 
     * Struktur vor der Rotation:
     * 
     *        y                  x
     *       / \               /   \
     *      x   C    →       A     y
     *     / \                  / \
     *    A   B                B   C
     * 
     * x rückt nach oben, y rutscht nach rechts.
     * A bleibt links von x, B wechselt als rechtes Kind von y, C bleibt rechts von y.
     * 
     * Klausur-Fallstrick: Parent-Referenzen MÜSSEN aktualisiert werden, sonst
     * ist der Baum an der Wurzel der Rotation kaputt!
     */
    public static RBNode rotateRight(RBNode y) {
        // TODO: Implementiere Rechts-Rotation
        // Hinweis:
        // 1. x = y.left speichern
        // 2. y.left = x.right (B wird linkes Kind von y)
        // 3. Wenn x.right != null: x.right.parent = y (Parent von B aktualisieren)
        // 4. x.parent = y.parent (x übernimmt y's Parent-Beziehung)
        // 5. Wenn y.parent == null: x wird neue Wurzel
        // 6. Sonst: je nachdem ob y linkes oder rechtes Kind war, x einhängen
        // 7. x.right = y; y.parent = x
        // 8. return x (neue lokale Wurzel der Rotation)
        return null;
    }

    /**
     * Links-Rotation um den Knoten x.
     * 
     * Struktur vor der Rotation:
     * 
     *      x                    y
     *     / \                 /   \
     *    A   y       →      x     C
     *       / \            / \
     *      B   C          A   B
     * 
     * y rückt nach oben, x rutscht nach links.
     * Spiegelbild zur Rechts-Rotation.
     */
    public static RBNode rotateLeft(RBNode x) {
        // TODO: Implementiere Links-Rotation (Spiegelbild von rotateRight)
        // 1. y = x.right speichern
        // 2. x.right = y.left (B wird rechtes Kind von x)
        // 3. Wenn y.left != null: y.left.parent = x
        // 4. y.parent = x.parent
        // 5. Wenn x.parent == null: y wird neue Wurzel
        // 6. Sonst: je nachdem ob x linkes/rechtes Kind war, y einhängen
        // 7. y.left = x; x.parent = y
        // 8. return y
        return null;
    }

    /**
     * Hilfsmethode: Prüft, ob ein Knoten rot ist.
     * Null-Knoten werden als schwarz behandelt (RBT-Eigenschaft).
     */
    private static boolean isRed(RBNode node) {
        // TODO: Implementiere
        // null-Knoten sind IMMER schwarz → false zurückgeben
        return false;
    }

    // ============================================================
    // Teil 2: Einfügen — Top-Down mit Color-Flip
    // ============================================================

    /**
     * Fügt einen key in den RBT ein.
     * 
     * Einfüge-Algorithmus (Top-Down, nach Sedgewick):
     * 
     * 1. Normale BST-Einfügeung — neuer Knoten ist ROT.
     * 2. Während des Abstiegs von der Wurzel:
     *    - Wenn rechter Kind ROT UND linkes Kind ROT → Color-Flip
     *    - Wenn linkes Kind ROT UND linkes-linkes Kind ROT → Rechts-Rotation
     *    - Wenn linkes Kind ROT UND linkes-rechtes Kind ROT → Links-Rotation dann Rechts-Rotation
     * 3. Wurzel am Ende immer Schwarz färben.
     * 
     * Die drei Reorganisations-Fälle beim Einfügen:
     * 
     * Fall 1 (Onkel rot): Color-Flip
     *    P(rot) U(rot) G(black) → P schwarz, U schwarz, G rot
     *    G rot propagiert das Problem eine Ebene nach oben.
     * 
     * Fall 2a (Onkel schwarz, äußeres Kind): Einfache Rotation
     *    G(black) P(rot) UU(rot) → G schwarz, P rot, dann Rotation
     * 
     * Fall 2c (Onkel schwarz, inneres Kind): Doppel-Rotation
     *    G(black) P(rot) UU(rot) → Rotation, dann Color-Flip
     * 
     * Klausur-Fallstrick: Bei Top-Down-Einfügen werden ALLE 4er-Knoten auf dem
     * Weg nach unten SOFORT gesplittet (Color-Flip), nicht erst beim Einfügen!
     * Das garantiert, dass beim Erreichen des Blattes der Vater immer Platz hat.
     */
    public static RBNode insert(RBNode root, int key) {
        // TODO: Implementiere Top-Down Einfüge-Algorithmus
        // 
        // Ansatz (Sedgewick, 2008):
        // - Neuer Knoten wird als roter Blatt-Knoten eingefügt
        // - Während des Abstiegs: 4er-Knoten sofort splitten
        // - Am Ende: Wurzel schwarz färben
        //
        // Wichtiger Trick: Der neue Knoten wird als "temporärer linker Kind"-Knoten
        // angehängt. Die Rotationen während des Abstiegs reparieren den Baum
        // ON-THE-FLY, so dass am Ende keine zweite Pass nötig ist.
        
        return null;
    }

    /**
     * Hilfsmethode: Color-Flip (für Fall 1 beim Einfügen).
     * 
     * Flippt die Farben eines 4er-Knotens (Parent + beide Kinder werden rot)
     * und macht den Parent schwarz.
     * 
     *        G(black)              G(red)
     *       /           →        /      \
     *    P(red)   U(red)     P(red)   U(red)
     */
    private static void flipColors(RBNode node) {
        // TODO: Implementiere Color-Flip
        // 1. node.color umkehren (black → red, red → black)
        // 2. node.left.color umkehren
        // 3. node.right.color umkehren
    }

    // ============================================================
    // Teil 3: Suchen
    // ============================================================

    /**
     * Sucht einen key im RBT (identisch zu BST-Suche, da RBT ein BST ist).
     * 
     * Laufzeit: O(log n) garantiert (im Gegensatz zu BST mit O(n) Worst-Case),
     * da die RBT-Eigenschaften eine Degeneration verhindern.
     */
    public static boolean contains(RBNode root, int key) {
        // TODO: Implementiere BST-Suche
        // Hinweis: Identisch zu BST.contains() — RBT ist ein BST mit zusätzlichen Eigenschaften
        return false;
    }

    // ============================================================
    // Teil 4: Traversierung und Ausgabe
    // ============================================================

    /**
     * In-Order-Traversierung mit Farbinformation.
     * 
     * Ausgabeformat: "Key(Color)" — z.B. "30(BLACK)" oder "20(RED)"
     * 
     * Die In-Order-Traversierung eines RBTs ergibt IMMER sortierte Ausgabe,
     * da ein RBT per Definition ein Binärer Suchbaum ist.
     */
    public static void inOrderWithColor(RBNode node) {
        // TODO: Implementiere In-Order mit Farbausgabe
        // Ausgabe: "Key(Color)" — z.B. "30(BLACK)"
    }

    /**
     * Zeichnet den Baum visuell mit Farbinformation.
     * Rote Knoten werden mit [R] markiert, schwarze mit [B].
     */
    public static void printTree(RBNode node, String prefix, boolean isLeft) {
        if (node == null) return;

        printTree(node.right, prefix + (isLeft ? "│   " : "    "), true);
        String colorTag = node.color == Color.BLACK ? "[B]" : "[R]";
        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.key + colorTag);
        printTree(node.left, prefix + (isLeft ? "    " : "│   "), false);
    }

    // ============================================================
    // Teil 5: RBT-Eigenschaften validieren (Klausur-Transfer)
    // ============================================================

    /**
     * Prüft alle fünf RBT-Eigenschaften.
     * 
     * Die fünf Eigenschaften:
     * 1. Jeder Knoten ist entweder rot oder schwarz.
     * 2. Die Wurzel ist schwarz.
     * 3. Alle Blatt-Knoten (null) sind schwarz.
     * 4. Wenn ein Knoten rot ist, müssen beide Kinder schwarz sein.
     *    (Keine zwei roten Knoten hintereinander.)
     * 5. Alle Pfade von einem Knoten zu seinen Blatt-Nachkommen enthalten
     *    dieselbe Anzahl schwarzer Knoten (Black-Height uniform).
     * 
     * Klausur-Frage: Warum garantiert Eigenschaft 5, dass der Baum O(log n) Höhe hat?
     * Tipp: Der längste mögliche Pfad (abwechselnd rot/schwarz) ist maximal doppelt
     * so lang wie der kürzeste (alle schwarz).
     */
    public static boolean validateRBProperties(RBNode root) {
        // TODO: Implementiere Validierung
        // 
        // Eigenschaft 2: Wurzel muss schwarz sein
        // Eigenschaft 4: Keine zwei roten Knoten hintereinander
        // Eigenschaft 5: Black-Height muss uniform sein
        //
        // Hinweis: Eigenschaft 1 und 3 sind durch die Implementierung garantiert.
        
        return false;
    }

    /**
     * Berechnet die Black-Height eines Knotens.
     * Black-Height = Anzahl schwarzer Knoten (ohne den Knoten selbst) auf dem Pfad
     * zu einem Blatt.
     * 
     * Gibt -1 zurück, wenn die Black-Height nicht uniform ist.
     */
    private static int blackHeight(RBNode node) {
        // TODO: Implementiere Black-Height-Berechnung
        // null → 0 (Blatt, zählt als schwarzer Knoten → aber 0 für die Höhe)
        // Bei Inkonsistenz → -1 zurückgeben
        return 0;
    }

    // ============================================================
    // Hauptmethode — Test-Runner
    // ============================================================

    public static void main(String[] args) {

        // ----------------------------------------------------------
        // Test 1: Rotationen (Teil 1)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 1: Rotationen");
        System.out.println("========================================\n");

        // Erstelle einen kleinen Baum für Rotationstest:
        //    30(B)
        //   /
        //  20(B)
        // /
        // 10(B)
        RBNode n10 = new RBNode(10, Color.BLACK);
        RBNode n20 = new RBNode(20, Color.BLACK);
        RBNode n30 = new RBNode(30, Color.BLACK);
        n20.left = n10; n10.parent = n20;
        n30.left = n20; n20.parent = n30;

        System.out.println("Baum vor Rechts-Rotation um 30:");
        printTree(n30, "", false);
        System.out.println();

        // TODO: rotateRight(n30) ausführen und Ergebnis prüfen
        // Erwartet: 20 wird neue Wurzel, 30 rechtes Kind, 10 linkes Kind von 20
        System.out.println("Baum nach rotateRight(30):");
        // RBNode rotated = rotateRight(n30);
        // printTree(rotated, "", false);
        System.out.println("(Implementiere rotateRight zuerst!)\n");

        // ----------------------------------------------------------
        // Test 2: Einfügen (Teil 2)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 2: Einfügen in RBT");
        System.out.println("========================================\n");

        RBNode root = null;
        
        // Einfüge-Sequenz: 10, 20, 30, 40, 50
        // Erwartetes Verhalten:
        // - insert(10): 10[B]
        // - insert(20): 10[B] ← 20[R]  (kein Konflikt)
        // - insert(30): Color-Flip → 10[R] mit 20[B] als Vater, 10[R] und 30[R] als Kinder
        //                dann Wurzel-Fix → 20[B] mit 10[B] und 30[B]
        // - insert(40): ...
        // - insert(50): ...

        int[] keys = {10, 20, 30, 40, 50};

        System.out.println("--- Einfüge-Sequenz ---");
        for (int key : keys) {
            root = insert(root, key);
            System.out.println("\nNach Einfügen von " + key + ":");
            inOrderWithColor(root);
            System.out.println();
            printTree(root, "", false);
        }

        // ----------------------------------------------------------
        // Test 3: Validierung (Teil 5)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 3: RBT-Eigenschaften validieren");
        System.out.println("========================================\n");

        boolean valid = validateRBProperties(root);
        System.out.println("Alle RBT-Eigenschaften erfüllt: " + valid);
        System.out.println("(Erwartet: true, wenn insert() korrekt implementiert ist)\n");

        // ----------------------------------------------------------
        // Test 4: Suche (Teil 3)
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  TEST 4: Suche");
        System.out.println("========================================\n");

        int[] searchKeys = {10, 25, 50, 99};
        for (int key : searchKeys) {
            boolean found = contains(root, key);
            System.out.println("contains(" + key + ") = " + found);
        }
        System.out.println();

        // ----------------------------------------------------------
        // Klausur-Fragen zum Nachdenken
        // ----------------------------------------------------------
        System.out.println("========================================");
        System.out.println("  KLAUSUR-FRAGEN");
        System.out.println("========================================");
        System.out.println("1. Warum muss die Wurzel IMMER schwarz sein?");
        System.out.println("   (Kann sie auch rot sein?)");
        System.out.println();
        System.out.println("2. Was passiert beim Einfügen von 10, 20, 30, 40, 50");
        System.out.println("   in einen initially leeren RBT? Zeichne jeden Schritt!");
        System.out.println();
        System.out.println("3. Warum ist die maximale Höhe eines RBT mit n Knoten");
        System.out.println("   höchstens 2 * log2(n+1)?");
        System.out.println("========================================\n");
    }
}
