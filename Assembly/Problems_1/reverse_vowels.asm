section .data
	; declare global vars here

section .text
	global reverse_vowels

;;	void reverse_vowels(char *string)
;	Cauta toate vocalele din string-ul `string` si afiseaza-le
;	in ordine inversa. Consoanele raman nemodificate.
;	Modificare se va face in-place
reverse_vowels:
    
    push ebp
    push esp   
    pop ebp    
    ; creez stack frame
    ; ebp a primit esp
    
	push dword [ebp + 8]  
	pop edi       ; edi este stringul

    xor esi, esi  ; index in string
    
find_vowels:
    push word[edi + esi]   ; extrag primii 2 byte de la edi + esi
    pop ax                 ; extragerea in ax
	xor ah, ah             ; ah il fac 0 pentru ca am extras si al doilea byte
    cmp ax, 0              ; caracter nul 
    je replace_vowels      ; ies din acest loop, am ajuns la sfarsitul stringului
    
    ; verific daca e vocala (ax are ah = 0)
    cmp ax, 'a'
	je adauga
    cmp ax, 'e'
    je adauga
	cmp ax, 'i'
    je adauga
	cmp ax, 'o'
    je adauga
	cmp ax, 'u'
	je adauga
    
	jmp next_char          ; nu e vocala

adauga: 
    push ax                ; adaug vocala in stiva
    
next_char:
    inc esi                ; cresc contorul
    jmp find_vowels        ; reiau loop-ul
    
replace_vowels:
    xor esi, esi           ; reinitializez contorul cu 0
    
replace_loop:
    push word[edi + esi]   ; extrag primii 2 byte de la edi + esi
    pop ax                 ; extragerea in ax
	xor ah, ah             ; ah il fac 0 pentru ca am extras si al doilea byte
    cmp ax, 0              ; caracter nul 
    je end_replace         ; ies din acest loop, am ajuns la sfarsitul stringului
    
    ; verific daca e vocala (ax are ah = 0)
    cmp ax, 'a'
	je modifica
    cmp ax, 'e'
    je modifica
	cmp ax, 'i'
    je modifica
	cmp ax, 'o'
    je modifica
	cmp ax, 'u'
	je modifica
    
    jmp next_replace_char  ; nu e vocala

modifica:
    ; e vocala, extrag din stiva
    ; vocalele vor fi inversate, se garanteaza ca numarul de vocale din stiva e la fel cu cel din string
    pop ax                 ; extrag
	and byte[edi + esi], 0 ; fac 0 in string cu and
    or byte[edi + esi], al ; or cu 0 de fapt atribuie al

next_replace_char:
    inc esi                ; cresc contorul
    jmp replace_loop       ; reiaul loop-ul
    
end_replace:
    pop ebp                ; echivalent cu leave, reiau stack frame-ul
	ret