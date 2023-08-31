// Chipuc Valentin-Daniel - 313CC
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// tipurile de date folosite
typedef unsigned char uchar;
typedef unsigned int uint;
typedef unsigned long long ull;

// functie care imi aloca dinamica matricea de pixeli,
// avand date ca parametru dimensiunile imaginii
uchar ***Image(uint a, uint b) {
  uint i, j;
  uchar ***pixels = (uchar ***)malloc(a * sizeof(uchar **));
  for (i = 0; i < a; i++) {
    pixels[i] = (uchar **)malloc(b * sizeof(uchar *));
    for (j = 0; j < b; j++) {
      pixels[i][j] = (uchar *)malloc(3 * sizeof(uchar));
    }
  }

  return pixels;
}

// functie pentru eliberarea memoriei matricii de pixeli
void Free_Image(uchar ***pixels, uint a, uint b) {
  uint i, j;
  for (i = 0; i < a; i++) {
    for (j = 0; j < b; j++) {
      free(pixels[i][j]);
    }
    free(pixels[i]);
  }
  free(pixels);
}

// structurile pentru arbore si noduri
typedef struct Quadtree {
  ull Factor; // factorul din argument
  uint Depth, Block, Side, Nodes; // cerinta 1
  struct Node *Root;
} Quadtree;

typedef struct Node {
  uchar Color[3], Leaf;
  uint Height;
  struct Node *Son[4];
} Node;

// Se aloca memoria pentru arbore + radacina
void Create_Quadtree(Quadtree **Tree, ull factor) {
  *Tree = (Quadtree *)malloc(sizeof(Quadtree));
  (*Tree)->Factor = factor;
  (*Tree)->Depth = (*Tree)->Block = (*Tree)->Side = 0;
  (*Tree)->Root = (Node *)malloc(sizeof(Node));
  (*Tree)->Root->Height = 1;
  (*Tree)->Root->Leaf = (*Tree)->Nodes = 0;
}

// Creez recursiv nodurile arborelui
// a si b reprezinta indicii coltului stang din matricea de pixeli
// l reprezinta latura patratului cu coltul in (a,b)
void Create_Nodes(Quadtree *Tree, Node *node, uchar ***pixels, uint a, uint b, uint l) {
  Tree->Nodes++;
  uint i, j, z;
  
  // parcurg matricea nodului respectiv si creez culorile
  for (z = 0; z < 3; z++) {
    ull s = 0;
    for (i = a; i < a + l; i++)
      for (j = b; j < b + l; j++) {
        s += pixels[i][j][z];
      }
    s /= l * l;
    node->Color[z] = (uchar)s;
  }
  
  // calculez mean ul
  ull Mean = 0;
  for (z = 0; z < 3; z++) {
    for (i = a; i < a + l; i++)
      for (j = b; j < b + l; j++) {
        uchar big = node->Color[z], small = pixels[i][j][z];
        if (big < small) { // am grija sa aflu care e mai mare pentru ca am unsigned
          uchar aux = big;
          big = small;
          small = aux;
        }
        Mean += 1ULL * (big - small) * (big - small);
      }
  }
  Mean = Mean / (3 * l * l);
  
  // daca mean-ul e mai mic decat factorul, devine frunza si opresc recursivitatea
  // e garantat ca se va ajunge sa se opreasca
  if (Mean <= Tree->Factor) {
    node->Leaf = 1;
    Tree->Block++;
    if (l > Tree->Side) Tree->Side = l;
    if (node->Height > Tree->Depth) Tree->Depth = node->Height;
    node->Son[0] = node->Son[1] = node->Son[2] = node->Son[3] = NULL;
    return;
  }
  
  // mean-ul e mai mare, inseamna ca aloc exista fii
  for (i = 0; i < 4; i++) {
    node->Son[i] = (Node *)malloc(sizeof(Node));
    node->Son[i]->Height = node->Height + 1;
    node->Son[i]->Leaf = 0;
  }
  
  // apelez recursiv in cele 4 submatrici
  Create_Nodes(Tree, node->Son[0], pixels, a, b, l / 2);
  Create_Nodes(Tree, node->Son[1], pixels, a, b + l / 2, l / 2);
  Create_Nodes(Tree, node->Son[2], pixels, a + l / 2, b + l / 2, l / 2);
  Create_Nodes(Tree, node->Son[3], pixels, a + l / 2, b, l / 2);
}

// avand arborele construit, extrag matricea de pixeli
void Dfs(Node *node, uchar ***pixels, uint a, uint b, uint l) {
  uint i, j, z;
  if (node->Leaf == 0) {
    Dfs(node->Son[0], pixels, a, b, l / 2);
    Dfs(node->Son[1], pixels, a, b + l / 2, l / 2);
    Dfs(node->Son[2], pixels, a + l / 2, b + l / 2, l / 2);
    Dfs(node->Son[3], pixels, a + l / 2, b, l / 2);

    for (z = 0; z < 3; z++) {
      // nu era necesar, dar am caculat culoarea medie
      // si pentru nodurile care nu sunt frunze
      ull s = 0;
      for (i = a; i < a + l; i++)
        for (j = b; j < b + l; j++) {
          s += pixels[i][j][z];
        }
      s /= l * l;
      node->Color[z] = (uchar)s;
    }
  } else {
    for (z = 0; z < 3; z++) {
      for (i = a; i < a + l; i++) {
        for (j = b; j < b + l; j++) {
          pixels[i][j][z] = node->Color[z];
        }
      }
    }
  }
}

