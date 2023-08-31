global get_words
global compare_func
global sort

section .data
    print_format db "Aici %s", 10, 0
    del db " .,", 10, 0                 ; delimitatorii

section .text
    extern printf
    extern strchr
    extern strcmp
    extern strlen
    extern qsort

;; sort(char **words, int number_of_words, int size)
;  functia va trebui sa apeleze qsort pentru soratrea cuvintelor 
;  dupa lungime si apoi lexicografix

comparatie:
    enter 0, 0
    
    push esi                              
    push edi                              
    push ebx                             

    mov edx, dword[ebp + 8]              ; adresa catre primul string din parametru 
    mov ebx, dword[ebp + 12]             ; adresa catre al doilea string din parametru
    
    ; extrag in edi lungimea lui edx
    push edx
    push ebx

    push dword [edx]
    call strlen
    add esp, 4
    mov edi, eax

    pop ebx
    pop edx
    

    ; extrag in esi lungimea lui ebx
    push edx
    push ebx
    
    push dword [ebx]
    call strlen
    add esp, 4
    mov esi, eax

    pop ebx                               
    pop edx                              
    
    
    
    cmp edi, esi                         ; compar lungimile
    jg greater 
    jl lower
    
    jmp equal                            

greater:
    mov eax, 1                           ; daca edi e mai mare, returnez 1
    jmp gata
     
    
lower:
    mov eax, -1                          ; daca edi e mai mic, returnez -1
    jmp gata

equal:
    ; compar lexicografic stringurile
    ; in eax se returneaza ceea ce trebuie sa returnez din comparatie
    push edx
    push ebx
    
    mov eax, 0
    push dword [ebx]
    push dword [edx]
    call strcmp
    add esp, 8
    
    pop ebx
    pop edx
    
    jmp gata

gata:
    pop ebx
    pop edi
    pop esi

    leave 
    ret

sort:
    enter 0, 0
    pusha
    
    mov ebx, dword[ebp + 8]              ; vectorul de cuvinte
    mov edi, dword[ebp + 12]             ; numarul de cuvinte
    mov esi, dword[ebp + 16]             ; dimensiunea care se da la qsort
    
    ; apelez qsort pentru a sorta cuvintele dupa lungime, si apoi lexicografic
    pusha
    push comparatie
    push esi
    push edi
    push ebx
    call qsort
    add esp, 16
    popa

    popa
    leave
    ret
    
;; get_words(char *s, char **words, int number_of_words)
;  separa stringul s in cuvinte si salveaza cuvintele in words
;  number_of_words reprezinta numarul de cuvinte
get_words:
    
    enter 0, 0

    mov edi, dword [ebp + 8]            ; string
	mov edx, dword [ebp + 12]           ; vectorul de stringuri
    mov esi, dword [ebp + 16]           ; numarul de cuvinte
    
    ; initializare cu 0
    xor ecx, ecx 
    xor eax, eax
    xor esi, esi                        ; oricum nu aveam nevoie de numarul de cuvinte
                                        ; esi va fi caracterul precedent celui curent, deci initializez cu 0

start_loop:
    mov eax, 0                          
    mov al, byte [edi + ecx]            ; eax contine doar caracterul ecx din string
    cmp al, 0                           ; verific daca e 0
    je end_loop                         ; ies din loop
    
    ; verific daca eax e in del cu strchr
    pusha
    push eax
    push del
    call strchr
    add esp, 8
    
    cmp eax, 0                          ; verific daca e 0, adica daca nu e in del
    popa
    je skip_null                        ; nu e in del, dau skip
    
    mov byte [edi + ecx], 0             ; pun caracter nul unde e delimitator
    jmp skip_word                       ; dau skip

skip_null:                              ; nu e caracter nul
    cmp esi, 0                          ; daca caracter precent e 0, inseamna ca aici incepe un cuvant
    jne skip_word
    
    ; adaug adresa de unde incepe cuvantul
    mov eax, edi                         
    add eax, ecx                        
    mov [edx], eax                      ; eax e pointerul la string  
    
    add edx, 4                          ; incrementez pozitia in vectorul de stringuri
    
skip_word:
    
    mov eax, 0                          
    mov al, byte [edi + ecx]
    mov esi, eax                        ; caracterul curent il bag in caracter precedent

    inc ecx                             ; incrementez contorul
    jmp start_loop                      ; reiau loop ul
end_loop:

    leave
    ret
    