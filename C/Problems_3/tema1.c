/* Chipuc Valentin-Daniel - 313CC */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Node { /// Nodurile listei
  char Data;
  struct Node *Prev;
  struct Node *Next;
} Node, *TNode;

typedef struct TList { /// Lista cu santinela in Head si degetul in Pos
  TNode Head;
  TNode Pos;
} List, *TList;

typedef struct Stack { /// Stiva
  TNode Pos;
  struct Stack *Prev;
} Stack, *TStack;

typedef struct Queue { /// Coada
  char C, *S;
  struct Queue *Prev;
  struct Queue *Next;
} Queue, *TQueue;

typedef int (*Functions)(TList, TStack *, char); /// Definitia functiilor pentru Update

///////////////////////////------------>HELPER<------------///////////////////////////
void Clear_UNDO_REDO(TStack *Undo, TStack *Redo) { /// Goleste stivele
  TStack aux;
  while (*Undo != NULL) {
    aux = *Undo;
    *Undo = (*Undo)->Prev;
    free(aux);
  }
  while (*Redo != NULL) {
    aux = *Redo;
    *Redo = (*Redo)->Prev;
    free(aux);
  }
}
///////////////////////////------------>HELPER<------------////////////////////////////
void Clear_MEMORY(TList *Tape, TStack *Undo, TStack *Redo, TQueue *First, TQueue *Last) {
  /// Goleste memoria alocata in stive, coada si lista
  Clear_UNDO_REDO(Undo, Redo);

  TNode Current;
  Current = (*Tape)->Head;
  (*Tape)->Head = (*Tape)->Pos = NULL;
  while (Current != NULL) {
    TNode aux = Current->Next;
    Current->Prev = Current->Next = NULL;
    free(Current);
    Current = aux;
  }
  free(*Tape);

  *Last = NULL;
  while (*First != NULL) {
    TQueue aux = (*First)->Next;
    (*First)->Next = (*First)->Prev = NULL;
    free((*First)->S);
    free(*First);
    *First = aux;
  }
}
///////////////////////////------------>HELPER<------------///////////////////////////
void Add_In_Stack(TNode Pos, TStack *Stk) { /// Adaug in stiva
  TStack Top = (TStack)malloc(sizeof(Stack));
  Top->Pos = Pos;
  Top->Prev = *Stk;
  *Stk = Top;
}
///////////////////////////------------>HELPER<------------///////////////////////////
void UNDO_REDO(TList Tape, TStack *GO_OUT, TStack *GO_IN) {
  /// Adaug in stiva GO_IN si extrag din stiva GO_OUT
  Add_In_Stack(Tape->Pos, GO_IN);
  Tape->Pos = (*GO_OUT)->Pos;
  TStack aux = *GO_OUT;
  *GO_OUT = (*GO_OUT)->Prev;
  aux->Pos = NULL;
  aux->Prev = NULL;
  free(aux);
}
///////////////////////////------------>HELPER<------------///////////////////////////
void Add_In_Queue(TQueue *First, TQueue *Last, char *S, char C) {
  /// Se adauga comanda in coada, deci se deplaseaza se adauga in Last
  TQueue aux = (TQueue)malloc(sizeof(Queue));
  aux->C = C;
  aux->S = (char *)malloc((strlen(S) + 1) * sizeof(char));
  strcpy(aux->S, S);
  if (*Last == NULL) {
    aux->Prev = NULL;
    aux->Next = NULL;
    *First = aux;
    *Last = aux;
  } else {
    aux->Prev = *Last;
    aux->Next = NULL;
    (*Last)->Next = aux;
    *Last = aux;
  }
}
///////////////////////////------------>HELPER<------------///////////////////////////
void Create_ListNode(TList Tape, TNode Prev, TNode Next, char c) {
  /// Se creaza nod nou in lista cu Prev, Next si c stiuti ca parametrii
  TNode New_node;
  New_node = (TNode)malloc(sizeof(Node));
  New_node->Data = c;
  New_node->Next = Next;
  New_node->Prev = Prev;
  if (Prev != NULL) Prev->Next = New_node;
  if (Next != NULL) Next->Prev = New_node;
  Tape->Pos = New_node;
}
///////////////////////////------------>HELPER<------------///////////////////////////

/// Se initializeaza lista
void INIT(TList *Tape) {
  *Tape = (TList)malloc(sizeof(List));
  Create_ListNode(*Tape, NULL, NULL, '#');
  (*Tape)->Head = (*Tape)->Pos;
  Create_ListNode(*Tape, (*Tape)->Head, NULL, '#');
}

/// Se muta degetul cu o pozitie la stanga
int MOVE_LEFT(TList Tape, TStack *undo, char c) {
  if (Tape->Pos->Prev != Tape->Head) {
    Add_In_Stack(Tape->Pos, undo);
    Tape->Pos = Tape->Pos->Prev;
  }
  return 1;
}

/// Se muta degetul cu o pozitie la dreapta
int MOVE_RIGHT(TList Tape, TStack *undo, char c) { 
  Add_In_Stack(Tape->Pos, undo);
  if (Tape->Pos->Next != NULL) {
    Tape->Pos = Tape->Pos->Next;
  } else {
    Create_ListNode(Tape, Tape->Pos, NULL, '#');
  }
  return 1;
}

