#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct node {
  char nume[100];
  int cost;
  struct node *next;
} node;
typedef struct vect {
  char name[100];
  int val;
} vect;
typedef struct v_cost {
  int cost;
  int val;
} v_cost;
typedef struct list_graph {
  int n;        // numar de noduri(nodes)
  node *a[50];  // vector de pointeri catre liste de vecini
} list_graph;

list_graph Initialize(list_graph g, int N) {
  g.n = N;
  int i;
  for (i = 0; i < N; i++) {
    g.a[i] = NULL;
  }
  return g;
}

list_graph insertEdge(list_graph g, vect x, vect y, v_cost c) {
  node *new = malloc(sizeof(node));
  strcpy(new->nume, y.name);
  new->next = g.a[x.val];
  new->cost = c.cost;
  g.a[x.val] = new;
  return g;
}

list_graph build(list_graph g, int M, vect v1[], vect v2[], v_cost vc[]) {
  int i;
  for (i = 0; i < M; i++) {
    g = insertEdge(g, v1[i], v2[i], vc[i]);
    g = insertEdge(g, v2[i], v1[i], vc[i]);
  }
  return g;
}

void print(list_graph g, int N, vect copie[]) {
  int i;
  node *p;
  printf("Graful reprezentat cu liste de adiacenta \n");
  for (i = 0; i < N; i++) {
    p = g.a[i];
    printf("%s : ", copie[i].name);
    while (p != NULL) {
      printf("%s(%d) ", p->nume, p->cost);
      p = p->next;
    }
    printf("\n");
  }
}

void DFS(int v, int visited[], list_graph g, vect copie[]) {
  int i, next;
  visited[v] = 1;
  node *p = g.a[v];
  while (p != NULL) {
    for (i = 0; i < g.n; i++) {
      if (strcmp(copie[i].name, p->nume) == 0) {
        next = i;
        break;
      }
    }
    if (visited[next] == 0) {
      DFS(next, visited, g, copie);
    }
    p = p->next;
  }
}
int connected_components(int N, int visited[], list_graph g, vect copie[]) {
  int i, count = 0;
  // printf("da");
  for (i = 0; i < N; i++) {
    if (visited[i] == 0) {
      DFS(i, visited, g, copie);
      count = count + 1;
    }
  }
  return count;
}

int minim(int key[], int mstSet[], int N) {
  int min = 1000;
  int min_index = -1;
  int i;
  for (i = 0; i < N; i++) {
    if (mstSet[i] == 0 && key[i] < min) {
      min = key[i];
      min_index = i;
    }
  }
  return min_index;
}
void printMST(int parent[], int N, list_graph g, vect copie[]) {
  int i;
  printf("Minimum Spanning Tree:\n");
  for (i = 1; i < N; i++) {
    if (parent[i] != -1) {
      printf("%s - %s\n", copie[parent[i]].name, copie[i].name);
    }
  }
}

void MST(list_graph g, int N, vect copie[], int start, int started[],
         int sums[], int *nr) {
  if (started[start] != 0) {
    return;
  } else {
    started[start] = 1;
  }
  // int sum = 0;/
  int mstSet[N];
  int parent[N];
  int key[N];
  int i;
  for (i = 0; i < N; i++) {
    mstSet[i] = 0;
    key[i] = 1000;
    parent[i] = -1;
  }
  key[start] = 0;
  int j, next;
  for (i = 0; i < N - 1; i++) {
    int m = minim(key, mstSet, N);
    if (m == -1) {
      for (j = 0; j < N; j++) {
        if (mstSet[j] == 0) {
          break;
        }
      }
      MST(g, N, copie, j, started, sums, nr);
      break;
      continue;
    }
    mstSet[m] = 1;
    node *p = g.a[m];
    while (p != NULL) {
      for (j = 0; j < N; j++) {
        if (strcmp(copie[j].name, p->nume) == 0) {
          next = j;
          if (mstSet[next] == 0 && p->cost < key[next]) {
            parent[next] = m;
            key[next] = p->cost;
            // sum = sum + key[next];
            // aici trb sa bag in suma
            printf("key:%d\n", key[next]);
          }
          break;
        }
      }
      p = p->next;
    }
  }
  int sum = 0;
  for (i = 0; i < N; i++) {
    if (parent[i] != -1) {
      sum = sum + key[i];
    }
  }
  printf("sum:%d\n", sum);
  sums[(*nr)] = sum;
  printf("sum[%d]=%d\n", *nr, sums[(*nr)]);
  (*nr)++;
  printMST(parent, N, g, copie);
}

void sort_array(int sums[], int l) {
  int i, j, x;
  for (i = 0; i < l - 1; i++) {
    for (j = i + 1; j < l; j++) {
      if (sums[i] > sums[j]) {
        x = sums[i];
        sums[i] = sums[j];
        sums[j] = x;
      }
    }
  }
}

int main(int argc, char *argv[]) {
  FILE *in, *out;
  in = fopen("tema3.in", "r");
  out = fopen("tema3.out", "w");
  int OK, j, i, N, M;
  fscanf(in, "%d %d\n", &N, &M);
  int nr = 1;
  vect v1[M], v2[M];
  v_cost vc[M];
  vect copie[N];
  for (i = 0; i < M; i++) {
    fscanf(in, "%s %s %d\n", v1[i].name, v2[i].name, &vc[i].cost);
  }
  if (strcmp(argv[1], "1") == 0) {
    strcpy(copie[0].name, v1[0].name);
    int a = 1;
    for (i = 1; i < M; i++) {
      OK = 1;
      for (j = 0; j < a; j++) {
        if (strcmp(copie[j].name, v1[i].name) == 0) {
          OK = 0;
        }
      }
      if (OK == 1) {
        strcpy(copie[a].name, v1[i].name);
        a++;
      }
      OK = 1;
      for (j = 0; j < a; j++) {
        if (strcmp(copie[j].name, v2[i].name) == 0) {
          OK = 0;
        }
      }
      if (OK == 1) {
        strcpy(copie[a].name, v2[i].name);
        a++;
      }
    }
    for (i = 0; i < N; i++) {
      printf("%s\n", copie[i].name);
    }
    for (i = 0; i < M; i++) {
      OK = 1;
      for (j = 0; j < N; j++) {
        if (strcmp(v1[i].name, copie[j].name) == 0 && OK == 1) {
          v1[i].val = j;
          OK = 0;
        }
      }
    }
    for (i = 0; i < M; i++) {
      OK = 1;
      for (j = 0; j < N; j++) {
        if (strcmp(v2[i].name, copie[j].name) == 0 && OK == 1) {
          v2[i].val = j;
          OK = 0;
        }
      }
    }
    int visited[N], started[N];
    for (i = 0; i < N; i++) {
      visited[i] = 0;
      started[i] = 0;
    }
    // for (i = 0; i < N; i++) {
    //   printf("%d", visited[i]);
    // }
    list_graph g;
    g = Initialize(g, N);
    g = build(g, M, v1, v2, vc);
    // print(g, N, copie);
    // printf("%s\n", g.a[0]->nume);
    fprintf(out, "%d\n", connected_components(N, visited, g, copie));
    int x = 0, sums[N];
    for (i = 0; i < N; i++) {
      sums[i] = 0;
    }
    MST(g, N, copie, 0, started, sums, &x);
    sort_array(sums, x);
    for (i = 0; i < x; i++) {
      fprintf(out, "%d\n", sums[i]);
    }
    // DFS(0, visited, g, copie);
  }
}