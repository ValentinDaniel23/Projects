// Chipuc Valentin-Daniel - 313CC
#ifndef TASK2_H_
#define TASK2_H_

#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "heap.h"

typedef struct Edge2 {
  int val;             // Nod destinatie
  float cost;          // Cost muchie (distanta/adancime)
  int distance;        // Cost distanta
  struct Edge2 *next;  // Urmatorul nod din lista de adiacenta
} Edge2;

typedef struct Graph2 {
  int nr_nodes;               // Numar noduri
  struct Edge2 **neighbours;  // Vector de pointeri pentru
                              // lista de adiacenta
  int treasure;               // greutate comoara
  int *depth;                 // vector de adancime
} Graph2;

// Golesc listele de adiacenta, recursiv de la capat
void destroyGraph2(Edge2 *curr_edge) {
  if (curr_edge == NULL) return;
  if (curr_edge->next == NULL) {
    free(curr_edge);
    return;
  }
  destroyGraph2(curr_edge->next);
  curr_edge->next = NULL;
  free(curr_edge);
}

// Identificare noduri
int findNode(char *s) {
  int i, n;

  if (strcmp("Insula", s) == 0) return 0;
  if (strcmp("Corabie", s) == 0) return 1;

  n = strlen(s);
  int node = 0;
  for (i = 0; i < n; i++) {
    if (s[i] == '_') {
      i++;
      while (i < n) {
        node = node * 10 + s[i++] - '0';
      }
    }
  }

  return node;
}

Graph2 *Read_2() {
  FILE *in_File = fopen("tema3.in", "r");

  if (in_File == NULL) {
    printf("Eroare la deschiderea fișierului.\n");
    return NULL;
  }

  Graph2 *G = (Graph2 *)calloc(1, sizeof(Graph2));
  int N, M, i;

  fscanf(in_File, "%d %d\n", &N, &M);
  G->neighbours = (Edge2 **)calloc(N, sizeof(Edge2 *));
  for (i = 0; i < N; i++) {
    G->neighbours[i] = NULL;
  }

  while (M--) {
    Edge2 *new_edge;
    int x, y, z;
    char s1[20], s2[20];
    fscanf(in_File, "%s %s %d\n", s1, s2, &z);

    {
      x = findNode(s1);
      y = findNode(s2);
    }
    // Insula e mereu nodul 0
    // Corabie e mereu nodul 1
    new_edge = (Edge2 *)calloc(1, sizeof(Edge2));
    new_edge->val = y;
    new_edge->distance = z;
    new_edge->next = G->neighbours[x];
    G->neighbours[x] = new_edge;
  }
  G->nr_nodes = N;

  G->depth = (int *)calloc(G->nr_nodes, sizeof(int));
  for (i = 0; i < G->nr_nodes; i++) {
    int x;
    char s[20];
    fscanf(in_File, "%s %d\n", s, &x);
    G->depth[i] = x;
  }
  fscanf(in_File, "%d\n", &G->treasure);

  for (i = 0; i < G->nr_nodes; i++) {
    Edge2 *curr_edge = G->neighbours[i];
    while (curr_edge != NULL) {
      curr_edge->cost = (float)curr_edge->distance / G->depth[curr_edge->val];
      curr_edge = curr_edge->next;
    }
  }

  fclose(in_File);

  return G;
}

// dfs pe graf, se returneaza daca se poate ajunge la nodul 0
int dfs(Graph2 *G, int node, int *vis) {
  vis[node] = 1;  // s-a trecut prin nodul node
  if (node == 0) {
    return 1;
  }

  Edge2 *curr_edge = G->neighbours[node];
  while (curr_edge != NULL) {
    if (vis[curr_edge->val] == 0) {
      if (dfs(G, curr_edge->val, vis) == 1) return 1;
    }
    curr_edge = curr_edge->next;
  }

  return 0;
}

