#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int words = 15;  // sunt 15 cuvite/sintagme folosite in tabloul keys

void Read(int n, char **s) {  // functia de citire
  int i;

  for (i = 0; i < n; i++) {
    char line[101];
    scanf("%[^\n]\n", line);  // citire linie
    s[i] = (char *)malloc((strlen(line) + 1) * sizeof(char));  // alocare de memorie cat este necesara
    strcpy(s[i], line);
  }
}

void Solve(int n, char **s, char keys[][9]) {  // functia de rezolvare
  int i;
  for (i = 0; i < n; i++) {
    char *output, aux[101];  // output este linia respectiva liniei i, in care
                             // sunt prezente underline-urile aux este un char
                             // ajutator pentru cuvinte
    int lst = -1, j, z, k, dim = strlen(s[i]);  // j, z si k sunt indici ajutatori
                             // lst este pozitia in i de unde incepe cuvantul
                             // dinaintea celui prezent, practic retin ultimile
                             // 2 cuvinte initializez lst cu -1, semn ce arata
                             // ca nu am completat un cuvant inca

    output = (char *)malloc((dim + 1) * sizeof(char));  // output primeste dimensiunea liniei i

    for (j = 0; j < dim; j++) {
      output[j] = ' ';  // initializare output cu spatii
    }
    output[dim] = '\0';  // terminator in output

    for (j = 0; j < dim; j++) {
      if (s[i][j] != ' ') {  // daca caracterul j nu e spatiu, inseamna ca e
                             // inceputul unui cuvant
        int start = j, cnt = 0, poz;  // inceputul cuvantului j, il pun in start
                                      // cnt este numarul de caractere care il
                                      // va avea cuvantul cand trec prin el

        if (lst != -1) {  // daca exista un cuvant precedent, il parcurg,
                          // introducandu l in aux
          for (z = lst;; z++) {
            aux[cnt++] = s[i][z];  // evidenta caracterelor o am cu ajutorul variabilei cnt
            if (s[i][z] == ' ') break;  // fiind un for fara conditie, ne folosim de faptul ca
                      // ajung la un moment dat la spatiu si dau break
          }
        }

        poz = cnt;  // in variabila poz, pun unde incepe al doilea cuvant, adica
                    // cel prezent
        for (; j < dim && s[i][j] != ' '; j++) {  // cresc simultan variabila lui j, cand trec prin cuvantul
                     // prezent
          aux[cnt++] = s[i][j];  // formez cuvantul prezent
        }
        aux[cnt] = '\0';  // adaug terminator

        for (z = 0; z < words; z++) {  // trec prin tabloul keys
          if (lst != -1 && strcmp(aux, keys[z]) == 0) {  // daca era un cuvant precedent, atunci exista sansa ca
                        // aux sa se potriveasca cu o sintagma de 2 cuvinte
            for (k = lst; k < j; k++) {
              output[k] = '_';  // daca s a potrivit, introduc in output
            }
          } else if (strcmp(aux + poz, keys[z]) == 0) {  // verific acum doar cuvantul actual daca se
                           // potriveste cu sintagma de pe pozitia z din keys
            for (k = start; k < j; k++) {
              output[k] = '_';  // daca s a potrivit, introduc in output
            }
          }
        }

        lst = start;  // cuvantul gasit, va deveni un cuvant trecut si modific lst
      }
    }
    printf("%s\n", s[i]);    // afisez linia initiala
    printf("%s\n", output);  // afisez linia ceruta
  }
}

int main() {
  int n;
  char **s, keys[][9] = {
                "first of", "for",     "for each", "from", "in",    "is",
                "is a",     "list of", "unit",     "or",   "while", "int",
                "float",    "double",  "string"};  // lista de cuvinta/sintagme

  scanf("%d\n", &n);  // citesc numarul de linii
  s = (char **)malloc(n * sizeof(char *));  // in matricea de linii, aloc memorie pentru linii
  Read(n, s);               // apelez functia de citire
  Solve(n, s, keys);        // apelez functia de rezolvare

  return 1;
}