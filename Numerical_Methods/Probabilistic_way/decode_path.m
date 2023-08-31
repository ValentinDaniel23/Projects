function [decoded_path] = decode_path (path, lines, cols)
	decoded_path = zeros(length(path)-1, 2);

  for i = 1:length(path)-1
    node = path(i);
    x = floor( (node-1)/cols ) + 1; 
    y = node - (x-1)*cols;
    % x si y sunt pozitiile in matrice
    decoded_path(i,1) = x;
    decoded_path(i,2) = y;
  end
endfunction