function [Error] = ridge_regression_cost_function(Theta, Y, FeatureMatrix, lambda)
  m = size(Y, 1);
  % Implementează functia de cost, asa cum a fost descrisă, 
  % folosind cei doi vectori, o matrice si un scalar de la intrare
  % Se omite coeficientul 0
  Theta = Theta(2:end);
  
  Features = FeatureMatrix * Theta;
  Error = Features - Y;
  Error = Error' * Error / (2*m);

  Error = Error + lambda * (Theta' * Theta);
endfunction