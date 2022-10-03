package dictionary;

import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 4444;
    private int size;
    public Entry<K, V>[] data;

    SortedArrayDictionary() {
        data = new Entry[DEF_CAPACITY];
        size = 0;
    }


    @Override
    public V insert(K key, V value) {

        int i = searchKey(key);// Vorhandener Eintrag wird überschrieben:
        if (i != -1) {
            V r = data[i].getValue();
            data[i].setValue(value);
            return r;
        }// Neueintrag:
        if (data.length == size) {
            ensureCapacity(2 * size);
        }
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = new Entry<K, V>(key, value);
        size++;
        return null;
    }

    @Override
    public V search(K key) {
        int intKey = searchKey(key);
        if (intKey != -1) return data[intKey].getValue();
        return null;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1) return null;

        // Datensatz loeschen und Lücke schließen
        V r = data[i].getValue();
        for (int j = i; j < size - 1; j++) data[j] = data[j + 1];
        data[size--] = null;
        return r;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return data[index + 1] != null;
            }

            @Override
            public Entry<K, V> next() {
                return data[index++];
            }
        };
    }


    private int searchKey(K key) {
        int li = 0;
        int re = size - 1;
        while (re >= li) {
            int m = (li + re) / 2;
            if (key.compareTo(data[m].getKey()) < 0) re = m - 1;
            else if (key.compareTo(data[m].getKey()) > 0) li = m + 1;
            else return m; // key gefunden
        }
        return -1; // key nicht gefunden
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int newCapacity) {
        if (newCapacity < size) return;
        Entry[] old = data;
        data = new Entry[newCapacity];
        System.arraycopy(old, 0, data, 0, size);
    }

    public Entry<K, V>[] getData() {
        return data;
    }
}
