%include "../include/io.mac"

struc proc
    .pid: resw 1
    .prio: resb 1
    .time: resw 1
endstruc

section .text
    global sort_procs

sort_procs:
    ;; DO NOT MODIFY
    enter 0,0
    pusha

    mov edx, [ebp + 8]      ; processes
    mov eax, [ebp + 12]     ; length
    ;; DO NOT MODIFY

    ;; Your code starts here
    
    dec   eax     ; parcurge elementele in bubble sort de la 0 la length-2
    while_start:
      mov   edi, 0     ; variabila ok din bubble sort
      push  edi        ; pot folosi edi

      mov   esi, 0     ; contorul din for
      push  esi        ; pot folosi esi

      for_start:
         mov   edi, esi           ; pozitie curenta
         imul  edi, proc_size     ; inmultire intre elemente pozitive, deci fara semn

         mov   esi, edi           ; esi acum primeste pozitia curenta 
         add   esi, proc_size     ; esi retine urmatoare pozitie
         
         ; registrul ebx primeste ce e in pozitia curenta (bx, bl)
         ; registrul ecx primeste ce e in pozitia imediat urmatoare (cx, cl)
         ; ca la bubble sort, daca ecx are prioritati mai mici, se face swap

         mov   bl, byte[edx + edi + proc.prio]      
         mov   cl, byte[edx + esi + proc.prio]   
         cmp   bl, cl
         jg    sort
         jl    next
         
         mov   bx, word[edx + edi + proc.time]
         mov   cx, word[edx + esi + proc.time]
         cmp   bx, cx
         jg    sort
         jl    next
         
         mov   bx, word[edx + edi + proc.pid]
         mov   cx, word[edx + esi + proc.pid]
         cmp   bx, cx
         jg    sort
         jl    next
         
         sort:     ; daca e nevoie de swap, intra in sort
         mov   bx, word[edx + edi + proc.pid]
         mov   cx, word[edx + esi + proc.pid]
         xchg  cx, word[edx + edi + proc.pid]
         xchg  bx, word[edx + esi + proc.pid]

         mov   bl, byte[edx + edi + proc.prio]
         mov   cl, byte[edx + esi + proc.prio] 
         xchg  cl, byte[edx + edi + proc.prio]
         xchg  bl, byte[edx + esi + proc.prio]
          
         mov   bx, word[edx + edi + proc.time]
         mov   cx, word[edx + esi + proc.time]
         xchg  cx, word[edx + edi + proc.time]
         xchg  bx, word[edx + esi + proc.time]
          
         ; s-a facut swap la fiecare element din structura
          
         pop   esi      ; vreau sa ajung la ok din bubble sort
         pop   edi      ; am extras ok-ul
         or    edi, 1   ; ok primeste 1, deci s-a sortat ceva
         push  edi      ; reintroduc ok in stiva
         push  esi      ; reintroduc contorul deoarece, urmeaza sa-l scot
          
         next:
         pop   esi      ; scot contorul
         inc   esi      ; cresc contorul
         push  esi      ; reintroduc contorul in stiva
         cmp   esi, eax    ; verific daca depaseste penultimul element
         jl    for_start   ; daca nu depaseste, continui cu contorul crescut
      
      pop   esi   ; vreau sa ajung la ok din bubble sort
      pop   edi   ; am extras ok-ul
      cmp edi, 1  ; verific daca s-a sortat ceva
      je while_start   ; daca s-a sortat, reiaul pasii, urmand sa setez ok 0
    
    ;; Your code ends here
    
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY