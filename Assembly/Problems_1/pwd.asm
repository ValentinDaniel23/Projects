section .data
	back db "..", 0
	curr db ".", 0
	slash db "/", 0
	print_format db "Aici %d", 10, 0
	; declare global vars here

section .text
    extern strcmp                       
	extern strcpy
	extern strcat
	extern printf
	global pwd

;;	void pwd(char **directories, int n, char *output)
;	Adauga in parametrul output path-ul rezultat din
;	parcurgerea celor n foldere din directories

pwd:
	enter 0, 0
    
    mov edi, dword [ebp + 8]           ; vectorul de directoare
	mov esi, dword [ebp + 12]          ; numar directoare
    mov edx, dword [ebp + 16]          ; vectorul caii parcurse
    
    
     
    xor ecx, ecx                       ; contorul il fac 0

loop_start:
	xor eax, eax                       ; initializez eax cu 0
    
	; apelez strcmp intre directorul la pozitia ecx si ".."
	pusha
	push dword [edi + 4 * ecx]
	push back
    call strcmp
	add esp, 8
    cmp eax, 0                         ; verific egalitate
	popa
    je verifica_parinte                ; e director parinte, sar la parinte
	
	; apelez strcmp intre directorul la pozitia ecx si ".."
	pusha
    push dword [edi + 4 * ecx]
	push curr
	call strcmp
	add esp, 8
	cmp eax, 0                         ; verific egalitate
	popa
	je reia                            ; e director curent, sar peste operatii
    
    push dword [edi + 4 * ecx]         ; adaug directorul pe stiva
    jmp reia                           ; sar peste operatii

verifica_parinte:
    cmp esp, ebp                       
    je reia                            ; daca stiva nu a depasit frame-ul, nu am la ce sa dau pop

	pop eax                            ; extrag director oriunde, irelevant
	xor eax, eax                     
    
reia:
    inc ecx                            ; cresc contor
	cmp ecx, esi
	jne loop_start                     ; daca ecx nu a ajuns la esi, reiau loop
    
 
    ; s-a terminat primul loop

    ; copiez in output cu strcpy un slash "/"
	pusha
    push slash
	push edx
	call strcpy
    add esp, 8
	popa

	mov ecx, ebp                       ; ecx primeste frame-ul, ca sa parcurg stiva de la ebp la esp
	                                   ; asa parcurg in ordine directoarele din stiva
    
loop_start2:
    sub ecx, 4                         ; merg catre esp
    cmp ecx, esp                 
     
    jl skip_loop                       ; daca ecx a ajuns mai mic decat esp, termin loop-ul
    
	; adauga directorul din momentul curent in stiva, adica ecx, in output cu strcat
	pusha
	push dword [ecx]
    push edx
	call strcat
	add esp, 8
	popa
    
	; dupa fiecare director adaugat, adaug cu strcat un slash "/"
	pusha
	push slash
    push edx
	call strcat
	add esp, 8
	popa
    
	jmp loop_start2                    ; reiau loop
skip_loop:
    

    
clear_stack:                           ; loop pentru golire stiva (readuc esp la ebp)
    cmp ebp, esp                       
	je stack_cleared                   ; daca au devenit egale, ies din loop 
	pop ecx                            ; dau un pop oriunde
	jmp clear_stack                    ; reiau
stack_cleared:
    
	leave
	ret