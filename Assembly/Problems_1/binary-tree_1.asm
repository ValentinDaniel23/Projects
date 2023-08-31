extern array_idx_1      ;; int array_idx_1

section .data
    print_format db "Aici %d", 10, 0
    
section .text
    extern printf
    global inorder_parc

;   struct node {
;       int value;
;       struct node *left;
;       struct node *right;
;   } __attribute__((packed));

;;  inorder_parc(struct node *node, int *array);
;       functia va parcurge in inordine arborele binar de cautare, salvand
;       valorile nodurilor in vectorul array.
;    @params:
;        node  -> nodul actual din arborele de cautare;
;        array -> adresa vectorului unde se vor salva valorile din noduri;

; ATENTIE: vectorul array este INDEXAT DE LA 0!
;          Cititi SI fisierul README.md din cadrul directorului pentru exemple,
;          explicatii mai detaliate!
; HINT: folositi variabila importata array_idx_1 pentru a retine pozitia
;       urmatorului element ce va fi salvat in vectorul array.
;       Este garantat ca aceasta variabila va fi setata pe 0 la fiecare
;       test.

inorder_parc:
    enter 0, 0
    pusha
    
    mov ebx, dword [ebp + 8]               ; nodul curent
    mov edx, dword [ebp + 12]              ; vectorul
    
    
    mov eax, dword [ebx + 4]               ; nodul stang
    cmp eax, 0                             ; verific daca e null
    je skip_left_son
    
    ; apelez recursiv functia in nodul stang
    pusha
    push edx
    push eax
    call inorder_parc
    add esp, 8
    popa
    
skip_left_son:
    
    ; la fel ca in inordine, dupa ce am apelat nodul stang, introduc valoarea nodului curent in vector
    mov ecx, dword [array_idx_1]       
    mov eax, dword [ebx]         
    mov dword [ edx + 4 * ecx ], eax
    inc dword [array_idx_1]                ; incrementez contorul in vector
    

    mov eax, dword [ebx + 8]               ; nodul drept
    cmp eax, 0                             ; verific daca e null
    je skip_right_son
    
    ; apelez recursiv functia in nodul drept
    pusha
    push edx
    push eax
    call inorder_parc
    add esp, 8
    popa
    
skip_right_son:
    

    popa
    leave
    ret
