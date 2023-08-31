// Chipuc Valentin-Daniel - 313CC
#ifndef HEAP_H_
#define HEAP_H_

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const float tol = 0.00001;  // Toleranta pentru comparatii intre float-uri

typedef struct Element {
  float value;  // Heap-ul se mentine dupa value
  int node;     // Variabila auxiliara in cadrul taskurilor
} Element;

typedef struct Heap {
  Element* elements;  // Heap pe post de vector
  int size;           // Dimensiune actuala heap
  int capacity;       // Dimensiune maxima heap
} Heap;

Heap* createHeap(int capacity) {
  Heap* heap = (Heap*)malloc(sizeof(Heap));
  heap->elements = (Element*)malloc(capacity * sizeof(Element));
  heap->size = 0;
  heap->capacity = capacity;
  return heap;
}

void destroyHeap(Heap* heap) {
  free(heap->elements);
  free(heap);
}

void swap(Element* a, Element* b) {
  Element temp = *a;
  *a = *b;
  *b = temp;
}

int compare(float a, float b) {  // Comparare intre 2 float-uri
  if (fabs(a - b) <= tol) {
    return 0;
  } else if (a < b) {
    return 0;
  }
  return 1;
}

void swap_pos(int* a, int* b) {
  int temp = *a;
  *a = *b;
  *b = temp;
}

void heapifyUp(Heap* heap, int index,
               int* pos) {  // Aplic teoria de heap cu parintele
  if (index <= 0) {
    return;
  }

  int parentIndex = (index - 1) / 2;  // Parintele in vector
  if (compare(heap->elements[parentIndex].value, heap->elements[index].value)) {
    swap_pos(&pos[heap->elements[parentIndex].node],
             &pos[heap->elements[index].node]);
    swap(&heap->elements[parentIndex], &heap->elements[index]);
    heapifyUp(heap, parentIndex, pos);
  }
}

void heapifyDown(Heap* heap, int index,
                 int* pos) {            // Aplic teoria de heap cu fii
  int smallestIndex = index;            // Nod actual
  int leftChildIndex = 2 * index + 1;   // Fiu stanga
  int rightChildIndex = 2 * index + 2;  // Fiu dreapta

  if (leftChildIndex < heap->size &&
      compare(heap->elements[smallestIndex].value,
              heap->elements[leftChildIndex].value)) {
    smallestIndex = leftChildIndex;
  }

  if (rightChildIndex < heap->size &&
      compare(heap->elements[smallestIndex].value,
              heap->elements[rightChildIndex].value)) {
    smallestIndex = rightChildIndex;
  }

  if (smallestIndex != index) {  // Daca e valoare mai mica intr-un fiu, propag heap-ul
    swap_pos(&pos[heap->elements[index].node],
             &pos[heap->elements[smallestIndex].node]);
    swap(&heap->elements[index], &heap->elements[smallestIndex]);
    heapifyDown(heap, smallestIndex, pos);
  }
}

void insert(Heap* heap, Element element, int* pos) {
  if (heap->size >= heap->capacity) {
    printf("Heap is full.\n");
    return;
  }

  heap->elements[heap->size] = element;  // Adaug element nou pe ultima pozitie in heap
  pos[element.node] = heap->size;    // Modific pozitia nodului in pos
  heapifyUp(heap, heap->size, pos);  // Propag elementul nou
  heap->size++;
}

Element deleteMin(Heap* heap, int* pos) {
  if (heap->size <= 0) {
    printf("Heap is empty.\n");
    Element invalidElement = {(float)-1, -1};  // Valoare invalida
    return invalidElement;
  }

  Element minValue = heap->elements[0];  // Returnez varful heap-ului
  heap->elements[0] =
      heap->elements[heap->size - 1];  // Ultimul element, il pun primul
  pos[heap->elements[0].node] = 0;
  heap->size--;
  heapifyDown(heap, 0, pos);  // Propag in heap
  return minValue;
}

void updateValue(Heap* heap, int position, float newValue, int* pos) {
  if (position < 0 || position >= heap->size) {
    printf("Invalid position.\n");
    return;
  }
  if (compare(newValue, heap->elements[position]
                            .value)) {  // Daca e valoare mai mica, modific
    return;
  }

  heap->elements[position].value = newValue;
  heapifyUp(heap, position, pos);
}

#endif