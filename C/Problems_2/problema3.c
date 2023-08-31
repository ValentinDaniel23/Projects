#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned int (*functions)(unsigned int, unsigned int);  // definirea vectorului de functii

unsigned int Adunare(unsigned int a, unsigned int b) {  // functia de adunare
  unsigned int x = 0;  // initializarea numarului de returnat cu 0
  int i;
  bool tr = 0;  // variabila bool care arata transportul sumei in binar
  for (i = 0; i < 4; i++) {                    // a si b au maxim 4 biti
    if (tr) {                                  // este transport
      if (((a >> i) & 1) == ((b >> i) & 1)) {  // a si b au bitul i egal
        x |= 1 << i;                           // adaug bitul i in x
        tr = 0;                                // scot transportul
        if ((a >> i) & 1) {
          tr = 1;  // bitul i este 1, deci prin adunare avem transport
        }
      }
    } else {                                   // nu este transport
      if (((a >> i) & 1) == ((b >> i) & 1)) {  // a si b au bitul i egal
        if ((1 << i) & a) {                    // bitul i este 1 in a
          tr = 1;  // asadar, bitul i este 1 si la a, si la b -> deci avem
                   // transport
        }
      } else {
        x |= 1 << i;  // neavand transport si bitii diferiti, adaug bitul i
      }
    }
  }

  return x;  // returnez suma dintre a si b
}

unsigned int Interschimbare(unsigned int a, unsigned int b) {  // functia de interschimbare de biti
  int p1 = 3 - (b >> 2),
      p2 = 3 - (b & 3);  // la p1 : b >> 2 reprezinta numarul din primi 2 biti de la
                     // stanga la dreapta din b la p2 : b & 3 reprezinta ultimii
                     // 2 biti de la stanga la dreapta din b la p1 si p2, cum
                     // numerotarea e de le dreapta la stanga, inversam cu 3 - p
  if (((a >> p1) & 1) != ((a >> p2) & 1)) {  // bitii de pe pozitiile p1 si p2 sunt diferiti
    a ^= 1 << p1;         // adaug bitul p1
    a ^= 1 << p2;         // adaug bitul p2
  }

  return a;  // returnez numarul a, cum bitii interschimbati
}

unsigned int Rotatie_stanga(unsigned int a, unsigned int b) {  // functia de rotatie la stanga a bitilor
  unsigned int x = 0;                  // initializarea numarului rezultat
  int i;
  b %= 4;  // stiind ca b este numarul de rotatii, si ca numarul a e definit pe
           // 4 biti, la fiecare 4 rotiri se ajunge de unde a inceput
  for (i = 0; i < 4; i++) {
    int p = (i + b) % 4;  // bitul de pe pozitia i, va avea noua pozitie (i + b) % 4
    if ((1 << i) & a) {  // bitul i este in a
      x |= 1 << p;       // adaug bitul p in x
    }
  }

  return x;  // returnez numarul x, reprezentand numarul a cu b rotatii la
             // stanga a bitilor
}

unsigned int Xor(unsigned int a, unsigned int b) {  // functia de operatie Xor intre numerele a si b
  unsigned int x = 0;  // initializarea numarului rezultat
  int i;
  for (i = 0; i < 4; i++) {
    if (((a >> i) & 1) !=
        ((b >> i) & 1)) {  // bitii i ai numerelor a si b sunt diferiti
      x |= 1 << i;         // adaug bitul i
    }
  }

  return x;  // returnez numarul x, reprezentand suma xor a numerelor a si b
}

int main() {
  unsigned int x, a, b;  // pentru aplicarea optima a operatiilor pe biti, folosim unsigned int
  int n, i, op;
  functions operatie[] = {Adunare, Interschimbare, Rotatie_stanga, Xor};  // vectorul de functii, in ordinea lor in binar

  scanf("%u %u", &n, &x);         // citire
  a = x >> (n * 6) & 15;          // numarul cu care se incep operatiile
  for (i = n - 1; i >= 0; i--) {  // cele n operatii
    op = x >> (i * 6 + 4) & 3;    // tipul de operatie
    b = x >> i * 6 & 15;          // numarul cu care se aplica operatia
    a = operatie[op](a, b);       // se apeleaza functia respectiva lui op, si
                             // rezultatul intra in a pentru operatii viitoare
  }

  printf("%u", a);  // afisare raspuns

  return 1;
}