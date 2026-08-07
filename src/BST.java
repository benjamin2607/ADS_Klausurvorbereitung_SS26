 public class BST {

    // ============================================================
    // Knoten-Klasse
    // ============================================================
    public static class BSTNode {
        int key;
        BSTNode left;
        BSTNode right;

        public BSTNode(int key) {
            this.key = key;
            this.left = null;
            this.right = null;
        }
    }

    // ============================================================
    // Teil a) — Einfügen
    // ============================================================

    /**
     * Fügt einen neuen Knoten mit dem gegebenen key in den BST ein.
     * BST-Eigenschaft: linke Kinder < Parent < rechte Kinder
     * Keine Duplikate erlaubt.
     * 
     * Bisheriger Fehler: root == null hat null zurückgegeben → Baum blieb leer.
     * Bisheriger Fehler: Rekursive Ergebnisse wurden keinen Kinder-Referenzen zugewiesen.
     * Bisheriger Fehler: Duplikate wurden nicht explizit abgefangen.
     */
    public static BSTNode insert(BSTNode root, int key) {
        // Neuer Knoten — hier wird die Rekursion gestoppt und ein Knoten erstellt
        if (root == null) {
            return new BSTNode(key);  // Vorher: return null → kein Knoten erstellt
        }
        // Duplikat — nicht erlaubt in BST
        if (key == root.key) {
            return root;
        }
        // Kleiner als aktueller Key → nach links
        if (key < root.key) {
            root.left = insert(root.left, key);  // Vorher: Ergebnis nicht zugewiesen → Knoten verloren
        }
        // Größer als aktueller Key → nach rechts
        else {
            root.right = insert(root.right, key);  // Vorher: Ergebnis nicht zugewiesen → Knoten verloren
        }
        return root;  // Original-Wurzel (mit evtl. neuem Kind) zurückgeben
    }

    // ============================================================
    // Teil b1) — Suchen
    // ============================================================

    /**
     * Prüft, ob ein key im BST vorhanden ist.
     * 
     * Bisheriger Fehler: Blatt-Knoten wurden pauschal als "nicht gefunden" markiert.
     * Bisheriger Fehler: Suchrichtung war vertauscht — kleinerer Key ging nach rechts.
     */
    public static boolean contains(BSTNode root, int key) {
        if (root == null) {
            return false;
        }
        // Treffer — muss VOR dem Blatt-Check kommen
        if (root.key == key) {
            return true;
        }
        // Vorher: root.left → falsch, Key ist größer als root.key → nach rechts
        if (key < root.key) {
            return contains(root.left, key);
        } else {
            return contains(root.right, key);
        }
    }

    // ============================================================
    // Teil b2) — Löschen
    // ============================================================

    /**
     * Hilfsmethode: Findet den Knoten mit dem maximalen key
     * in einem BST (ganz nach rechts gehen).
     */
    private static int findMax(BSTNode node) {
        if (node.right != null){
            return findMax(node.right);
        }
        return node.key;
    }

    /**
     * Löscht einen Knoten mit dem gegebenen key aus dem BST.
     * 
     * Fall 1: Blatt → Referenz auf null setzen
     * Fall 2: Ein Kind → Kind rückt nach
     * Fall 3: Zwei Kinder → symmetrischer Vorgänger (Maximum des linken Teilbaums)
     *         ersetzen, dann Vorgänger löschen (fällt auf Fall 1 oder 2)
     * 
     * Bisheriger Fehler: Nur Key-Werte überschrieben, aber Baumstruktur (Zeiger) nicht verändert.
     * Bisheriger Fehler: Keine rekursive Suche nach dem zu löschenden Knoten.
     * Bisheriger Fehler: Alle Fälle wurden parallel mit if geprüft statt exklusiv.
     */
    public static BSTNode delete(BSTNode root, int key) {
        // BST-Suche: Zum zu löschenden Knoten wandern
        if (root == null) {
            return null;
        }
        if (key < root.key) {
            root.left = delete(root.left, key);  // Vorher: Ergebnis nicht zugewiesen
            return root;
        } else if (key > root.key) {
            root.right = delete(root.right, key);  // Vorher: Ergebnis nicht zugewiesen
            return root;
        }
        
        // === Key gefunden — jetzt Fallunterscheidung ===
        
        // Fall 1: Blatt (kein linkes, kein rechtes Kind)
        if (root.left == null && root.right == null) {
            return null;  // Vorher: root.key = 0 → Key nur gelöscht, Knoten blieb im Baum
        }
        
        // Fall 2a: Nur linkes Kind
        if (root.left != null && root.right == null) {
            return root.left;  // Vorher: root.key = root.left.key → alter Knoten blieb erhalten
        }
        
        // Fall 2b: Nur rechtes Kind
        if (root.left == null && root.right != null) {
            return root.right;  // Vorher: root.key = root.right.key → alter Knoten blieb erhalten
        }
        
        // Fall 3: Beide Kinder vorhanden
        // Symmetrischen Vorgänger finden (Maximum des linken Teilbaums)
        int predecessorKey = findMax(root.left);
        root.key = predecessorKey;  // Key ersetzen
        root.left = delete(root.left, predecessorKey);  // Vorgänger-Knoten löschen (fällt auf Fall 1 oder 2)
        return root;
    }

    // ============================================================
    // Teil c) — Traversierungen
    // ============================================================

    /**
     * Pre-Order: Eltern → Links → Rechts
     */
    public static void preOrder(BSTNode node) {
        if (node == null){return;}
        System.out.println("Key: " + node.key);
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * In-Order: Links → Eltern → Rechts
     * (sollte sortierte Ausgabe ergeben)
     */
    public static void inOrder(BSTNode node) {
        if (node == null){return;}
        inOrder(node.left);
        System.out.println("Key: " + node.key);
        inOrder(node.right);
    }

    /**
     * Post-Order: Links → Rechts → Eltern
     * 
     * Bisheriger Fehler: preOrder() statt postOrder() aufgerufen.
     */
    public static void postOrder(BSTNode node) {
        if (node == null) { return; }
        postOrder(node.left);    // Vorher: preOrder(node.left)
        postOrder(node.right);   // Vorher: preOrder(node.right)
        System.out.println("Key: " + node.key);
    }

    // ============================================================
    // Hilfs-Methode: Baum zeichnen (für Debugging)
    // ============================================================

    /**
     * Zeichnet den Baum visuell in die Konsole (rotiert 90° gegen den Uhrzeigersinn).
     */
    public static void printTree(BSTNode node, String prefix, boolean isLeft) {
        if (node == null) return;

        printTree(node.right, prefix + (isLeft ? "│   " : "    "), true);
        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.key);
        printTree(node.left, prefix + (isLeft ? "    " : "│   "), false);
    }

    // ============================================================
    // Hauptmethode — Test-Runner
    // ============================================================

    public static void main(String[] args) {

        // ----------------------------------------------------------
        // Test-Sequenz aus Teil d):
        // insert(50), insert(30), insert(70), insert(20), insert(40), insert(60), insert(80)
        // ----------------------------------------------------------
        BSTNode root = null;

        int[] keys = {50, 30, 70, 20, 40, 60, 80};

        System.out.println("=== Einfüge-Test (Teil a & d) ===");
        for (int key : keys) {
            root = insert(root, key);
            System.out.println("Eingefügt: " + key);
            printTree(root, "", false);
            System.out.println();
        }

        // ----------------------------------------------------------
        // Test: Suchen (Teil b1)
        // ----------------------------------------------------------
        System.out.println("=== Suchen-Test (Teil b1) ===");
        int[] searchKeys = {30, 25, 80, 10};
        for (int key : searchKeys) {
            boolean found = contains(root, key);
            System.out.println("contains(" + key + ") = " + found);
        }

        // ----------------------------------------------------------
        // Test: Traversierungen (Teil c)
        // ----------------------------------------------------------
        System.out.println("\n=== Traversierungs-Test (Teil c) ===");
        System.out.print("Pre-Order : ");
        preOrder(root);
        System.out.println();

        System.out.print("In-Order  : ");
        inOrder(root);
        System.out.println();

        System.out.print("Post-Order: ");
        postOrder(root);
        System.out.println();

        // ----------------------------------------------------------
        // Test: Löschen (Teil b2)
        // ----------------------------------------------------------
        System.out.println("\n=== Lösch-Test (Teil b2) ===");

        // Fall 1: Blatt löschen (z.B. 20)
        System.out.println("--- Fall 1: Blatt löschen (20) ---");
        root = delete(root, 20);
        printTree(root, "", false);
        System.out.println("In-Order nach delete(20): ");
        inOrder(root);
        System.out.println();

        // Fall 2: Ein Kind löschen (z.B. 30, nachdem 20 gelöscht wurde)
        System.out.println("--- Fall 2: Ein Kind löschen (30) ---");
        root = delete(root, 30);
        printTree(root, "", false);
        System.out.println("In-Order nach delete(30): ");
        inOrder(root);
        System.out.println();

        // Fall 3: Zwei Kinder löschen (z.B. 50, die ursprüngliche Wurzel)
        System.out.println("--- Fall 3: Zwei Kinder löschen (50) ---");
        root = delete(root, 50);
        printTree(root, "", false);
        System.out.println("In-Order nach delete(50): ");
        inOrder(root);
        System.out.println();

        // ----------------------------------------------------------
        // Abschluss
        // ----------------------------------------------------------
        System.out.println("=== Alle Tests abgeschlossen ===");
    }
}
