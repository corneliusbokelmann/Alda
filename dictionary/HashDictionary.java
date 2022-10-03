package dictionary;

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    private int size;
    public LinkedList<Entry<K, V>>[] data;
    private int load;

    HashDictionary(int load) {
        this.load = load;
        data = new LinkedList[load];
        size = 0;
    }

    @Override
    public V insert(K key, V value) {
        ensurecapacity();
        int adr = myHasCode(key);

        if (search(key) != null) {
            for (Entry<K, V> entry : data[adr]) {
                if (entry.getKey().equals(key)) {
                    V val = entry.getValue();
                    entry.setValue(value);
                    return val;
                }
            }
        }


        if (data[adr] == null) {
            data[adr] = new LinkedList<>();
            data[adr].add(new Entry<K, V>(key, value));
            ++size;
        } else {
            data[adr].add(new Entry<K, V>(key, value));
            ++size;
        }
        return null;
    }

    public void ensurecapacity() {
        if (size + 1 == load) {

            int newLoad = 2 * data.length;

            while (!istPrim(newLoad)) {
                ++newLoad;
            }

            HashDictionary<K, V> newDict = new HashDictionary<>(newLoad);

            for (LinkedList<Entry<K, V>> index : data) {
                if (index == null) continue;
                for (Entry<K, V> entry : index) {
                    newDict.insert(entry.getKey(), entry.getValue());
                }
            }
            data = new LinkedList[newLoad];
            load = newLoad;
            for (LinkedList<Entry<K, V>> index : newDict.data) {
                if (index == null) continue;
                for (Entry<K, V> entry : index) {
                    this.insert(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private int myHasCode(K k) {
        String key;
        if (!(k instanceof String)) key = String.valueOf(k);
        else key = (String) k;
        int adr = 0;
        for (int i = 0; i < key.length(); i++)
            adr = 31 * adr + key.charAt(i);
        if (adr < 0)
            adr = -adr;
        return adr % (data.length - 1);
    }

    @Override
    public V search(K key) {
        int adr = myHasCode(key);
        if (data[adr] != null) {
            for (Entry<K, V> entry : data[adr]) {
                if (entry.getKey().equals(key)) return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int adr = myHasCode(key);
        for (Entry<K, V> entry : data[adr]) {
            if (entry.getKey().equals(key)) {
                V old = entry.getValue();
                data[adr].remove(entry);
                return old;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    static boolean istPrim(int n) {
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) // i ist Teiler von n
                return false;
        }
        return true;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {

            int index = -1;
            Iterator<Entry<K, V>> listIterator;

            @Override
            public boolean hasNext() {
                if (listIterator != null && listIterator.hasNext())
                    return true;
                while (++index < data.length) {
                    if (data[index] != null) {
                        listIterator = data[index].iterator();
                        return listIterator.hasNext();
                    }
                }
                return false;
            }

            @Override
            public Entry<K, V> next() {
                return listIterator.next();
            }
        };
    }
}