// Eliberez recursiv (postordine) memoria pentru arbore
void Free_Quadtree(Node *node) {
  if (node->Leaf == 1) {
    free(node);
  } else {
    uint i;
    for (i = 0; i < 4; i++) {
      Free_Quadtree(node->Son[i]);
    }
    free(node);
  }
}

int main(int argc, char *argv[]) {
  if (argc < 3) {
    printf("Usage: %s [-c1 factor | -c2 factor | -d] input_file output_file\n",
           argv[0]);
    return 1;
  }

  uchar type[3];
  uint height, width, maxval, i, j, z;
  char *option = argv[1];
  FILE *input_file;
  FILE *output_file;
  
  // Verific cerintele date ca argument
  if (strcmp(option, "-d") == 0) {
    input_file = fopen(argv[2], "rb");
    output_file = fopen(argv[3], "w");

    fread(&height, sizeof(uint), 1, input_file);
    width = height;

    uint SIZE = 256, bytes_read;
    uchar byte;
    Node **Queue = (Node **)malloc(SIZE * sizeof(Node *));

    Quadtree *Tree;
    Create_Quadtree(&Tree, 0); // Creez arbore
    i = 0;
    j = 0;
    Queue[0] = Tree->Root; // Radacina mereu exista
   
    // Citesc pe rand bytes-ii
    while ((bytes_read = fread(&byte, 1, sizeof(uchar), input_file)) > 0) {
      if (SIZE <= j + 4) { // Dublez dimensiunea din coada daca e sansa sa se depaseasca
        SIZE *= 2;
        Queue = (Node **)realloc(Queue, SIZE * sizeof(Node *));
      }
      Queue[i]->Leaf = byte;
      if (byte == 0) { // Daca nu e frunza, aloc memorie in coada pentru viitorii fii
        for (z = 0; z < 4; z++) {
          Queue[++j] = (Node *)malloc(sizeof(Node));
          Queue[i]->Son[z] = Queue[j];
          Queue[j]->Height = Queue[i]->Height + 1;
        }
      } else { // E frunza, deci urmatorii bytes-i reprezinta culorile acestui nod
        for (z = 0; z < 3; z++) {
          fread(&byte, 1, sizeof(uchar), input_file);
          Queue[i]->Color[z] = byte;
        }
        for (z = 0; z < 4; z++) {
          Queue[i]->Son[z] = NULL;
        }
      }
      i++;
    }
    

    uchar ***pixels = Image(height, width); 
    Dfs(Tree->Root, pixels, 0, 0, width); // Reconstitui imaginea stiind arborele
    maxval = 255;
    
    // Afisarea sub format .ppm
    fprintf(output_file, "P6\n%u %u\n%u\n", height, width, maxval);
    for (i = 0; i < height; i++) {
      for (j = 0; j < width; j++) {
        for (z = 0; z < 3; z++) {
          fwrite(&pixels[i][j][z], sizeof(uchar), 1, output_file);
        }
      }
    }
    
    // Eliberez memoria
    free(Queue);
    Free_Image(pixels, height, height);
    Free_Quadtree(Tree->Root);
    free(Tree);
  } else {
    ull factor = strtoull(argv[2], NULL, 10); // Factorul
    input_file = fopen(argv[3], "rb");

    fscanf(input_file, "%s\n", type);
    fscanf(input_file, "%u %u\n", &width, &height);
    fscanf(input_file, "%u", &maxval);
    fseek(input_file, 1, SEEK_CUR);

    uchar ***pixels = Image(height, width);

    for (i = 0; i < height; i++) {
      for (j = 0; j < width; j++) {
        fread(pixels[i][j], sizeof(uchar), 3, input_file);
      }
    }
    
    // Se creeaza arborele pe baza matricei de pixeli citita
    // In acelasi timp se afla informatiile pentru output ul din cerinta 1 si 2
    Quadtree *Tree;
    Create_Quadtree(&Tree, factor);
    Create_Nodes(Tree, Tree->Root, pixels, 0, 0, width);

    if (strcmp(option, "-c1") == 0) {
      // Afisare in format text output
      output_file = fopen(argv[4], "wt");
      fprintf(output_file, "%u\n%u\n%u\n", Tree->Depth, Tree->Block,
              Tree->Side);
    } else {
      output_file = fopen(argv[4], "wb");
      fwrite(&height, sizeof(uint), 1, output_file);
      const uint SIZE = Tree->Nodes; // Numar total de noduri din arbore
      Node **Queue = (Node **)malloc(SIZE * sizeof(Node *)); // Coada pentru bfs
      j = 0;
      Queue[0] = Tree->Root; 

      for (i = 0; i < SIZE; i++) {
        fwrite(&Queue[i]->Leaf, sizeof(uchar), 1, output_file); // Afisez tipul nodului
        if (Queue[i]->Leaf == 0) { // Daca nu e frunza, adaug nodurile fiilor mai departe
          for (z = 0; z < 4; z++) {
            Queue[++j] = Queue[i]->Son[z];
          }
        } else {
          for (z = 0; z < 3; z++) {
            fwrite(&Queue[i]->Color[z], sizeof(uchar), 1, output_file);
            // Daca e frunza, afisez bytes-ii pentru culori
          }
        }
      }
      // Memoria cozii
      free(Queue);
    }
    // Eliberez memoria alocata pentru cerintele 1 si 2
    Free_Image(pixels, height, height);
    Free_Quadtree(Tree->Root);
    free(Tree);
  }

  fclose(input_file);
  fclose(output_file);
  return 0;
}
