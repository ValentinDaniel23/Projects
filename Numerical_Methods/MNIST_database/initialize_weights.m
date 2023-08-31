function [matrix] = initialize_weights(L_prev, L_next)
  
  % epsilon recomandat
  epsilon = sqrt(6) / sqrt(L_prev + L_next + 1);
  
  % initializare matrice
  matrix = (rand(L_next, L_prev + 1) * 2 - 1 ) * epsilon;

endfunction
