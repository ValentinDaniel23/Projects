function [Adj] = get_adjacency_matrix (Labyrinth)
	[m, n] = size(Labyrinth);
  % Numar total de elemente
  Nodes = m*n; 

  Adj = sparse(Nodes+2, Nodes+2);

  for i = 1:m
    for j = 1:n
      % Next_Node este nodul cu care se leaga
      % bitget functie pentru a afla un bit, ce reprezinta daca e blocat
      if i < m
        Next_Node = i * n + j; 
        if ~(bitget(Labyrinth(i,j), 3)) 
          Adj((i-1) * n + j, Next_Node) = 1;
          Adj(Next_Node, (i-1) * n + j) = 1;
        endif
      endif

      if j < n
        Next_Node = (i - 1) * n + j + 1;
        if ~(bitget(Labyrinth(i,j), 2))
          Adj((i-1)*n + j, Next_Node) = 1;
          Adj(Next_Node, (i-1)*n + j) = 1;
        endif
      endif
    end
  end
  
  % Se iau cazurile cu nodurile catre Win/Lose
  for j = 1:n
    if ~(bitget(Labyrinth(1,j), 4))
      Adj(j, Nodes+1) = 1;
    endif

    if ~(bitget(Labyrinth(m,j), 3))
      Adj((m-1)*n + j, Nodes+1) = 1;
    endif
  endfor

  for i = 1:m
    if ~(bitget(Labyrinth(i,1), 1))
      Adj((i-1)*n + 1, Nodes+2) = 1;
    endif

    if ~(bitget(Labyrinth(i,n), 2))
      Adj((i-1)*n + n, Nodes+2) = 1;
    endif
  endfor

  Adj(Nodes+1, Nodes+1) = 1;
  Adj(Nodes+2, Nodes+2) = 1;
  
endfunction
