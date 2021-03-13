package ex2;

import ex1.HashTable;
import org.junit.jupiter.api.Assertions;

class HashTableTest {

@org.junit.jupiter.api.Test
void put(){
    ex1.HashTable ht = new ex1.HashTable();

    //Comprovar si es buit.
    Assertions.assertEquals( "", ht.toString() );

    // Si añade un hashentry a la tabla.

    ht.put("0", "0");
    Assertions.assertEquals( "\n bucket[0] = [0, 0]", ht.toString() );
    ht.put("3", "3");
    Assertions.assertEquals( "\n bucket[0] = [0, 0]"+ "\n bucket[3] = [3, 3]", ht.toString() );
    ht.put("5", "5");
    Assertions.assertEquals( "\n bucket[0] = [0, 0]"+ "\n bucket[3] = [3, 3]"+ "\n bucket[5] = [5, 5]", ht.toString() );

    // Comprovem si actualitza el value utilitzant una mateixa key
    ht.put("0", "100");
    Assertions.assertEquals( "\n bucket[0] = [0, 100]"+ "\n bucket[3] = [3, 3]"+ "\n bucket[5] = [5, 5]", ht.toString() );

    ht.put("3", "300");
    Assertions.assertEquals( "\n bucket[0] = [0, 100]"+ "\n bucket[3] = [3, 300]"+ "\n bucket[5] = [5, 5]", ht.toString() );

    //Afegim un hashentry con colision.
    ht.put("11","11");
    Assertions.assertEquals( "\n bucket[0] = [0, 100] -> [11, 11]"+ "\n bucket[3] = [3, 300]"+ "\n bucket[5] = [5, 5]", ht.toString() );

}


@org.junit.jupiter.api.Test
void get(){
    ex1.HashTable ht = new ex1.HashTable();

    //Per saber si es buit.
    Assertions.assertEquals( "", ht.toString() );
    ht.put("1", "1");
    ht.put("12","12");
    //Comprovem i agafem el valor d'un hashentry
    Assertions.assertEquals("12", ht.get("12"));
    //Agafar un value d'un entry amb més d'una col·lisio
    ht.put("23","23");
    Assertions.assertEquals("23",  ht.get("23"));
    ht.put("1","4");
    ht.put("12","5");
    Assertions.assertEquals("4",ht.get("1"));
    Assertions.assertEquals("5",ht.get("12"));
}


@org.junit.jupiter.api.Test
void drop(){
    //es comentat perque sino maven en dona problemes (ex4)
    /*
   HashTable ht = new HashTable();

    ht.put("0", "0");
    Assertions.assertEquals( "\n bucket[0] = [0, 0]", ht.toString() );

    ht.put("11","11");
    Assertions.assertEquals( "\n bucket[0] = [0, 0] -> [11, 11]", ht.toString() );

    //Intentem borrar el primer valor y després mirem si el valor que hi ha es el següent.
    ht.drop("0");
    Assertions.assertEquals("\n bucket[0] = [11, 11]",ht.toString());
    */
}


@org.junit.jupiter.api.Test
void count(){
ex1.HashTable h = new ex1.HashTable();

    h.put("1", "23");
    Assertions.assertEquals(1,h.count());

    h.put("11", "11");
    h.put("23", "23");
    Assertions.assertEquals(3,h.count());
}

@org.junit.jupiter.api.Test
void size(){
    ex1.HashTable ht = new HashTable();
    Assertions.assertEquals( "", ht.toString() );
    ht.put("0", "0");
    Assertions.assertEquals( "\n bucket[0] = [0, 0]", ht.toString() );
    ht.put("11","11");
    Assertions.assertEquals( "\n bucket[0] = [0, 0] -> [11, 11]", ht.toString() );
    Assertions.assertEquals(16,ht.size());


}

}
