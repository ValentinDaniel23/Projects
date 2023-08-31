function [G, c] = get_Jacobi_parameters (Link)
	[m, n] = size(Link);

  G = sparse(m-2, n-2);
  c = sparse(m-2, 1);

  [X, Y,  Elem] = find(Link);
  % Creez G si c parcurgand elementele din Link
  for p=1:length(Elem)
    i = X(p);
    j = Y(p);
    k = Elem(p);

    if i <= m-2 && j <= n-2
      G(i, j) = k;
    endif

    if i <= m-2 && j == n-1
      c(i, 1) = k;
    endif
  end

endfunction
