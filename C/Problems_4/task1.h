// Chipuc Valentin-Daniel - 313CC
#ifndef TASK1_H_
#define TASK1_H_

#include <limits.h>
#include <stdio.h>
#include <stdlib.h>

#include "heap.h"
#include "trie.h"

typedef struct Edge {
  int val;            // Nod destinatie
  float cost;         // Cost muchie
  struct Edge *next;  // Urmatorul nod din lista de adiacenta
} Edge;

typedef struct Graph {
  int nr_nodes;              // Numar noduri
  struct Edge **neighbours;  // Vector de pointeri pentru
                             // lista de adiacenta
} Graph;

// Golesc listele de adiacenta, recursiv de la capat
void destroyGraph(Edge *curr_edge) {
  if (curr_edge == NULL) return;
  if (curr_edge->next == NULL) {
    free(curr_edge);
    return;
  }
  destroyGraph(curr_edge->next);
  curr_edge->next = NULL;
  free(curr_edge);
}

Graph *Read_1() {
  FILE *in_File = fopen("tema3.in", "r");

  if (in_File == NULL) {
    printf("Eroare la deschiderea fișierului.\n");
    return NULL;
  }

  Graph *G = (Graph *)calloc(1, sizeof(Graph));
  int N, M, i;

  fscanf(in_File, "%d %d\n", &N, &M);
  G->neighbours = (Edge **)calloc(N, sizeof(Edge *));

  Trie *T = Create_trie();  // Creez trie-ul pentru identificare noduri

  while (M--) {
    Edge *new_edge;
    int x, y;
    float z;
    char s1[20], s2[20];
    fscanf(in_File, "%s %s %f\n", s1, s2, &z);

    x = Insert(T, T->Root, s1);  // Aflu numarul nodului s1 in trie
    y = Insert(T, T->Root, s2);  // Aflu numarul nodului s2 in trie
    // Muchie de la x la y
    new_edge = (Edge *)calloc(1, sizeof(Edge));
    new_edge->val = y;
    new_edge->cost = z;
    new_edge->next = G->neighbours[x];
    G->neighbours[x] = new_edge;

    // Muchie de la y la x
    new_edge = (Edge *)calloc(1, sizeof(Edge));
    new_edge->val = x;
    new_edge->cost = z;
    new_edge->next = G->neighbours[y];
    G->neighbours[y] = new_edge;
  }
  G->nr_nodes = N;

  // Eliberez trie-ul din memorie
  Delete(T->Root);
  free(T);

  fclose(in_File);

  return G;
}

void Solve_1(Graph *G) {
  FILE *out_File = fopen("tema3.out", "w");

  if (out_File == NULL) {
    printf("Eroare la deschiderea fișierului.\n");
    return;
  }

  // Mai departe aplic algoritmul lui prim cu heap
  // Folosesc vis pentru a afla daca un nod e deja in arborele de cost minim
  // Folosesc pos pentru a afla pozitia unui nod in heap, -1 in caz ca nu e
  // Folosesc v ca vector auxiliar pentru heap-ul ce sorteaza costurile
  // v ar avea acelasi scop ca pos, dar nu ajuta la nimic
  // Cum nodurile din heap-ul de costuri sunt fixate 0, v va avea dimensiunea 0

  int *vis, *pos, *v, i;
  vis = (int *)calloc(G->nr_nodes, sizeof(int));
  pos = (int *)calloc(G->nr_nodes, sizeof(int));
  v = (int *)calloc(1, sizeof(int));

  for (i = 0; i < G->nr_nodes; i++) {
    pos[i] = -1;
  }

  Heap *Sol = createHeap(G->nr_nodes);  // Creez heap pentru a sorta costurile
  Element aux;                          // Nod ajutator din heap
  for (i = 0; i < G->nr_nodes; i++)
    if (!vis[i]) {         // Daca nodul i nu e intr-un arbore, incep algoritmul
      float min_cost = 0;  // Costul arborelui actual
      Heap *H = createHeap(G->nr_nodes);  // Creez heap nou
      insert(H, (Element){0, i}, pos);    // Introduc nod sursa in heap

      while (H->size) {
        aux = deleteMin(H, pos);  // Returnez varful stivei si dau pop
        vis[aux.node] = 1;        // Marchez ca si intrat in arbore
        min_cost += aux.value;    // Updatez costul

        Edge *curr_edge = G->neighbours[aux.node];
        while (curr_edge != NULL) {
          if (!vis[curr_edge->val]) {
            if (pos[curr_edge->val] == -1) {  // Daca nu se afla in heap, inserez
              insert(H, (Element){curr_edge->cost, curr_edge->val}, pos);
            } else {  // Daca se afla in heap, folosesc functia de update
              updateValue(H, pos[curr_edge->val], curr_edge->cost, pos);
            }
          }
          curr_edge = curr_edge->next;  // Trec prin lista de adiacenta
        }
      }

      insert(Sol, (Element){min_cost, 0}, v); // Introduc in heapul de costuri
                                              // Irelevant ce pun pentru node
                                              // Irelevant vectorul v
      destroyHeap(H);  // Scot din memorie heap-ul creat
    }

  fprintf(out_File, "%d\n", Sol->size);
  while (Sol->size) {
    aux = deleteMin(Sol, v); // Scot din heap, elementele iesind crescator
    fprintf(out_File, "%d\n", (int)aux.value); // Afisez costurile
  }
  
  // Dealoc memoria folosita
  for (i = 0; i < G->nr_nodes; i++) {
    Edge *curr_edge = G->neighbours[i];
    destroyGraph(curr_edge);
  }
  free(G->neighbours);
  free(G);
  destroyHeap(Sol);
  free(vis);
  free(pos);
  free(v);

  fclose(out_File);
}

#endif