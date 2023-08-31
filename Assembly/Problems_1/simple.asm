%include "../include/io.mac"

section .text
    global simple
    extern printf

simple:
    ;; DO NOT MODIFY
    push    ebp
    mov     ebp, esp
    pusha

    mov     ecx, [ebp + 8]  ; len
    mov     esi, [ebp + 12] ; plain
    mov     edi, [ebp + 16] ; enc_string
    mov     edx, [ebp + 20] ; step

    ;; DO NOT MODIFY
   
    ;; Your code starts here
    
    mov   ebx, 0          ; contor

    for_start:
       
    mov   al, byte [esi + ebx] ; citim caracterul curent
    
    add   al, dl               ; fac shiftarea
    cmp   al, 'Z'              
    jle   salt                 ; daca e caracter valid, sar
    sub   al, 26               ; nu e valid, scad 26 sa devina valid
    
    salt:
    
    mov   byte [edi + ebx], al ; modific valoarea in textul criptat
    inc   ebx                  ; cresc contorul
    
    cmp   ebx, ecx  
    jne   for_start            ; verificam daca s-a terminat sirul

    ;; Your code ends here
    
    ;; DO NOT MODIFY

    popa
    leave
    ret
    
    ;; DO NOT MODIFY