/// Se parcurge lista la stanga pana la un anumit caracter dat ca parametru
int MOVE_LEFT_CHAR(TList Tape, TStack *undo, char c) {
  TNode Curent = Tape->Pos;
  while (Curent != Tape->Head) {
    if (Curent->Data == c) break;
    Curent = Curent->Prev;
  }
  if (Curent != Tape->Head) {
    Add_In_Stack(Tape->Pos, undo);
    Tape->Pos = Curent;
    return 1;
  }
  return -1;
}

/// Se parcurge lista la dreapta pana la un anumit caracter dat ca parametru
int MOVE_RIGHT_CHAR(TList Tape, TStack *undo, char c) {
  Add_In_Stack(Tape->Pos, undo);
  if (Tape->Pos->Data == c) return 1;
  while (Tape->Pos->Next != NULL) {
    if (Tape->Pos->Next->Data == c) break;
    Tape->Pos = Tape->Pos->Next;
  }
  if (Tape->Pos->Next == NULL) {
    Create_ListNode(Tape, Tape->Pos, NULL, '#');
  } else {
    Tape->Pos = Tape->Pos->Next;
  }
  return 1;
}

/// Se insereaza nod nou in stanga degetului
int INSERT_LEFT(TList Tape, TStack *undo, char c) {
  if (Tape->Pos->Prev != Tape->Head) {
    Create_ListNode(Tape, Tape->Pos->Prev, Tape->Pos, c);
    return 1;
  }
  return -1;
}

/// Se insereaza nod nou in dreapta degetului
int INSERT_RIGHT(TList Tape, TStack *undo, char c) {
  if (Tape->Pos->Next != NULL) {
    Create_ListNode(Tape, Tape->Pos, Tape->Pos->Next, c);
  } else {
    Create_ListNode(Tape, Tape->Pos, NULL, c);
  }
  return 1;
}

/// Se modifica caracterul din deget cu caracterul c
int WRITE(TList Tape, TStack *undo, char c) {
  Tape->Pos->Data = c;
  return 1;
}

/// Afisare caracter curent din deget
void SHOW_CURRENT(FILE *fout, TList Tape) { 
  fprintf(fout, "%c\n", Tape->Pos->Data);
}

/// Afisare lista
void SHOW(FILE *fout, TList Tape) {
  TNode Curent = Tape->Head->Next;
  while (Curent != NULL) {
    if (Curent == Tape->Pos) {
      fprintf(fout, "|%c|", Curent->Data);
    } else {
      fprintf(fout, "%c", Curent->Data);
    }
    Curent = Curent->Next;
  }
  fprintf(fout, "\n");
}

/// Se intra in coada, se scoate ultima comanda de update si se e executa
int EXECUTE(TList Tape, TQueue *First, TQueue *Last, TStack *Undo, TStack *Redo) {
  if (First == NULL) return -1;
  int Returned, i;
  char *Op_Names[] = {"MOVE_LEFT", "MOVE_RIGHT",  "MOVE_LEFT_CHAR", "MOVE_RIGHT_CHAR",
                      "WRITE",     "INSERT_LEFT", "INSERT_RIGHT"};
  Functions Operations[] = {MOVE_LEFT, MOVE_RIGHT,  MOVE_LEFT_CHAR, MOVE_RIGHT_CHAR,
                            WRITE,     INSERT_LEFT, INSERT_RIGHT};

  for (i = 0; i < 7; i++) {
    if (strcmp((*First)->S, Op_Names[i]) == 0) {
      Returned = Operations[i](Tape, Undo, (*First)->C);
    }
  }

  TQueue Next = (*First)->Next;
  free((*First)->S);
  free(*First);
  *First = Next;
  if (Next == NULL) *Last = NULL;
  return Returned;
}

int main() {
  FILE *fin = fopen("tema1.in", "r");
  FILE *fout = fopen("tema1.out", "w");
  int N;
  char S[20], C;
  TList Tape = NULL; /// Lista initial nula
  TStack Undo = NULL, Redo = NULL; /// Undo si Redo initial nule
  TQueue First_Command = NULL, Last_Command = NULL; ///Coada retinand prima si ultima comanda adaugata
  
  INIT(&Tape); /// Initializez lista

  fscanf(fin, "%d\n", &N);

  while (N--) {
    /// Citesc 
    fscanf(fin, "%s", S);
    
    /// In functie de comanda, aplic ce imi cere problema
    if (strcmp(S, "SHOW") == 0) {
      SHOW(fout, Tape);
      continue;
    }

    if (strcmp(S, "SHOW_CURRENT") == 0) {
      SHOW_CURRENT(fout, Tape);
      continue;
    }

    if (strcmp(S, "UNDO") == 0) {
      UNDO_REDO(Tape, &Undo, &Redo);
      continue;
    }

    if (strcmp(S, "REDO") == 0) {
      UNDO_REDO(Tape, &Redo, &Undo);
      continue;
    }

    if (strcmp(S, "EXECUTE") == 0) {
      if (EXECUTE(Tape, &First_Command, &Last_Command, &Undo, &Redo) == -1){
        fprintf(fout, "ERROR\n");
      }
      continue;
    }
    
    /// De aici pot aparea doar comenzile de update, deci adaug in coada
    if (strcmp(S, "MOVE_LEFT") == 0 || strcmp(S, "MOVE_RIGHT") == 0) {
      Add_In_Queue(&First_Command, &Last_Command, S, '#');
      continue;
    }

    fscanf(fin, " %c", &C);
    Add_In_Queue(&First_Command, &Last_Command, S, C);
  }
  
  /// Eliberez toata memoria alocata
  Clear_MEMORY(&Tape, &Undo, &Redo, &First_Command, &Last_Command);

  fclose(fin);
  fclose(fout);

  return 0;
}