void Solve_2(Graph2 *G) {
  FILE *out_File = fopen("tema3.out", "w");

  if (out_File == NULL) {
    printf("Eroare la deschiderea fișierului.\n");
    return;
  }

  // Mai departe aplic algoritmul lui dijkstra cu heap
  // Folosesc pos pentru a afla pozitia unui nod in heap, -1 in caz ca nu e
  // Folosesc vis pentru dfs
  // Folosesc bck pentru a retine nodul optim de unde s-a actualizat dijkstra
  // Folosesc dist pentru a retine distanta retinuta dupa dijsktra
  int i, *pos, *vis, *bck, *dist;
  float *d;
  d = (float *)calloc(G->nr_nodes, sizeof(float));
  pos = (int *)calloc(G->nr_nodes, sizeof(int));
  vis = (int *)calloc(G->nr_nodes, sizeof(int));
  bck = (int *)calloc(G->nr_nodes, sizeof(int));
  dist = (int *)calloc(G->nr_nodes, sizeof(int));

  for (i = 0; i < G->nr_nodes; i++) {
    d[i] = (float)999999999;
    pos[i] = -1;
  }
  d[0] = (float)0;

  Heap *H = createHeap(G->nr_nodes);       // Creez heap pentru dijkstra
  Element aux;                             // Nod ajutator din heap
  insert(H, (Element){(float)0, 0}, pos);  // Introduc nodul insula

  while (H->size) {
    aux = deleteMin(H, pos);  // Returnez varful stivei si dau pop

    Edge2 *curr_edge = G->neighbours[aux.node];
    while (curr_edge != NULL) {
      if (compare(d[curr_edge->val], d[aux.node] + curr_edge->cost)) {
        d[curr_edge->val] = d[aux.node] + curr_edge->cost;
        bck[curr_edge->val] = aux.node;
        dist[curr_edge->val] = dist[aux.node] + curr_edge->distance;
        if (pos[curr_edge->val] == -1) {  // Daca nu se afla in heap, inserez
          insert(H, (Element){d[curr_edge->val], curr_edge->val}, pos);
        } else {  // Daca se afla in heap, folosesc functia de update
          updateValue(H, pos[curr_edge->val], d[curr_edge->val], pos);
        }
      }
      curr_edge = curr_edge->next;
    }
  }
  destroyHeap(H);  // Scot din memorie heap-ul creat

  int Solution = 1;    // Solution e 1 daca se poate ajunge de
                       // la insula la corabie si invers
  if (pos[1] == -1) {  // Nu s-a ajuns la corabie
    Solution = 0;
    fprintf(out_File, "Echipajul nu poate transporta comoara inapoi la corabie\n");
  }

  if (Solution == 1) {
    Solution = dfs(G, 1, vis);
    if (Solution == 0)  // Nu se poate intoarce la insula
      fprintf(out_File, "Echipajul nu poate ajunge la insula\n");
  }

  if (Solution == 1) {
    int distance = dist[1];  // Distanta parcursa
    int min_depth = INT_MAX;
    int *path = (int *)calloc(G->nr_nodes, sizeof(int));  // retine traseul
    int cnt = 1, node = bck[1];
    path[0] = 1;

    // Se afla adancimea minima din traseu
    while (1 == 1) {
      path[cnt++] = node;
      if (G->depth[node] < min_depth) min_depth = G->depth[node];

      node = bck[node];  // Muchia de intoarcere
      if (node == 0) {
        path[cnt++] = node;
        break;
      }
    }

    // Afisez traseul
    for (i = cnt - 1; i >= 0; i--) {
      if (path[i] == 0) fprintf(out_File, "Insula ");
      if (path[i] == 1) fprintf(out_File, "Corabie\n");
      if (path[i] > 1) {
        fprintf(out_File, "Nod_%d ", path[i]);
      }
    }
    fprintf(out_File, "%d\n%d\n", distance, min_depth);
    // aflu numarul de drumuri necesare
    fprintf(out_File, "%d\n", G->treasure / min_depth + (G->treasure % min_depth != 0));
    free(path);
  }

  for (i = 0; i < G->nr_nodes; i++) {
    Edge2 *curr_edge = G->neighbours[i];
    destroyGraph2(curr_edge);
  }
  free(G->neighbours);
  free(G->depth);
  free(G);
  free(d);
  free(pos);
  free(vis);
  free(bck);
  free(dist);

  fclose(out_File);
}

#endif