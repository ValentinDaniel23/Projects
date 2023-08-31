function [Link] = get_link_matrix (Labyrinth)
	[m, n] = size(Labyrinth);
  Nodes = m*n;

  Link = sparse(Nodes+2, Nodes+2);
  Elem = zeros(Nodes);
  
  % Precalculez probabilitatile nodurilor
  for i = 1:m
    for j = 1:n
      for z = 1:4
        Elem((i-1)*n + j) += ~(bitget(Labyrinth(i,j), z));
      end
    end
  end

  for i = 1:m
    for j = 1:n
    % Next_Node este nodul cu care se face legatura
    % Leg doar cu nodul din dreapta/jos, deoarece leg bidirectional
      if i < m
        Next_Node = i * n + j;
        if ~(bitget(Labyrinth(i,j), 3))
          Link((i-1) * n + j, Next_Node) = 1.0/Elem((i-1)*n + j);
          Link(Next_Node, (i-1) * n + j) = 1.0/Elem(Next_Node);
        endif
      endif

      if j < n
        Next_Node = (i - 1) * n + j + 1;
        if ~(bitget(Labyrinth(i,j), 2))
          Link((i-1)*n + j, Next_Node) = 1.0/Elem((i-1)*n + j);
          Link(Next_Node, (i-1)*n + j) = 1.0/Elem(Next_Node);
        endif
      endif
    end
  end
  
  % Cazurile pentru nodurile Win si Lose
  for j = 1:n
    if ~(bitget(Labyrinth(1,j), 4))
      Link(j, Nodes+1) = 1.0/Elem(j);
    endif

    if ~(bitget(Labyrinth(m,j), 3))
      Link((m-1)*n + j, Nodes+1) = 1.0/Elem((m-1)*n + j);
    endif
  endfor

  for i = 1:m
    if ~(bitget(Labyrinth(i,1), 1))
      Link((i-1)*n + 1, Nodes+2) = 1.0/Elem((i-1)*n + 1);
    endif

    if ~(bitget(Labyrinth(i,n), 2))
      Link((i-1)*n + n, Nodes+2) = 1.0/Elem((i-1)*n + n);
    endif
  endfor

  Link(Nodes+1, Nodes+1) = 1.0;
  Link(Nodes+2, Nodes+2) = 1.0;
  
endfunction