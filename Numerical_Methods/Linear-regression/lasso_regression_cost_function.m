function [Error] = lasso_regression_cost_function(Theta, Y, FeatureMatrix, lambda)
  m = size(Y, 1);
  n = size(Theta, 1);
  
  % Implementează functia de cost, asa cum a fost descrisă,
  % folosind cei doi vectori, o matrice si un scalar de la intrare
  Theta = Theta(2:end);
  Features = FeatureMatrix * Theta;
  Error = Y - Features;
  Error = Error' * Error / m;

  Aux = ones(size(Theta, 1), 1);

  Sum = Theta' * Aux;
  Error = Error + lambda * Sum;
endfunction