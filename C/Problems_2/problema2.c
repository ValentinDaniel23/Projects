#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct dictionary_entry { // dictionarul de cuvinte
  char* word;
  int priority;
};

void Read(int N, struct dictionary_entry** v) {
  int i;
  *v = (struct dictionary_entry*)malloc(N * sizeof(struct dictionary_entry)); // alocare memorie
  for (i = 0; i < N; i++) { // initializeaza dictionarul
    char s[21];
    scanf("%s ", s);
    (*v)[i].word = (char*)malloc((strlen(s) + 1) * sizeof(char));
    strcpy((*v)[i].word, s);
    (*v)[i].priority = 0;
  }
}

void Increase(struct dictionary_entry* v) { // creste prioritatea unui cuvant
  ((*v).priority)++;
}

void Add(int* N, struct dictionary_entry** v, char* s) { // adaug cuvant in dictionar
  *v = realloc(*v, (*N + 1) * sizeof(struct dictionary_entry)); // cresc dimensiunea dictionarului cu 1
  (*v)[*N].word = (char*)malloc((strlen(s) + 1) * sizeof(char)); 
  strcpy((*v)[*N].word, s);
  (*v)[*N].priority = 0;
  // initializez cuvantul adaugat
  Increase(*v+*N); // cresc prioritatea la 1 la cuvantul (*v)[*N]
  (*N)++;
}

int Find(int N, struct dictionary_entry* v, char* s) {
  int i;
  for (i = 0; i < N; i++) {
    if (strcmp(s, v[i].word) == 0) return i; // cauta daca s este deja in dictionar si returneaza pozitia
  }

  return N; // returneaza prima pozitie mai mare decat dimensiunea semnificand ca e un cuvant nou
}

int Match(int N, struct dictionary_entry* v, char* s) {
  int i, j, p = N, dim = strlen(s);
  
  for (i = 0; i < N; i++) {
    if (strlen(v[i].word) >= dim) { // cuvantul e cel putin cat s de lung
      bool OK = 1;
      for (j = 0; j < dim; j++) {
        if (v[i].word[j] != s[j]) {
          OK = 0;
          break;
        }
      }
      if (OK) { /// v[i].word are s in prefix
        if (p == N) { /// primul cuvant gasit
          p = i;
        } else if ((v[i].priority > v[p].priority) ||
                   (v[i].priority == v[p].priority &&
                    strcmp(v[i].word, v[p].word) < 0)) { /// respecta ordinea prioritatii/lexicografica
          p = i;
        }
      }
    }
  }
  
  return p;
}

int main() {
  int N, M;

  scanf("%d\n", &N);
  struct dictionary_entry* v; // dictionarul de cuvinte
  Read(N, &v);

  scanf("%d\n", &M);
  int i;
  for (i = 0; i < M; i++) {
    char s[21], aux[] = ",.:!?", *t;
    int p;
    scanf("%s ", s);
    t = strchr(aux, s[0]);

    if (t == NULL) { // daca s nu contine nimic din aux
      if (s[strlen(s) - 1] == '*') { // iau fix cuvantul dat
        s[strlen(s) - 1] = '\0'; // mut terminatorul
        p = Find(N, v, s); // caut daca se afla cuvantul in text
      } else {
        p = Match(N, v, s); // caut ce cuvant il are ca prefix din dictionar si 
                            // respecta prioritatea si ordinea lexicografica
      }

      if (p == N) {
        Add(&N, &v, s); // e cuvant nou si il adaug
      } else {
        Increase(v+p); // cresc prioritatea
        strcpy(s, v[p].word);
      }
    }

    printf("%s ", s); // afisez cuvantul gasit in output
  }

  return 1;
}

