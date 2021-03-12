package ex3;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

import java.util.ArrayList;

/**
 * Implementació d'una taula de hash sense col·lisions.
 * Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
 */

public class HashTable {
    private int SIZE = 16;
    private int ITEMS = 0;
    private HashEntry[] entries = new HashEntry[SIZE];

    // error: en el put y el drop es necessari posar un ITEM++ o ITEM-- sino no compta res
    public int count(){
        return this.ITEMS;
    }

    public int size(){
        return this.SIZE;
    }

    /**
     * Permet afegir un nou element a la taula.
     * @param key La clau de l'element a afegir.
     * @param value El propi element que es vol afegir.
     */
    public void put(String key, String value) {
        int hash = getHash(key);
        final HashEntry hashEntry = new HashEntry(key, value);

        if(entries[hash] == null) {
            entries[hash] = hashEntry;

        }
        else {
            //Error: Comprovem si es diferent de null i després si es la mateixa key y si dona correcte, cambia el value
            HashEntry temp = entries[hash];
            while (temp != null) {
                if (temp.key.equals(key)) {
                    temp.value = value;
                    return;
                } else {
                    temp = temp.next;
                }
            }
            temp = entries[hash];
            while (temp.next != null)
                temp = temp.next;
                temp.next = hashEntry;

        }
        //error: he añadido esto porque sino el count no sirve de nada
        ITEMS++;
    }

    /**
     * Permet recuperar un element dins la taula.
     * @param key La clau de l'element a trobar.
     * @return El propi element que es busca (null si no s'ha trobat).
     */
    public String get(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {
            //error: ha de comprovar si es nulo porque si pasa al siguiente y no existe salta nullpointer
            HashEntry temp = entries[hash];
            temp = getHashEntry(key, temp);
            //per que retorni el valor de manera correcta si ho troba sino retorna un null
            if(temp.key.equals(key)){
                //fem un to string perque ara value es object
                return  temp.value.toString();
            }
            else{
                return null;
            }
        }
        return null;
    }

    /**
     * Permet esborrar un element dins de la taula.
     * @param key La clau de l'element a trobar.
     */

    public void drop(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {
            //error: el mismo que en el get
            HashEntry temp = entries[hash];
            temp = getHashEntry(key, temp);

            if(temp.prev == null && temp.next == null) entries[hash] = null;             //esborrar element únic (no col·lissió)
            else{
                if(temp.next != null) temp.next.prev = temp.prev;   //esborrem temp, per tant actualitzem l'anterior al següent
                temp.prev.next = temp.next;                         //esborrem temp, per tant actualitzem el següent de l'anterior
            }
        }
        //Similar al put pero en vez sumar restar para que el count sirva
        ITEMS--;
    }

    //REFACCIÓ: busca la key que dona temp y si no pasa a la següent fins al final de hash ho trobi o no (aixo es dona al drop y el get)
    private HashEntry getHashEntry(String key, HashEntry temp) {
        while (!temp.key.equals(key) && temp.next != null)
            temp = temp.next;
        return temp;
    }

    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return key.hashCode() % SIZE;
    }

    private class HashEntry {
        String key;
        Object value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    @Override
    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (HashEntry entry : entries) {
            if(entry == null) {
                bucket++;
                continue;
            }

            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            HashEntry temp = entry.next;
            while(temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @return Una clau que, de fer-se servir, provoca col·lisió amb la que s'ha donat.
     */
    public String getCollisionsForKey(String key) {
        return getCollisionsForKey(key, 1).get(0);
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @param quantity La quantitat de col·lisions a calcular.
     * @return Un llistat de claus que, de fer-se servir, provoquen col·lisió.
     */
    public ArrayList<String> getCollisionsForKey(String key, int quantity){
        /*
          Main idea:
          alphabet = {0, 1, 2}

          Step 1: "000"
          Step 2: "001"
          Step 3: "002"
          Step 4: "010"
          Step 5: "011"
           ...
          Step N: "222"

          All those keys will be hashed and checking if collides with the given one.
        * */

        final char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> newKey = new ArrayList();
        ArrayList<String> foundKeys = new ArrayList();

        newKey.add(0);
        int collision = getHash(key);
        int current = newKey.size() -1;

        while (foundKeys.size() < quantity){
            //building current key
            String currentKey = "";
            for(int i = 0; i < newKey.size(); i++)
                currentKey += alphabet[newKey.get(i)];

            if(!currentKey.equals(key) && getHash(currentKey) == collision)
                foundKeys.add(currentKey);

            //increasing the current alphabet key
            newKey.set(current, newKey.get(current)+1);

            //overflow over the alphabet on current!
            if(newKey.get(current) == alphabet.length){
                int previous = current;
                do{
                    //increasing the previous to current alphabet key
                    previous--;
                    if(previous >= 0)  newKey.set(previous, newKey.get(previous) + 1);
                }
                while (previous >= 0 && newKey.get(previous) == alphabet.length);

                //cleaning
                for(int i = previous + 1; i < newKey.size(); i++)
                    newKey.set(i, 0);

                //increasing size on underflow over the key size
                if(previous < 0) newKey.add(0);

                current = newKey.size() -1;
            }
        }

        return  foundKeys;
    }

}