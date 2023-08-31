function [Theta] = gradient_descent(FeatureMatrix, Y, n, m, alpha, iter)
  
  Theta = zeros(n, 1);

  for i = 1:iter
    % Se calculeaza folosind tehnica gradientului descendent, coeficientii specifici
    Features = FeatureMatrix * Theta;
    Error = Features - Y;
    Gradient = (alpha/m) * FeatureMatrix' * Error;
    Theta = Theta - Gradient;
  end

  Theta = [0; Theta];
endfunction