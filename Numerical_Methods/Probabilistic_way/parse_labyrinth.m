function [Labyrinth] = parse_labyrinth(file_path)
	  fin = fopen(file_path, 'r');
  
  % Citesc separat dimensiunile
  m = fscanf(fin, '%d', [1, 1]);
  n = fscanf(fin, '%d', [1, 1]);
  
  % Citesc normal matricea
  Labyrinth = fscanf(fin, '%d', [n, m])';

  fclose(fin);
endfunction