/*
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define alphabet 26

struct dictionary_entry {
  char* word;
  int priority;
};

struct Trie {
  int priority, priority_max;
  bool head;
  struct Trie* ch[alphabet];
};

int Max(int a, int b) {
  if (a > b) return a;
  return b;
}

struct Trie* getNewTrieNode() {
  struct Trie* node = (struct Trie*)malloc(sizeof(struct Trie));
  node->priority = 0;
  node->priority_max = 0;
  node->head = 0;
  int i;
  for (i = 0; i < alphabet + 1; i++) {
    node->ch[i] = NULL;
  }

  return node;
}

void Insert(struct Trie* node, char* s) {
  char c = s[0];

  if (c == '-') {
    if (node->ch[26] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[26] = son;
    }
    Insert(node->ch[26], s + 1);
  } else if (c != '\0') {
    if (node->ch[c - 'a'] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[c - 'a'] = son;
    }
    Insert(node->ch[c - 'a'], s + 1);
  } else {
    node->head = 1;
  }
}

void Add(struct Trie* node, char* s) {
  char c = s[0];

  if (c == '-') {
    if (node->ch[26] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[26] = son;
    }
    Add(node->ch[26], s + 1);
    node->priority_max = Max(node->priority_max, node->ch[26]->priority_max);
  } else if (c != '\0') {
    if (node->ch[c - 'a'] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[c - 'a'] = son;
    }
    Add(node->ch[c - 'a'], s + 1);
    node->priority_max =
        Max(node->priority_max, node->ch[c - 'a']->priority_max);
  } else {
    node->priority++;
    node->priority_max = Max(node->priority, node->priority_max);
    node->head = 1;
  }
}

void Search(struct Trie* node, char* s, char* sout) {
  char c = s[0];

  if (c == '-') {
    sout[0] = s[0];
    if (node->ch[26] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[26] = son;
    }
    Search(node->ch[26], s + 1, sout + 1);
    node->priority_max = Max(node->priority_max, node->ch[26]->priority_max);
  } else if (c != '\0') {
    sout[0] = s[0];
    if (node->ch[c - 'a'] == NULL) {
      struct Trie* son = getNewTrieNode();
      node->ch[c - 'a'] = son;
    }
    Search(node->ch[c - 'a'], s + 1, sout + 1);
    node->priority_max =
        Max(node->priority_max, node->ch[c - 'a']->priority_max);
  } else {
    if (node->priority == node->priority_max && node->head) {
      node->priority++;
      node->priority_max = node->priority;
      sout[0] = '\0';
    } else {
      int i;
      int poz = -1;

      if (node->ch[26] != NULL &&
          node->ch[26]->priority_max == node->priority_max) {
        poz = 26;
        sout[0] = '-';
        Search(node->ch[26], s, sout + 1);
      }

      if (poz == -1)
        for (i = 0; i < alphabet; i++) {
          if (node->ch[i] != NULL &&
              node->ch[i]->priority_max == node->priority_max) {
            poz = i;
            sout[0] = 'a' + i;
            Search(node->ch[i], s, sout + 1);
            break;
          }
        }

      if (poz == -1) {
        node->priority++;
        node->priority_max = node->priority;
        node->head = 1;
        sout[0] = '\0';
      } else {
        node->priority_max =
            Max(node->priority_max, node->ch[poz]->priority_max);
      }
    }
  }
}

void Read(int N, struct dictionary_entry* v, struct Trie* r) {
  int i;
  for (i = 0; i < N; i++) {
    char s[21];
    scanf("%s ", s);
    v[i].word = (char*)malloc((strlen(s) + 1) * sizeof(char));
    strcpy(v[i].word, s);
    v[i].priority = 1;

    Insert(r, v[i].word);
  }
}

int main() {
  int N, M;

  scanf("%d\n", &N);

  struct dictionary_entry* v =
      (struct dictionary_entry*)malloc(N * sizeof(struct dictionary_entry));
  struct Trie* r = getNewTrieNode();
  Read(N, v, r);

  scanf("%d\n", &M);
  int i;
  for (i = 0; i < M; i++) {
    char s[21], sout[21];
    scanf("%s ", s);

    if (strlen(s) == 1 && (s[0] < 'a' || s[0] > 'z')) {
      printf("%c ", s[0]);
    } else {
      if (s[strlen(s) - 1] == '*') {
        s[strlen(s) - 1] = '\0';
        Add(r, s);
        strcpy(sout, s);
      } else {
        Search(r, s, sout);
      }

      printf("%s ", sout);
    }
  }

  return 1;
}
*/