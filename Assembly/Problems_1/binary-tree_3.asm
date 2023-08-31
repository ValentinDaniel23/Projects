section .text
    global inorder_fixing

;   struct node {
;       int value;
;       struct node *left;
;       struct node *right;
;   } __attribute__((packed));

;;  inorder_fixing(struct node *node, struct node *parent)
;       functia va parcurge in inordine arborele binar de cautare, modificand
;       valorile nodurilor care nu respecta proprietatea de arbore binar
;       de cautare: |node->value > node->left->value, daca node->left exista
;                   |node->value < node->right->value, daca node->right exista.
;
;       Unde este nevoie de modificari se va aplica algoritmul:
;           - daca nodul actual este fiul stang, va primi valoare tatalui - 1,
;                altfel spus: node->value = parent->value - 1;
;           - daca nodul actual este fiul drept, va primi valoare tatalui + 1,
;                altfel spus: node->value = parent->value + 1;

;    @params:
;        node   -> nodul actual din arborele de cautare;
;        parent -> tatal/parintele nodului actual din arborele de cautare;

; ATENTIE: DOAR in frunze pot aparea valori gresite! 
;          Cititi SI fisierul README.md din cadrul directorului pentru exemple,
;          explicatii mai detaliate!

inorder_fixing:
    enter 0, 0
    pusha 
    
    mov ebx, dword [ebp + 8]               ; nodul curent
    mov edx, dword [ebp + 12]              ; tatal nodului
    mov esi, dword [ebx]                   ; valoare nod curent
    
    mov eax, dword [ebx + 4]               ; nod stang
    cmp eax, 0                             ; verific daca e null
    je skip_left_son
    
    ; apelez recursiv functia in nodul stang
    pusha
    push ebx
    push eax
    call inorder_fixing
    add esp, 8
    popa
    
    ; exista nod in stanga
    push eax
    mov eax, dword[eax]                    ; valoarea din nodul stang
    cmp eax, esi                           ; compar valoarea din nodul curent cu cel stang
    pop eax
    jl skip_left_son
    
    dec esi
    mov dword [eax], esi                   ; modific valoarea din nodul stang cu esi decrementat o data
    inc esi
    
skip_left_son:
    mov eax, dword [ebx + 8]               ; nod stang
    cmp eax, 0                             ; verific daca e null
    je skip_right_son
    
    ; apelez recursiv functia in nodul drept
    pusha
    push ebx
    push eax
    call inorder_fixing
    add esp, 8
    popa
    
    ; exista nod in drept
    push eax
    mov eax, dword[eax]                    ; valoarea din nodul drept
    cmp eax, esi                           ; compar valoarea din nodul curent cu cel drept
    pop eax
    jg skip_right_son
    
    inc esi
    mov dword [eax], esi                   ; modific valoarea din nodul stang cu esi incrementat o data
    dec esi
    
skip_right_son:
    
    
    popa
    leave
    ret
