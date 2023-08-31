extern array_idx_2      ;; int array_idx_2

section .text
    global inorder_intruders

;   struct node {
;       int value;
;       struct node *left;
;       struct node *right;
;   } __attribute__((packed));

;;  inorder_intruders(struct node *node, struct node *parent, int *array)
;       functia va parcurge in inordine arborele binar de cautare, salvand
;       valorile nodurilor care nu respecta proprietatea de arbore binar
;       de cautare: |node->value > node->left->value, daca node->left exista
;                   |node->value < node->right->value, daca node->right exista
;
;    @params:
;        node   -> nodul actual din arborele de cautare;
;        parent -> tatal/parintele nodului actual din arborele de cautare;
;        array  -> adresa vectorului unde se vor salva valorile din noduri;

; ATENTIE: DOAR in frunze pot aparea valori gresite!
;          vectorul array este INDEXAT DE LA 0!
;          Cititi SI fisierul README.md din cadrul directorului pentru exemple,
;          explicatii mai detaliate!

; HINT: folositi variabila importata array_idx_2 pentru a retine pozitia
;       urmatorului element ce va fi salvat in vectorul array.
;       Este garantat ca aceasta variabila va fi setata pe 0 la fiecare
;       test al functiei inorder_intruders.      

inorder_intruders:
    enter 0, 0
    pusha 
    
    mov ebx, dword [ebp + 8]               ; nodul curent
    mov edi, dword [ebp + 12]              ; tatal
    mov edx, dword [ebp + 16]              ; vectorul
    mov esi, dword [ebx]                   ; valoare nod curent
    
    mov eax, dword [ebx + 4]               ; nod stang
    cmp eax, 0                             ; verific daca e null
    je skip_left_son
    
    ; apelez recursiv functia in nodul stang
    pusha
    push edx
    push ebx
    push eax
    call inorder_intruders
    add esp, 12
    popa
    
    ; exista nod in stanga
    mov eax, dword[eax]                    ; valoarea din nodul stang
    cmp eax, esi                           ; compar valoarea din nodul curent cu cel stang
    jl skip_left_son
    
    ; nodul stang e mai mare, deci il bag in vector
    mov ecx, dword [array_idx_2]
    mov dword [ edx + 4 * ecx ], eax
    inc dword [array_idx_2]
    
skip_left_son:
    mov eax, dword [ebx + 8]               ; nod drept
    cmp eax, 0                             ; verific daca e null
    je skip_right_son
    
    ; apelez recursiv functia in nodul drept
    pusha
    push edx
    push ebx
    push eax
    call inorder_intruders
    add esp, 12
    popa
    
    ; exista nod in dreapta
    mov eax, dword[eax]                    ; valoarea din nodul drept
    cmp eax, esi                           ; compar valoarea din nodul curent cu cel drept
    jg skip_right_son
    
    ; nodul drept e mai mic, deci il bag in vector
    mov ecx, dword [array_idx_2]
    mov dword [ edx + 4 * ecx ], eax
    inc dword [array_idx_2]
    
skip_right_son:
    
    
    popa
    leave
    ret
