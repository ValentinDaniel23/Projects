%include "../include/io.mac"

    ;;
    ;;   TODO: Declare 'avg' struct to match its C counterpart
    ;;
struc avg
    .quo: resw 1
    .remain resw 1
endstruc

struc proc
    .pid: resw 1
    .prio: resb 1
    .time: resw 1
endstruc

    ;; Hint: you can use these global arrays
section .data
    prio_result dd 0, 0, 0, 0, 0
    time_result dd 0, 0, 0, 0, 0

section .text
    global run_procs

run_procs:
    ;; DO NOT MODIFY

    push ebp
    mov ebp, esp
    pusha

    xor ecx, ecx

clean_results:
    mov dword [time_result + 4 * ecx], dword 0
    mov dword [prio_result + 4 * ecx],  0

    inc ecx
    cmp ecx, 5
    jne clean_results

    mov ecx, [ebp + 8]      ; processes
    mov ebx, [ebp + 12]     ; length
    mov eax, [ebp + 16]     ; proc_avg
    ;; DO NOT MODIFY
   
    ;; Your code starts here
    
    mov   edx, 0   ; cati bytes trebuie sariti pentru a ajunge
                   ; corect in vectorul de structuri
                   ; primul element e in ecx + 0, deci edx = 0
    mov   edi, 0   ; e contorul folosit

    for_start:
      push  edi    ; retin contorul pe stiva
      movzx edi, byte [ecx + edx + proc.prio]   ; prioritatea
      movzx esi, byte [ecx + edx + proc.time]   ; timpul
      
      dec   edi    ; prioritatea se indexeaza de la 0

      inc   dword [prio_result + 4 * edi]       ; cresc contor prioritati
      add   dword [time_result + 4 * edi], esi  ; cresc timpul pentru o prioritate
      
      pop   edi    ; iau de pe stiva contorul
      
      add   edx, proc_size   ; numar bytes pentru urmarorul element de structura
      inc   edi    ; cresc contorul

      cmp   edi, ebx
      jne   for_start   ; daca nu s-a ajuns in capat, reiau
    
    
    mov   edx, 0   ; edx se foloseste la fel

    for_start2:
      push  edx   ; pun pe stiva sa folosesc edx
      push  eax   ; pun pe stiva sa folosesc eax
      
      mov   esi, 0  ; esi va fi catul (initial 0)
                    ; edi va fi restul

      mov   eax, dword [time_result + 4 * edx]   ; deimpartit
      mov   edi, dword [prio_result + 4 * edx]   ; impartitor
      mov   edx, 0  ; edx initializat cu 0, deimpartiul fiind edx:eax

      cmp   edi, 0  ; impartitor diferit de 0
      je    next    ; daca e 0, sar peste impartire, esi si edi fiind 0
      
      div   edi
      mov   esi, eax   ; extrag catul
      mov   edi, edx   ; extrag restul
      
      next:
      pop   eax   ; reextrag ce era pe stiva 
      pop   edx   ; reextrag ce era pe stiva

      ; avand solutiile in esi si edi si se stie ca-s de tip word
      ; atunci pot folosi si, di
      mov   word [eax + 4 * edx + avg.quo], si    
      mov   word [eax + 4 * edx + avg.remain], di   
      
      inc   edx      ; cresc contor
      cmp   edx, 5   ; verific daca am ajuns la final
      jne for_start2   ; reiau
    
    ;; Your code ends here
    
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY