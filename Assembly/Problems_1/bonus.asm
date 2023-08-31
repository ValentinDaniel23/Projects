section .data

section .text
    global bonus

bonus:
    ;; DO NOT MODIFY
    push ebp
    mov ebp, esp
    pusha

    mov eax, [ebp + 8]	; x
    mov ebx, [ebp + 12]	; y
    mov ecx, [ebp + 16] ; board

    ;; DO NOT MODIFY
    ;; FREESTYLE STARTS HERE
    
    ; parcurg matricea si verific daca e nevoie sa pun 1
    ; se pune 1 daca in diferenta in modul
    ; pe linii si coloane fata de x si y este 1
    
    mov   esi, 0   ; contor la primul for
    for1_start:
      mov   edi, 0   ; contor la al doilea for
      for2_start:
        mov   edx, esi
        imul  edx, 8     ; inmultire unsigned
        add   edx, edi   ; edx e pozitia in bytes a elementului curent
        
        push  esi        ; retin contorul esi
        push  edi        ; retin contorul edi
        
        sub   esi, eax   ; aflu diferenta pe linii
        sub   edi, ebx   ; aflu diferenta pe coloane
        
        ; daca pe linii e diferenta 1 sau -1, posibil sa fie bun
        ; urmeaza sa verific si pe coloane in caz afirmatiz
        
        cmp   esi, -1    
        je    column     
        
        cmp   esi, 1
        je    column
        
        mov   esi, 0
        cmp   esi, 0
        je    next       ; fortez sa ajung la next  
        
        column:
        
        ; daca am ajuns aici si diferenta pe linii e 1 sau -1
        ; atunci pun 1 in matrice
        
        cmp   edi, -1
        je    change
          
        cmp   edi, 1
        je    change
          
        mov   edi, 0
        cmp   edi, 0
        je    next      ; fortez sa ajung la next
        
        change:
        
        ; change va duce, in functie de pozitia pe tabla
        ; catre parte de sus, sau cea de jos
        cmp   edx, 32
        jl    first_board 
        jge   second_board
        
        first_board:

        sub   edx, 32   ; pozitia pe tabla depaseste 32 de bytes
        mov   esi, 1    ; initializez bit cu 1

        push  ecx       ; pun pe stiva sa folosesc ecx
        mov   ecx, edx  ; retin numarul de pozitii cu care sa shiftez
        shl   esi, cl   ; shiftez cu shl, care necesita cl
        pop   ecx       ; reiau board in ecx

        or    dword [ecx + 4], esi   ; adaug in solutie

        mov   esi, 0    
        cmp   esi, 0     
        je    next      ; fortez sa ajung la next 

        second_board:

        mov   esi, 1    ; initializez bit cu 1
          
        push  ecx       ; pun pe stiva sa folosesc ecx
        mov   ecx, edx  ; retin numarul de pozitii cu care sa shiftez
        shl   esi, cl   ; shiftez cu shl, care necesita cl
        pop   ecx       ; reiau board in ecx
          
        or    dword [ecx], esi       ; adaug in solutie

        next:
        
        pop   edi   ; reiau contorul 2 
        pop   esi   ; retiau contorul 1
        
        inc   edi   
        cmp   edi, 8
        jne   for2_start   ; daca nu ajung la capatul coloanei, reiau cu o coloana in plus
      
    inc   esi
    cmp   esi, 8
    jne   for1_start   ; daca nu ajung la capatul liniilor, reiau cu o linie in plus
    
    ;; FREESTYLE ENDS HERE
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY