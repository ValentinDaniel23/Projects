function [Error] = linear_regression_cost_function(Theta, Y, FeatureMatrix)  
   m = size(Y, 1);
   % Functia de cost, asa cum a fost descrisă, 
   % folosind cei doi vectori si o matrice
   % Se omite coeficientul 0
   Theta = Theta(2:end); 
   Error = Theta;
   
   Features = FeatureMatrix * Theta;
   Error = ( Features - Y )' * ( Features - Y ) / (2 * m);
endfunction